#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// à partir du point de coordonnées qui vient d'être joué par le joueur j : x,y
int alignement(mat, x, y, j){//1 si alignement, 0 sinon
    int gagne = 0;

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
    nb_align = 1;
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
    nb_align = 1;
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
    while(mat[nvx][nvy]==y && !gagne){
        nb_align += 1;
        nvx += 1;
        nvy += 1;
        if (nb_align == 5)
            gagne = 1;
    }

    //cas diag bas gauche
    nvx = x-1;
    nvy = y+1;
    nb_align = 1;
    while(mat[nvx][nvy]==y && !gagne){
        nb_align += 1;
        nvx -= 1;
        nvy += 1;
        if (nb_align == 5)
            gagne = 1;
    }

    //cas diag haut droite
    nvx = x+1;
    nvy = y-1;
    nb_align = 1;
    while(mat[nvx][nvy]==y && !gagne){
        nb_align += 1;
        nvx += 1;
        nvy -= 1;
        if (nb_align == 5)
            gagne = 1;
    }

    //cas diag haut gauche
    nvx = x-1;
    nvy = y-1;
    nb_align = 1;
    while(mat[nvx][nvy]==y && !gagne){
        nb_align += 1;
        nvx -= 1;
        nvy -= 1;
        if (nb_align == 5)
            gagne = 1;
    }
}
