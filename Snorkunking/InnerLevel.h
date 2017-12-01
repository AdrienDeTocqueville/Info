#ifndef INNERLEVEL_H
#define INNERLEVEL_H

#include "Level.h"

class InnerLevel : public Level
{
    public:
        virtual ~InnerLevel()
        {
            delete chest;
        }

        int caveId = 0;
        Chest* chest = nullptr;

        InnerLevel(int _caveId, int _depth):
            Level(_depth)
        {
            caveId = _caveId;
            chest = new Chest(caveId);
        }

        bool isPersistent()
        {
            return (chest != nullptr);
        }

        bool hasChest()
        {
            return (chest != nullptr);
        }

        Chest* takeChest()
        {
            Chest* _copy = chest;
            chest = nullptr;

            return _copy;
        }

        int getCaveId()
        {
            return caveId;
        }
};

#endif // INNERLEVEL_H
