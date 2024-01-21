package pt.ulusofona.lp2.deisichess;

public class InvalidTeamException extends Exception{


    public InvalidTeamException(String message){
        super(message);
    }

    public String getInvalidPieceName(){
        return null;
    }

}
