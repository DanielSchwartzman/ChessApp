package model.chess_pieces;

import com.example.chessapp.R;
import java.util.ArrayList;
import model.Coordinate.Coordinate;
import model.chess_pieces.AbstractClasses.ChessPiece;
import model.chess_pieces.AbstractClasses.SpecialChessPiece;

public class Pawn extends SpecialChessPiece
{

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Variables

    ArrayList<Coordinate> movement;
    ArrayList<Coordinate> enPassant;
    Boolean movedTwoSquares;

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Constructor

    public Pawn(int row,int col,int allegiance)
    {
        super(row,col,allegiance);
        movedTwoSquares=false;
        setImages(allegiance);
    }

    private void setImages(int allegiance)
    {
        if(allegiance==0)
        {
            image= R.drawable.pawn_white;
            imageTargeted= R.drawable.pawn_white_selected;
        }
        else
        {
            image= R.drawable.pawn_black;
            imageTargeted= R.drawable.pawn_black_selected;
        }
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Getter methods

    public ArrayList<Coordinate> getMovement(){return movement;}
    public ArrayList<Coordinate> getEnPassant(){return enPassant;}

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Calculate coordinates which given Pawn threatens

    public void calculateThreatening(ChessPiece[][] chessBoard, int calcType,int lastMoveRow, int lastMoveCol)
    {//calcType: 0=Regular calculation,1=Calculate with regards to king
        threatening=new ArrayList<>();
        if(allegiance==0)
        {
            calculateUpwards(chessBoard);
            calculateEnPassantUpwards(chessBoard,lastMoveRow,lastMoveCol);
        }
        else
        {
            calculateDownwards(chessBoard);
            calculateEnPassantDownwards(chessBoard,lastMoveRow,lastMoveCol);
        }

        if(calcType==1)
        {
            removeIllegalMoves(chessBoard);
            removeIllegalEnPassant(chessBoard);
            removeIllegalMovement(chessBoard);
        }
    }

    private void calculateUpwards(ChessPiece[][] chessBoard)
    {
        movement=new ArrayList<>();
        int oneUp = location.getRow() - 1;
        if(oneUp>=0)
        {
            if (chessBoard[oneUp][location.getCol()] == null)
            {
                movement.add(new Coordinate(oneUp, location.getCol()));
                if ((oneUp>0)&&(timesMoved==0))
                {
                    if (chessBoard[oneUp - 1][location.getCol()] == null)
                    {
                        movement.add(new Coordinate(oneUp - 1, location.getCol()));
                    }
                }
            }
            int oneLeft = location.getCol() - 1;
            int oneRight = location.getCol() + 1;
            if(oneLeft>=0)
            {
                if(chessBoard[oneUp][oneLeft]!=null)
                {
                    if(chessBoard[oneUp][oneLeft].getAllegiance()!=allegiance)
                    {
                        Coordinate oneUpOneLeft = new Coordinate(oneUp, oneLeft);
                        threatening.add(oneUpOneLeft);
                    }
                }
                else
                {
                    Coordinate oneUpOneLeft = new Coordinate(oneUp, oneLeft);
                    threatening.add(oneUpOneLeft);
                }
            }
            if(oneRight<=7)
            {
                if(chessBoard[oneUp][oneRight]!=null)
                {
                    if(chessBoard[oneUp][oneRight].getAllegiance()!=allegiance)
                    {
                        Coordinate oneUpOneRight = new Coordinate(oneUp, oneRight);
                        threatening.add(oneUpOneRight);
                    }
                }
                else
                {
                    Coordinate oneUpOneRight = new Coordinate(oneUp, oneRight);
                    threatening.add(oneUpOneRight);
                }
            }
        }
    }

    private void calculateDownwards(ChessPiece[][] chessBoard)
    {
        movement=new ArrayList<>();
        int oneDown = location.getRow() + 1;
        if(oneDown<=7)
        {
            if(chessBoard[oneDown][location.getCol()]==null)
            {
                movement.add(new Coordinate(oneDown, location.getCol()));
                if((oneDown<7)&&(timesMoved==0))
                {
                    if((chessBoard[oneDown+1][location.getCol()]==null))
                    {
                        movement.add(new Coordinate(oneDown+1, location.getCol()));
                    }
                }
            }
            int oneLeft = location.getCol() - 1;
            int oneRight = location.getCol() + 1;
            if(oneLeft>=0)
            {
                if(chessBoard[oneDown][oneLeft]!=null)
                {
                    if(chessBoard[oneDown][oneLeft].getAllegiance()!=allegiance)
                    {
                        Coordinate oneDownOneLeft = new Coordinate(oneDown, oneLeft);
                        threatening.add(oneDownOneLeft);
                    }
                }
                else
                {
                    Coordinate oneDownOneLeft = new Coordinate(oneDown, oneLeft);
                    threatening.add(oneDownOneLeft);
                }
            }
            if(oneRight<=7)
            {
                if(chessBoard[oneDown][oneRight]!=null)
                {
                    if(chessBoard[oneDown][oneRight].getAllegiance()!=allegiance)
                    {
                        Coordinate oneDownOneRight = new Coordinate(oneDown, oneRight);
                        threatening.add(oneDownOneRight);
                    }
                }
                else
                {
                    Coordinate oneDownOneRight = new Coordinate(oneDown, oneRight);
                    threatening.add(oneDownOneRight);
                }
            }
        }
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //EnPassant methods

    private void calculateEnPassantUpwards(ChessPiece[][] chessBoard,int lastMoveRow,int lastMoveCol)
    {
        enPassant= new ArrayList<>();
        if(location.getRow() > 0)
        {
            if(location.getCol() < 7)
            {
                enPassantUpRight(chessBoard,lastMoveRow,lastMoveCol);
            }
            if(location.getCol() > 0)
            {
                enPassantUpLeft(chessBoard,lastMoveRow,lastMoveCol);
            }
        }
    }

    private void calculateEnPassantDownwards(ChessPiece[][] chessBoard,int lastMoveRow,int lastMoveCol)
    {
        enPassant= new ArrayList<>();
        if(location.getRow() < 7)
        {
            if(location.getCol() < 7)
            {
                enPassantDownRight(chessBoard,lastMoveRow,lastMoveCol);
            }
            if(location.getCol() > 0)
            {
                enPassantDownLeft(chessBoard,lastMoveRow,lastMoveCol);
            }
        }
    }

    private void enPassantUpRight(ChessPiece[][] chessBoard,int lastMoveRow,int lastMoveCol)
    {
        int oneRight= location.getCol() + 1;
        if((chessBoard[location.getRow()][oneRight] instanceof Pawn)&&((location.getRow()==lastMoveRow)&&(oneRight==lastMoveCol)))
        {
            if((((Pawn)chessBoard[location.getRow()][oneRight]).canBeEnPassant())&&(chessBoard[location.getRow()][oneRight].getAllegiance()!=allegiance))
            {
                enPassant.add(new Coordinate(location.getRow() - 1,oneRight));
            }
        }
    }

    private void enPassantUpLeft(ChessPiece[][] chessBoard,int lastMoveRow,int lastMoveCol)
    {
        int oneLeft=location.getCol() - 1;
        if((chessBoard[location.getRow()][oneLeft] instanceof Pawn)&&((location.getRow()==lastMoveRow)&&(oneLeft==lastMoveCol)))
        {
            if((((Pawn)chessBoard[location.getRow()][oneLeft]).canBeEnPassant()) && (chessBoard[location.getRow()][oneLeft].getAllegiance()!=allegiance))
            {
                enPassant.add(new Coordinate(location.getRow() - 1,oneLeft));
            }
        }
    }

    private void enPassantDownRight(ChessPiece[][] chessBoard,int lastMoveRow,int lastMoveCol)
    {
        int oneRight= location.getCol() + 1;
        if((chessBoard[location.getRow()][oneRight] instanceof Pawn)&&((location.getRow()==lastMoveRow)&&(oneRight==lastMoveCol)))
        {
            if((((Pawn)chessBoard[location.getRow()][oneRight]).canBeEnPassant())&&(chessBoard[location.getRow()][oneRight].getAllegiance()!=allegiance))
            {
                enPassant.add(new Coordinate(location.getRow() + 1,oneRight));
            }
        }
    }

    private void enPassantDownLeft(ChessPiece[][] chessBoard,int lastMoveRow,int lastMoveCol)
    {
        int oneLeft=location.getCol() - 1;
        if((chessBoard[location.getRow()][oneLeft] instanceof Pawn)&&((location.getRow()==lastMoveRow)&&(oneLeft==lastMoveCol)))
        {
            if((((Pawn)chessBoard[location.getRow()][oneLeft]).canBeEnPassant()) && (chessBoard[location.getRow()][oneLeft].getAllegiance()!=allegiance))
            {
                enPassant.add(new Coordinate(location.getRow() + 1,oneLeft));
            }
        }
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //General Functions

    private boolean canBeEnPassant(){return ((timesMoved==1)&&(movedTwoSquares));}
    public void pawnMovedTwoSquares(){movedTwoSquares=true;}
    public void setMovementOnCoordinate(Coordinate movement){this.movement.add(movement);}
    public void nullifyMovementAndEnPassant(){enPassant=new ArrayList<>();movement=new ArrayList<>();}

    public boolean canPawnEliminatePieceWithEnPassant(Coordinate toCheck)
    {
        for (int i = 0; i < enPassant.size(); i++)
        {
            if(toCheck.equals(new Coordinate(location.getRow(),enPassant.get(i).getCol())))
            {
                return true;
            }
        }
        return false;
    }

    public void setEnPassantOnCoordinate(Coordinate pieceToThreaten)
    {
        if(allegiance==0)
        {
            this.enPassant.add(new Coordinate(pieceToThreaten.getRow()-1,pieceToThreaten.getCol()));
        }
        else
        {
            this.enPassant.add(new Coordinate(pieceToThreaten.getRow()+1,pieceToThreaten.getCol()));
        }
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Check if coordinate can be blocked by give pawn

    public boolean canPawnBlockCoordinate(Coordinate toCheck)
    {
        for (int i = 0; i < movement.size() ; i++)
        {
            if(toCheck.equals(movement.get(i)))
            {
                return true;
            }
        }
        return false;
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Copy method

    public ChessPiece generateCopyChessPiece()
    {
        return new Pawn(location.getRow(),location.getCol(),allegiance);
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Remove illegal moves methods

    private void removeIllegalEnPassant(ChessPiece[][] chessBoard)
    {
        for (int i = 0; i < enPassant.size(); i++)
        {
            ChessPiece[][] copy=copyChessBoard(chessBoard);
            int row=enPassant.get(i).getRow();
            int col=enPassant.get(i).getCol();
            copy[location.getRow()][location.getCol()]=null;
            copy[location.getRow()][col]=null;
            copy[row][col]=this;
            calculateRegularMovesForAllEnemies(copy);
            if(isKingThreatened(copy))
            {
                enPassant.remove(i);
                i--;
            }
        }
    }

    private void removeIllegalMovement(ChessPiece[][] chessBoard)
    {
        for (int i = 0; i < movement.size(); i++)
        {
            ChessPiece[][] copy=copyChessBoard(chessBoard);
            int row=movement.get(i).getRow();
            int col=movement.get(i).getCol();
            copy[location.getRow()][location.getCol()]=null;
            copy[row][col]=this;
            calculateRegularMovesForAllEnemies(copy);
            if(isKingThreatened(copy))
            {
                movement.remove(i);
                i--;
            }
        }
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

}
