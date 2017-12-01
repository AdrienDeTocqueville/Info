#ifndef SNORKUNKING_H
#define SNORKUNKING_H

#include <vector>
#include "NEAT/Network.h"

class Player;
class Caves;
class Level;
class BottomLevel;

class Snorkunking
{
    public:
        Snorkunking(Network* _net1, Network* _net2);
        ~Snorkunking();

        bool update();
        void draw();

        std::vector<float> getInputs(Player* p);

        void consumeOxygen(int count);

        Player* getPlayer(int _id);
        Level* getLevel(int id);
        BottomLevel* getBottomLevel();

        void nextPlayer();
        void nextPhase();

        static Snorkunking* game;
        const static int NB_PHASES = 3;

        std::vector<Player*> players;
        Caves* caves;

        unsigned initalLevelCount = 0;
        unsigned currentPlayer = 0;
        unsigned currentPhase = 0;
        int oxygen = 0;

        Network* nets[2];
};

#endif // SNORKUNKING_H
