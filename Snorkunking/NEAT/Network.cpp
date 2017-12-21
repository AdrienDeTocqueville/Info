#include "Network.h"
#include "Pool.h"

#include <map>
#include <cmath>

float sigmoid(float x)
{
    return 2.0f / (1.0f + exp(x))  -1.0f;
}

Neuron::Neuron():
    output(0.0f)
{ }

float Neuron::compute()
{
    float sum = 0.0f;
    for (unsigned i(0) ; i < inputs.size() ; i++)
        sum += weights[i] * inputs[i]->output;

    //output = sigmoid(sum);
    output = tanh(sum);

    return output;
}

Network::Network(Genome* _genome)
{
	// Ce constructeur doit etre modifie pour prendre en argument une chaine de caractere
	// et charger le genome a partir de celle ci
	// Cette fonction est la derniere du fichier Genome.cpp

    // Chaque gene represente une liaison entre deux neurones identifies par des numeros
    // On cree un dictionnaire qui a un numero associe un neurone
    // les numeros entre 0 et Genome::pool->inputSize-1 representent les neurones de la premiere couche
    // les numeros entre Genome::pool->inputSize et Genome::pool->outputSize-1 representent les neurones de la derniere couche

    map<unsigned, Neuron*> neurons; // (unsigned est un entier non signe, donc positif, mais ca marcherait aussi avec int)

    // On itere sur tous les genes
    // Donc sur tous les neurones existants

    // Note: cette boucle est probablement simplifiable en java parceque la memoire est geree automatiquement
    for (const Gene& gene: _genome->genes)
    {
        if (!gene.enabled)
            continue;

        auto it = neurons.find(gene.in);
        if (it == neurons.end())              // Si il n'existe pas encore
            neurons[gene.in] = new Neuron();  // on le rajoute

        it = neurons.find(gene.out);
        if (it == neurons.end())              // Si il n'existe pas encore
            neurons[gene.out] = new Neuron(); // on le rajoute
    }

    // Maintenant que le dictionnaire est cree
    // On remplit les neurones avec les donnees contenues dans les genes
    for (const Gene& gene: _genome->genes)
    {
        if (!gene.enabled)
            continue;

        Neuron* n = neurons[gene.out];
        n->inputs.push_back(neurons[gene.in]); // On rajoute un neurone d'entree
        n->weights.push_back(gene.weight);     // et le poids correspondant
    }

    // Maintenant que les genes sont traduits sous forme de neurones
    // On finalise l'organisation du reseau
    // Chaque neurone est soit sur la couche d'entree, celle de sortie ou une couche intermediaire (dite cachée)

	// Ces deux couches ont des tailles fixes, contrairement a la couche cachée
    inputLayer.resize(Genome::pool->inputSize, nullptr);
    outputLayer.resize(Genome::pool->outputSize, nullptr);

	// Les elements de std::map sont des std::pair<unsigned, Neuron*>
	// Les elements d'une paire sont nommés first et second

	// On itere sur les paires
    for (auto it(neurons.begin()) ; it != neurons.end() ; ++it)
    {
        unsigned index = it->first;

        if (index < Genome::pool->inputSize) // neurone d'entrée
            inputLayer[index] = it->second;

        else if (index < Genome::pool->inputSize + Genome::pool->outputSize) // neurone de sortie
            outputLayer[index - Genome::pool->inputSize] = it->second;

        else // neurone caché
            hiddenLayer.push_back(it->second);
    }
}

Network::~Network()
{
    for (unsigned i(0) ; i < inputLayer.size() ; i++)
        delete inputLayer[i];

    for (unsigned i(0) ; i < hiddenLayer.size() ; i++)
        delete hiddenLayer[i];

    for (unsigned i(0) ; i < outputLayer.size() ; i++)
        delete outputLayer[i];
}


vector<float> Network::evaluate(vector<float>& _input)
{
    vector<float> output(outputLayer.size(), -1.0f);

    _input.push_back(1.0f); // bias

    if (_input.size() != inputLayer.size())
        std::cout << "input size invalid: " << _input.size() << " instead of " << inputLayer.size() << std::endl;

    for (unsigned i(0) ; i < inputLayer.size() ; i++)
    {
        if (inputLayer[i] != nullptr) // C'est possible qu'un genome n'utilise pas toutes le entrees
            inputLayer[i]->output = _input[i];
    }

    for (Neuron* n: hiddenLayer)
        n->compute();

    for (unsigned i(0) ; i < outputLayer.size() ; i++)
    {
        if (outputLayer[i] != nullptr) // ni toutes les sorties
            output[i] = outputLayer[i]->compute();
    }


    return output;
}

// juste pour faire des verifications, pas essentiel
void Network::display()
{
    std::cout << "Inputs: " << inputLayer.size() << std::endl;
        for (unsigned i(0) ; i < inputLayer.size() ; i++)
        {
            if (inputLayer[i] == nullptr)
                std::cout << "_" << std::endl;
            else
            std::cout << inputLayer[i]->inputs.size() << std::endl;
        }

    std::cout << "Hidden: " << hiddenLayer.size() << std::endl;
        for (unsigned i(0) ; i < hiddenLayer.size() ; i++)
        {
            std::cout << hiddenLayer[i]->inputs.size() << std::endl;
        }

    std::cout << "Output: " << outputLayer.size() << std::endl;
        for (unsigned i(0) ; i < outputLayer.size() ; i++)
        {
            if (outputLayer[i] == nullptr)
                std::cout << "_" << std::endl;
            else
            std::cout << outputLayer[i]->inputs.size() << std::endl;
        }

}
