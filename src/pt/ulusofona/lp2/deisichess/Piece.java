package pt.ulusofona.lp2.deisichess;


public class Piece {

    public int id;

    public String name;

    public int value;
    public int type;
    public int team;
    public String nickName;



    //CAMINHO DAS IMAGENS DAS PEÇAS
    //Put the image in src/Image folder then use filename with the xtension

    public String avatarImgPath = null;

    public Piece(int id, int type, int team, String nickName) {
        this.id = id;
        this.type = type;
        this.team = team;
        this.nickName = nickName;


        fillComponents();
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
                name = "Homer";
                value = 1000;
                break;
            case 7:
                name = "Joker/";
                value = 0;
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
            }
        } else {
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
            }
        }
    }

    public String toString(){
        if (avatarImgPath != null){
            return id+":"+type+":"+team+":"+nickName+":"+avatarImgPath;
        }
        return id+":"+type+":"+team+":"+nickName;
    }

    public String[] toStringInfo(){
        return new String[]{
                String.valueOf(id),
                String.valueOf(type),
                String.valueOf(team),
                nickName, avatarImgPath};
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
}
