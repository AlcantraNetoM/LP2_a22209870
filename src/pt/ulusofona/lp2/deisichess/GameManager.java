package pt.ulusofona.lp2.deisichess;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class GameManager {


    private final int BLACK_PIECE = 0;

    private final int WHITE_PIECE = 1;
    private final int BLACK_PIECES_ELIMINATED_CONTROLLER = 0;
    private final int WHITE_PIECES_ELIMINATED_CONTROLLER = 1;
    private final int LIMIT_OF_MOVES_BY_PIECES_CONTROLLER = 2;
    private final int BLACK_PIECES_VALID_MOVES_COUNTER = 3;
    private final int WHITE_PIECES_VALID_MOVES_COUNTER = 4;
    private final int BLACK_PIECES_INVALID_MOVES_COUNTER = 5;
    private final int WHITE_PIECES_INVALID_MOVES_COUNTER = 6;

    ArrayList<String> chessInfo = new ArrayList<>();
    HashMap<Integer,Piece> piecesDictionary = new HashMap<>();

    HashMap<Integer, HashMap<Integer,Integer>> chessMatrix = new HashMap<>();

    //Controla o número de peças eliminadas e jogadas em casas vazias
    HashMap<Integer, Integer> piecesCounter = new HashMap<>();
    int initialLine = 2;


    String result;
    int currentTeam = 0;
    public GameManager(){

    }

    //Leitor dos arquivos de informação do Jogo
    public boolean loadGame(File file) throws InvalidGameInputException, IOException{

        try{
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

            return true;
        }

        catch (IOException io){
            System.out.println("Erro");
            return false;
        }
    }

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


            //Coloca peça na nova posição
            chessMatrix.get(y1).put(x1,piece.id);
            chessMatrix.get(y0).put(x0,0);


            //Eliminar peça inimiga
            if (nextPiece != null){

                piecesCounter.put(LIMIT_OF_MOVES_BY_PIECES_CONTROLLER,0);
                piecesCounter.put(nextPiece.team, piecesCounter.get(nextPiece.team)-1);

            } else{
                piecesCounter.put(LIMIT_OF_MOVES_BY_PIECES_CONTROLLER, piecesCounter.get(LIMIT_OF_MOVES_BY_PIECES_CONTROLLER)+1);
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
    private void insertPiecesInfo(){


        //Percorre a lista das peças
        for (int i = 0; i < getPiecesSize(); ++i){

            String[] pieceInfo = chessInfo.get(initialLine).split(":");

            int id = Integer.parseInt(pieceInfo[0]);
            int type = Integer.parseInt(pieceInfo[1]);
            int team = Integer.parseInt(pieceInfo[2]);
            String nickName = pieceInfo[3];

            Piece peça = new Piece(id,type,team,nickName);

            piecesDictionary.put(id, peça);
            ++initialLine;

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
