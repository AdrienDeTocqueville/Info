#ifndef TOPLEVEL_H
#define TOPLEVEL_H

#include "Level.h"

class TopLevel: public Level
{
    public:
        TopLevel(int _depth):
            Level(_depth)
        { }

        virtual ~TopLevel()
        {

        }

        Level* getPrevious()
        {
            return nullptr;
        }

        bool hasChest()
        {
            return false;
        }

        Chest* takeChest()
        {
            return nullptr;
        }

        int getCaveId()
        {
            return 0;
        }
};

#endif // TOPLEVEL_H
