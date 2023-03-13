package model.ChessBoard;

import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Objects;
import controller.Controller;
import model.Coordinate.Coordinate;
import model.Bot.Bot;
import model.chess_pieces.AbstractClasses.ChessPiece;
import model.chess_pieces.AbstractClasses.SpecialChessPiece;
import model.chess_pieces.Bishop;
import model.chess_pieces.King;
import model.chess_pieces.Knight;
import model.chess_pieces.Pawn;
import model.chess_pieces.Queen;
import model.chess_pieces.Rook;

public class ChessBoard
{
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Variables, allegiance: 0=white 1:black

    Controller controller;
    ChessPiece[][] chessBoard;
    int turn;
    boolean check;


    Coordinate[] lastMove;

    Bot enemy;
    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Constructor, allegiance: 0=white 1:black

    public ChessBoard(Controller controller,String gameMode)
    {
        initializeBot(gameMode);
        turn = 0;
        check=true;
        this.controller = controller;
        chessBoard = new ChessPiece[8][8];

        lastMove=new Coordinate[2];
        lastMove[0]=new Coordinate(-1,-1);
        lastMove[1]=new Coordinate(-1,-1);

        initializePieces();
        calculateMovesForEachPiece();
    }

    private void initializePieces()
    {
        initializePawns();
        initializeKings();
        initializeRooks();
        initializeBishops();
        initializeKnights();
        initializeQueens();
    }

