#include "Genome.h"
#include "Pool.h"

#include <map>
#include <cmath>
#include <cstdlib>
#include <sstream>


float random(float _min, float _max)
{
    return _min +  rand() / (RAND_MAX/(_max-_min));
}

int randomInt(int _min, int _max)
{
    return _min + (rand() % static_cast<int>(_max - _min + 1));
}

std::string toString(int _number)
{
    std::stringstream os;
    os << _number;

    return os.str();
}

std::string toStringF(float _number)
{
    std::stringstream os;
    os << _number;

    return os.str();
}

Gene::Gene(unsigned _in, unsigned _out, float _weight):
    in(_in), out(_out),
    innovation(0),
    weight(_weight),
    enabled(true)
{
    innovation = Genome::pool->getInnovation();
}

Gene::Gene(unsigned _in, unsigned _out, unsigned _innovation, float _weight, bool _enabled):
    in(_in), out(_out),
    innovation(_innovation),
    weight(_weight),
    enabled(_enabled)
{ }

Gene::~Gene()
{ }


Pool* Genome::pool = nullptr;

Genome::Genome():
    nextNeuron(pool->inputSize+pool->outputSize),
    globalRank(0),
    fitness(0.0f),
    weightMutationChance(0.25f),
    linkMutationChance(2.0f),
    biasMutationChance(0.4f),
    nodeMutationChance(0.5f),
    enableMutationChance(0.2f),
    disableMutationChance(0.4f),
    perturbChance(0.9f),
    stepSize(0.1f)
{ }

Genome::Genome(const Genome& g):
    Genome()
{
    genes = g.genes;
    nextNeuron = g.nextNeuron;

    copyRates(g);
}

// g1 should always have higher fitness than g2
Genome::Genome(const Genome& g1, const Genome& g2):
    Genome()
{
    map<unsigned, const Gene*> innovations2;
    for (unsigned i(0) ; i < g2.genes.size() ; i++)
        innovations2[g2.genes[i].innovation] = &(g2.genes[i]);


    for (unsigned i(0) ; i < g1.genes.size() ; i++)
    {
        auto gene2 = innovations2.find(g1.genes[i].innovation);

        if (gene2 != innovations2.end() && random() < 0.5f && gene2->second->enabled)
            genes.push_back(*gene2->second);
        else
            genes.push_back(g1.genes[i]);
    }

	nextNeuron = max(g1.nextNeuron, g2.nextNeuron);

    copyRates(g1);
}

Genome::~Genome()
{ }

void Genome::copyRates(const Genome& g)
{
    weightMutationChance = g.weightMutationChance;
    linkMutationChance = g.linkMutationChance;
    biasMutationChance = g.biasMutationChance;
    nodeMutationChance = g.nodeMutationChance;
    enableMutationChance = g.enableMutationChance;
    disableMutationChance = g.disableMutationChance;
}

void Genome::mutate()
{
    vector<float*> rates = {&weightMutationChance, &linkMutationChance, &biasMutationChance,
                            &nodeMutationChance, &enableMutationChance, &disableMutationChance, &stepSize};

    for (unsigned i(0) ; i < rates.size() ; i++)
    {
        if (random() < 0.5f)
            *(rates[i]) *= 0.95;
        else
            *(rates[i]) *= 1.05263;
    }


    if (random() < weightMutationChance)
        weightMutate();

    float chanceCopy = linkMutationChance;
    while (chanceCopy > 0.0f)
    {
        if (random() < chanceCopy)
            linkMutate(false);

        chanceCopy -= 1.0f;
    }

    chanceCopy = biasMutationChance;
    while (chanceCopy > 0.0f)
    {
        if (random() < chanceCopy)
            linkMutate(true);

        chanceCopy -= 1.0f;
    }

    chanceCopy = nodeMutationChance;
    while (chanceCopy > 0.0f)
    {
        if (random() < chanceCopy)
            nodeMutate();

        chanceCopy -= 1.0f;
    }

    chanceCopy = enableMutationChance;
    while (chanceCopy > 0.0f)
    {
        if (random() < chanceCopy)
            enableDisableMutate(false);

        chanceCopy -= 1.0f;
    }

    chanceCopy = disableMutationChance;
    while (chanceCopy > 0.0f)
    {
        if (random() < chanceCopy)
            enableDisableMutate(true);

        chanceCopy -= 1.0f;
    }
}

// Cette mutation modifie le poids d'une liaison existante
void Genome::weightMutate()
{
	for (Gene& gene: genes)
    {
        if (random() < perturbChance)
            gene.weight += random(-stepSize, stepSize);

        else
            gene.weight = random(-2.0f, 2.0f);
    }
}

