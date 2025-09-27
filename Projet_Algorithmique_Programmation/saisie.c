#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

struct point
{
  int x;
  int y;
};

/***************************************************************************************/
/* Fonction matrice_pleine                                                             */
/* Vérifie si la matrice est pleine                                                    */
/* mat : Matrice[19][19] de caractère  - Donnée - plateau a traiter           */
/* rslt : Booléen - Résultat
/***************************************************************************************/

bool matrice_pleine(char mat[19][19])
{
  int i=0;
  int j=0;
  while (i<19 && mat[i][j] != '.')
    {
      j=0;
      while (j<19 && mat[i][j] != '.')
	{
	  j++;
	}
      i++;
    }
  return (i==19);
}

/***************************************************************************************/
/* Action remplir_matrice_vide                                                         */
/* Rempli une matrice 19x19 par des ‘.’                                                */
/* mat : Matrice[19][19] de caractère  - Donnee/Resultat - plateau a traiter           */
/***************************************************************************************/

void remplir_matrice_vide(char mat[][19]){
    int i,j;
    for (i = 0; i < 19; i++) {
        for (j = 0; j < 19; j++) {
            mat[i][j] = '.';
        }
    }
}



/*********************************************************************************/
/* Action Affichage                                                              */
/* Affiche le plateau                                                            */
/* mat : Matrice[19][19] de caractère  - Donnee - plateau a afficher             */
/*********************************************************************************/

void affichage(char mat[][19])
{
    int i, j;

    //indices des colonnes
    printf("   ");
    for (j = 0; j < 19; j++)
        printf("%2d  ", j + 1);
    printf("\n");

    //affichage matrice
    for (i = 0; i < 19; i++) {
        printf("%2d  ", i + 1);
        for (j = 0; j < 19; j++) {
            printf("%c   ", mat[i][j]);
        }
        printf("\n");
    }

    printf("\n");
}


/**************************************************************************************/
/* Action saisie                                                                                                 */
/* Remplie le point des coordonnées saisies                                                   */
/* p - Point - Donnée - coordonnées du dernier coup joué                              */
/* jou : Caractère - Donnée - joueur                                                                */
/* ab - Entier - Donnée/Résultat - Indicateur pour l’abandon du joueur           */
/* ch - Chaîne - Locale - Saisie “ligne,colonne”                                               */
/* virgule - Pointeur sur caractère - Locale - Position de la virgule                  */
/* lig - Pointeur sur chaîne - Locale - Position de la ligne                                 */
/* lig - Pointeur sur chaîne - Locale - Position de la colonne                            */
/**************************************************************************************/


void saisie(struct point* p, int *ab, char jou){
    char ch[5];
    printf("Joueur %c : veuillez saisir la case ou vous souhaitez jouer au format ligne,colonne : \n", jou);
    scanf("%s", ch);

    char *virgule = strchr(ch, ','); //séparer en 2
    *virgule = '\0';
    char *lig = ch;
    char *col = virgule + 1;

    p->x = atoi(lig); //convertir les chaines en entiers
    p->y = atoi(col);

    if(p->x <= 19 && p->x >=1 && p->y <= 19 && p->y >=1){
        p->x -= 1;
        p->y -= 1;
    }
    else if (p->x == 0 && p->y == 0)
        *ab = 1;
    else // l'info que lig = -1 est suffisante, pas besoin de col = -1
        p->x = -1;
}

/**************************************************************************************/
/* Action saisie correcte                                                             */
/* Vérifie que le coup que l’on souhaite jouer est possible                           */
/* p - Point - Donnée - coordonnées du dernier coup joué                              */
/* jou : Caractère - Donnée - joueur                                                  */
/* ab - Entier - Donnée/Résultat - Indicateur pour l’abandon du joueur                */
/**************************************************************************************/

void saisie_correcte(struct point* p, int *ab, int jou){
    saisie(p, ab, jou);
    while (p->x == -1 && !*ab){
        printf("\nSaisie incorrecte\n");
        saisie(p, ab, jou);
    }
}

/**************************************************************************************/
/* Action jouer coup                                                                  */
/* Joue le coup sur la grille                                                         */
/* mat : Matrice[19][19] de caractère  - Donnee/Resultat - plateau à traiter          */
/* p - Point - Donnée - coordonnées du dernier coup joué                              */
/* jou : Caractère - Donnée - joueur                                                  */
/* ab - Entier - Donnée/Résultat - Indicateur pour l’abandon du joueur                */
/**************************************************************************************/

