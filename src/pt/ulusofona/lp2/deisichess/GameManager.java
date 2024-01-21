package pt.ulusofona.lp2.deisichess;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GameManager {


    private final int BLACK_PIECE = 10;
    private final int WHITE_PIECE = 20;

    private final int YELLOW_PIECE = 30;
    private final int BLACK_PIECES_ELIMINATED_CONTROLLER = 0;
    private final int WHITE_PIECES_ELIMINATED_CONTROLLER = 1;
    private final int LIMIT_OF_MOVES_BY_PIECES_CONTROLLER = 2;
    private final int BLACK_PIECES_VALID_MOVES_COUNTER = 3;
    private final int WHITE_PIECES_VALID_MOVES_COUNTER = 4;
    private final int BLACK_PIECES_INVALID_MOVES_COUNTER = 5;
    private final int WHITE_PIECES_INVALID_MOVES_COUNTER = 6;
    private final int JOKER_COPY_PIECE_COUNTER = 7;
    

    ArrayList<String> undoList;
    HashMap<Integer, String> typeDictionary = new HashMap<>();

    ArrayList<String> chessInfo;
    HashMap<Integer,Piece> piecesDictionary = new HashMap<>();
    HashMap<Integer, HashMap<Integer,Integer>> chessMatrix = new HashMap<>();

    //Controla o número de peças eliminadas e jogadas em casas vazias
    HashMap<Integer, Integer> piecesCounter = new HashMap<>();

    int initialLine;

    boolean isHomerAwake = false;
    boolean isWhiteKingCaptured = false;
    boolean isBlackKingCaptured = false;
    boolean areOnlyKings = false;
    String result;
    int currentTeam = BLACK_PIECE;

    Statistics statisticsKT = new Statistics();


    //Leitor dos arquivos de informação do Jogo
    public void loadGame(File file) throws InvalidGameInputException,InvalidTeamException, IOException{

        undoList = new ArrayList<>();
        chessInfo = new ArrayList<>();
        initialLine = 2;

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

            piecesCounter.put(JOKER_COPY_PIECE_COUNTER, 1);
           
            typeDictionary.put(0, "Rei");
            typeDictionary.put(1, "Rainha");
            typeDictionary.put(2, "Ponei Mágico");
            typeDictionary.put(3, "Padre da Vila");
            typeDictionary.put(4, "TorreHor");
            typeDictionary.put(5, "TorreVert");
            typeDictionary.put(6, "Homer");
            typeDictionary.put(7, "Joker/");

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

            try {
                //Coloca a peça Rei na nova posição
                if (piece.getType() == 0) {
                    //Coloca o Rei na Diagonal
                    if ((Math.abs(y1 - y0) == Math.abs(x1 - x0)) && Math.abs(y1 - y0) <= 1) {
                        for (int i = x0, j = y0; i != x1; ) {
                            if (i < x1 && j < y1) {
                                ++i;
                                ++j;
                                if (chessMatrix.get(j).get(i) != 0 && i != x1 && j != y1) {
                                    return false;
                                }

                            }
                            else if (i > x1 && j > y1) {
                                --i;
                                --j;
                                if (chessMatrix.get(j).get(i) != 0 && i != x1 && j != y1) {
                                    return false;
                                }

                            } else if (i > x1 && j < y1) {
                                --i;
                                ++j;
                                if (chessMatrix.get(j).get(i) != 0 && i != x1 && j != y1) {
                                    return false;
                                }
                            } else if (i < x1 && j > y1) {
                                ++i;
                                --j;
                                if (chessMatrix.get(j).get(i) != 0 && i != x1 && j != y1) {
                                    return false;
                                }
                            }
                        }
                        movePiece(y1, y0, x1, x0, piece);
                    }

                    //Coloca o Rei na horizontal
                    else if ((y1 == y0) && (Math.abs(x1 - x0) <= 1)) {
                        for (int i = x0; i != x1; ) {
                            if (i < x1) {
                                ++i;
                                if (chessMatrix.get(y1).get(i) != 0 && i != x1) {
                                    return false;
                                }
                            } else {
                                --i;
                                if (chessMatrix.get(y1).get(i) != 0 && i != x1) {
                                    return false;
                                }
                            }
                        }
                        movePiece(y1, y0, x1, x0, piece);
                    }

                    //Coloca o Rei na Vertical
                    else if ((x1 == x0) && (Math.abs(y1 - y0) <= 1)) {
                        for (int j = y0; j != y1; ) {
                            if (j < y1) {
                                ++j;
                                if (chessMatrix.get(j).get(x1) != 0 && j != y1) {
                                    return false;
                                }
                            } else {
                                --j;
                                if (chessMatrix.get(j).get(x1) != 0 && j != y1) {
                                    return false;
                                }
                            }
                        }
                        movePiece(y1, y0, x1, x0, piece);
                    } else {
                        return false;
                    }
                }
                //Coloca a peça da Rainha na nova Posição
                else if (piece.getType() == 1) {

                    //Coloca da rainha na posição diagonal
                    if ((Math.abs(y1 - y0) == Math.abs(x1 - x0)) && Math.abs(y1 - y0) <= 5) {
                        for (int i = x0, j = y0; i != x1; ) {
                            if (i < x1 && j < y1) {
                                ++i;
                                ++j;
                                if (chessMatrix.get(j).get(i) != 0 && i != x1 && j != y1) {
                                    return false;
                                }

                            } else if (i > x1 && j > y1) {
                                --i;
                                --j;
                                if (chessMatrix.get(j).get(i) != 0 && i != x1 && j != y1) {
                                    return false;
                                }
                            } else if (i > x1 && j < y1) {
                                --i;
                                ++j;
                                if (chessMatrix.get(j).get(i) != 0 && i != x1 && j != y1) {
                                    return false;
                                }
                            } else if (i < x1 && j > y1) {
                                ++i;
                                --j;
                                if (chessMatrix.get(j).get(i) != 0 && i != x1 && j != y1) {
                                    return false;
                                }
                            }
                        }
                        if (nextPiece != null && nextPiece.getType() == 1) {
                            return false;
                        }
                        movePiece(y1, y0, x1, x0, piece);
                    }

                    //Coloca da rainha na horizontal
                    else if ((y1 == y0) && (Math.abs(x1 - x0) <= 5)) {
                        for (int i = x0; i != x1; ) {
                            if (i < x1) {
                                ++i;
                                if (chessMatrix.get(y1).get(i) != 0 && i != x1) {
                                    return false;
                                }
                            } else {
                                --i;
                                if (chessMatrix.get(y1).get(i) != 0 && i != x1) {
                                    return false;
                                }
                            }
                        }
                        if (nextPiece != null && nextPiece.getType() == 1) {
                            return false;
                        }
                        movePiece(y1, y0, x1, x0, piece);
                    }

                    //Coloca da rainha na Vertical
                    else if ((x1 == x0) && (Math.abs(y1 - y0) <= 5)) {
                        for (int j = y0; j != y1; ) {
                            if (j < y1) {
                                ++j;
                                if (chessMatrix.get(j).get(x1) != 0 && j != y1) {
                                    return false;
                                }
                            } else {
                                --j;
                                if (chessMatrix.get(j).get(x1) != 0 && j != y1) {
                                    return false;
                                }
                            }
                        }
                        if (nextPiece != null && nextPiece.getType() == 1) {
                            return false;
                        }
                        movePiece(y1, y0, x1, x0, piece);
                    } else {
                        return false;
                    }
                }
                //Coloca a peça da Ponei Mágico na nova Posição
                else if (piece.getType() == 2) {
                    if (Math.abs(y1 - y0) == 2 && Math.abs(x1 - x0) == 2) {

                        boolean isVerticalLineBlocked = false;
                        boolean isHorizontalLineBlocked = false;

                        for (int i = x0, j = y0; i != x1 || j != y1; ) {
                            if (!isVerticalLineBlocked) {
                                if (j == y1) {
                                    if (i < x1) {
                                        ++i;
                                    } else {
                                        --i;
                                    }

                                    if (chessMatrix.get(j).get(i) != 0 && i != x1) {
                                        isVerticalLineBlocked = true;
                                        i = x0;
                                        j = y0;
                                    }
                                } else {
                                    if (j < y1) {
                                        ++j;
                                    } else {
                                        --j;
                                    }
                                    if (chessMatrix.get(j).get(i) != 0 && i != x1 && j != y1) {
                                        isVerticalLineBlocked = true;
                                        j = y0;
                                        continue;
                                    }
                                }
                            }
                            else {
                                if (i == x1) {
                                    if (j < y1) {
                                        ++j;
                                    } else {
                                        --j;
                                    }

                                } else {
                                    if (i < x1) {
                                        ++i;
                                    } else {
                                        --i;
                                    }
                                }
                                if (chessMatrix.get(j).get(i) != 0 && i != x1 && j != y1) {
                                    isHorizontalLineBlocked = true;
                                }
                            }
                            if (isVerticalLineBlocked && isHorizontalLineBlocked) {
                                return false;
                            }

                        }
                        movePiece(y1, y0, x1, x0, piece);
                    }
                    else {
                        return false;
                    }
                }
                //Coloca a peça do Padre na Vila na nova Posição
                else if (piece.getType() == 3) {
                    if ((Math.abs(y1 - y0) == Math.abs(x1 - x0)) && Math.abs(y1 - y0) <= 3) {
                        for (int i = x0, j = y0; i != x1; ) {
                            if (i < x1 && j < y1) {
                                ++i;
                                ++j;
                                if (chessMatrix.get(j).get(i) != 0 && i != x1 && j != y1) {
                                    return false;
                                }

                            } else if (i > x1 && j > y1) {
                                --i;
                                --j;
                                if (chessMatrix.get(j).get(i) != 0 && i != x1 && j != y1) {
                                    return false;
                                }
                            } else if (i > x1 && j < y1) {
                                --i;
                                ++j;
                                if (chessMatrix.get(j).get(i) != 0 && i != x1 && j != y1) {
                                    return false;
                                }
                            } else if (i < x1 && j > y1) {
                                ++i;
                                --j;
                                if (chessMatrix.get(j).get(i) != 0 && i != x1 && j != y1) {
                                    return false;
                                }
                            }
                        }
                        movePiece(y1, y0, x1, x0, piece);
                    } else {
                        return false;
                    }
                }
                //Coloca a peça da Torre Horizontal na nova Posição
                else if (piece.getType() == 4) {
                    if ((y1 == y0)) {
                        for (int i = x0; i != x1; ) {
                            if (i < x1) {
                                ++i;
                                if (chessMatrix.get(y1).get(i) != 0 && i != x1) {
                                    return false;
                                }
                            } else {
                                --i;
                                if (chessMatrix.get(y1).get(i) != 0 && i != x1) {
                                    return false;
                                }
                            }
                        }
                        movePiece(y1, y0, x1, x0, piece);
                    }
                    else {
                        return false;
                    }
                }
                //Coloca a peça da Torre Vertical na nova Posição
                else if (piece.getType() == 5) {
                    if ((x1 == x0)) {
                        for (int j = y0; j != y1; ) {
                            if (j < y1) {
                                ++j;
                                if (chessMatrix.get(j).get(x1) != 0 && j != y1) {
                                    return false;
                                }
                            } else {
                                --j;
                                if (chessMatrix.get(j).get(x1) != 0 && j != y1) {
                                    return false;
                                }
                            }
                        }
                        movePiece(y1, y0, x1, x0, piece);
                    }
                    else {
                        return false;
                    }
                }
                //Coloca a peça Homer Simpson na nova Posição
                else if (piece.getType() == 6) {
                    int blackValidMoves = piecesCounter.get(BLACK_PIECES_VALID_MOVES_COUNTER);
                    int whiteValidMoves = piecesCounter.get(WHITE_PIECES_VALID_MOVES_COUNTER);
                    if ((piece.getTeam() == 10 &&  (blackValidMoves + 1) % 3 == 0) || (piece.getTeam() == 20 &&  (whiteValidMoves + 1) % 3 == 0)){
                        if ((Math.abs(y1 - y0) == Math.abs(x1 - x0)) && Math.abs(y1 - y0) <= 1) {
                            for (int i = x0, j = y0; i != x1; ) {
                                if (i < x1 && j < y1) {
                                    ++i;
                                    ++j;
                                    if (chessMatrix.get(j).get(i) != 0 && i != x1 && j != y1) {
                                        return false;
                                    }

                                } else if (i > x1 && j > y1) {
                                    --i;
                                    --j;
                                    if (chessMatrix.get(j).get(i) != 0 && i != x1 && j != y1) {
                                        return false;
                                    }
                                } else if (i > x1 && j < y1) {
                                    --i;
                                    ++j;
                                    if (chessMatrix.get(j).get(i) != 0 && i != x1 && j != y1) {
                                        return false;
                                    }
                                } else if (i < x1 && j > y1) {
                                    ++i;
                                    --j;
                                    if (chessMatrix.get(j).get(i) != 0 && i != x1 && j != y1) {
                                        return false;
                                    }
                                }
                            }
                            movePiece(y1, y0, x1, x0, piece);
                        }
                        else {
                            return false;
                        }
                    }
                    else{
                        return false;
                    }


                }

                else if (piece.getType() == 7) {
                        if (piecesCounter.get(JOKER_COPY_PIECE_COUNTER) == 1){
                                //Coloca da rainha na posição diagonal
                                if ((Math.abs(y1 - y0) == Math.abs(x1 - x0)) && Math.abs(y1 - y0) <= 5) {
                                    for (int i = x0, j = y0; i != x1; ) {
                                        if (i < x1 && j < y1) {
                                            ++i;
                                            ++j;
                                            if (chessMatrix.get(j).get(i) != 0 && i != x1 && j != y1) {
                                                return false;
                                            }

                                        } else if (i > x1 && j > y1) {
                                            --i;
                                            --j;
                                            if (chessMatrix.get(j).get(i) != 0 && i != x1 && j != y1) {
                                                return false;
                                            }
                                        } else if (i > x1 && j < y1) {
                                            --i;
                                            ++j;
                                            if (chessMatrix.get(j).get(i) != 0 && i != x1 && j != y1) {
                                                return false;
                                            }
                                        } else if (i < x1 && j > y1) {
                                            ++i;
                                            --j;
                                            if (chessMatrix.get(j).get(i) != 0 && i != x1 && j != y1) {
                                                return false;
                                            }
                                        }
                                    }
                                    if (nextPiece != null && nextPiece.getType() == 1) {
                                        return false;
                                    }
                                    movePiece(y1, y0, x1, x0, piece);
                                }
                                //Coloca da rainha na horizontal
                                else if ((y1 == y0) && (Math.abs(x1 - x0) <= 5)) {
                                    for (int i = x0; i != x1; ) {
                                        if (i < x1) {
                                            ++i;
                                            if (chessMatrix.get(y1).get(i) != 0 && i != x1) {
                                                return false;
                                            }
                                        } else {
                                            --i;
                                            if (chessMatrix.get(y1).get(i) != 0 && i != x1) {
                                                return false;
                                            }
                                        }
                                    }
                                    if (nextPiece != null && nextPiece.getType() == 1) {
                                        return false;
                                    }
                                    movePiece(y1, y0, x1, x0, piece);
                                }
                                //Coloca da rainha na Vertical
                                else if ((x1 == x0) && (Math.abs(y1 - y0) <= 5)) {
                                    for (int j = y0; j != y1; ) {
                                        if (j < y1) {
                                            ++j;
                                            if (chessMatrix.get(j).get(x1) != 0 && j != y1) {
                                                return false;
                                            }
                                        } else {
                                            --j;
                                            if (chessMatrix.get(j).get(x1) != 0 && j != y1) {
                                                return false;
                                            }
                                        }
                                    }
                                    if (nextPiece != null && nextPiece.getType() == 1) {
                                        return false;
                                    }
                                    movePiece(y1, y0, x1, x0, piece);
                                }

                                else {
                                    return false;
                                }



                        }
                        else if (piecesCounter.get(JOKER_COPY_PIECE_COUNTER) == 2) {
                            if (Math.abs(y1 - y0) == 2 && Math.abs(x1 - x0) == 2) {

                                boolean isVerticalLineBlocked = false;
                                boolean isHorizontalLineBlocked = false;

                                for (int i = x0, j = y0; i != x1 || j != y1; ) {
                                    if (!isVerticalLineBlocked) {
                                        if (j == y1) {
                                            if (i < x1) {
                                                ++i;
                                            } else {
                                                --i;
                                            }

                                            if (chessMatrix.get(j).get(i) != 0 && i != x1) {
                                                isVerticalLineBlocked = true;
                                                i = x0;
                                                j = y0;
                                            }
                                        } else {
                                            if (j < y1) {
                                                ++j;
                                            } else {
                                                --j;
                                            }
                                            if (chessMatrix.get(j).get(i) != 0 && i != x1 && j != y1) {
                                                isVerticalLineBlocked = true;
                                                j = y0;
                                                continue;
                                            }
                                        }
                                    } else {
                                        if (i == x1) {
                                            if (j < y1) {
                                                ++j;
                                            } else {
                                                --j;
                                            }

                                        } else {
                                            if (i < x1) {
                                                ++i;
                                            } else {
                                                --i;
                                            }
                                        }
                                        if (chessMatrix.get(j).get(i) != 0 && i != x1 && j != y1) {
                                            isHorizontalLineBlocked = true;
                                        }
                                    }

                                    if (isVerticalLineBlocked && isHorizontalLineBlocked) {
                                        return false;
                                    }

                                }
                                movePiece(y1, y0, x1, x0, piece);
                            }
                            else {
                                return false;
                            }


                        }
                        else if (piecesCounter.get(JOKER_COPY_PIECE_COUNTER) == 3) {
                            if ((Math.abs(y1 - y0) == Math.abs(x1 - x0)) && Math.abs(y1 - y0) <= 3) {
                                for (int i = x0, j = y0; i != x1; ) {
                                    if (i < x1 && j < y1) {
                                        ++i;
                                        ++j;
                                        if (chessMatrix.get(j).get(i) != 0 && i != x1 && j != y1) {
                                            return false;
                                        }

                                    } else if (i > x1 && j > y1) {
                                        --i;
                                        --j;
                                        if (chessMatrix.get(j).get(i) != 0 && i != x1 && j != y1) {
                                            return false;
                                        }
                                    } else if (i > x1 && j < y1) {
                                        --i;
                                        ++j;
                                        if (chessMatrix.get(j).get(i) != 0 && i != x1 && j != y1) {
                                            return false;
                                        }
                                    } else if (i < x1 && j > y1) {
                                        ++i;
                                        --j;
                                        if (chessMatrix.get(j).get(i) != 0 && i != x1 && j != y1) {
                                            return false;
                                        }
                                    }
                                }
                                movePiece(y1, y0, x1, x0, piece);
                            }
                            else {
                                return false;
                            }

                        }
                        else if (piecesCounter.get(JOKER_COPY_PIECE_COUNTER) == 4) {
                            if ((y1 == y0)) {
                                for (int i = x0; i != x1; ) {
                                    if (i < x1) {
                                        ++i;
                                        if (chessMatrix.get(y1).get(i) != 0 && i != x1) {
                                            return false;
                                        }
                                    } else {
                                        --i;
                                        if (chessMatrix.get(y1).get(i) != 0 && i != x1) {
                                            return false;
                                        }
                                    }
                                }
                                movePiece(y1, y0, x1, x0, piece);
                            }
                            else {
                                return false;
                            }

                        }
                        else if (piecesCounter.get(JOKER_COPY_PIECE_COUNTER) == 5) {
                            if ((x1 == x0)) {
                                for (int j = y0; j != y1; ) {
                                    if (j < y1) {
                                        ++j;
                                        if (chessMatrix.get(j).get(x1) != 0 && j != y1) {
                                            return false;
                                        }
                                    } else {
                                        --j;
                                        if (chessMatrix.get(j).get(x1) != 0 && j != y1) {
                                            return false;
                                        }
                                    }
                                }
                                movePiece(y1, y0, x1, x0, piece);
                            }
                            else {
                                return false;
                            }

                        }
                        else if (piecesCounter.get(JOKER_COPY_PIECE_COUNTER) == 6) {
                            return false;
                        }
                }

            }
            catch (NullPointerException nullPointerException){
                nullPointerException.printStackTrace();
            }

            //Eliminar peça inimiga
            if (nextPiece != null){

                //reseta o número de jogadas sem eliminações
                piecesCounter.put(LIMIT_OF_MOVES_BY_PIECES_CONTROLLER,0);
                //
                //piecesCounter.put(nextPiece.team, piecesCounter.get(nextPiece.team)-1);
                if (nextPiece.getType() == 1 && nextPiece.getTeam() == 10){
                    isBlackKingCaptured = true;
                }
                else if (nextPiece.getType() == 1 && nextPiece.getTeam() == 20) {
                    isWhiteKingCaptured = true;
                }

                int capturedBlackPieces = piecesCounter.get(BLACK_PIECES_ELIMINATED_CONTROLLER);
                int capturedWhitePieces = piecesCounter.get(WHITE_PIECES_ELIMINATED_CONTROLLER);

                if (getPiecesSize() - (capturedBlackPieces + capturedWhitePieces) == 2
                 && piece.getType() == 1 && nextPiece.getType() == 1){
                    areOnlyKings = true;
                }



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


            int blackValidMoves = piecesCounter.get(BLACK_PIECES_VALID_MOVES_COUNTER);
            int whiteValidMoves = piecesCounter.get(WHITE_PIECES_VALID_MOVES_COUNTER);
            if ((piece.getTeam() == 10 &&  (blackValidMoves + 1) % 3 == 0) || (piece.getTeam() == 20 &&  (whiteValidMoves + 1) % 3 == 0)){
                for (Piece pieceHomer : piecesDictionary.values()) {
                    if (pieceHomer.getType() == 6){
                        String pieceInfo =
                                pieceHomer.getId()
                                +":"+pieceHomer.getType()
                                +":"+pieceHomer.getTeam()
                                +":"+pieceHomer.getNickName();

                        pieceHomer.setPieceInfo(pieceInfo);
                    }
                }
            }
            else{
                for (Piece pieceHomer : piecesDictionary.values()) {
                    if (pieceHomer.getType() == 6){
                        pieceHomer.setPieceInfo("Doh zzzzzzz");
                    }
                }
            }

            changeJokerSkill();

            return true;
        }
      return false;
    }
    private void movePiece(int y1, int y0, int x1, int x0, Piece piece){
        updateUndoList();
        chessMatrix.get(y1).put(x1,piece.id);
        chessMatrix.get(y0).put(x0,0);
    }
    private void changeJokerSkill() {
        int jokerNextSkillCounter = piecesCounter.get(JOKER_COPY_PIECE_COUNTER)+1;

        if (jokerNextSkillCounter == 7){
            jokerNextSkillCounter = 1;
        }
        piecesCounter.put(JOKER_COPY_PIECE_COUNTER, jokerNextSkillCounter);

        for (Piece piece : piecesDictionary.values()) {
            if (piece.getType() == 7){
                piece.setName(typeDictionary.get(7)+typeDictionary.get(jokerNextSkillCounter));
                piece.updatePieceInfo();
            }
        }
    }

    private void updateUndoList(){
        String content = "";
        for (HashMap<Integer,Integer> chessY : chessMatrix.values()) {
            String chessCoordinate = "";
            for (Integer chessX : chessY.values()) {
                chessCoordinate = chessCoordinate.concat(chessX+":");
            }
            chessCoordinate = chessCoordinate.substring(0, chessCoordinate.lastIndexOf(':'));
            content = content.concat(chessCoordinate+"\n");
        }

        undoList.add(content);

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

        if (isBlackKingCaptured){
            result = "VENCERAM AS BRANCAS";
            return true;
        }
        else if (isWhiteKingCaptured){
            result = "VENCERAM AS PRETAS";
            return true;
        }
        else if (piecesCounter.get(LIMIT_OF_MOVES_BY_PIECES_CONTROLLER) == 10
                || areOnlyKings){
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
    private void insertPiecesInfo() throws InvalidGameInputException, InvalidTeamException {
            //Percorre a lista das peças
            for (int i = 0; i < getPiecesSize(); ++i) {

                String[] pieceInfo = chessInfo.get(initialLine).split(":");

                if (pieceInfo.length == 4){
                    int id = Integer.parseInt(pieceInfo[0]);
                    int type = Integer.parseInt(pieceInfo[1]);
                    int team = Integer.parseInt(pieceInfo[2]);
                    String nickName = pieceInfo[3];

                    if (team != 10 && team != 20 && team != 30){
                        throw new InvalidTeamException("Equipa inválida");
                    }


                    Piece peca = new Piece(id,type, team, nickName);

                    if (type == 6){
                        peca.setPieceInfo("Doh! zzzzzzz");
                    }

                    if (type == 7){
                        peca.setName("Joker/Rainha");
                        peca.updatePieceInfo();
                    }
                    piecesDictionary.put(id, peca);
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
        if (chessInfo.size() - (getPiecesSize()+getBoardSize()) == 3){
            currentTeam = Integer.parseInt(chessInfo.get(initialLine));
        }
    }
    public Map<String, String> customizeBoard(){
        return new HashMap<>();
    }


    public void saveGame(File file) throws IOException{


        // Conteúdo a ser escrito no arquivo
        String conteudo = "";
        conteudo = conteudo.concat(String.valueOf(getBoardSize())+"\n");
        conteudo = conteudo.concat(String.valueOf(getPiecesSize())+"\n");

        for (Piece piece : piecesDictionary.values()) {
            conteudo =  conteudo.concat(
                                piece.getId() +
                            ":"+piece.getType()+
                            ":"+piece.getTeam()+
                            ":"+piece.getNickName()+"\n");

        }

        for (HashMap<Integer,Integer> chessY : chessMatrix.values()) {
            String chessCoordinate = "";
            for (Integer chessX : chessY.values()) {
                chessCoordinate = chessCoordinate.concat(chessX+":");
            }
            chessCoordinate = chessCoordinate.substring(0, chessCoordinate.lastIndexOf(':'));
            conteudo = conteudo.concat(chessCoordinate+"\n");
        }

        conteudo = conteudo.concat(String.valueOf(currentTeam));

        try {
            FileWriter writer = new FileWriter(file.getPath());
            writer.write(conteudo);
            writer.close();

            System.out.println("Conteúdo gravado com sucesso no arquivo.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void undo(){
        if (!undoList.isEmpty()){
            String[] chessMatrixInfo = undoList.get(undoList.size()-1).split("\n");
            for (int i = 0; i < getBoardSize(); i++) {

                String[] chessLineInfo = chessMatrixInfo[i].split(":");

                chessMatrix.put(i, new HashMap<>());

                for (int j = 0; j < getBoardSize(); j++) {

                    chessMatrix.get(i).put(j,Integer.parseInt(chessLineInfo[j]));

                }
            }
            undoList.remove(undoList.size()-1);
        }
    }

    public List<Comparable> getHints(int x, int y) {

        /*
        int pieceId = chessMatrix.get(y).get(x);
        Piece piece = piecesDictionary.get(pieceId);
        List<ChessCoordinates> list = new ArrayList<>();


        //Coloca a peça Rei na nova posição

        if (piece.getType() == 1) {

            //Coloca da rainha na posição diagonal
            for (int i = x, j = y; i < getBoardSize() && j < getBoardSize(); ++i, ++j) {

                if (!(chessMatrix.get(j).get(i) != 0 && i != getBoardSize() - 1 && j != getBoardSize() - 1)) {
                    list.add(new ChessCoordinates(i, j, 0));
                } else if (chessMatrix.get(j).get(i) != 0 && i == getBoardSize() - 1 && j == getBoardSize() - 1) {
                    int nextPieceId = chessMatrix.get(j).get(i);
                    Piece nextPiece = piecesDictionary.get(nextPieceId);
                    list.add(new ChessCoordinates(i, j, nextPiece.getValue()));
                }

            }

            for (int i = x - 1, j = y - 1; i > 0 || j > 0; --i, --j) {

                if (i < 0 || i >= getBoardSize() ||j < 0 || j >= getBoardSize()){
                    break;
                }

                if (Math.abs(x - 1) != 6){
                    if (i == 0 || j == 0) {
                        if (chessMatrix.get(j).get(i) != 0) {
                            int nextPieceId = chessMatrix.get(j).get(i);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                list.add(new ChessCoordinates(i, j, nextPiece.getValue()));
                            }
                        }
                        else {
                            list.add(new ChessCoordinates(i, j, 0));
                        }
                        break;
                    }
                    else {
                        if (chessMatrix.get(j).get(i) == 0) {
                            list.add(new ChessCoordinates(i, j, 0));
                        } else {
                            int nextPieceId = chessMatrix.get(j).get(i);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                list.add(new ChessCoordinates(i, j, nextPiece.getValue()));
                            }
                            break;
                        }
                    }
                }
                else{
                    break;
                }
            }

            for (int i = x + 1, j = y - 1; i < getBoardSize() || j > 0; ++i, --j) {

                if (i < 0 || i >= getBoardSize() ||j < 0 || j >= getBoardSize()){
                    break;
                }

                if (Math.abs(x-i) != 6){
                    if (i == getBoardSize() || j == 0) {
                        if (chessMatrix.get(j).get(i) != 0) {
                            int nextPieceId = chessMatrix.get(j).get(i);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                list.add(new ChessCoordinates(i, j, nextPiece.getValue()));
                            }
                        }
                        else {
                            list.add(new ChessCoordinates(i, j, 0));
                        }
                        break;
                    }
                    else {
                        if (chessMatrix.get(j).get(i) == 0) {
                            list.add(new ChessCoordinates(i, j, 0));
                        }
                        else {
                            int nextPieceId = chessMatrix.get(j).get(i);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                list.add(new ChessCoordinates(i, j, nextPiece.getValue()));
                            }
                            break;
                        }
                    }
                }
                else{
                    break;
                }

            }

            for (int i = x - 1, j = y + 1; i > 0 || j < getBoardSize(); --i, ++j) {

                if (i < 0 || i >= getBoardSize() ||j < 0 || j >= getBoardSize()){
                    break;
                }

                if (Math.abs(x-i) != 6){
                    if (i == 0 || j == getBoardSize()) {
                        if (chessMatrix.get(j).get(i) != 0) {
                            int nextPieceId = chessMatrix.get(j).get(i);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                list.add(new ChessCoordinates(i, j, nextPiece.getValue()));
                            }
                        }
                        else {
                            list.add(new ChessCoordinates(i, j, 0));
                        }
                        break;
                    }
                    else {
                        if (chessMatrix.get(j).get(i) == 0) {
                            list.add(new ChessCoordinates(i, j, 0));
                        }
                        else {
                            int nextPieceId = chessMatrix.get(j).get(i);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                list.add(new ChessCoordinates(i, j, nextPiece.getValue()));
                            }
                            break;
                        }
                    }
                }
                else{
                    break;
                }

            }


            //Horizontal
            for (int i = x+1; i < getBoardSize(); ++i){
                if (Math.abs(x-i) != 6){
                    if (i == getBoardSize()) {
                        if (chessMatrix.get(y).get(i) != 0) {
                            int nextPieceId = chessMatrix.get(y).get(i);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                list.add(new ChessCoordinates(i, y, nextPiece.getValue()));
                            }
                        }
                        else {
                            list.add(new ChessCoordinates(i, y, 0));
                        }
                        break;
                    }
                    else {
                        if (chessMatrix.get(y).get(i) == 0) {
                            list.add(new ChessCoordinates(i, y, 0));
                        }
                        else {
                            int nextPieceId = chessMatrix.get(y).get(i);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                list.add(new ChessCoordinates(i, y, nextPiece.getValue()));
                            }
                            break;
                        }
                    }
                }
                else{
                    break;
                }
            }
            for (int i = x-1; i >= 0; --i){
                if (Math.abs(x-i) != 6){
                    if (i == 0) {
                        if (chessMatrix.get(y).get(i) != 0) {
                            int nextPieceId = chessMatrix.get(y).get(i);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                list.add(new ChessCoordinates(i, y, nextPiece.getValue()));
                            }
                        }
                        else {
                            list.add(new ChessCoordinates(i, y, 0));
                        }
                        break;
                    }
                    else {
                        if (chessMatrix.get(y).get(i) == 0) {
                            list.add(new ChessCoordinates(i, y, 0));
                        }
                        else {
                            int nextPieceId = chessMatrix.get(y).get(i);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                list.add(new ChessCoordinates(i, y, nextPiece.getValue()));
                            }
                            break;
                        }
                    }
                }
                else{
                    break;
                }
            }


            //Vertical
            for (int j = y+1; j < getBoardSize(); ++j){
                if (Math.abs(j-y) != 6){
                    if (j == getBoardSize()) {
                        if (chessMatrix.get(j).get(x) != 0) {
                            int nextPieceId = chessMatrix.get(j).get(x);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                list.add(new ChessCoordinates(j, x, nextPiece.getValue()));
                            }
                        }
                        else {
                            list.add(new ChessCoordinates(j, x, 0));
                        }
                        break;
                    }
                    else {
                        if (chessMatrix.get(j).get(x) == 0) {
                            list.add(new ChessCoordinates(j, x, 0));
                        }
                        else {
                            int nextPieceId = chessMatrix.get(j).get(x);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                list.add(new ChessCoordinates(j, x, nextPiece.getValue()));
                            }
                            break;
                        }
                    }
                }
                else{
                    break;
                }
            }
            for (int j = y-1; j >= 0; --j){
                if (Math.abs(j-y) != 6){
                    if (j == 0) {
                        if (chessMatrix.get(j).get(x) != 0) {
                            int nextPieceId = chessMatrix.get(j).get(x);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                list.add(new ChessCoordinates(j, x, nextPiece.getValue()));
                            }
                        }
                        else {
                            list.add(new ChessCoordinates(j, x, 0));
                        }
                        break;
                    }
                    else {
                        if (chessMatrix.get(j).get(x) == 0) {
                            list.add(new ChessCoordinates(j, x, 0));
                        }
                        else {
                            int nextPieceId = chessMatrix.get(j).get(x);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                list.add(new ChessCoordinates(j, x, nextPiece.getValue()));
                            }
                            break;
                        }
                    }
                }
                else{
                    break;
                }
            }

        }
        else if (piece.getType() == 2){
        }


        return list;
         */
        return new ArrayList<>();
    }
}
