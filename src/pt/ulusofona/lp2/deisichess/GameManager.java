package pt.ulusofona.lp2.deisichess;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameManager {


    private final int BLACK_PIECE = 10;
    private final int WHITE_PIECE = 20;
    private final int BLACK_PIECES_ELIMINATED_CONTROLLER = 0;
    private final int WHITE_PIECES_ELIMINATED_CONTROLLER = 1;
    private final int LIMIT_OF_MOVES_BY_PIECES_CONTROLLER = 2;
    private final int BLACK_PIECES_VALID_MOVES_COUNTER = 3;
    private final int WHITE_PIECES_VALID_MOVES_COUNTER = 4;
    private final int BLACK_PIECES_INVALID_MOVES_COUNTER = 5;
    private final int WHITE_PIECES_INVALID_MOVES_COUNTER = 6;

    ArrayList<String> chessInfo = new ArrayList<>();
    HashMap<Integer, String> piecesTypeDictionary = new HashMap<>();
    HashMap<Integer,Piece> piecesDictionary = new HashMap<>();
    HashMap<Integer, HashMap<Integer,Integer>> chessMatrix = new HashMap<>();

    //Controla o número de peças eliminadas e jogadas em casas vazias
    HashMap<Integer, Integer> piecesCounter = new HashMap<>();

    int initialLine = 2;
    String result;
    int currentTeam = BLACK_PIECE;

    //Leitor dos arquivos de informação do Jogo
    public void loadGame(File file) throws InvalidGameInputException, IOException{

        Scanner leitor = new Scanner(file);

            while(leitor.hasNextLine()){
                chessInfo.add(leitor.nextLine());
            }

            //Adiciona as informações das Peças numa lista de objectos
            insertPiecesInfo();
            insertChessMatrix();

            //Controla o número de peças eliminadas
            piecesCounter.put(BLACK_PIECES_ELIMINATED_CONTROLLER, getPiecesSize()/2);
            piecesCounter.put(WHITE_PIECES_ELIMINATED_CONTROLLER, getPiecesSize()/2);


            //Controla se as 10 jogadas foram feitas
            piecesCounter.put(LIMIT_OF_MOVES_BY_PIECES_CONTROLLER,0);

            //Conta o número de jogadas válidas de cada equipa
            piecesCounter.put(BLACK_PIECES_VALID_MOVES_COUNTER, 0);
            piecesCounter.put(WHITE_PIECES_VALID_MOVES_COUNTER, 0);

            //Conta o número de jogadas inválidas de cada equipa
            piecesCounter.put(BLACK_PIECES_INVALID_MOVES_COUNTER, 0);
            piecesCounter.put(WHITE_PIECES_INVALID_MOVES_COUNTER, 0);


    }

    /*
    private boolean isGameValid(){
        String numberValidator = "\\d+";
        String pieceValidator = "\\d+:\\d+:\\d+:[\\p{L}\\s]+";
        Pattern pattern = Pattern.compile(numberValidator);

        if (pattern.matcher(chessInfo.get(0)).matches()) {

        } else {
            throw new InvalidGameInputException("Error");
        }

        if (pattern.matcher(chessInfo.get(1)).matches()) {

        } else {
            throw new InvalidGameInputException("Error");
        }

    }

     */

    public int getBoardSize(){
        return Integer.parseInt(chessInfo.get(0));
    }


    //Fazer jogada
    public boolean move(int x0, int y0, int x1, int y1){

        //Peça a jogar
        Piece piece = piecesDictionary.get(chessMatrix.get(y0).get(x0));

        //Peça na posição ocupada
        Piece nextPiece = piecesDictionary.get(chessMatrix.get(y1).get(x1));


        if (piece.team == currentTeam){

            if (nextPiece != null && nextPiece.team == piece.team){
                if (piece.team == BLACK_PIECE){

                    int currentResult = piecesCounter.get(BLACK_PIECES_INVALID_MOVES_COUNTER);
                    piecesCounter.put(BLACK_PIECES_INVALID_MOVES_COUNTER, ++currentResult);
                }
                else {
                    int currentResult = piecesCounter.get(WHITE_PIECES_INVALID_MOVES_COUNTER);
                    piecesCounter.put(WHITE_PIECES_INVALID_MOVES_COUNTER, ++currentResult);
                }
                return false;
            }



            //Coloca a peça Rei na nova posição
            if (piece.getType() == 0){
                //Coloca o Rei na Diagonal
                if ((Math.abs(y1-y0) == Math.abs(x1-x0)) && Math.abs(y1-y0) <= 1){
                    for (int i = x0, j=y0 ; i != x1;){
                        if (i < x1 && j < y1){
                            ++i;
                            ++j;
                            if (chessMatrix.get(j).get(i) != 0) {
                                return false;
                            }

                        }
                        else if (i > x1 && j > y1) {
                            --i;
                            --j;
                            if (chessMatrix.get(j).get(i) != 0) {
                                return false;
                            }
                        }
                        else if (i > x1 && j < y1){
                            --i;
                            ++j;
                            if (chessMatrix.get(j).get(i) != 0) {
                                return false;
                            }
                        }
                        else if (i < x1 && j > y1){
                            ++i;
                            --j;
                            if (chessMatrix.get(j).get(i) != 0) {
                                return false;
                            }
                        }
                    }
                    movePiece(y1,y0,x1,x0,piece);
                }

                //Coloca o Rei na horizontal
                else if ((y1 == y0) && (Math.abs(x1 - x0) <= 1)){
                    for (int i = x0; i != x1;){
                        if (i < x1){
                            ++i;
                            if (chessMatrix.get(y1).get(i) != 0) {
                                return false;
                            }
                        }
                        else{
                            --i;
                            if (chessMatrix.get(y1).get(i) != 0) {
                                return false;
                            }
                        }
                    }
                    movePiece(y1,y0,x1,x0,piece);
                }

                //Coloca o Rei na Vertical
                else if ((x1 == x0) && (Math.abs(y1-y0) <= 1)){
                    for (int j = y0; j != y1;){
                        if (j < y1){
                            ++j;
                            if (chessMatrix.get(j).get(x1) != 0) {
                                return false;
                            }
                        }
                        else{
                            --j;
                            if (chessMatrix.get(j).get(x1) != 0) {
                                return false;
                            }
                        }
                    }
                    movePiece(y1,y0,x1,x0,piece);
                }

                else{
                    return false;
                }
            }

            //Coloca a peça da Rainha na nova Posição
            else if (piece.getType() == 1){

                //Coloca da rainha na posição diagonal
                if ((Math.abs(y1-y0) == Math.abs(x1-x0)) && Math.abs(y1-y0) <= 5){
                    for (int i = x0, j=y0 ; i != (x1-1);){
                        if (i < x1 && j < y1){
                            ++i;
                            ++j;
                            if (chessMatrix.get(j).get(i) != 0) {
                                return false;
                            }

                        }
                        else if (i > x1 && j > y1) {
                            --i;
                            --j;
                            if (chessMatrix.get(j).get(i) != 0) {
                                return false;
                            }
                        }
                        else if (i > x1 && j < y1){
                            --i;
                            ++j;
                            if (chessMatrix.get(j).get(i) != 0) {
                                return false;
                            }
                        }
                        else if (i < x1 && j > y1){
                            ++i;
                            --j;
                            if (chessMatrix.get(j).get(i) != 0) {
                                return false;
                            }
                        }
                    }
                    if (nextPiece != null && nextPiece.getType() == 1){
                        return false;
                    }
                    movePiece(y1,y0,x1,x0,piece);
                }

                //Coloca da rainha na horizontal
                else if ((y1 == y0) && (Math.abs(x1 - x0) <= 5)){
                    for (int i = x0; i != x1-1;){
                        if (i < x1){
                            ++i;
                            if (chessMatrix.get(y1).get(i) != 0) {
                                return false;
                            }
                        }
                        else{
                            --i;
                            if (chessMatrix.get(y1).get(i) != 0) {
                                return false;
                            }
                        }
                    }
                    if (nextPiece != null && nextPiece.getType() == 1){
                        return false;
                    }
                    movePiece(y1,y0,x1,x0,piece);
                }

                //Coloca da rainha na Vertical
                else if ((x1 == x0) && (Math.abs(y1-y0) <= 5)){
                    for (int j = y0; j != y1-1;){
                        if (j < y1){
                            ++j;
                            if (chessMatrix.get(j).get(x1) != 0) {
                                return false;
                            }
                        }
                        else{
                            --j;
                            if (chessMatrix.get(j).get(x1) != 0) {
                                return false;
                            }
                        }
                    }
                    if (nextPiece != null && nextPiece.getType() == 1){
                        return false;
                    }
                    movePiece(y1,y0,x1,x0,piece);
                }

                else{
                    return false;
                }
            }

            //Coloca a peça da Ponei Mágico na nova Posição
            else if (piece.getType() == 2){
                if (Math.abs(y1-y0) == 2 && Math.abs(x1-x0) == 2){

                    boolean isVerticalLineBlocked = false;
                    boolean isHorizontalLineBlocked = false;

                    for (int i = x0, j = y0; i != x1 || j != y1;){
                        if (!isVerticalLineBlocked){
                            if (j == y1) {
                                    if (i < x1) {
                                        ++i;
                                    } else {
                                        --i;
                                    }

                                    if (chessMatrix.get(j).get(i) != 0) {
                                        isVerticalLineBlocked = true;
                                        i = x0;
                                        j = y0;
                                    }
                            }
                            else{
                                if (j < y1){
                                    ++j;
                                }
                                else{
                                    --j;
                                }
                                if (chessMatrix.get(j).get(i) != 0) {
                                    isVerticalLineBlocked = true;
                                    j = y0;
                                    continue;
                                }
                            }
                        }
                        else{

                            if (i == x1){
                                if (j < y1) {
                                    ++j;
                                } else {
                                    --j;
                                }

                            }
                            else{
                                if (i < x1){
                                    ++i;
                                }
                                else{
                                    --i;
                                }
                            }
                            if (chessMatrix.get(j).get(i) != 0) {
                                isHorizontalLineBlocked = true;
                            }
                        }

                        if (isVerticalLineBlocked && isHorizontalLineBlocked){
                            return false;
                        }

                    }
                    movePiece(y1,y0,x1,x0,piece);
                }
                else{
                    return false;
                }
            }

            //Coloca a peça do Padre na Vila na nova Posição
            else if (piece.getType() == 3){
                if ((Math.abs(y1-y0) == Math.abs(x1-x0)) && Math.abs(y1-y0) <= 3){
                    for (int i = x0, j=y0 ; i != x1;){
                        if (i < x1 && j < y1){
                            ++i;
                            ++j;
                            if (chessMatrix.get(j).get(i) != 0) {
                                return false;
                            }

                        }
                        else if (i > x1 && j > y1) {
                            --i;
                            --j;
                            if (chessMatrix.get(j).get(i) != 0) {
                                return false;
                            }
                        }
                        else if (i > x1 && j < y1){
                            --i;
                            ++j;
                            if (chessMatrix.get(j).get(i) != 0) {
                                return false;
                            }
                        }
                        else if (i < x1 && j > y1){
                            ++i;
                            --j;
                            if (chessMatrix.get(j).get(i) != 0) {
                                return false;
                            }
                        }
                    }
                    movePiece(y1,y0,x1,x0,piece);
                }
                else{
                    return false;
                }
            }

            //Coloca a peça da Torre Horizontal na nova Posição
            else if (piece.getType() == 4){
                if ((y1 == y0)){
                    for (int i = x0; i != x1;){
                        if (i < x1){
                            ++i;
                            if (chessMatrix.get(y1).get(i) != 0) {
                                return false;
                            }
                        }
                        else{
                            --i;
                            if (chessMatrix.get(y1).get(i) != 0) {
                                return false;
                            }
                        }
                    }
                    movePiece(y1,y0,x1,x0,piece);
                }
                else{
                    return false;
                }
            }

            //Coloca a peça da Torre Vertical na nova Posição
            else if (piece.getType() == 5){
                if ((x1 == x0)){
                    for (int j = y0; j != y1;){
                        if (j < y1){
                            ++j;
                            if (chessMatrix.get(j).get(x1) != 0) {
                                return false;
                            }
                        }
                        else{
                            --j;
                            if (chessMatrix.get(j).get(x1) != 0) {
                                return false;
                            }
                        }
                    }
                    movePiece(y1,y0,x1,x0,piece);
                }
                else{
                    return false;
                }
            }

            //Coloca a peça Homer Simpson na nova Posição
            else if (piece.getType() == 6){
                if ((Math.abs(y1-y0) == Math.abs(x1-x0)) && Math.abs(y1-y0) <= 1){
                    for (int i = x0, j=y0 ; i != x1;){
                        if (i < x1 && j < y1){
                            ++i;
                            ++j;
                            if (chessMatrix.get(j).get(i) != 0) {
                                return false;
                            }

                        }
                        else if (i > x1 && j > y1) {
                            --i;
                            --j;
                            if (chessMatrix.get(j).get(i) != 0) {
                                return false;
                            }
                        }
                        else if (i > x1 && j < y1){
                            --i;
                            ++j;
                            if (chessMatrix.get(j).get(i) != 0) {
                                return false;
                            }
                        }
                        else if (i < x1 && j > y1){
                            ++i;
                            --j;
                            if (chessMatrix.get(j).get(i) != 0) {
                                return false;
                            }
                        }
                    }
                    movePiece(y1,y0,x1,x0,piece);
                }
                else{
                    return false;
                }
            }

            else if (piece.getType() == 7){
                System.out.println("Joker");
            }


            //Eliminar peça inimiga
            if (nextPiece != null){

                piecesCounter.put(LIMIT_OF_MOVES_BY_PIECES_CONTROLLER,0);
                piecesCounter.put(nextPiece.team, piecesCounter.get(nextPiece.team)-1);

            }
            else{
                piecesCounter.put(LIMIT_OF_MOVES_BY_PIECES_CONTROLLER,
                        piecesCounter.get(LIMIT_OF_MOVES_BY_PIECES_CONTROLLER)+1);
            }



            //Trocar a vez do jogo das peças
            if (piece.team == BLACK_PIECE){

                int currentResult = piecesCounter.get(BLACK_PIECES_VALID_MOVES_COUNTER);
                piecesCounter.put(BLACK_PIECES_VALID_MOVES_COUNTER, ++currentResult);
                currentTeam = WHITE_PIECE;

            }
            else {
                int currentResult = piecesCounter.get(WHITE_PIECES_VALID_MOVES_COUNTER);
                piecesCounter.put(WHITE_PIECES_VALID_MOVES_COUNTER, ++currentResult);
                currentTeam = BLACK_PIECE;
            }


            return true;
        }

        if (piece.team == BLACK_PIECE){
            int currentResult = piecesCounter.get(BLACK_PIECES_INVALID_MOVES_COUNTER);
            piecesCounter.put(BLACK_PIECES_INVALID_MOVES_COUNTER, ++currentResult);
        }
        else {
            int currentResult = piecesCounter.get(WHITE_PIECES_INVALID_MOVES_COUNTER);
            piecesCounter.put(WHITE_PIECES_INVALID_MOVES_COUNTER, ++currentResult);
        }

      return false;
    }

    private void movePiece(int y1, int y0, int x1, int x0, Piece piece){
        chessMatrix.get(y1).put(x1,piece.id);
        chessMatrix.get(y0).put(x0,0);
    }

    //retorna
    public String[] getSquareInfo(int x, int y){
        return piecesDictionary.get(chessMatrix.get(y).get(x)) != null ?
                piecesDictionary.get(chessMatrix.get(y).get(x)).toStringInfo() : null;
    }

    //retorna a informação da Peça
    public String[] getPieceInfo(int ID){
        return piecesDictionary.get(ID).toString().split(":");
    }
    //
    public String getPieceInfoAsString(int ID){
        return piecesDictionary.get(ID).toString();
    }
    public int getCurrentTeamID(){
        return currentTeam;
    }
    public boolean gameOver(){

        if (piecesCounter.get(BLACK_PIECES_ELIMINATED_CONTROLLER) == 0){
            result = "VENCERAM AS BRANCAS";
            return true;
        }
        else if ( piecesCounter.get(WHITE_PIECES_ELIMINATED_CONTROLLER) == 0){
            result = "VENCERAM AS PRETAS";
            return true;
        }
        else if (piecesCounter.get(LIMIT_OF_MOVES_BY_PIECES_CONTROLLER) == 10
                || (piecesCounter.get(BLACK_PIECES_ELIMINATED_CONTROLLER) ==1 && piecesCounter.get(WHITE_PIECES_ELIMINATED_CONTROLLER)==1)){

            result = "EMPATE";
            return true;
        }

        return false;
    }

    public ArrayList<String> getGameResults(){
        ArrayList<String> gameStatistic = new ArrayList<>();

        gameStatistic.add("JOGO DE CRAZY CHESS");
        gameStatistic.add("Resultado: "+result);
        gameStatistic.add("---");
        gameStatistic.add("Equipa das Pretas");
        gameStatistic.add(String.valueOf(getPiecesSize()/2-piecesCounter.get(WHITE_PIECES_ELIMINATED_CONTROLLER)));
        gameStatistic.add(String.valueOf(piecesCounter.get(BLACK_PIECES_VALID_MOVES_COUNTER)));
        gameStatistic.add(String.valueOf(piecesCounter.get(BLACK_PIECES_INVALID_MOVES_COUNTER)));

        gameStatistic.add("Equipas das Brancas");
        gameStatistic.add(String.valueOf(getPiecesSize()/2-piecesCounter.get(BLACK_PIECES_ELIMINATED_CONTROLLER)));
        gameStatistic.add(String.valueOf(piecesCounter.get(WHITE_PIECES_VALID_MOVES_COUNTER)));
        gameStatistic.add(String.valueOf(piecesCounter.get(WHITE_PIECES_INVALID_MOVES_COUNTER)));

        return gameStatistic;
    }

    JPanel getAuthorsPanel(){
        return null;
    }

    /**
     *  DEFINED PRIVATE ADDITIONAL METHODS
     */

    //Adiciona as informações das Peças numa lista de objectos
    private void insertPiecesInfo() throws InvalidGameInputException{



            //Percorre a lista das peças
            for (int i = 0; i < getPiecesSize(); ++i) {

                String[] pieceInfo = chessInfo.get(initialLine).split(":");

                if (pieceInfo.length == 4){
                    int id = Integer.parseInt(pieceInfo[0]);
                    int type = Integer.parseInt(pieceInfo[1]);
                    int team = Integer.parseInt(pieceInfo[2]);
                    String nickName = pieceInfo[3];


                    Piece peça = new Piece(id,type, team, nickName);


                    piecesDictionary.put(id, peça);
                    ++initialLine;
                }
                else if (pieceInfo.length > 4) {
                    throw new InvalidGameInputException("");
                }
                else {
                    throw new InvalidGameInputException("");
                }


            }

    }

    private int getPiecesSize(){
        return Integer.parseInt(chessInfo.get(1));
    }


    //Posicionar as peças no tabuleiro
    private void insertChessMatrix(){
        for (int i = 0; i < getBoardSize(); i++) {

            String[] chessLineInfo = chessInfo.get(initialLine).split(":");

            chessMatrix.put(i, new HashMap<>());

            for (int j = 0; j < getBoardSize(); j++) {

                chessMatrix.get(i).put(j,Integer.parseInt(chessLineInfo[j]));

            }
           ++initialLine;
        }
    }

    public Map<String, String> customizeBoard(){
        return new HashMap<>();
    }


    public void saveGame(File file) throws IOException{

    }

    public void undo(){

    }

    List<Comparable> getHints(int x, int y){
        return new ArrayList<>();
    }
}
