#ifndef IAPLAYER_H
#define IAPLAYER_H

#include "Player.h"
#include "windows.h"

class IAPlayer : public Player
{
    public:
        IAPlayer(int id):
            Player(id)
        {
            #ifdef DEBUG
                start = 0;
            #endif // DEBUG
        }

        bool play()
        {
            #ifdef DEBUG
                if (start == 0)
                {
                    start = GetTickCount();
                    return false;
                }

                if (start+Random::nextInt(500, 1500) > GetTickCount()) // Think for approx. 1 second
                    return false;

                start = 0;
            #endif // DEBUG

            // Evaluate network
            auto inputs = Snorkunking::game->getInputs(this);
            auto output = Snorkunking::game->nets[id]->evaluate( inputs );

            // Get argmax
            int am = 0;
            if (output[1] > output[am])
                am = 1;

            if (output[2] > output[am])
                am = 2;

            // Try choosen action
            bool val;

            if (am == 0)
                val = up();

            else if (am == 1)
                val = down();

            else if (am == 2)
                val = takeChest();

            if (val)
                return true;

            // If it is impossible, move
            if (!down())
                up();

            return true;
        }

        #ifdef DEBUG
            DWORD start;
        #endif // DEBUG
};

#endif // IAPLAYER_H
