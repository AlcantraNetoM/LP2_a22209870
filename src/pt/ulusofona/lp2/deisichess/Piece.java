package pt.ulusofona.lp2.deisichess;


public class Piece {

    public int id;
    public int type;
    public int team;

    public String nickName;


    //CAMINHO DAS IMAGENS DAS PEÇAS
    private final String BLACK_QUEEN_IMAGE_PATH = "icons8-red-queen-50.png";
    private final String WHITE_QUEEN_IMAGE_PATH = "icons8-blue-queen-50.png";
    //Put the image in src/Image folder then use filename with the xtension

    public String avatarImgPath = null;

    public Piece(int id, int type, int team, String nickName) {
        this.id = id;
        this.type = type;
        this.team = team;
        this.nickName = nickName;

        if (team == 0){
            avatarImgPath = BLACK_QUEEN_IMAGE_PATH;
        }else {
            avatarImgPath = WHITE_QUEEN_IMAGE_PATH;
        }
    }

    /*
    public Piece(int id, int type, int team, String nickName, String avatarImgPath) {
        this.id = id;
        this.type = type;
        this.team = team;
        this.nickName = nickName;
        this.avatarImgPath = avatarImgPath;
    }

     */


    public String toString(){
        if (avatarImgPath != null){
            return id+":"+type+":"+team+":"+nickName+":"+avatarImgPath;
        }
        return id+":"+type+":"+team+":"+nickName;
    }

    public String[] toStringInfo(){
        return new String[]{String.valueOf(id),String.valueOf(type),String.valueOf(team),nickName,avatarImgPath};
    }

}
