package pt.ulusofona.lp2.deisichess;

public class InvalidGameInputException extends Exception {

    public InvalidGameInputException(String message){
        super(message);
    }

    public int getLineWithError(){
        return 0;
    }

    public String getProblemDescription(){
        return null;
    }
}
