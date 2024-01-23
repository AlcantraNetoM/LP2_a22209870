package pt.ulusofona.lp2.deisichess;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GameManager {


    private final int blackPiece = 10;
    private final int whitePiece = 20;

    private final int yellowPiece = 30;
    private final int blackPiecesEliminatedController = 0;
    private final int whitePiecesEliminatedController = 1;
    private final int limitOfMovesByPiecesController = 2;
    private final int blackPiecesValidMovesCounter = 3;
    private final int whitePiecesValidMovesCounter = 4;
    private final int blackPiecesInvalidMovesCounter = 5;
    private final int whitePiecesInvalidMovesCounter = 6;

    private final int homerPieceCounter = 7;
    private final int jokerCopyPieceCounter = 8;

    private final int johnMcClaneCounter = 9;
    ArrayList<String> undoList;

    ArrayList<Integer> teamsList;
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
    boolean isBlackPieceInBoard = false;
    String result;
    int currentTeam;



    //Leitor dos arquivos de informação do Jogo
    public void loadGame(File file) throws InvalidGameInputException,InvalidTeamException, IOException{

        teamsList = new ArrayList<>();
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
            piecesCounter.put(blackPiecesEliminatedController, getPiecesSize()/2);
            piecesCounter.put(whitePiecesEliminatedController, getPiecesSize()/2);

            //Controla se as 10 jogadas foram feitas
            piecesCounter.put(limitOfMovesByPiecesController,0);

            //Conta o número de jogadas válidas de cada equipa
            piecesCounter.put(blackPiecesValidMovesCounter, 0);
            piecesCounter.put(whitePiecesValidMovesCounter, 0);

            //Conta o número de jogadas inválidas de cada equipa
            piecesCounter.put(blackPiecesInvalidMovesCounter, 0);
            piecesCounter.put(whitePiecesInvalidMovesCounter, 0);

            piecesCounter.put(jokerCopyPieceCounter, 1);
            piecesCounter.put(johnMcClaneCounter, 1);
            piecesCounter.put(homerPieceCounter, 1);

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


        if ((x0 >= 0 && x0 < getBoardSize()) && (x1 >= 0 && x1 < getBoardSize()) &&
                (y0 >= 0 && y0 < getBoardSize()) && (y1 >= 0 && y1 < getBoardSize())) {
            //Peça a jogar
            Piece piece = piecesDictionary.get(chessMatrix.get(y0).get(x0));
            //Peça na posição ocupada
            Piece nextPiece = piecesDictionary.get(chessMatrix.get(y1).get(x1));


            if (piece != null) {
                if (piece.getTeam() == currentTeam) {

                    if (nextPiece != null && nextPiece.getTeam() == piece.getTeam()) {
                        if (piece.getTeam() == blackPiece) {
                            int currentResult = piecesCounter.get(blackPiecesInvalidMovesCounter);
                            piecesCounter.put(blackPiecesInvalidMovesCounter, ++currentResult);
                        } else {
                            int currentResult = piecesCounter.get(whitePiecesInvalidMovesCounter);
                            piecesCounter.put(whitePiecesInvalidMovesCounter, ++currentResult);
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
                                movePiece(y1, y0, x1, x0, piece, nextPiece);
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
                                movePiece(y1, y0, x1, x0, piece, nextPiece);
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
                                movePiece(y1, y0, x1, x0, piece, nextPiece);
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
                                movePiece(y1, y0, x1, x0, piece, nextPiece);
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
                                movePiece(y1, y0, x1, x0, piece, nextPiece);
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
                                movePiece(y1, y0, x1, x0, piece, nextPiece);
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
                                movePiece(y1, y0, x1, x0, piece, nextPiece);
                            } else {
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
                                movePiece(y1, y0, x1, x0, piece, nextPiece);
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
                                movePiece(y1, y0, x1, x0, piece, nextPiece);
                            } else {
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
                                movePiece(y1, y0, x1, x0, piece, nextPiece);
                            } else {
                                return false;
                            }
                        }
                        //Coloca a peça Homer Simpson na nova Posição
                        else if (piece.getType() == 6) {

                            if (piecesCounter.get(homerPieceCounter) % 3 == 0) {
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
                                        }
                                        else if (i > x1 && j < y1) {
                                            --i;
                                            ++j;
                                            if (chessMatrix.get(j).get(i) != 0 && i != x1 && j != y1) {
                                                return false;
                                            }
                                        }
                                        else if (i < x1 && j > y1) {
                                            ++i;
                                            --j;
                                            if (chessMatrix.get(j).get(i) != 0 && i != x1 && j != y1) {
                                                return false;
                                            }
                                        }
                                    }
                                    movePiece(y1, y0, x1, x0, piece, nextPiece);
                                } else {
                                    return false;
                                }
                            }
                            else {
                                return false;
                            }

                        /*
                        int blackValidMoves = piecesCounter.get(blackPiecesValidMovesCounter);
                        int whiteValidMoves = piecesCounter.get(whitePiecesValidMovesCounter);
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
                                movePiece(y1, y0, x1, x0, piece, nextPiece);
                            }
                            else {
                                return false;
                            }
                        }
                        else{
                            return false;
                        }

                         */


                        }

                        //Coloca a peça Joker na nova Posição
                        else if (piece.getType() == 7) {
                            if (piecesCounter.get(jokerCopyPieceCounter) == 1) {
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
                                    movePiece(y1, y0, x1, x0, piece, nextPiece);
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
                                    movePiece(y1, y0, x1, x0, piece, nextPiece);
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
                                    movePiece(y1, y0, x1, x0, piece, nextPiece);
                                } else {
                                    return false;
                                }


                            } else if (piecesCounter.get(jokerCopyPieceCounter) == 2) {
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
                                    movePiece(y1, y0, x1, x0, piece, nextPiece);
                                } else {
                                    return false;
                                }


                            } else if (piecesCounter.get(jokerCopyPieceCounter) == 3) {
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
                                    movePiece(y1, y0, x1, x0, piece, nextPiece);
                                } else {
                                    return false;
                                }

                            } else if (piecesCounter.get(jokerCopyPieceCounter) == 4) {
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
                                    movePiece(y1, y0, x1, x0, piece, nextPiece);
                                } else {
                                    return false;
                                }

                            } else if (piecesCounter.get(jokerCopyPieceCounter) == 5) {
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
                                    movePiece(y1, y0, x1, x0, piece, nextPiece);
                                } else {
                                    return false;
                                }

                            } else if (piecesCounter.get(jokerCopyPieceCounter) == 6) {
                                if (piecesCounter.get(homerPieceCounter) % 3 == 0) {
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
                                        movePiece(y1, y0, x1, x0, piece, nextPiece);
                                    } else {
                                        return false;
                                    }
                                } else {
                                    return false;
                                }
                            }
                        }

                        //John McClaine
                        else if (piece.getType() == 10) {
                            return false;
                        }

                    } catch (NullPointerException nullPointerException) {
                        nullPointerException.printStackTrace();
                    }

                    //Eliminar peça inimiga
                    if (nextPiece != null) {

                        //reseta o número de jogadas sem eliminações
                        piecesCounter.put(limitOfMovesByPiecesController, 0);
                        //
                        //piecesCounter.put(nextPiece.team, piecesCounter.get(nextPiece.team)-1);
                        if (nextPiece.getType() == 1 && nextPiece.getTeam() == 10) {
                            isBlackKingCaptured = true;
                        } else if (nextPiece.getType() == 1 && nextPiece.getTeam() == 20) {
                            isWhiteKingCaptured = true;
                        }

                        int capturedBlackPieces = piecesCounter.get(blackPiecesEliminatedController);
                        int capturedWhitePieces = piecesCounter.get(whitePiecesEliminatedController);

                        if (getPiecesSize() - (capturedBlackPieces + capturedWhitePieces) == 2
                                && piece.getType() == 1 && nextPiece.getType() == 1) {
                            areOnlyKings = true;
                        }


                    } else {
                        piecesCounter.put(limitOfMovesByPiecesController,
                                piecesCounter.get(limitOfMovesByPiecesController) + 1);
                    }


                    //Trocar a vez do jogo das peças
                    if (teamsList.contains(blackPiece)) {
                        if (piece.getTeam() == blackPiece) {
                            int currentResult = piecesCounter.get(blackPiecesValidMovesCounter);
                            piecesCounter.put(blackPiecesValidMovesCounter, ++currentResult);
                            currentTeam = teamsList.contains(whitePiece) ? whitePiece : yellowPiece;
                        } else if (piece.getTeam() == whitePiece) {
                            int currentResult = piecesCounter.get(whitePiecesValidMovesCounter);
                            piecesCounter.put(whitePiecesValidMovesCounter, ++currentResult);
                            currentTeam = blackPiece;
                        } else if (piece.getTeam() == yellowPiece) {
                            currentTeam = blackPiece;
                        }
                    }
                    else {
                        if (piece.getTeam() == whitePiece) {
                            int currentResult = piecesCounter.get(whitePiecesValidMovesCounter);
                            piecesCounter.put(blackPiecesValidMovesCounter, ++currentResult);
                            currentTeam = yellowPiece;
                        } else if (piece.getTeam() == yellowPiece) {
                            currentTeam = whitePiece;
                        }
                    }




                /*
                int blackValidMoves = piecesCounter.get(blackPiecesValidMovesCounter);
                int whiteValidMoves = piecesCounter.get(whitePiecesValidMovesCounter);
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

                 */


                    changeJokerSkill();
                    increaseJohnMcClaneCounter();
                    increaseHomerCounter();

                    return true;
                }
            }
        }
      return false;
    }
    private void movePiece(int y1, int y0, int x1, int x0, Piece piece, Piece nextPiece){
        updateUndoList();

        if (nextPiece != null){
            if (nextPiece.getType() == 10 && piecesCounter.get(johnMcClaneCounter) % 4 != 0){
                chessMatrix.get(y1).put(x1,piece.getId());
                chessMatrix.get(y0).put(x0,nextPiece.getId());
                piece.setCoordinateX(x1);
                piece.setCoordinateY(y1);
                piece.updatePieceInfo();

                nextPiece.setCoordinateX(x0);
                nextPiece.setCoordinateY(y0);
                nextPiece.updatePieceInfo();
                return;
            }
        }
        chessMatrix.get(y1).put(x1,piece.getId());
        chessMatrix.get(y0).put(x0,0);
        piece.setCoordinateY(y1);
        piece.setCoordinateX(x1);
        piece.updatePieceInfo();

    }

    private void increaseHomerCounter(){
        int counter = piecesCounter.get(homerPieceCounter)+1;
        piecesCounter.put(homerPieceCounter, counter);
        for (Piece piece : piecesDictionary.values()) {
            if (piece.getType() == 6){
               if (counter % 3 == 0){
                   piece.updatePieceInfo();
               }else{
                   piece.setPieceInfo("Doh! zzzzzz");
               }
            }
        }
    }
    private void changeJokerSkill() {
        int jokerNextSkillCounter = piecesCounter.get(jokerCopyPieceCounter)+1;

        if (jokerNextSkillCounter == 7){
            jokerNextSkillCounter = 1;
        }
        piecesCounter.put(jokerCopyPieceCounter, jokerNextSkillCounter);

        for (Piece piece : piecesDictionary.values()) {
            if (piece.getType() == 7){
                piece.setName(typeDictionary.get(7)+typeDictionary.get(jokerNextSkillCounter));
                piece.updatePieceInfo();
            }
        }
    }

    private void increaseJohnMcClaneCounter(){
        int counter = piecesCounter.get(johnMcClaneCounter)+1;
        piecesCounter.put(johnMcClaneCounter, counter);
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
                piecesDictionary.get(chessMatrix.get(y).get(x)).toStringInfo() : new String[]{};
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
        else if (piecesCounter.get(limitOfMovesByPiecesController) == 10
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
        gameStatistic.add(String.valueOf(getPiecesSize()/2-piecesCounter.get(whitePiecesEliminatedController)));
        gameStatistic.add(String.valueOf(piecesCounter.get(blackPiecesValidMovesCounter)));
        gameStatistic.add(String.valueOf(piecesCounter.get(blackPiecesInvalidMovesCounter)));

        gameStatistic.add("Equipas das Brancas");
        gameStatistic.add(String.valueOf(getPiecesSize()/2-piecesCounter.get(blackPiecesEliminatedController)));
        gameStatistic.add(String.valueOf(piecesCounter.get(whitePiecesValidMovesCounter)));
        gameStatistic.add(String.valueOf(piecesCounter.get(whitePiecesInvalidMovesCounter)));

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
                        peca.setPieceInfo("Doh! zzzzzz");
                    }

                    if (type == 7){
                        peca.setName("Joker/Rainha");
                        peca.updatePieceInfo();
                    }

                    if (teamsList.isEmpty()){
                        teamsList.add(team);
                    }
                    else if (!teamsList.contains(team)) {
                        teamsList.add(team);
                    }
                    /*
                    if (team == 10){
                        isBlackPieceInBoard = true;
                    }

                     */

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

            if (teamsList.contains(blackPiece)){
                currentTeam = blackPiece;
            }else{
                currentTeam = yellowPiece;
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

                int pieceId = Integer.parseInt(chessLineInfo[j]);

                if (pieceId != 0){
                    Piece piece = piecesDictionary.get(pieceId);
                    piece.setCoordinateX(j);
                    piece.setCoordinateY(i);
                    if (piece.getType() != 6){
                        piece.updatePieceInfo();
                    }
                }


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

            if (teamsList.contains(blackPiece)) {
                if (currentTeam == blackPiece) {
                    int currentResult = piecesCounter.get(blackPiecesValidMovesCounter);
                    piecesCounter.put(blackPiecesValidMovesCounter, --currentResult);
                    currentTeam = teamsList.contains(whitePiece) ? whitePiece : yellowPiece;
                }
                else if (currentTeam == whitePiece) {
                    int currentResult = piecesCounter.get(whitePiecesValidMovesCounter);
                    piecesCounter.put(whitePiecesValidMovesCounter, --currentResult);
                    currentTeam = blackPiece;
                } else if (currentTeam == yellowPiece) {
                    currentTeam = blackPiece;
                }
            }
            else {
                if (currentTeam == whitePiece) {
                    int currentResult = piecesCounter.get(whitePiecesValidMovesCounter);
                    piecesCounter.put(blackPiecesValidMovesCounter, --currentResult);
                    currentTeam = yellowPiece;
                } else if (currentTeam == yellowPiece) {
                    currentTeam = whitePiece;
                }
            }
            undoList.remove(undoList.size()-1);
        }
    }

    public List<Comparable> getHints(int x, int y) {

        List<Comparable> comparables = new ArrayList<>();


        int pieceId = chessMatrix.get(y).get(x);
        Piece piece = piecesDictionary.get(pieceId);

        //Coloca a peça Rei na nova posição

        if (piece.getType() == 0) {

            //Coloca da rainha na posição diagonal
            for (int i = x + 1, j = y + 1; i < getBoardSize() && j < getBoardSize(); ++i, ++j) {
                if (i < 0 || i >= getBoardSize() ||j < 0 || j >= getBoardSize()){
                    break;
                }

                if (Math.abs(x - i) != 2){
                    if (i == getBoardSize() || j == getBoardSize()) {
                        if (chessMatrix.get(j).get(i) != 0) {
                            int nextPieceId = chessMatrix.get(j).get(i);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                comparables.add(new Comparable(i, j, nextPiece.getValue(),nextPiece.getType()));
                            }
                        }
                        else {
                            comparables.add(new Comparable(i, j, 0, -1));
                        }
                        break;
                    }
                    else {
                        if (chessMatrix.get(j).get(i) == 0) {
                            comparables.add(new Comparable(i, j, 0, -1));
                        } else {
                            int nextPieceId = chessMatrix.get(j).get(i);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                comparables.add(new Comparable(i, j, nextPiece.getValue(),nextPiece.getType()));
                            }
                            break;
                        }
                    }
                }
                else{
                    break;
                }
            }

            for (int i = x - 1, j = y - 1; i >= 0 || j >= 0; --i, --j) {

                if (i < 0 || i >= getBoardSize() || j < 0 || j >= getBoardSize()){
                    break;
                }

                if (Math.abs(x - i) != 2){
                    if (i == 0 || j == 0) {
                        if (chessMatrix.get(j).get(i) != 0) {
                            int nextPieceId = chessMatrix.get(j).get(i);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                comparables.add(new Comparable(i, j, nextPiece.getValue(),nextPiece.getType()));
                            }
                        }
                        else {
                            comparables.add(new Comparable(i, j, 0, -1));
                        }
                        break;
                    }
                    else {
                        if (chessMatrix.get(j).get(i) == 0) {
                            comparables.add(new Comparable(i, j, 0, -1));
                        }
                        else {
                            int nextPieceId = chessMatrix.get(j).get(i);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                comparables.add(new Comparable(i, j, nextPiece.getValue(),nextPiece.getType()));
                            }
                            break;
                        }
                    }
                }
                else{
                    break;
                }
            }

            for (int i = x + 1, j = y - 1; i < getBoardSize() || j >= 0; ++i, --j) {

                if (i < 0 || i >= getBoardSize() ||j < 0 || j >= getBoardSize()){
                    break;
                }

                if (Math.abs(x-i) != 2){
                    if (i == getBoardSize() || j == 0) {
                        if (chessMatrix.get(j).get(i) != 0) {
                            int nextPieceId = chessMatrix.get(j).get(i);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                comparables.add(new Comparable(i, j, nextPiece.getValue(),nextPiece.getType()));
                            }
                        }
                        else {
                            comparables.add(new Comparable(i, j, 0, -1));
                        }
                        break;
                    }
                    else {
                        if (chessMatrix.get(j).get(i) == 0) {
                            comparables.add(new Comparable(i, j, 0, -1));
                        }
                        else {
                            int nextPieceId = chessMatrix.get(j).get(i);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                comparables.add(new Comparable(i, j, nextPiece.getValue(),nextPiece.getType()));
                            }
                            break;
                        }
                    }
                }
                else{
                    break;
                }

            }

            for (int i = x - 1, j = y + 1; i >= 0 || j < getBoardSize(); --i, ++j) {

                if (i < 0 || i >= getBoardSize() ||j < 0 || j >= getBoardSize()){
                    break;
                }

                if (Math.abs(x-i) != 2){
                    if (i == 0 || j == getBoardSize()) {
                        if (chessMatrix.get(j).get(i) != 0) {
                            int nextPieceId = chessMatrix.get(j).get(i);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                comparables.add(new Comparable(i, j, nextPiece.getValue(),nextPiece.getType()));
                            }
                        }
                        else {
                            comparables.add(new Comparable(i, j, 0, -1));
                        }
                        break;
                    }
                    else {
                        if (chessMatrix.get(j).get(i) == 0) {
                            comparables.add(new Comparable(i, j, 0, -1));
                        }
                        else {
                            int nextPieceId = chessMatrix.get(j).get(i);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                comparables.add(new Comparable(i, j, nextPiece.getValue(),nextPiece.getType()));
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
                if (Math.abs(x-i) != 2){
                    if (i == getBoardSize()) {
                        if (chessMatrix.get(y).get(i) != 0) {
                            int nextPieceId = chessMatrix.get(y).get(i);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                comparables.add(new Comparable(i, y, nextPiece.getValue(), nextPiece.getType()));
                            }
                        }
                        else {
                            comparables.add(new Comparable(i, y, 0, -1));
                        }
                        break;
                    }

                    else {
                        if (chessMatrix.get(y).get(i) == 0) {
                            comparables.add(new Comparable(i, y, 0, -1));
                        }
                        else {
                            int nextPieceId = chessMatrix.get(y).get(i);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                comparables.add(new Comparable(i, y, nextPiece.getValue(), nextPiece.getType()));
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
                if (Math.abs(x-i) != 2){
                    if (i == 0) {
                        if (chessMatrix.get(y).get(i) != 0) {
                            int nextPieceId = chessMatrix.get(y).get(i);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                comparables.add(new Comparable(i, y, nextPiece.getValue(), nextPiece.getType()));
                            }
                        }
                        else {
                            comparables.add(new Comparable(i, y, 0, -1));
                        }
                        break;
                    }
                    else {
                        if (chessMatrix.get(y).get(i) == 0) {
                            comparables.add(new Comparable(i, y, 0, -1));
                        }
                        else {
                            int nextPieceId = chessMatrix.get(y).get(i);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                comparables.add(new Comparable(i, y, nextPiece.getValue(), nextPiece.getType()));
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
                if (Math.abs(j-y) != 2){
                    if (j == getBoardSize()) {
                        if (chessMatrix.get(j).get(x) != 0) {
                            int nextPieceId = chessMatrix.get(j).get(x);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                comparables.add(new Comparable(x, j, nextPiece.getValue(), nextPiece.getType()));
                            }
                        }
                        else {
                            comparables.add(new Comparable(x, j, 0, -1));
                        }
                        break;
                    }
                    else {
                        if (chessMatrix.get(j).get(x) == 0) {
                            comparables.add(new Comparable(x, j, 0, -1));
                        }
                        else {
                            int nextPieceId = chessMatrix.get(j).get(x);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                comparables.add(new Comparable(x, j, nextPiece.getValue(), nextPiece.getType()));
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
                if (Math.abs(j-y) != 2){
                    if (j == 0) {
                        if (chessMatrix.get(j).get(x) != 0) {
                            int nextPieceId = chessMatrix.get(j).get(x);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                comparables.add(new Comparable(x, j, nextPiece.getValue(), nextPiece.getType()));
                            }
                        }
                        else {
                            comparables.add(new Comparable(x, j, 0, -1));
                        }
                        break;
                    }
                    else {
                        if (chessMatrix.get(j).get(x) == 0) {
                            comparables.add(new Comparable(x, j, 0, -1));
                        }
                        else {
                            int nextPieceId = chessMatrix.get(j).get(x);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                comparables.add(new Comparable(x, j, nextPiece.getValue(), nextPiece.getType()));
                            }
                            break;
                        }
                    }
                }
                else{
                    break;
                }
            }

            comparables.add(new Comparable(x,y,piece.getValue(),piece.getType()));

        }
        else if (piece.getType() == 1) {

            //Coloca da rainha na posição diagonal
            for (int i = x, j = y; i < getBoardSize() && j < getBoardSize(); ++i, ++j) {

                if (!(chessMatrix.get(j).get(i) != 0 && i != getBoardSize() - 1 && j != getBoardSize() - 1)) {
                    comparables.add(new Comparable(i, j, 0, -1));
                } 
                else if (chessMatrix.get(j).get(i) != 0 && i == getBoardSize() - 1 && j == getBoardSize() - 1) {
                    int nextPieceId = chessMatrix.get(j).get(i);
                    Piece nextPiece = piecesDictionary.get(nextPieceId);
                    comparables.add(new Comparable(i, j, nextPiece.getValue(),nextPiece.getType()));
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
                                comparables.add(new Comparable(i, j, nextPiece.getValue(),nextPiece.getType()));
                            }
                        }
                        else {
                            comparables.add(new Comparable(i, j, 0, -1));
                        }
                        break;
                    }
                    else {
                        if (chessMatrix.get(j).get(i) == 0) {
                            comparables.add(new Comparable(i, j, 0, -1));
                        } else {
                            int nextPieceId = chessMatrix.get(j).get(i);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                comparables.add(new Comparable(i, j, nextPiece.getValue(),nextPiece.getType()));
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
                                comparables.add(new Comparable(i, j, nextPiece.getValue(),nextPiece.getType()));
                            }
                        }
                        else {
                            comparables.add(new Comparable(i, j, 0, -1));
                        }
                        break;
                    }
                    else {
                        if (chessMatrix.get(j).get(i) == 0) {
                            comparables.add(new Comparable(i, j, 0, -1));
                        }
                        else {
                            int nextPieceId = chessMatrix.get(j).get(i);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                comparables.add(new Comparable(i, j, nextPiece.getValue(),nextPiece.getType()));
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
                                comparables.add(new Comparable(i, j, nextPiece.getValue(),nextPiece.getType()));
                            }
                        }
                        else {
                            comparables.add(new Comparable(i, j, 0, -1));
                        }
                        break;
                    }
                    else {
                        if (chessMatrix.get(j).get(i) == 0) {
                            comparables.add(new Comparable(i, j, 0, -1));
                        }
                        else {
                            int nextPieceId = chessMatrix.get(j).get(i);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                comparables.add(new Comparable(i, j, nextPiece.getValue(),nextPiece.getType()));
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
                                comparables.add(new Comparable(i, y, nextPiece.getValue(), nextPiece.getType()));
                            }
                        }
                        else {
                            comparables.add(new Comparable(i, y, 0, -1));
                        }
                        break;
                    }
                    else {
                        if (chessMatrix.get(y).get(i) == 0) {
                            comparables.add(new Comparable(i, y, 0, -1));
                        }
                        else {
                            int nextPieceId = chessMatrix.get(y).get(i);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                comparables.add(new Comparable(i, y, nextPiece.getValue(), nextPiece.getType()));
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
                                comparables.add(new Comparable(i, y, nextPiece.getValue(), nextPiece.getType()));
                            }
                        }
                        else {
                            comparables.add(new Comparable(i, y, 0, -1));
                        }
                        break;
                    }
                    else {
                        if (chessMatrix.get(y).get(i) == 0) {
                            comparables.add(new Comparable(i, y, 0, -1));
                        }
                        else {
                            int nextPieceId = chessMatrix.get(y).get(i);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                comparables.add(new Comparable(i, y, nextPiece.getValue(), nextPiece.getType()));
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
                                comparables.add(new Comparable(x, j, nextPiece.getValue(), nextPiece.getType()));
                            }
                        }
                        else {
                            comparables.add(new Comparable(x, j, 0, -1));
                        }
                        break;
                    }
                    else {
                        if (chessMatrix.get(j).get(x) == 0) {
                            comparables.add(new Comparable(x, j, 0, -1));
                        }
                        else {
                            int nextPieceId = chessMatrix.get(j).get(x);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                comparables.add(new Comparable(x, j, nextPiece.getValue(), nextPiece.getType()));
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
                                comparables.add(new Comparable(x, j, nextPiece.getValue(), nextPiece.getType()));
                            }
                        }
                        else {
                            comparables.add(new Comparable(x, j, 0, -1));
                        }
                        break;
                    }
                    else {
                        if (chessMatrix.get(j).get(x) == 0) {
                            comparables.add(new Comparable(x, j, 0, -1));
                        }
                        else {
                            int nextPieceId = chessMatrix.get(j).get(x);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                comparables.add(new Comparable(x, j, nextPiece.getValue(), nextPiece.getType()));
                            }
                            break;
                        }
                    }
                }
                else{
                    break;
                }
            }

            comparables.add(new Comparable(x,y,piece.getValue(),piece.getType()));

        }
        else if (piece.getType() == 5){
            //Vertical
            for (int j = y+1; j < getBoardSize(); ++j){
                    if (j == getBoardSize()) {
                        if (chessMatrix.get(j).get(x) != 0) {
                            int nextPieceId = chessMatrix.get(j).get(x);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                comparables.add(new Comparable(x, j, nextPiece.getValue(), nextPiece.getType()));
                            }
                        }
                        else {
                            comparables.add(new Comparable(x, j, 0, -1));
                        }
                        break;
                    }
                    else {
                        if (chessMatrix.get(j).get(x) == 0) {
                            comparables.add(new Comparable(x, j, 0, -1));
                        }
                        else {
                            int nextPieceId = chessMatrix.get(j).get(x);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                comparables.add(new Comparable(x, j, nextPiece.getValue(), nextPiece.getType()));
                            }
                            break;
                        }
                    }
            }
            for (int j = y-1; j >= 0; --j){
                    if (j == 0) {
                        if (chessMatrix.get(j).get(x) != 0) {
                            int nextPieceId = chessMatrix.get(j).get(x);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                comparables.add(new Comparable(x, j, nextPiece.getValue(), nextPiece.getType()));
                            }
                        }
                        else {
                            comparables.add(new Comparable(x, j, 0, -1));
                        }
                        break;
                    }
                    else {
                        if (chessMatrix.get(j).get(x) == 0) {
                            comparables.add(new Comparable(x, j, 0, -1));
                        }
                        else {
                            int nextPieceId = chessMatrix.get(j).get(x);
                            Piece nextPiece = piecesDictionary.get(nextPieceId);
                            if (nextPiece.getTeam() != piece.getTeam() && nextPiece.getType() != 1) {
                                comparables.add(new Comparable(x, j, nextPiece.getValue(), nextPiece.getType()));
                            }
                            break;
                        }
                    }

            }

            comparables.add(new Comparable(x,y,piece.getValue(),piece.getType()));
        }
        else if (piece.getType() == 10){
            comparables.add(new Comparable(x,y,piece.getValue(),piece.getType()));
        }


        return comparables;
    }
}
