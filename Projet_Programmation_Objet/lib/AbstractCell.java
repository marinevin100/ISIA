package lib;

public class AbstractCell {
    private int row;
    private int col;
    private boolean revealed;
    private boolean bomb;
    private int nbBombAdj;

    public AbstractCell(int row, int col){
        this.row = row;
        this.col = col;
        this.revealed = false;
        this.bomb = false;
        this.nbBombAdj= 0;
    }

    public boolean isRevealed(){
        return this.revealed;
    }

    public void reveal(){
        this.revealed = true;
    }


    public boolean isBomb(){
        return this.bomb;
    }
    

    public void devientBomb(){
        this.bomb = true;
    }

    public int getAdjacentBombs(){
        return this.nbBombAdj;
    }

    public void addBombAdj(){
        this.nbBombAdj += 1;
    }

    
}