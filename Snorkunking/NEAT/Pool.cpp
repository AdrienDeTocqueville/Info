#include "Pool.h"

#include <set>
#include <cmath>
#include <ctime>
#include <cstdlib>
#include <fstream>

const unsigned Pool::maxStaleness(15);

Pool::Pool():
    Pool(0, 0, 0)
{ }

Pool::Pool(unsigned _population, unsigned _inputSize, unsigned _outputSize):
    population(_population),
    generation(0), innovation(0),
    currentGenome(-1), currentSpecies(0), currentOrganism(0),
    maxFitness(0.0f),
    inputSize(_inputSize+1), outputSize(_outputSize)
{
    srand(time(NULL));

    Genome::pool = this;
}

Pool::~Pool()
{
    Genome::pool = nullptr;

    for (unsigned i(0) ; i < species.size() ; i++)
        delete species[i];
}

void Pool::init()
{
    for (unsigned i(0) ; i < species.size() ; i++)
        delete species[i];

    species.clear();

    for (unsigned i(0) ; i < population ; i++)
    {
        Genome genome;
        genome.mutate();

        addToSpecies(genome);
    }

    saveToFolder();
}

bool Pool::loadFromFolder(unsigned _generation)
{
    generation = _generation;

    std::string folder = "Populations\\Generation" + toString(generation);

    unsigned speSize = 0;
    std::ifstream infos(folder + "\\infos.txt");

    if (!infos)
    {
        std::cout << "Informations file not found: " << folder + "\\infos.txt" << std::endl;
        return false;
    }

    infos >> population >> innovation >> inputSize >> outputSize >> speSize >> maxFitness;
    infos.close();

    for (unsigned i(0) ; i < species.size() ; i++)
        delete species[i];

    species.clear();

    for (unsigned i(0) ; i < speSize ; i++)
        species.push_back( new Species(folder + "\\species" + toString(i) + ".genomes") );

    return true;
}

void Pool::saveToFolder()
{
    std::cout << "Saving generation: " << generation << std::endl;

    std::string folder = "Populations\\Generation" + toString(generation);


    // Make room for save
    std::string clearCommand = "rmdir /Q /S " + folder;
    std::string createCommand = "mkdir " + folder;

    system(clearCommand.c_str());
    system(createCommand.c_str());

    std::ofstream infos(folder + "\\infos.txt");

    if (!infos)
    {
        std::cout << "Error while saving generation" << std::endl;
        return;
    }

    infos << population << " " << innovation << " " << inputSize << " " << outputSize << " " << species.size() << " " << maxFitness;
    infos.close();

    for (unsigned i(0) ; i < species.size() ; i++)
        species[i]->saveToFile(folder + "\\species" + toString(i) + ".genomes");
}

unsigned Pool::getCurrentOrganism()
{
    return currentOrganism;
}

unsigned Pool::getInnovation()
{
    return ++innovation;
}

Genome* Pool::getRandomGenome()
{
    return species[ randomInt(0, species.size()-1) ]->getRandomGenome();
}

Genome* Pool::getBestGenome()
{
    float bestFitness = 0.0f;
    Genome* bestGenome = nullptr;

    for (Species* s: species)
    {
        for (auto g(s->genomes.begin()) ; g != s->genomes.end() ; ++g)
        {
            if (g->fitness > bestFitness)
            {
                bestFitness = g->fitness;
                bestGenome = &(*g);
            }
        }
    }

    return bestGenome;
}

bool Pool::computeGenerationFitness(std::function< float(Network*, bool&) > _fitnessFunction)
{
    bool stop = false;

    do
    {
        Genome* g = nextGenome();

        std::cout << "Generation: " << generation << ", Species: " << currentSpecies << ", Genome: " << currentGenome << std::endl;
        std::cout << "Organism: "  << getCurrentOrganism() << " (" << (getCurrentOrganism()*100)/population << "%)" << std::endl;
        std::cout << "Fitness = ";

        Network net(g);
        g->fitness = _fitnessFunction(&net, stop);

        if (g->fitness > maxFitness)
            maxFitness = g->fitness;

        if (stop)
            return false;

        std::cout << g->fitness << std::endl << std::endl;
    }
    while (getCurrentOrganism() != population);

    return true;
}

void Pool::addToSpecies(const Genome& _genome)
{
	for (Species* s: species)
    {
        if (s->contains(_genome))
        {
            s->addGenome(_genome);
            return;
        }
    }

    species.push_back( new Species(_genome) );
}

