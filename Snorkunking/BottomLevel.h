#ifndef BOTTOMLEVEL_H
#define BOTTOMLEVEL_H

#include "Level.h"

class BottomLevel: public Level
{
    public:
        std::vector<Chest*> chests;

        BottomLevel(int _depth):
            Level(_depth)
        {
            chests.push_back(new Chest(2));
        }

        virtual ~BottomLevel()
        {
            for (unsigned i(0) ; i < chests.size() ; i++)
                delete chests[i];
        }

        Level* getNext()
        {
            return nullptr;
        }

        bool hasChest()
        {
            return (chests.size() > 0);
        }

        Chest* takeChest()
        {
            if (chests.size() > 0)
            {
                Chest* temp = chests.back();
                chests.pop_back();
                return temp;
            }

            return nullptr;
        }

        void addChest(Chest* chest)
        {
            chests.push_back(chest);
        }

        int getCaveId()
        {
            return 2;
        }
};

#endif // BOTTOMLEVEL_H
