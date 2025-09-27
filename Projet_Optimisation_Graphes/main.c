#include "Structures/Graphes/graphe.h"
#include "Structures/File/file.h"

#define int_max 2147483647

/*****************************************************/
/* Construit le graphe résiduel                      */
/* Donnée - n - Entier - nombre de sommets du graphe */
/* Résultat - g - Graphe - graphe résiduel           */
/*****************************************************/
struct graphe* buildRG(int *nodes)
{
  //Initialisation 
  char c;
  int u, v, capa;
  int links;
  int node;
  char role;
  bool fin = false; //Arrete la premiere boucle 

  //Traitement
  while (!fin) //Premiere boucle qui s'arrete quand on trouve les variables du problème (nombre de sommets/arc)
    {
      scanf(" %c", &c);


      if (c == 'c') { //Commentaire
        char ch;
        printf("Commentaire : ");
        while ((ch = getchar()) != '\n' && ch != EOF) {
	  putchar(ch);
        }
        putchar('\n'); 
      } else if (c == 'p') //Problème
	{
	  scanf("%d %d", nodes, &links); 
	  printf("Problème : %d noeuds, %d arcs\n", *nodes, links);
	  fin = true;
	}
    }
  
  struct graphe *g = init_graphe(*nodes); 
  
  while (scanf(" %c", &c) == 1) {
    if (c == 'c') { //Commentaire
        char ch;
        printf("Commentaire : ");
        while ((ch = getchar()) != '\n' && ch != EOF) {
            putchar(ch);
        }
        putchar('\n');
    } else if (c == 'n') { //Source et puit
        if (scanf("%d %c", &node, &role) == 2) {
            if (role == 's') {
                printf("Source : %d\n", node);
            } else if (role == 't') {
                printf("Puit : %d\n", node);
            }
        }
    } else if (c == 'a') { //Arc à ajouter
        if (scanf("%d %d %d", &u, &v, &capa) == 3) {
            ajouter_en_queue(g->tab[u - 1], v - 1, capa);
            ajouter_en_queue(g->tab[v - 1], u - 1, 0); // arc retour capacité 0
        }
    } else {
      while (getchar() != '\n'); //On finit de lire la ligne
    }
}

  return g;
  
}

/************************************************************************************************************/
/* Trouve le plus court chemin en nombre d’arc                                                              */
/* Donnée - g - Graphe - graphe résiduel                                                                    */
/* Donnée - s - Entier - sommet source du réseau                                                            */
/* Donnée - p - Entier - sommet puit du réseau                                                              */
/* Résultat - parents[] - Tableau d’entier - Tableau contenant les parents de chaque sommets dans le chemin */
/************************************************************************************************************/
int* shortestPath(struct graphe *g, int s, int p)
{
  //Initialisation
  int n = g->size;

  int checked[n]; //sommet déjà visité -> 1 et non visité -> 0
  for (int i=0; i<n; i++)
    {
      checked[i] = 0;
    }
  
  int* parents = malloc(n * sizeof(int));
  for (int i=0; i<n; i++)
    {
      parents[i] = -1; //On définit ici -1 signifie qu’il n’y a pas de successeurs connus
    }
  struct file *Q = init_file(n);
  
  //Traitement
  enfiler(Q, s);
  checked[s] = 1;

  int u = s;

  while (!est_vide(Q) && u != p) //On s'arrete si on est arrivé au puit ou si Q est vide
    {
      u = defiler(Q);
      struct listeC *L = g->tab[u];
      struct maillon *M = L->first;
      
      while (M != NIL)
	{
	  if (M->capa > 0 && checked[M->cle] == 0) //Si le voisin a une capacité non nul et qu'il n'a pas été visité
	    {                                      //Rappel : Si la capacité est nul l'arc n'existe pas réellement dans le graphe
	      enfiler(Q, M->cle);
	      parents[M->cle] = u;
	      checked[M->cle] = 1;
	    }
	  M = M->next;
	}
    }

  free_file(Q);
  
  return parents;
}


/****************************************************/
/* Donne le min entre a et b                        */
/* Donnée - a - Entier - premier entier à comparer  */
/* Donnée - b - Entier - deuxieme entier à comparer */
/* Résultat - som - Entier - somme de a et b        */
/****************************************************/
int min(int a, int b)
{
  if (a>b)
    return b;
  else
    return a;
}


/**********************************************************************************************************/
/* Détermine l’arc de plus petite capacité le long du chemin donné                                        */
/* Donnée - g - Graphe - graphe résiduel                                                                  */
/* Donnée - parents[] - Tableau d’entier - Tableau contenant les parents de chaque sommets dans le chemin */
/* Résultat - k - Entier - plus petite capacité le long du chemin donné                                   */
/**********************************************************************************************************/

int minCapa(struct graphe *g, int* parents)
{
  //Initialisation
  int u;
  int k = int_max;
  int v = g->size - 1;

  //Traitement
  while (v != 0) //On s'arrete quand on est remonté au sommet puit
    {
      u = parents[v];
      struct maillon *M = g->tab[u]->first;
      while (M != NIL && M->cle != v)  //On cherche l'arc (uv)
	{
	  M = M->next;
	}

      k = min(k, M->capa);
      v = u;
    }

  return k;
}

/**********************************************************************************************************/
/* Met à jour le graphe d’écart.                                                                          */
/* Donnée/Résultat - g - Graphe - graphe résiduel                                                         */
/* Donnée - parents[] - Tableau d’entier - Tableau contenant les parents de chaque sommets dans le chemin */
/* Donnée - k - Entier - plus petite capacité le long du chemin donné                                     */
/**********************************************************************************************************/
void updateFlowInRG(struct graphe *g, int* parents, int k)
{
  //Initialisation
  int u;
  int v = g->size - 1;

  //Traitement
  while (v!=0) //On remonte jusqu'au puit
    {
      u = parents[v];

      struct maillon *M = g->tab[v]->first;
      while (M != NIL && M->cle != u) // On cherche l'arc (vu)
	{
	  M = M->next;
	}
      if (M != NIL)
	M->capa += k;
      
      M = g->tab[u]->first;
      while (M != NIL && M->cle != v) // On cherche l'arc (uv)
	{
	  M = M->next;
	}
      if (M != NIL)
	M->capa -= k;

      v = u;  
    }
}

int main()
{
  int flopopt = 0;
  int k;
  int nodes = 0;
  struct graphe *g = buildRG(&nodes); 

  int* parents = shortestPath(g, 0, nodes-1);

  while (parents[nodes-1] != -1)
    {
      k = minCapa(g, parents);
      flopopt += k;
      updateFlowInRG(g, parents, k);

      free(parents);
      parents = shortestPath(g, 0, nodes-1);
    }

  FILE *fp = fopen("resultat.txt", "w");

  afficher_fichier_flux(g, fp);
  fprintf(fp, "flot opt : %d\n", flopopt);

  fclose(fp);

  free(parents);
  free_graphe(g);

  return 0;
}