    private void initializeBot(String gameMode)
    {
        switch (gameMode)
        {
            case "TwoPlayers":
            {
                enemy=null;
                break;
            }
            case "SinglePlayer":
            {
                enemy=new Bot(this);
                break;
            }
        }
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Make a move methods, allegiance: 0=white 1:black

    public void askModelToMakeTurn(int fromRow, int fromCol, int toRow, int toCol,String moveType,String initiator)
    {
        lastMove[0].setLocation(fromRow,fromCol);
        lastMove[1].setLocation(toRow,toCol);

        controller.askControllerToRemoveCheck();

        movePiece(fromRow,fromCol,toRow,toCol,moveType);
        checkForPromotion(toRow,toCol);
        calculateMovesForEachPiece();
        checkVictory(chessBoard[toRow][toCol]);
        switchCurrentPlayer();
        controller.askControllerToDisplayChessBoard();

        if(initiator.equals("Player")&&(enemy!=null))
        {
            enemy.askBotToMakeMove(chessBoard);
        }
    }

    private void movePiece(int fromRow, int fromCol, int toRow, int toCol,String moveType)
    {
        switch (moveType)
        {
            case "regularMove":
            {
                regularMove(fromRow,fromCol,toRow,toCol);
                break;
            }
            case "enPassant":
            {
                enPassant(fromRow,fromCol,toRow,toCol);
                break;
            }
            case "castling":
            {
                castling(fromRow,fromCol,toRow,toCol);
                break;
            }
        }
    }

    private void regularMove(int fromRow, int fromCol, int toRow, int toCol)
    {
        chessBoard[toRow][toCol]=chessBoard[fromRow][fromCol];
        chessBoard[fromRow][fromCol]=null;
        chessBoard[toRow][toCol].setLocation(toRow,toCol);
        chessBoard[toRow][toCol].calculateThreatening(chessBoard,1,lastMove[1].getRow(),lastMove[1].getCol());
        if(chessBoard[toRow][toCol] instanceof SpecialChessPiece)
        {
            ((SpecialChessPiece)chessBoard[toRow][toCol]).increaseTimesMoved();
            if((fromRow-toRow==2||fromRow-toRow==-2) && (chessBoard[toRow][toCol] instanceof Pawn))
            {
                ((Pawn)chessBoard[toRow][toCol]).pawnMovedTwoSquares();
            }
        }
    }

    private void enPassant(int fromRow, int fromCol, int toRow, int toCol)
    {
        chessBoard[fromRow][toCol]=null;
        chessBoard[toRow][toCol]=chessBoard[fromRow][fromCol];
        chessBoard[fromRow][fromCol]=null;
        chessBoard[toRow][toCol].setLocation(toRow,toCol);
        ((SpecialChessPiece)chessBoard[toRow][toCol]).increaseTimesMoved();
        chessBoard[toRow][toCol].calculateThreatening(chessBoard,1,lastMove[1].getRow(),lastMove[1].getCol());
    }

    private void castling(int fromRow, int fromCol, int toRow, int toCol)
    {
        if(toCol<fromCol)
        {
            chessBoard[fromRow][fromCol-2]=chessBoard[fromRow][fromCol];
            chessBoard[fromRow][fromCol-1]=chessBoard[fromRow][0];
            chessBoard[fromRow][fromCol-2].setLocation(fromRow,fromCol-2);
            chessBoard[fromRow][fromCol-1].setLocation(fromRow,fromCol-1);
            chessBoard[fromRow][fromCol]=null;
            chessBoard[fromRow][0]=null;
            ((SpecialChessPiece)chessBoard[fromRow][fromCol-1]).increaseTimesMoved();
        }
        else
        {
            chessBoard[fromRow][fromCol+2]=chessBoard[fromRow][fromCol];
            chessBoard[fromRow][fromCol+1]=chessBoard[fromRow][7];
            chessBoard[fromRow][fromCol+2].setLocation(fromRow,fromCol+2);
            chessBoard[fromRow][fromCol+1].setLocation(fromRow,fromCol+1);
            chessBoard[fromRow][fromCol]=null;
            chessBoard[fromRow][7]=null;
            ((SpecialChessPiece)chessBoard[fromRow][fromCol+1]).increaseTimesMoved();
        }
        chessBoard[toRow][toCol].calculateThreatening(chessBoard,1,lastMove[1].getRow(),lastMove[1].getCol());
        ((SpecialChessPiece)chessBoard[toRow][toCol]).increaseTimesMoved();
    }

    private void checkForPromotion(int row,int col)
    {
        if((((chessBoard[row][col].getAllegiance()==0)&&(row==0))||((chessBoard[row][col].getAllegiance()==1)&&(row==7)))&&(chessBoard[row][col] instanceof Pawn))
        {
            String promoteTo=controller.askControllerForPromotion();
            switch (promoteTo)
            {
                case "queen":
                {
                    Queen q=new Queen(row,col,chessBoard[row][col].getAllegiance());
                    chessBoard[row][col]=q;
                    break;
                }
                case "knight":
                {
                    Knight k=new Knight(row,col,chessBoard[row][col].getAllegiance());
                    chessBoard[row][col]=k;
                    break;
                }
                case "rook":
                {
                    Rook r=new Rook(row,col,chessBoard[row][col].getAllegiance());
                    r.increaseTimesMoved();
                    chessBoard[row][col]=r;
                    break;
                }
                case "bishop":
                {
                    Bishop b=new Bishop(row,col,chessBoard[row][col].getAllegiance());
                    chessBoard[row][col]=b;
                    break;
                }
            }
        }
    }

    private void switchCurrentPlayer()
    {
        if(turn==0)
        {
            turn=1;
        }
        else
        {
            turn=0;
        }
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Check Victory methods, allegiance: 0=white 1:black

    private void checkVictory(ChessPiece threatening)
    {
        int row;
        int col;

        for (int i = 0; i < threatening.getThreatening().size() ; i++)
        {
            row=threatening.getThreatening().get(i).getRow();
            col=threatening.getThreatening().get(i).getCol();
            if(chessBoard[row][col] instanceof King)
            {
                controller.askControllerToDisplayCheck(threatening.getLocation().getRow(),threatening.getLocation().getCol(),row,col);
                Check(chessBoard[row][col],threatening);
            }
        }
    }

    private void Check(ChessPiece king, ChessPiece attacker)
    {
        ArrayList<ChessPiece> piecesThatCanKill= AllWhoThreatenCoordinate(attacker.getLocation(), attacker.getAllegiance());
        Hashtable<Coordinate, ArrayList<ChessPiece> > piecesThatCanBlock=allWhoCanBlockThreatening(king,attacker);

        ArrayList<ChessPiece> pawnsThatCanEnPassant= AllWhoCanEnPassantCoordinate(attacker.getLocation(),attacker.getAllegiance());

        if((piecesThatCanKill.size()>0)||(canKingMove(king))||(piecesThatCanBlock.size()>0)||(pawnsThatCanEnPassant.size()>0))
        {
            removeAllPossibleMovesFromBoard(king.getAllegiance());
            setPiecesThreateningFocusOnCoordinate(piecesThatCanKill,attacker);
            setPawnsEnPassantFocusOnCoordinate(pawnsThatCanEnPassant,attacker);
            setBlockingPiecesFocusOnCoordinate(piecesThatCanBlock);
        }
        else
        {
            controller.askControllerToDisplayVictory(turn);
        }
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Change all pieces threatening array if CheckMate is not yet reached

    private void removeAllPossibleMovesFromBoard(int kingsAllegiance)
    {
        for (int i = 0; i < 8 ; i++)
        {
            for (int j = 0; j < 8 ; j++)
            {
                if (chessBoard[i][j] != null)
                {
                    if((!(chessBoard[i][j] instanceof King))||(!(chessBoard[i][j].getAllegiance()==kingsAllegiance)))
                        chessBoard[i][j].nullifyThreatening();
                }
            }
        }
    }

    private void setPiecesThreateningFocusOnCoordinate(ArrayList<ChessPiece> piecesThatCanKill,ChessPiece attacker)
    {
        for (int i = 0; i < piecesThatCanKill.size() ; i++)
        {
            piecesThatCanKill.get(i).setFocusOnCoordinate(attacker.getLocation());
        }
    }

    private void setPawnsEnPassantFocusOnCoordinate(ArrayList<ChessPiece> pawnsThatCanEnPassant,ChessPiece attacker)
    {
        for (int i = 0; i < pawnsThatCanEnPassant.size(); i++)
        {
            ((Pawn)pawnsThatCanEnPassant.get(i)).setEnPassantOnCoordinate(attacker.getLocation());
        }
    }

    private void setBlockingPiecesFocusOnCoordinate(Hashtable<Coordinate, ArrayList<ChessPiece>> piecesThatCanBlock)
    {
        Enumeration<Coordinate> allKeys = piecesThatCanBlock.keys();
        while (allKeys.hasMoreElements())
        {
            Coordinate key=allKeys.nextElement();
            for (int i = 0; i < Objects.requireNonNull(piecesThatCanBlock.get(key)).size() ; i++)
            {
                if(Objects.requireNonNull(piecesThatCanBlock.get(key)).get(i) instanceof Pawn)
                {
                    ((Pawn)Objects.requireNonNull(piecesThatCanBlock.get(key)).get(i)).setMovementOnCoordinate(key);
                }
                else
                {
                    Objects.requireNonNull(piecesThatCanBlock.get(key)).get(i).setFocusOnCoordinate(key);
                }
            }
        }
    }


    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Check if king can move out of check method

    private boolean canKingMove(ChessPiece king)
    {
        king.calculateThreatening(chessBoard,1,lastMove[1].getRow(),lastMove[1].getCol());
        return (king.getThreatening().size() > 0);
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Check if checking piece can be eaten

    private ArrayList<ChessPiece> AllWhoThreatenCoordinate(Coordinate toCheck, int coordinateAllegiance)
    {
        ArrayList<ChessPiece> allThreatening=new ArrayList<>();
        for (int i = 0; i < 8 ; i++)
        {
            for (int j = 0; j < 8 ; j++)
            {
                if(chessBoard[i][j]!=null)
                {
                    if(chessBoard[i][j].getAllegiance()!=coordinateAllegiance)
                    {
                        if(chessBoard[i][j].isChessPieceThreateningCoordinate(toCheck))
                        {
                            allThreatening.add(chessBoard[i][j]);
                        }
                    }
                }
            }
        }
        return allThreatening;
    }

    private ArrayList<ChessPiece> AllWhoCanEnPassantCoordinate(Coordinate toCheck, int coordinateAllegiance)
    {
        ArrayList<ChessPiece> allEnPassant=new ArrayList<>();
        for (int i = 0; i < 8 ; i++)
        {
            for (int j = 0; j < 8 ; j++)
            {
                if(chessBoard[i][j]!=null)
                {
                    if((chessBoard[i][j].getAllegiance()!=coordinateAllegiance)&&(chessBoard[i][j] instanceof Pawn))
                    {
                        if(((Pawn)chessBoard[i][j]).canPawnEliminatePieceWithEnPassant(toCheck))
                        {
                            allEnPassant.add(chessBoard[i][j]);
                        }
                    }
                }
            }
        }
        return allEnPassant;
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Check if someone can block the checking piece

    private Hashtable<Coordinate, ArrayList<ChessPiece>> allWhoCanBlockThreatening(ChessPiece king, ChessPiece checkingPiece)
    {
        ArrayList<Coordinate> allBetween=CoordinatesBetweenKindAndCheckingPiece(king,checkingPiece);
        Hashtable<Coordinate, ArrayList<ChessPiece>> allWhoCanBlock=new Hashtable<>();

        for (int i = 0; i < allBetween.size() ; i++)
        {
            ArrayList<ChessPiece> allWhoCanBlockCoordinate = AllWhoCanBlockCoordinate(allBetween.get(i),king.getAllegiance());
            if(allWhoCanBlockCoordinate.size()>0)
            {
                allWhoCanBlock.put(allBetween.get(i),allWhoCanBlockCoordinate);
            }
        }
        return allWhoCanBlock;
    }


    private ArrayList<Coordinate> CoordinatesBetweenKindAndCheckingPiece(ChessPiece king, ChessPiece checkingPiece)
    {
        ArrayList<Coordinate> allBetween=new ArrayList<>();

        if(!(checkingPiece instanceof Knight))
        {
            recursiveCalcAllBetween(allBetween, king.getLocation().getRow(), king.getLocation().getCol(),
                    checkingPiece.getLocation().getRow(), checkingPiece.getLocation().getCol());
        }

        return allBetween;
    }

    private ArrayList<ChessPiece> AllWhoCanBlockCoordinate(Coordinate toCheck, int kingsAllegiance)
    {
        ArrayList<ChessPiece> allWhoCanBlock=new ArrayList<>();
        for (int i = 0; i < 8 ; i++)
        {
            for (int j = 0; j < 8 ; j++)
            {
                if(chessBoard[i][j]!=null)
                {
                    if(chessBoard[i][j].getAllegiance()==kingsAllegiance)
                    {
                        if(chessBoard[i][j] instanceof Pawn)
                        {
                            if(((Pawn)chessBoard[i][j]).canPawnBlockCoordinate(toCheck))
                            {
                                allWhoCanBlock.add(chessBoard[i][j]);
                            }
                        }
                        else
                        {
                            if(chessBoard[i][j].isChessPieceThreateningCoordinate(toCheck))
                                allWhoCanBlock.add(chessBoard[i][j]);
                        }
                    }
                }
            }
        }
        return allWhoCanBlock;
    }

    private void recursiveCalcAllBetween(ArrayList<Coordinate> allBetween, int startRow, int startCol, int endRow, int endCol)
    {
        if((startRow==endRow)&&(startCol==endCol))
            allBetween.remove(0);
        else
        {
            allBetween.add(new Coordinate(startRow,startCol));
            if((startRow==endRow))
            {
                if(startCol<endCol)
                    recursiveCalcAllBetween(allBetween,startRow,startCol+1,endRow,endCol);
                else
                    recursiveCalcAllBetween(allBetween,startRow,startCol-1,endRow,endCol);
            }
            else if((startCol==endCol))
            {
                if(startRow<endRow)
                    recursiveCalcAllBetween(allBetween,startRow+1,startCol,endRow,endCol);
                else
                    recursiveCalcAllBetween(allBetween,startRow-1,startCol,endRow,endCol);
            }
            else
            {
                if(startRow<endRow)
                {
                    if(startCol<endCol)
                        recursiveCalcAllBetween(allBetween,startRow+1,startCol+1,endRow,endCol);
                    else
                        recursiveCalcAllBetween(allBetween,startRow+1,startCol-1,endRow,endCol);
                }
                else
                {
                    if(startCol<endCol)
                        recursiveCalcAllBetween(allBetween,startRow-1,startCol+1,endRow,endCol);
                    else
                        recursiveCalcAllBetween(allBetween,startRow-1,startCol-1,endRow,endCol);
                }
            }
        }

    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Calculate available moves method, allegiance: 0=white 1:black

    public void calculateMovesForEachPiece()
    {
        for (int i = 0; i < 8 ; i++)
        {
            for (int j = 0; j < 8 ; j++)
            {
                if(chessBoard[i][j]!=null)
                {
                    chessBoard[i][j].calculateThreatening(chessBoard,1,lastMove[1].getRow(),lastMove[1].getCol());
                }
            }
        }
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Initialization methods, allegiance: 0=white 1:black

    private void initializePawns()
    {
        Pawn initializePawn;

        for (int i = 0; i <= 7; i++)
        {
            initializePawn = new Pawn(1, i, 1);
            chessBoard[initializePawn.getRow()][initializePawn.getCol()] = initializePawn;

        }
        for (int i = 0; i <= 7; i++)
        {
            initializePawn=new Pawn(6,i,0);
            chessBoard[initializePawn.getRow()][initializePawn.getCol()]=initializePawn;
        }
    }

    private void initializeKings()
    {
        King initializeKing;

        initializeKing=new King(0,4,1);
        chessBoard[initializeKing.getRow()][initializeKing.getCol()]=initializeKing;
        initializeKing=new King(7,4,0);
        chessBoard[initializeKing.getRow()][initializeKing.getCol()]=initializeKing;
    }

    private void initializeRooks()
    {
        Rook initializeRook;

        initializeRook=new Rook(0,0,1);
        chessBoard[initializeRook.getRow()][initializeRook.getCol()]=initializeRook;
        initializeRook=new Rook(0,7,1);
        chessBoard[initializeRook.getRow()][initializeRook.getCol()]=initializeRook;
        initializeRook=new Rook(7,0,0);
        chessBoard[initializeRook.getRow()][initializeRook.getCol()]=initializeRook;
        initializeRook=new Rook(7,7,0);
        chessBoard[initializeRook.getRow()][initializeRook.getCol()]=initializeRook;

    }

    private void initializeBishops()
    {
        Bishop initializeBishop;

        initializeBishop=new Bishop(0,2,1);
        chessBoard[initializeBishop.getRow()][initializeBishop.getCol()]=initializeBishop;
        initializeBishop=new Bishop(0,5,1);
        chessBoard[initializeBishop.getRow()][initializeBishop.getCol()]=initializeBishop;
        initializeBishop=new Bishop(7,2,0);
        chessBoard[initializeBishop.getRow()][initializeBishop.getCol()]=initializeBishop;
        initializeBishop=new Bishop(7,5,0);
        chessBoard[initializeBishop.getRow()][initializeBishop.getCol()]=initializeBishop;

    }

    private void initializeKnights()
    {
        Knight initializeKnight;
        initializeKnight=new Knight(0,1,1);
        chessBoard[initializeKnight.getRow()][initializeKnight.getCol()]=initializeKnight;
        initializeKnight=new Knight(0,6,1);
        chessBoard[initializeKnight.getRow()][initializeKnight.getCol()]=initializeKnight;
        initializeKnight=new Knight(7,1,0);
        chessBoard[initializeKnight.getRow()][initializeKnight.getCol()]=initializeKnight;
        initializeKnight=new Knight(7,6,0);
        chessBoard[initializeKnight.getRow()][initializeKnight.getCol()]=initializeKnight;
    }

    private void initializeQueens()
    {
        Queen initializeQueen;

        initializeQueen=new Queen(0,3,1);
        chessBoard[initializeQueen.getRow()][initializeQueen.getCol()]=initializeQueen;
        initializeQueen=new Queen(7,3,0);
        chessBoard[initializeQueen.getRow()][initializeQueen.getCol()]=initializeQueen;
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //toString method

    @NonNull
    public String toString()
    {
        StringBuilder sbChessBoard=new StringBuilder();
        StringBuilder sbPieces=new StringBuilder();
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if(chessBoard[i][j]!=null)
                {
                    if(chessBoard[i][j] instanceof Rook)
                    {
                        sbChessBoard.append("4 ");
                    }
                    else if(chessBoard[i][j] instanceof King)
                    {
                        sbChessBoard.append("1 ");
                    }
                    else if(chessBoard[i][j] instanceof Bishop)
                    {
                        sbChessBoard.append("3 ");
                    }
                    else if(chessBoard[i][j] instanceof Knight)
                    {
                        sbChessBoard.append("5 ");
                    }
                    else if(chessBoard[i][j] instanceof Queen)
                    {
                        sbChessBoard.append("2 ");
                    }
                    else if(chessBoard[i][j] instanceof Pawn)
                    {
                        sbChessBoard.append("6 ");
                    }
                    sbPieces.append(chessBoard[i][j].toString());
                }
                else
                {
                    sbChessBoard.append("0 ");
                }
            }
            sbChessBoard.append("\n");
        }
        sbChessBoard.append("\n");

        sbChessBoard.append(sbPieces);
        sbChessBoard.append("\n\n\n");
        return sbChessBoard.toString();
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Controller to Model methods

    public boolean askModelIfNotNull(int row, int col)
    {
        return (chessBoard[row][col]!=null);
    }

    public int askModelForImage(int row, int col)
    {
        if(chessBoard[row][col]!=null)
        {
            return chessBoard[row][col].getImage();
        }
        else
        {
            return 0;
        }
    }

    public int askModelForTargetedImage(int row, int col)
    {
        if(chessBoard[row][col]!=null)
        {
            return chessBoard[row][col].getImageTargeted();
        }
        else
        {
            return 0;
        }
    }

    public int askModelForCheckImage(int row,int col)
    {
        if(chessBoard[row][col]!=null)
        {
            return chessBoard[row][col].getImageCheck();
        }
        else
        {
            return 0;
        }
    }

    public boolean askModelIfPieceAllowedToMove(int row, int col)
    {
        return (chessBoard[row][col].getAllegiance()==turn);
    }

    public ChessPiece askModelForChessPiece(int row, int col)
    {
        return chessBoard[row][col];
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

}
