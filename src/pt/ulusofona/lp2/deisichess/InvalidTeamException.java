package pt.ulusofona.lp2.deisichess;

public class InvalidTeamException extends Exception{


    String nickName;
    public InvalidTeamException(String message, String nickName){
        super(message);
        this.nickName = nickName;
    }

    public String getInvalidPieceName(){
        return nickName;
    }

}
