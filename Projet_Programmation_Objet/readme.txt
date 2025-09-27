Demineur - Programmation par Objet - Marine VINCENT
Mini Projet IS3

Le but de ce projet était de compléter une implémentation du jeu du Démineur.
Voici l'architecture du projet :

├── exceptions
│   ├── GameLostException.class
│   ├── GameLostException.java
│   ├── GameWonException.class
│   ├── GameWonException.java
│   ├── InvalidMoveException.class
│   └── InvalidMoveException.java
├── gui
│   ├── DemineurUI.class
│   ├── DemineurUI.java
│   ├── GamePanel.class
│   ├── GamePanel.java
│   ├── SetupDialog.class
│   ├── SetupDialog.java
│   ├── TopPanel.class
│   └── TopPanel.java
└── lib
    ├── AbstractCell.class
    ├── AbstractCell.java
    ├── Demineur.class
    └── Demineur.java

Les fichiers contenus dans gui (interface graphique) et exceptions étaient déjà fournis. Le but est de créer un dossier lib contenant les classes Demineur et AbstractCell.

Ici, le plateau est une grille de cellules AbstractCell, pouvant contenir des bombes placées aléatoirement. Tour à tout, le joueur révèle des cellules.
Il perd s'il tombe sur une bombe;
Il gagne si il découvre toutes les cases sans bombes;
Il y a erreur si il clique sur une case déjà révelée.

Les cellules sont définies par leurs coordonnées, si c'est une bombe, le nombre de bomes adjacentes et si elle est révélée.
Le jeu de demineur est défini par sa grille de jeu et le placement des bombes.



Pour lancer le jeu, il faut écrire dans le terminal depuis la racine du projet :
javac exceptions/*.java gui/*.java lib/*.java
java gui.DemineurUI


