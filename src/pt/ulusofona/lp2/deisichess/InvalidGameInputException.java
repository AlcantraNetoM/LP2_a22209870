package pt.ulusofona.lp2.deisichess;

public class InvalidGameInputException extends Throwable {


    public int getLineWithError(){
        return 0;
    }

    public String getProblemDescription(){
        return null;
    }
}
