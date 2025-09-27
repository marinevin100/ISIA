Pour compiler et exécuter notre programme, il faut écrire dans le terminal les commandes suivantes :

gcc -c Structures/Graphes/graphe.c
gcc -c Structures/Graphes/ListeC/listeC.c
gcc -c Structures/File/file.c
gcc -c main.c
gcc main.o graphe.o listeC.o file.o -o main
./main < fichier.txt

Le fichier.txt doit contenir une description d’un graphe au format attendu, comme dans les fichiers de test fournis.
