#ifndef CAVES_H
#define CAVES_H

#include "Level.h"

class Caves
{
    public:
        Caves();
        ~Caves();

        static const int NB_CAVES = 3;

        std::vector<Level*> levels;
        int heights[NB_CAVES];

        void removeEmptyLevels();
        void draw();

        Level* getLevel(int level)
        {
            return levels[level];
        }

        int getLevelCount()
        {
            return levels.size();
        }

        bool hasChest(int level)
        {
            return levels[level]->hasChest();
        }

        Chest* takeChest(int level)
        {
            return levels[level]->takeChest();
        }
};

#endif // CAVES_H
