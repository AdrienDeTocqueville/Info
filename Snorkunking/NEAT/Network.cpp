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
	// Ce constructeur doit être modifié pour prendre en argument une chaine de caractères
	// et charger le genome à partir de celle ci
	// Ce chargement est fait dans la dernière fonction du fichier Genome.cpp

    // Chaque gène représente une liaison entre deux neurones identifiés par des numéros
    // On crée un dictionnaire qui à un numéro associe un neurone
    // les numéros entre 0 et Genome::pool->inputSize-1 représentent les neurones de la première couche
    // les numéros entre Genome::pool->inputSize et Genome::pool->outputSize-1 représentent les neurones de la dernière couche

    map<unsigned, Neuron*> neurons; // (unsigned est un entier non signé, donc positif, mais ca marcherait aussi en utilisant int)

    // On itère sur tous les gènes, donc on rencontrera au moins une fois chaque neurone

    // Note: cette boucle est probablement simplifiable en java parce que la memoire est gerée automatiquement
    for (const Gene& gene: _genome->genes)
    {
        if (!gene.enabled)
            continue;

        auto it = neurons.find(gene.in);
        if (it == neurons.end())              // Si on n'a encore jamais rencontré ce neurone
            neurons[gene.in] = new Neuron();  // on alloue de la mémoire pour plus tard

        it = neurons.find(gene.out);
        if (it == neurons.end())              // Si on n'a encore jamais rencontré ce neurone
            neurons[gene.out] = new Neuron(); // on alloue de la mémoire pour plus tard
    }

    // Maintenant que le dictionnaire est initialisé
    // On remplit les neurones créés avec les données contenues dans les gènes
    for (const Gene& gene: _genome->genes)
    {
        if (!gene.enabled)
            continue;

        // Un gène code un liaison entre les neurones identifiés par gene.in et gene.out
        Neuron* n = neurons[gene.out];
        n->inputs.push_back(neurons[gene.in]);
        n->weights.push_back(gene.weight);
    }

    // Maintenant que les gènes sont traduits sous forme de neurones
    // On finalise l'organisation du réseau
    // Chaque neurone est soit sur la couche d'entrée, sur celle de sortie ou sur une couche intermediaire (dite cachée)

	// Ces deux couches ont des tailles fixes, contrairement à la couche cachée
    inputLayer.resize(Genome::pool->inputSize, nullptr);
    outputLayer.resize(Genome::pool->outputSize, nullptr);

	// Les elements de std::map sont des std::pair<unsigned, Neuron*>
	// Les elements d'une paire sont nommés first et second

	// On itère sur les paires
	// En fonction de l'index d'un neurone, on le place sur la couche appropriée
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
        if (inputLayer[i] != nullptr) // C'est possible qu'un genome n'utilise pas toutes les entrées
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
