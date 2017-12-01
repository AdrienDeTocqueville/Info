#include "Caves.h"
#include "Input.h"
#include "Player.h"

#include "TopLevel.h"
#include "InnerLevel.h"
#include "BottomLevel.h"

Caves::Caves()
{
    levels.push_back(new TopLevel(0));

    for (unsigned i = 0; i < NB_CAVES; i++)
    {
        heights[i] = 11 - 3 * i - Random::nextInt(0, NB_CAVES);

        for (int j = 0; j < heights[i]; j++)
            levels.push_back(new InnerLevel(i, levels.size()));
    }

    levels.push_back(new BottomLevel(levels.size()));

    heights[0]++;
    heights[NB_CAVES-1]++;
}

Caves::~Caves()
{
    for (unsigned i(0); i < levels.size() ; i++)
        delete levels[i];
}

void Caves::removeEmptyLevels()
{
    for (unsigned i = 0; i < levels.size(); i++)
    {
        levels[i]->depth = i;

        if (!levels[i]->isPersistent())
        {
            heights[levels[i]->getCaveId()]--;
            delete levels[i];
            levels.erase(levels.begin()+i);
            i--;
        }
    }
}

void Caves::draw()
{
    static sf::RectangleShape rect(sf::Vector2f(200 + 100*(Player::NB_MAX-1), 1));

    static int levelHeight = 2 * Player::RADIUS;
    int posy = 50;

    rect.setFillColor(sf::Color(32, 178, 170));
    for (int i = 0; i < Caves::NB_CAVES; i++)
    {
        int caveHeight = levelHeight * heights[i];

        rect.setPosition(25, posy);
        rect.setScale(1.0f, caveHeight);

        Input::getWindow()->draw(rect);

        posy += caveHeight + 10;
    }


    rect.setFillColor(sf::Color::White);
    rect.setScale(0.1f, levelHeight-1);
    for (unsigned i = 0; i < levels.size(); i++)
    {
        if (levels[i]->hasChest())
        {
            rect.setPosition(60, 50 + i * levelHeight + levels[i]->getCaveId()*10);

            Input::getWindow()->draw(rect);
        }
    }
}
