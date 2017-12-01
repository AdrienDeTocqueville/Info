#ifndef LEVEL_H
#define LEVEL_H

#include "Chest.h"
#include "Snorkunking.h"

class Level
{
    public:
        int depth;

        Level(int _depth)
        {
            depth = _depth;
        }

        virtual ~Level()
        {

        }

        virtual Level* getPrevious()
        {
            return Snorkunking::game->getLevel(depth-1);
        }

        virtual Level* getNext()
        {
            return Snorkunking::game->getLevel(depth+1);
        }

        virtual bool isPersistent()
        {
            return true;
        }

        virtual bool hasChest() = 0;
        virtual Chest* takeChest() = 0;
        virtual int getCaveId() = 0;
};

#endif // LEVEL_H
