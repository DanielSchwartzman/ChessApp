package model.chess_pieces;

import com.example.chessapp.R;
import java.util.ArrayList;
import model.Coordinate.Coordinate;
import model.chess_pieces.AbstractClasses.ChessPiece;

public class Bishop extends ChessPiece
{

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Constructor

    public Bishop(int row,int col,int allegiance)
    {
        super(row,col,allegiance);
        setImages(allegiance);
    }

    private void setImages(int allegiance)
    {
        if(allegiance==0)
        {
            image= R.drawable.bishop_white;
            imageTargeted= R.drawable.bishop_white_selected;
        }
        else
        {
            image= R.drawable.bishop_black;
            imageTargeted= R.drawable.bishop_black_selected;
        }
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Calculate coordinates which given Bishop threatens

    public void calculateThreatening(ChessPiece[][] chessBoard, int calcType,int unUsed, int unUsed2)
    {//calcType: 0=Regular calculation,1=Calculate with regards to king
        threatening= new ArrayList<>();
        calculateDiagonalUpRight(chessBoard);
        calculateDiagonalUpLeft(chessBoard);
        calculateDiagonalDownRight(chessBoard);
        calculateDiagonalDownLeft(chessBoard);

        if(calcType==1)
        {
            removeIllegalMoves(chessBoard);
        }
    }

    private void calculateDiagonalUpRight(ChessPiece[][] chessBoard)
    {
        int i= location.getRow()-1;
        int j= location.getCol()+1;
        for (; ((i >= 0) && (j<=7)); i-- , j++)
        {
            if((chessBoard[i][j]==null))
            {
                Coordinate movement = new Coordinate(i, j);
                threatening.add(movement);
            }
            else
            {
                break;
            }
        }
        if((j<=7)&&(i>=0))
        {
            if(chessBoard[i][j]!=null)
            {
                if(chessBoard[i][j].getAllegiance()!=allegiance)
                {
                    Coordinate movement = new Coordinate(i, j);
                    threatening.add(movement);
                }
            }
        }
    }

    private void calculateDiagonalUpLeft(ChessPiece[][] chessBoard)
    {
        int i= location.getRow() - 1;
        int j= location.getCol() - 1;
        for (; ((i >= 0) && (j >= 0)) ; i--, j--)
        {
            if((chessBoard[i][j]==null))
            {
                Coordinate movement = new Coordinate(i, j);
                threatening.add(movement);
            }
            else
            {
                break;
            }
        }
        if((j >= 0) && (i >= 0))
        {
            if(chessBoard[i][j]!=null)
            {
                if(chessBoard[i][j].getAllegiance()!=allegiance)
                {
                    Coordinate movement = new Coordinate(i, j);
                    threatening.add(movement);
                }
            }
        }

    }

    private void calculateDiagonalDownRight(ChessPiece[][] chessBoard)
    {
        int i= location.getRow() + 1;
        int j= location.getCol() + 1;
        for (;((i <= 7) && (j <= 7)); i++, j++)
        {
            if((chessBoard[i][j]==null))
            {
                Coordinate movement = new Coordinate(i, j);
                threatening.add(movement);
            }
            else
            {
                break;
            }
        }
        if((j <= 7) && (i <= 7))
        {
            if(chessBoard[i][j]!=null)
            {
                if(chessBoard[i][j].getAllegiance()!=allegiance)
                {
                    Coordinate movement = new Coordinate(i, j);
                    threatening.add(movement);
                }
            }
        }
    }

    private void calculateDiagonalDownLeft(ChessPiece[][] chessBoard)
    {
        int i = location.getRow() + 1;
        int j = location.getCol() - 1;
        for (;((i <= 7) && (j >= 0)); i++, j--)
        {
            if((chessBoard[i][j]==null))
            {
                Coordinate movement = new Coordinate(i, j);
                threatening.add(movement);
            }
            else
            {
                break;
            }
        }
        if((j >= 0) && (i <= 7))
        {
            if(chessBoard[i][j]!=null)
            {
                if(chessBoard[i][j].getAllegiance()!=allegiance)
                {
                    Coordinate movement = new Coordinate(i, j);
                    threatening.add(movement);
                }
            }
        }
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Copy method

    public ChessPiece generateCopyChessPiece()
    {
        return new Bishop(location.getRow(),location.getCol(),allegiance);
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

}
