package pt.ulusofona.lp2.deisichess;


public class Piece {

    private int id;

    private String name;

    private int value;
    private int type;
    private int team;
    private String nickName;
    private String pieceInfo;
    private int coordinateX;
    private int coordinateY;

    private int johnMcClainCounter = 3;


    //CAMINHO DAS IMAGENS DAS PEÇAS
    //Put the image in src/Image folder then use filename with the xtension

    public String avatarImgPath = null;

    public Piece(int id, int type, int team, String nickName) {
        this.id = id;
        this.type = type;
        this.team = team;
        this.nickName = nickName;

        fillComponents();

        updatePieceInfo();
    }


    public void updatePieceInfo(){

        if (getType() == 0){
            pieceInfo = id+" | "+name+" | (infinito) | "+team+" | "+nickName+" @ "+getCoordinates();
        }
        else{
            pieceInfo = id+" | "+name+" | "+value+" | "+team+" | "+nickName+" @ "+getCoordinates();
        }

    }


    public String getCoordinates(){
        if (isPieceCaptured()){
           return "(n/a)";
        }else{
            return "("+getCoordinateX()+", "+getCoordinateY()+")";
        }
    }


    private void fillComponents() {

        switch (type) {
            case 0:
                name = "Rei";
                value = 1000;
                break;
            case 1:
                name = "Rainha";
                value = 8;
                break;
            case 2:
                name = "Ponei Mágico";
                value = 5;
                break;
            case 3:
                name = "Padre da Vila";
                value = 3;
                break;
            case 4:
                name = "TorreHor";
                value = 3;
                break;
            case 5:
                name = "TorreVert";
                value = 3;
                break;
            case 6:
                name = "Homer Simpson";
                value = 2;
                break;
            case 7:
                name = "Joker/";
                value = 4;
                break;
            case 10:
                name = "John McClane";
                value = 20;
                break;
        }

        if (team == 10) {
            switch (type) {
                case 0:
                    avatarImgPath = "crazy_emoji_black.png";
                    break;
                case 1:
                    avatarImgPath = "rainha_black.png";
                    break;
                case 2:
                    avatarImgPath = "ponei_magico_black.png";
                    break;
                case 3:
                    avatarImgPath = "padre_vila_black.png";
                    break;
                case 4:
                    avatarImgPath = "torre_h_black.png";
                    break;
                case 5:
                    avatarImgPath = "torre_v_black.png";
                    break;
                case 6:
                    avatarImgPath = "homer_black.png";
                    break;
                case 7:
                    avatarImgPath = "joker_black.png";
                    break;
                case 10:
                    avatarImgPath = "icons8-king-50-black.png";
                    break;
            }
        }
        else if (team == 20){
            switch (type) {
                case 0:
                    avatarImgPath = "crazy_emoji_white.png";
                    break;
                case 1:
                    avatarImgPath = "rainha_white.png";
                    break;
                case 2:
                    avatarImgPath = "ponei_magico_white.png";
                    break;
                case 3:
                    avatarImgPath = "padre_vila_white.png";
                    break;
                case 4:
                    avatarImgPath = "torre_h_white.png";
                    break;
                case 5:
                    avatarImgPath = "torre_v_white.png";
                    break;
                case 6:
                    avatarImgPath = "homer_white.png";
                    break;
                case 7:
                    avatarImgPath = "joker_yellow.png";
                    break;
                case 10:
                    avatarImgPath = "icons8-king-50-white.png";
                    break;
            }
        }
        else {
            switch (type) {
                case 0:
                    avatarImgPath = "crazy_emoji_yellow.png";
                    break;
                case 1:
                    avatarImgPath = "rainha_yellow.png";
                    break;
                case 2:
                    avatarImgPath = "ponei_magico_yellow.png";
                    break;
                case 3:
                    avatarImgPath = "padre_vila_yellow.png";
                    break;
                case 4:
                    avatarImgPath = "torre_h_yellow.png";
                    break;
                case 5:
                    avatarImgPath = "torre_v_yellow.png";
                    break;
                case 6:
                    avatarImgPath = "homer_yellow.png";
                    break;
                case 7:
                    avatarImgPath = "joker_yellow.png";
                    break;
                case 10:
                    avatarImgPath = "unknown-piece.png";
                    break;
            }
        }
    }

    public String toString(){
        return pieceInfo;
    }

    public String[] toStringInfo(){
        return new String[]{
                String.valueOf(id),
                String.valueOf(type),
                String.valueOf(team),
                nickName, avatarImgPath};
    }


    public void setPieceInfo(String pieceInfo){
        this.pieceInfo = pieceInfo;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public int getType() {
        return type;
    }

    public int getTeam() {
        return team;
    }

    public String getNickName() {
        return nickName;
    }

    public int getCoordinateX() {
        return coordinateX;
    }

    public int getCoordinateY() {
        return coordinateY;
    }


    public boolean isPieceCaptured(){
        if (getCoordinateX() == -1 && getCoordinateY() == -1){
            return true;
        }
        else {
            return false;
        }
    }

    public void setCoordinateX(int coordinateX) {
        this.coordinateX = coordinateX;
    }

    public void setCoordinateY(int coordinateY) {
        this.coordinateY = coordinateY;
    }

    public boolean isJonhMcClainProtected(){
        if (johnMcClainCounter != 0){
            --johnMcClainCounter;
            return true;
        }
        else{
            return false;
        }
    }

}