// Cette mutation cr�e un nouvelle liaison
void Genome::linkMutate(bool _forceBias)
{
	unsigned neuronIn = pool->inputSize-1;
	if (!_forceBias)
		neuronIn = randomNeuron(true);

	unsigned neuronOut = randomNeuron(false);


	if (!containsLink(neuronIn, neuronOut))
        genes.emplace_back(neuronIn, neuronOut, random(-2.0f, 2.0f));
}

// Cette mutation insere un neurone au milieu d'une liaison existante
void Genome::nodeMutate()
{
    if (!genes.size())
        return;

    Gene& gene = genes[ randomInt(0, genes.size()-1) ];
    if (!gene.enabled)
        return;

    gene.enabled = false;

    genes.emplace_back(gene.in, nextNeuron, 1.0f);
    genes.emplace_back(nextNeuron, gene.out, gene.weight);

    nextNeuron++;
}

// Cette mutation passe dans l'etat _state une liaison qui etait dans l'autre etat
void Genome::enableDisableMutate(bool _state)
{
    vector<Gene*> candidates;
    for (unsigned i(0) ; i < genes.size() ; i++)
    {
        if (genes[i].enabled == _state)
            candidates.push_back( &(genes[i]) );
    }

    if (candidates.size())
        candidates[ randomInt(0, candidates.size()-1) ]->enabled = !_state;
}

unsigned Genome::randomNeuron(bool _includeInputs)
{
    map<unsigned, bool> neurons;

    if (_includeInputs)
    for (unsigned i(0) ; i < pool->inputSize ; i++)
        neurons[i] = true;

    for (unsigned i(0) ; i < pool->outputSize ; i++)
        neurons[pool->inputSize + i] = true;


    for (unsigned i(0) ; i < genes.size() ; i++)
    {
        if (_includeInputs || genes[i].in >= pool->inputSize)
            neurons[genes[i].in] = true;

        if (_includeInputs || genes[i].out >= pool->inputSize)
            neurons[genes[i].out] = true;
    }

    unsigned chosen = randomInt(0, neurons.size()-1);
    auto it = neurons.begin(); advance(it, chosen);

    return it->first;
}

bool Genome::containsLink(unsigned _neuronIn, unsigned _neuronOut)
{
    for (const Gene& gene: genes)
    {
        if (gene.in == _neuronIn && gene.out == _neuronOut)
            return true;
    }

    return false;
}

unsigned disjointGenes(const Genome& a, const Genome& b)
{
    map<unsigned, pair<bool,bool>> matchingGenes;

    for (const Gene& gene: a.genes)
    {
        matchingGenes[gene.innovation] = {true, false};
    }

    for (const Gene& gene: b.genes)
    {
        auto it = matchingGenes.find(gene.innovation);
        if (it == matchingGenes.end())
            matchingGenes[gene.innovation] = {false, true};
        else
            it->second.second = true;
    }

    unsigned disjointCount = 0;
    for (auto it(matchingGenes.begin()) ; it != matchingGenes.end() ; ++it)
        if (it->second.first == false || it->second.second == false)
            disjointCount++;

	return disjointCount / max(a.genes.size(), b.genes.size());
//    return disjointCount;
}

float averageWeightDifference(const Genome& a, const Genome& b)
{
    map<unsigned, float> weightMap;
    for (const Gene& gene: a.genes)
        weightMap[gene.innovation] = gene.weight;

    float sum = 0.0f;
    unsigned num = 0;

    for (const Gene& gene: b.genes)
    {
        auto it = weightMap.find(gene.innovation);
        if (it != weightMap.end())
        {
            sum += abs(it->second - gene.weight);
            num++;
        }
    }

	return sum / num;
}

string Genome::saveToString()
{
    string genome;

    genome = toString(nextNeuron) + " " + toStringF(fitness) + " " + toString(genes.size()) + " " +
             toString(weightMutationChance) + " " + toString(linkMutationChance) + " " +
             toString(biasMutationChance) + " " + toString(nodeMutationChance) + " " +
             toString(enableMutationChance) + " " + toString(disableMutationChance);

    for (const Gene& g: genes)
        genome += " " + toString(g.in) + " " + toString(g.out) + " " + toStringF(g.weight)
                + " " + toString(g.innovation) + " " + toString(g.enabled);

    return genome;
}

void Genome::loadFromString(string& _genome)
{
    std::stringstream ss(_genome);

    unsigned gs, in, out, innovation;
    float weight;
    bool enabled;

    ss >> nextNeuron >> fitness >> gs;
    ss >> weightMutationChance >> linkMutationChance >> biasMutationChance;
    ss >> nodeMutationChance >> enableMutationChance >> disableMutationChance;

    for (unsigned i(0) ; i < gs ; i++)
    {
        ss >> in >> out >> weight >> innovation >> enabled;
        genes.emplace_back(in, out, innovation, weight, enabled);
    }
}
