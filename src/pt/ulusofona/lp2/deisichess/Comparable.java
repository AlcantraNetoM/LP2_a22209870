package pt.ulusofona.lp2.deisichess;

public class Comparable implements java.lang.Comparable<Comparable> {

    int x;
    int y;
    int value;
    int type;
    public Comparable(int x, int y, int value, int type){
        this.x = x;
        this.y = y;
        this.value = value;
        this.type = type;
    }


    public int getValue(){
        return this.value;
    }

    @Override
    public int compareTo(Comparable chessCoord) {
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
        if (type == 10){
            return "Sou o John McClane. Yippee ki yay. Sou duro de roer, mas não me sei mover";
        }

        return "("+y+", "+x+") -> "+value;
    }

}