void Pool::buildNewGeneration(bool _saveToFolder)
{
    currentGenome = -1;
    currentSpecies = 0;

    currentOrganism = 0;

	generation++;
    std::cout << std::endl << "Creating generation: " << generation << std::endl;

    // Remove the worst half of the genomes from each species
    std::cout << "Culling" << std::endl;
    for (Species* s: species)
        s->cull(false);

    // Remove the species that evolved in a "bad way"
//    std::cout << "Removing stale species" << std::endl;
//	removeStaleSpecies();

    std::cout << "Assigning global rank" << std::endl;
	assignGlobalRank();

    std::cout << "Computing average fitness" << std::endl;
	for (Species* s: species)
        s->computeAverageFitness();

    std::cout << "Removing weak species" << std::endl;
	removeWeakSpecies();

    std::cout << "Breeding children: ";
	vector<Genome> children;
	float sum = totalAverageFitness();
	for (Species* s: species)
    {
        unsigned breed = floor(s->averageFitness / sum * population);

        for (unsigned i(0) ; i < breed ; i++)
            children.push_back(s->breedChild());
    }

    std::cout << children.size() << std::endl;

    // Keep the best genome of each species
    std::cout << "Culling again" << std::endl;
    for (Species* s: species)
        s->cull(true);

    std::cout << "Breeding children: ";
    while (children.size() + species.size() < population)
    {
        Species* s = species[ randomInt(0, species.size()-1) ];
        children.push_back(s->breedChild());
    }

    // keep population constant
    children.resize(population - species.size());

	// Repopulate
    std::cout << "Repopulating" << std::endl;
    for (const Genome& child: children)
        addToSpecies(child);

    if (_saveToFolder)
        saveToFolder();
}

void Pool::assignGlobalRank()
{
    auto increasingFitness = [](Genome* const& a, Genome* const& b)
    {
        return a->fitness > b->fitness;
    };

    multiset<Genome*, decltype(increasingFitness)> allGenomes(increasingFitness);

    for (Species* s: species)
    {
        for (auto genome(s->genomes.begin()) ; genome != s->genomes.end() ; genome++)
            allGenomes.insert(&*genome);
    }

	unsigned index = allGenomes.size();
    for (auto genome(allGenomes.begin()) ; genome != allGenomes.end() ; genome++)
        (*genome)->globalRank = index--;

    Genome* best = *allGenomes.begin();

    std::cout << std::endl;
    std::cout << "Best organism: " << best->saveToString() << std::endl;
    std::cout << "With fitness = " << best->fitness << std::endl << std::endl;
}

void Pool::removeStaleSpecies()
{
    vector<unsigned> staleSpecies;

    for (unsigned i(0) ; i < species.size() ; i++)
    {
        Species* s = species[i];

        s->sort();

        if (s->genomes.front().fitness > s->topFitness)
        {
			s->topFitness = s->genomes.front().fitness;
			s->staleness = 0;
        }
        else
            s->staleness++;

        if (s->staleness == maxStaleness && s->topFitness < maxFitness)
            staleSpecies.push_back(i);
    }

    std::cout << "Removed " << staleSpecies.size() << " stale species" << std::endl;

    for (int i(staleSpecies.size()-1) ; i >= 0 ; i--)
    {
        delete species[staleSpecies[i]];
        species.erase(species.begin() + staleSpecies[i]);
    }
}

void Pool::removeWeakSpecies()
{
    vector<unsigned> weakSpecies;

	float sum = totalAverageFitness();
    for (unsigned i(0) ; i < species.size() ; i++)
    {
        Species* s = species[i];

		float breed = floor(s->averageFitness / sum * population);
		if (breed < 1.0f)
			weakSpecies.push_back(i);
    }

    std::cout << "Removed " << weakSpecies.size() << " weak species" << std::endl;

    for (int i(weakSpecies.size()-1) ; i >= 0 ; i--)
    {
        delete species[weakSpecies[i]];
        species.erase(species.begin() + weakSpecies[i]);
    }
}

float Pool::totalAverageFitness()
{
    float total = 0.0f;
    for (Species* s: species)
        total += s->averageFitness;

    return total;
}

Genome* Pool::nextGenome()
{
    currentOrganism++;
    currentGenome++;

    if ((unsigned)currentGenome == species[currentSpecies]->genomes.size())
    {
        currentGenome = 0;
        currentSpecies++;

        if (currentSpecies == species.size())
            return nullptr;
    }

    auto g1 = species[currentSpecies]->genomes.begin();  advance(g1, currentGenome);

    return &(*g1);

}