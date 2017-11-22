3.1.1 Pour modeliser la balle, on utilise deux float pour la position et deux float pour la vitesse
Plus tard, pour creer une tableau, on fait une classe Balle contenant ces informations

3.1.2 On utilise l'espace de la fenetre

Description de l'algorithme
On initialise les balles avec des vitesses et positions aleatoires
Pour chaque balle dans le tableau, si la position selon un axe depasse sa borne, on inverse la vitesse et on la multiplie par 0.9 pour simuler des frottements
On calcule la distance entre chacune des balles pour tester les collisions