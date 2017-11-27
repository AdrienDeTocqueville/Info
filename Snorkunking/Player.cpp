#include "Player.h"
#include "BottomLevel.h"
#include "Input.h"
#include <iostream>

sf::Color Player::colors[2] = { sf::Color::Blue, sf::Color::Green };

Player::Player(int _id):
    id(_id)
{
    level = Snorkunking::game->getLevel(0);
}

Player::~Player()
{
    for (unsigned i(0) ; i < chests.size() ; i++)
        delete chests[i];
}


bool Player::down()
{
    Level* nextLevel = level->getNext();
    if (nextLevel == nullptr)
        return false;

    level = nextLevel;

    Snorkunking::game->consumeOxygen(1 + chests.size());
    return true;
}

bool Player::up()
{
    Level* previousLevel = level->getPrevious();
    if (previousLevel == nullptr)
        return false;

    else if (previousLevel->depth == 0)
        updateScore();

    level = previousLevel;

    Snorkunking::game->consumeOxygen(1 + chests.size());
    return true;
}

// get treasure
bool Player::takeChest()
{
    Chest* chest = level->takeChest();

    if (chest == nullptr)
        return false;

    chests.push_back(chest);

    Snorkunking::game->consumeOxygen(1);
    return true;

}

void Player::updateScore()
{
    if (chests.empty())
        return;

    int newScore = 0;
    for (unsigned i = 0; i < chests.size(); i++)
    {
        newScore += chests[i]->nbTreasures;
        delete chests[i];
    }

    #ifdef DEBUG
        std::cout << "P" << (id+1) << " scored " << newScore << " points" << std::endl;
    #endif // DEBUG

    chests.clear();

    score += newScore;
}

void Player::reset()
{
    level = Snorkunking::game->getLevel(0);

    if (chests.empty())
        return;

    #ifdef DEBUG
        std::cout << "P" << (id+1) << " lost " << chests.size() << " chests" << std::endl;
    #endif // DEBUG

    BottomLevel* bottom = Snorkunking::game->getBottomLevel();

    for (unsigned i = 0; i < chests.size(); i++)
        bottom->addChest( chests[i] );

    chests.clear();
}

void Player::draw()
{
    static sf::CircleShape circle(Player::RADIUS);

    // afficher les joueurs
    int posy = 50 + Player::RADIUS * (2*getDepth()) + 10*level->getCaveId();
    circle.setPosition(150 + id * 100, posy);
    circle.setFillColor(colors[id]);
    Input::getWindow()->draw(circle);
}
