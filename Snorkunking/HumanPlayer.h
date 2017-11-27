#ifndef HUMANPLAYER_H
#define HUMANPLAYER_H

#include "Player.h"
#include "Input.h"

class HumanPlayer : public Player
{
    public:
        HumanPlayer(int id):
            Player(id)
        {
        }

        bool play()
        {
//            auto inputs = Snorkunking::game->getInputs(this);
//            for (auto i: inputs)
//                std::cout << i << "  ";
//
//            std::cout << std::endl;


            if (Input::getKeyReleased(sf::Keyboard::Up))
                return up();

            if (Input::getKeyReleased(sf::Keyboard::Down))
                return down();

            if (Input::getKeyReleased(sf::Keyboard::Return))
                return takeChest();

            return false;
        }
};

#endif // HUMANPLAYER_H
