package pt.ulusofona.lp2.deisichess;

public class ChessCoordinates implements Comparable<ChessCoordinates> {

    int x;
    int y;
    int value;
    public ChessCoordinates(int x, int y, int value){
        this.x = x;
        this.y = y;
        this.value = value;
    }


    public int getValue(){
        return this.value;
    }

    @Override
    public int compareTo(ChessCoordinates chessCoord) {
        if (this.value == chessCoord.getValue()){
            return 0;
        }
        else if (this.value > chessCoord.getValue()) {
            return 1;
        }
        else{
            return -1;
        }
    }

    public String toString(){
        return "("+y+", "+x+") -> "+value;
    }

}