void jouer_coup(char mat[][19], struct point p, char j, int *ab)
{
  int x = p.x;
  int y = p.y;
    while (mat[x][y] != '.' && !*ab){
    printf("\nCoup impossible, la case est déjà occupée\n");
    saisie_correcte(&p, ab, j);
    }
    if (!*ab)
        mat[p.x][p.y] = j;
}


/**************************************************************************************/
/* Fonction - alignement                                                              */
/* Vérifie si un jour a gagné par alignement                                          */
/* mat : Matrice[19][19] de caractère  - Donnée - plateau à traiter                   */
/* p - Point - coordonnées du dernier coup joué                                       */
/* j : Caractère - Donnée - joueur                                                    */
/* nvx - Entier - Locale - indice qui se déplace dans la matrice                      */
/* nb_align - Locale - compteur qui indique le nombre de pions alignés                */
/* gagne - Entier - indique si le joueur a gagné ou non                               */
/**************************************************************************************/
int alignement(char mat[][19], struct point p , char j){//1 si alignement, 0 sinon

  int gagne = 0;
  int x = p.x;
  int y = p.y;
    //cas à droite
    int nvx = x+1;
    int nb_align = 1;
    while(mat[nvx][y]==j && !gagne){

        nb_align += 1;
        nvx += 1;
        if (nb_align == 5)
            gagne = 1;
    }

    //cas à gauche
    nvx = x-1;
    while(mat[nvx][y]==j && !gagne){
        nb_align += 1;
        nvx -= 1;
        if (nb_align == 5)
            gagne = 1;
    }

    //cas en haut
    int nvy = y+1;
    nb_align = 1;
    while(mat[x][nvy]==j && !gagne){
        nb_align += 1;
        nvy += 1;
        if (nb_align == 5)
            gagne = 1;
    }

    //cas en bas
    nvy = y-1;
    while(mat[x][nvy]==j && !gagne){
        nb_align += 1;
        nvy -= 1;
        if (nb_align == 5)
            gagne = 1;
    }

    //cas diag bas droite
    nvx = x+1;
    nvy = y+1;
    nb_align = 1;
    while(mat[nvx][nvy]==j && !gagne){
        nb_align += 1;
        nvx += 1;
        nvy += 1;
        if (nb_align == 5)
            gagne = 1;
    }

    //cas diag haut gauche
    nvx = x-1;
    nvy = y-1;
    while(mat[nvx][nvy]==j && !gagne){
        nb_align += 1;
        nvx -= 1;
        nvy -= 1;
        if (nb_align == 5)
            gagne = 1;
    }

    //cas diag bas gauche
    nvx = x-1;
    nvy = y+1;
    nb_align = 1;
    while(mat[nvx][nvy]==j && !gagne){
        nb_align += 1;
        nvx -= 1;
        nvy += 1;
        if (nb_align == 5)
            gagne = 1;
    }

    //cas diag haut droite
    nvx = x+1;
    nvy = y-1;
    while(mat[nvx][nvy]==j && !gagne){
        nb_align += 1;
        nvx += 1;
        nvy -= 1;
        if (nb_align == 5)
            gagne = 1;
    }

  return(gagne);
}



/******************************************************************************************/
/* Action prise                                                                           */
/* Modifie le plateau en fonction des prises et incrémente le compteur de prise           */
/* mat : Matrice[19][19] de caractère  - Donnee/Resultat - plateau à traiter              */
/* p - Point - coordonnées du dernier coup joué                                           */
/* cmpt : Entier - Donnee/Resultat - compteur de prise                                    */
/* jou : Caractère - Donnée - joueur                                                      */
/******************************************************************************************/

