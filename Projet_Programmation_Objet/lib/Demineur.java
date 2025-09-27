package lib;

import lib.AbstractCell;
import java.util.Random;
import exceptions.*;


public class Demineur {
    private int row;
    private int col;
    private int nbBomb;
    private int nbClick = 0;
    private int nbRevealed = 0;
    private AbstractCell[][] jeu;


    public Demineur(int row, int col, int nbMines){
        this.row = row;
        this.col = col;
        this.nbBomb = nbMines;

        fillGrid();
    }

    public void fillGrid(){
        //tableau de jeu
        this.jeu = new AbstractCell[this.getRowCount()][this.getColCount()];

        for(int i=0;i<this.getRowCount();i++){
            for(int j=0;j<this.getColCount();j++){
                this.jeu[i][j] = new AbstractCell(i,j);
            }
        }
        

        //placer les bombes
        Random randomNumbers = new Random();
        int i = 0;
        while (i<nbBomb){
            int ligne = randomNumbers.nextInt(row); 
            int colonne = randomNumbers.nextInt(col);
            //au cas où on tombe sur une case qui est déjà une bombe
            if (!(this.getCell(ligne, colonne).isBomb())){
                this.getCell(ligne, colonne).devientBomb();
                i += 1;
                
                //ajt une bombe adjacente aux voisins 
                for(int k=-1; k<=1; k++){
                    for(int l=-1; l<=1; l++){
                        if(ligne+k >= 0 && ligne+k < this.getRowCount() && colonne+l >= 0 && colonne+l < this.getColCount()){
                            this.getCell(ligne+k, colonne+l).addBombAdj();
                        }
                    }
                }
            }
        }
    }

    public int getRowCount(){
        return this.row;
    }

    public int getColCount(){
        return this.col;
    }

    public int getBombCount(){
        return this.nbBomb;
    }

    public int getClickCount(){
        return this.nbClick;
    }

    public void addClickCount(){
        this.nbClick += 1;
    }

    public int getRevealedCount(){
        return this.nbRevealed;
    }

    public void addRevealedCount(){
        this.nbRevealed += 1;
    }

    public void revealCell(int row, int col) throws InvalidMoveException,GameLostException,GameWonException{
        if (this.getCell(row,col).isRevealed()){
            throw new InvalidMoveException("Oups! Case déjà révelée...");
        }
            this.addClickCount();;
            this.getCell(row,col).reveal();
            this.addRevealedCount();

            //on perd si on est sur une bombe
            if (this.getCell(row,col).isBomb()){
                throw new GameLostException();
            }

            //on gagne si on a révelé toutes les cases sauf les bombes
            if (this.getRevealedCount() == this.getRowCount() * this.getColCount() - this.getBombCount()){
                throw new GameWonException();
            }
    }

    
    public int[] attemptRandomReveal() {
        this.addClickCount();

        Random randomNumbers = new Random();
        int ligne = randomNumbers.nextInt(row); 
        int colonne = randomNumbers.nextInt(col);

        //on cherche une case pas déjà révélée
        while (this.getCell(ligne, colonne).isRevealed()){
            ligne = randomNumbers.nextInt(row)+1; 
            colonne = randomNumbers.nextInt(col)+1;
        }

        //on renvoie si la case n'est pas une bombe
        if (!(this.getCell(ligne, colonne).isBomb())){
            int[] coord = {ligne, colonne};
            return coord;
        }else{
            return null;
        }


    }

    
    public AbstractCell getCell(int row, int col){
        return this.jeu[row][col];
    }

    
}