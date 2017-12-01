#ifndef CHEST_H
#define CHEST_H

#include "Random.h"

class Chest
{
    public:

	int nbTreasures;

	Chest(int caveId)
	{
		int nbTreasuresMin[] = {1, 5, 10};
		int nbTreasuresMax[] = {4, 5, 3};

		nbTreasures = Random::nextInt(nbTreasuresMin[caveId], nbTreasuresMax[caveId]);
	}
};

#endif // CHEST_H
