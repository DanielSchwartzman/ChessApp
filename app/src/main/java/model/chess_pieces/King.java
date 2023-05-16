package model.chess_pieces;

import com.example.chessapp.R;
import java.util.ArrayList;

import model.chess_pieces.AbstractClasses.ChessPiece;
import model.Coordinate.Coordinate;
import model.chess_pieces.AbstractClasses.SpecialChessPiece;

public class King extends SpecialChessPiece
{

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Variables

    private ArrayList<Coordinate> castling;
    private int imageCheck;

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Constructor

    public King(int row, int col,int allegiance)
    {
        super(row,col,allegiance);
        setImages(allegiance);
    }

    private void setImages(int allegiance)
    {
        if(allegiance==0)
        {
            image= R.drawable.king_white;
            imageTargeted= R.drawable.king_white_selected;
            imageCheck=R.drawable.king_white_check;
        }
        else
        {
            image= R.drawable.king_black;
            imageTargeted= R.drawable.king_black_selected;
            imageCheck=R.drawable.king_black_check;
        }
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Calculate coordinates which given King threatens

    public void calculateThreatening(ChessPiece[][] chessBoard, int calcType,int unUsed, int unUsed2,int orientation)
    { //calcType: 0=Regular calculation,1=Calculate with regards to king
        threatening = new ArrayList<>();
        calculateUpwards(chessBoard);
        calculateDownwards(chessBoard);
        calculateLeft(chessBoard);
        calculateRight(chessBoard);
        calculateUpLeft(chessBoard);
        calculateUpRight(chessBoard);
        calculateDownLeft(chessBoard);
        calculateDownRight(chessBoard);

        if(calcType==1)
        {
            removeIllegalMoves(chessBoard,orientation);
            calculateCastling(chessBoard,orientation);
        }
    }

    private void calculateUpwards(ChessPiece[][] chessBoard)
    {
        int upperRow=location.getRow()-1;
        if(upperRow>=0)
        {
            if(chessBoard[upperRow][location.getCol()]==null)
            {
                Coordinate up=new Coordinate(upperRow, location.getCol());
                threatening.add(up);
            }
            else
            {
                if(chessBoard[upperRow][location.getCol()].getAllegiance()!=allegiance)
                {
                    Coordinate up=new Coordinate(upperRow, location.getCol());
                    threatening.add(up);
                }
            }
        }
    }

    private void calculateDownwards(ChessPiece[][] chessBoard)
    {
        int lowerRow=location.getRow()+1;
        if(lowerRow<=7)
        {
            if(chessBoard[lowerRow][location.getCol()]==null)
            {
                Coordinate down=new Coordinate(lowerRow, location.getCol());
                threatening.add(down);
            }
            else
            {
                if(chessBoard[lowerRow][location.getCol()].getAllegiance()!=allegiance)
                {
                    Coordinate down=new Coordinate(lowerRow, location.getCol());
                    threatening.add(down);
                }
            }
        }
    }

    private void calculateLeft(ChessPiece[][] chessBoard)
    {
        int leftCol= location.getCol()-1;
        if(leftCol>=0)
        {
            if(chessBoard[location.getRow()][leftCol]==null)
            {
                Coordinate left=new Coordinate(location.getRow(), leftCol);
                threatening.add(left);
            }
            else
            {
                if(chessBoard[location.getRow()][leftCol].getAllegiance()!=allegiance)
                {
                    Coordinate left=new Coordinate(location.getRow(), leftCol);
                    threatening.add(left);
                }
            }
        }
    }

    private void calculateRight(ChessPiece[][] chessBoard)
    {
        int rightCol= location.getCol()+1;
        if(rightCol<=7)
        {
            if(chessBoard[location.getRow()][rightCol]==null)
            {
                Coordinate right=new Coordinate(location.getRow(), rightCol);
                threatening.add(right);
            }
            else
            {
                if(chessBoard[location.getRow()][rightCol].getAllegiance()!=allegiance)
                {
                    Coordinate right=new Coordinate(location.getRow(), rightCol);
                    threatening.add(right);
                }
            }
        }
    }

    private void calculateUpRight(ChessPiece[][] chessBoard)
    {
        int upperRow=location.getRow()-1;
        int rightCol= location.getCol()+1;
        if((upperRow>=0)&&(rightCol<=7))
        {
            if(chessBoard[upperRow][rightCol]==null)
            {
                Coordinate upperRight=new Coordinate(upperRow,rightCol);
                threatening.add(upperRight);
            }
            else
            {
                if(chessBoard[upperRow][rightCol].getAllegiance()!=allegiance)
                {
                    Coordinate upperRight=new Coordinate(upperRow,rightCol);
                    threatening.add(upperRight);
                }
            }
        }
    }

    private void calculateUpLeft(ChessPiece[][] chessBoard)
    {
        int upperRow=location.getRow()-1;
        int leftCol= location.getCol()-1;
        if((upperRow>=0)&&(leftCol>=0))
        {
            if(chessBoard[upperRow][leftCol]==null)
            {
                Coordinate upperLeft=new Coordinate(upperRow,leftCol);
                threatening.add(upperLeft);
            }
            else
            {
                if(chessBoard[upperRow][leftCol].getAllegiance()!=allegiance)
                {
                    Coordinate upperLeft=new Coordinate(upperRow,leftCol);
                    threatening.add(upperLeft);
                }
            }
        }
    }

    private void calculateDownRight(ChessPiece[][] chessBoard)
    {
        int downRow=location.getRow()+1;
        int rightCol= location.getCol()+1;
        if((downRow<=7)&&(rightCol<=7))
        {
            if(chessBoard[downRow][rightCol]==null)
            {
                Coordinate downRight=new Coordinate(downRow,rightCol);
                threatening.add(downRight);
            }
            else
            {
                if(chessBoard[downRow][rightCol].getAllegiance()!=allegiance)
                {
                    Coordinate downRight=new Coordinate(downRow,rightCol);
                    threatening.add(downRight);
                }
            }
        }
    }

    private void calculateDownLeft(ChessPiece[][] chessBoard)
    {
        int downRow=location.getRow()+1;
        int leftCol= location.getCol()-1;
        if((downRow<=7)&&(leftCol>=0))
        {
            if(chessBoard[downRow][leftCol]==null)
            {
                Coordinate downLeft=new Coordinate(downRow,leftCol);
                threatening.add(downLeft);
            }
            else
            {
                if(chessBoard[downRow][leftCol].getAllegiance()!=allegiance)
                {
                    Coordinate downLeft=new Coordinate(downRow,leftCol);
                    threatening.add(downLeft);
                }
            }
        }
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Castling methods

    private void calculateCastling(ChessPiece[][] chessboard,int orientation)
    {
        castling=new ArrayList<>();
        if((timesMoved==0)&&(!isKingThreatened(chessboard)))
        {
            calculateCastlingRight(chessboard);
            calculateCastlingLeft(chessboard);
        }
        removeIllegalCastling(chessboard,orientation);
    }

    private void calculateCastlingLeft(ChessPiece[][] chessBoard)
    {
        boolean ableToCastle=true;
        int i=location.getCol()-1;
        for (; i > 0 ; i--)
        {
            ableToCastle=ableToCastle&&(chessBoard[location.getRow()][i]==null);
        }
        ableToCastle = ((ableToCastle) && (chessBoard[location.getRow()][i] instanceof Rook) && (((Rook) chessBoard[location.getRow()][i]).getTimesMoved()==0));
        if(ableToCastle)
        {
            castling.add(new Coordinate(location.getRow(), location.getCol()-2));
        }
    }

    private void calculateCastlingRight(ChessPiece[][] chessBoard)
    {
        boolean ableToCastle=true;
        int i=location.getCol()+1;
        for (; i < 7 ; i++)
        {
            ableToCastle=ableToCastle&&(chessBoard[location.getRow()][i]==null);
        }
        ableToCastle = ((ableToCastle) && (chessBoard[location.getRow()][i] instanceof Rook) && (((Rook) chessBoard[location.getRow()][i]).getTimesMoved()==0));
        if(ableToCastle)
        {
            castling.add(new Coordinate(location.getRow(), location.getCol()+2));
        }
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Getter method

    public ArrayList<Coordinate> getCastling()
    {
        return castling;
    }
    public int getImageCheck() {return imageCheck;}

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Copy method

    public ChessPiece generateCopyChessPiece()
    {
        return new King(location.getRow(),location.getCol(),allegiance);
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Remove illegal moves methods

    protected void removeIllegalCastling(ChessPiece[][] chessBoard,int orientation)
    {
        for (int i = 0; i < castling.size(); i++)
        {
            ChessPiece[][] copy=copyChessBoard(chessBoard);
            int row=castling.get(i).getRow();
            int col=castling.get(i).getCol();
            copy[location.getRow()][location.getCol()]=null;
            copy[row][col]=this;
            calculateRegularMovesForAllEnemies(copy,orientation);
            if(isKingThreatened(copy))
            {
                castling.remove(i);
                i--;
            }
        }
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

}
