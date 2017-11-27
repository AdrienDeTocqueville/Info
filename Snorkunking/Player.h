#ifndef PLAYER_H
#define PLAYER_H

#include <SFML/Graphics.hpp>
#include <vector>

#include "Level.h"

class Player
{
    public:
        Player(int _id);
        virtual ~Player();

        bool down();
        bool up();
        bool takeChest();

        void updateScore();

        int getDepth()
        {
            return level->depth;
        }

        int getScore()
        {
            return score;
        }

        void reset();

        void draw();

        virtual bool play() = 0;

	static sf::Color colors[2];

	const static int RADIUS= 10;
	const static unsigned NB_MAX = 2;

	int id;
	int score = 0;
	Level* level;

	std::vector<Chest*> chests;
};

#endif // PLAYER_H
