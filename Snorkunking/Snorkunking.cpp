#include "Snorkunking.h"

#include "Caves.h"
#include "Player.h"
#include "HumanPlayer.h"
#include "IAPlayer.h"

Snorkunking* Snorkunking::game = nullptr;

Snorkunking::Snorkunking(Network* _net1, Network* _net2)
{
    nets[0] = _net1;
    nets[1] = _net2;

    game = this;

    caves = new Caves();

    for (unsigned i(0); i < Player::NB_MAX; i++)
    {
        if (nets[i] == nullptr)
            players.push_back(new HumanPlayer(i));
        else
            players.push_back(new IAPlayer(i));
    }

    initalLevelCount = caves->getLevelCount()-1; // surface doesn't count

    oxygen = 2 * initalLevelCount;
    nextPlayer();

    #ifdef DEBUG
    std::cout << "Au tour de " << players[currentPlayer]->id+1 << std::endl;
    std::cout << "Oxygene: " << oxygen << std::endl;
    #endif // DEBUG
}

Snorkunking::~Snorkunking()
{
    delete caves;

    delete players[0];
    delete players[1];
}

bool Snorkunking::update()
{
    if (players[currentPlayer]->play())
    {
        currentPlayer++;

        if (oxygen <= 0)
        {
            if (++currentPhase == NB_PHASES)
                return false;
            else
                nextPhase();
        }

        else if (currentPlayer == players.size())
            nextPlayer();

        #ifdef DEBUG
        std::cout << std::endl << "Au tour de " << players[currentPlayer]->id+1 << std::endl;
        std::cout << "Oxygene: " << oxygen << std::endl;

//        for (unsigned i(0); i < getInputs(players[currentPlayer]).size(); i++)
//            std::cout << getInputs(players[currentPlayer])[i] << std::endl;
        #endif // DEBUG
    }

    return true;
}

void Snorkunking::draw()
{
    // affiche les caves
    caves->draw();

    // affiche les joeurs
    for (unsigned i = 0; i < Player::NB_MAX; ++i)
        players[i]->draw();
}

std::vector<float> Snorkunking::getInputs(Player* p)
{
    std::vector<float> inputs;

    inputs.push_back(oxygen);

    inputs.push_back(p->level->depth);
    inputs.push_back(p->chests.size());

    for (int i(p->getDepth()); i < p->getDepth()+15; i++)
    {
        if (i < 0 || i >= caves->getLevelCount())
            inputs.push_back(0.0f);

        else
        {
            Level* l = getLevel(i);

            if (l->hasChest())
                inputs.push_back(0.3f * (l->getCaveId()+1));
            else
                inputs.push_back(0.0f);
        }

    }

    return inputs;
}

void Snorkunking::consumeOxygen(int count)
{
    oxygen -= count;
}

Player* Snorkunking::getPlayer(int _id)
{
    if (players[0]->id == _id)
        return players[0];

    return players[1];
}

Level* Snorkunking::getLevel(int id)
{
    return caves->getLevel(id);
}

BottomLevel* Snorkunking::getBottomLevel()
{
    Level* l = getLevel(caves->getLevelCount()-1);
    return reinterpret_cast<BottomLevel*>(l);
}

void Snorkunking::nextPlayer()
{
    if (players[0]->getDepth() == players[1]->getDepth() && Random::nextInt() == 0)
        std::swap(players[0], players[1]);

    else if (players[0]->getDepth() < players[1]->getDepth())
        std::swap(players[0], players[1]);

    currentPlayer = 0;
}

void Snorkunking::nextPhase()
{
    caves->removeEmptyLevels();
    oxygen = 2*initalLevelCount;

    for (unsigned i = 0; i < players.size(); i++)
        players[i]->reset();

    nextPlayer();
}