void prise(char mat[19][19], struct point p, int* cmtp, char jou)
{
  int x = p.x;
  int y = p.y;

  /* Ligne gauche */
  if (y>2)
    {
      if (mat[x][y-3] == jou)
	{
	  if (mat[x][y-1] != '.' && mat[x][y-1] != jou)
	    {
	      if (mat[x][y-2] != '.' && mat[x][y-2] != jou)
		{
		  mat[x][y-1] = '.';
		  mat[x][y-2] = '.';
		  *cmtp = *cmtp + 2;
		}
	    }
	}
    }

  /* Ligne droite */
  if (y<16)
    {
      if (mat[x][y+3] == jou)
	{
	  if (mat[x][y+1] != '.' && mat[x][y+1] != jou)
	    {
	      if (mat[x][y+2] != '.' && mat[x][y+2] != jou)
		{
		  mat[x][y+1] = '.';
		  mat[x][y+2] = '.';
		  *cmtp = *cmtp + 2;
		}
	    }
	}
    }

  /* Colonne basse */
  if (x<16)
    {
      if (mat[x+3][y] == jou)
	{
	  if (mat[x+1][y] != '.' && mat[x+1][y] != jou)
	    {
	      if (mat[x+2][y] != '.' && mat[x+2][y] != jou)
		{
		  mat[x+1][y] = '.';
		  mat[x+2][y] = '.';
		  *cmtp = *cmtp + 2;
		}
	    }
	}
    }

   /* Colonne haute */
  if (x>2)
    {
      if (mat[x-3][y] == jou)
	{
	  if (mat[x-1][y] != '.' && mat[x-1][y] != jou)
	    {
	      if (mat[x-2][y] != '.' && mat[x-2][y] != jou)
		{
		  mat[x-1][y] = '.';
		  mat[x-2][y] = '.';
		  *cmtp = *cmtp + 2;
		}
	    }
	}
    }

  /* Diagonale haut-gauche */
  if (x>2 && y>2)
    {
      if (mat[x-3][y-3] == jou)
	{
	  if (mat[x-1][y-1] != '.' && mat[x-1][y-1] != jou)
	    {
	      if (mat[x-2][y-2] != '.' && mat[x-2][y-2] != jou)
		{
		  mat[x-1][y-1] = '.';
		  mat[x-2][y-2] = '.';
		  *cmtp = *cmtp + 2;
		}
	    }
	}
    }

  /* Diagonale haut-droit */
  if (x>1 && y<16)
    {
      if (mat[x-3][y+3] == jou)
	{
	  if (mat[x-1][y+1] != '.' && mat[x-1][y+1] != jou)
	    {
	      if (mat[x-2][y+2] != '.' && mat[x-2][y+2] != jou)
		{
		  mat[x-1][y+1] = '.';
		  mat[x-2][y+2] = '.';
		  *cmtp = *cmtp + 2;
		}
	    }
	}
    }

  /* Diagonale bas-droit */
  if (x<16 && y<16)
    {
      if (mat[x+3][y+3] == jou)
	{
	  if (mat[x+1][y+1] != '.' && mat[x+1][y+1] != jou)
	    {
	      if (mat[x+2][y+2] != '.' && mat[x+2][y+2] != jou)
		{
		  mat[x+1][y+1] = '.';
		  mat[x+2][y+2] = '.';
		  *cmtp = *cmtp + 2;
		}
	    }
	}
    }

  /* Diagonale bas-gauche */
  if (x<16 && y>2)
    {
      if (mat[x+3][y-3] == jou)
	{
	  if (mat[x+1][y-1] != '.' && mat[x+1][y-1] != jou)
	    {
	      if (mat[x+2][y-2] != '.' && mat[x+2][y-2] != jou)
		{
		  mat[x+1][y-1] = '.';
		  mat[x+2][y-2] = '.';
		  *cmtp = *cmtp + 2;
		}
	    }
	}
    }
}




int main(){

    //création & affichage matrice
    char mat[19][19];
    remplir_matrice_vide(mat);
    affichage(mat);

    //variables générales
    int gagne_O = 0, gagne_X = 0;
    int compt_O = 0, compt_X = 0;
    int numtour = 1;
    struct point p = { 0 };
    char player;

    //tour
    while (!gagne_O && !gagne_X && !matrice_pleine(mat)){
      printf("Tour n°%d\n", numtour);
        //tour pair : X
        if (numtour%2 == 0){
            player = 'X';
            saisie_correcte(&p, &gagne_O, player);
            jouer_coup(mat, p, player, &gagne_O);
            if (!gagne_O){
                gagne_X = alignement(mat, p, player);
                if (!gagne_X){
                    prise(mat, p, &compt_X, player);
                    if (compt_X > 9)
                        gagne_X = 1;
                }
            }
        }

        //tour impair : O
        else{
            player = 'O';
            saisie_correcte(&p, &gagne_X, player);
            jouer_coup(mat, p, player, &gagne_X);
            if (!gagne_X){
                gagne_O = alignement(mat, p, player);
                if (!gagne_O){
                    prise(mat, p, &compt_O, player);
                    if (compt_O > 9)
                        gagne_O = 1;
                }
            }
        }
        numtour ++;
        affichage(mat);
        printf("Nombre de prise du joueur O : %d\n", compt_O);
        printf("Nombre de prise du joueur X : %d\n\n\n", compt_X);
    }

    //qui a gagné ?
    if (gagne_O)
        printf("\nVictoire du joueur O !\n");
    else if (gagne_X)
        printf("\nVictoire du joueur X !\n");
    else
        printf(“\nÉgalité le plateau de jeu est plein !\n”);
    return 0;
}



