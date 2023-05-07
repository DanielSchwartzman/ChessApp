package model.chess_pieces;

import com.example.chessapp.R;
import java.util.ArrayList;
import model.Coordinate.Coordinate;
import model.chess_pieces.AbstractClasses.ChessPiece;

public class Knight extends ChessPiece
{

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Constructor

    public Knight(int row,int col,int allegiance)
    {
        super(row,col,allegiance);
        setImages(allegiance);
    }

    private void setImages(int allegiance)
    {
        if(allegiance==0)
        {
            image= R.drawable.knight_white;
            imageTargeted= R.drawable.knight_white_selected;
        }
        else
        {
            image= R.drawable.knight_black;
            imageTargeted= R.drawable.knight_black_selected;
        }
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Calculate coordinates which given Knight threatens

    public void calculateThreatening(ChessPiece[][] chessBoard, int calcType,int unUsed, int unUsed2)
    {
        threatening=new ArrayList<>();
        calculateUpwards(chessBoard);
        calculateDownwards(chessBoard);
        calculateRight(chessBoard);
        calculateLeft(chessBoard);

        if(calcType==1)
        {
            removeIllegalMoves(chessBoard);
        }
    }

    private void calculateUpwards(ChessPiece[][] chessBoard)
    {
        int twoUp = location.getRow() - 2;
        if(twoUp>=0)
        {
            int oneLeft = location.getCol() - 1;
            int oneRight = location.getCol() + 1;
            if(oneLeft>=0)
            {
                if(chessBoard[twoUp][oneLeft]==null)
                {
                    Coordinate twoUpOneLeft = new Coordinate(twoUp, oneLeft);
                    threatening.add(twoUpOneLeft);
                }
                else
                {
                    if(chessBoard[twoUp][oneLeft].getAllegiance()!=allegiance)
                    {
                        Coordinate twoUpOneLeft = new Coordinate(twoUp, oneLeft);
                        threatening.add(twoUpOneLeft);
                    }
                }
            }
            if(oneRight<=7)
            {
                if(chessBoard[twoUp][oneRight]==null)
                {
                    Coordinate twoUpOneRight = new Coordinate(twoUp, oneRight);
                    threatening.add(twoUpOneRight);
                }
                else
                {
                    if(chessBoard[twoUp][oneRight].getAllegiance()!=allegiance)
                    {
                        Coordinate twoUpOneRight = new Coordinate(twoUp, oneRight);
                        threatening.add(twoUpOneRight);
                    }
                }
            }
        }
    }

    private void calculateDownwards(ChessPiece[][] chessBoard)
    {
        int twoDown = location.getRow() + 2;
        if(twoDown<=7)
        {
            int oneLeft = location.getCol() - 1;
            int oneRight = location.getCol() + 1;
            if(oneLeft>=0)
            {
                if(chessBoard[twoDown][oneLeft]==null)
                {
                    Coordinate twoDownOneLeft = new Coordinate(twoDown, oneLeft);
                    threatening.add(twoDownOneLeft);
                }
                else
                {
                    if(chessBoard[twoDown][oneLeft].getAllegiance()!=allegiance)
                    {
                        Coordinate twoDownOneLeft = new Coordinate(twoDown, oneLeft);
                        threatening.add(twoDownOneLeft);
                    }
                }
            }
            if(oneRight<=7)
            {
                if(chessBoard[twoDown][oneRight]==null)
                {
                    Coordinate twoDownOneRight = new Coordinate(twoDown, oneRight);
                    threatening.add(twoDownOneRight);
                }
                else
                {
                    if(chessBoard[twoDown][oneRight].getAllegiance()!=allegiance)
                    {
                        Coordinate twoDownOneRight = new Coordinate(twoDown, oneRight);
                        threatening.add(twoDownOneRight);
                    }
                }
            }
        }
    }

    private void calculateLeft(ChessPiece[][] chessBoard)
    {
        int twoLeft = location.getCol() - 2;
        if(twoLeft>=0)
        {
            int oneUp = location.getRow() - 1;
            int oneDown = location.getRow() + 1;
            if(oneUp>=0)
            {
                if(chessBoard[oneUp][twoLeft]==null)
                {
                    Coordinate oneUpTwoLeft = new Coordinate(oneUp,twoLeft);
                    threatening.add(oneUpTwoLeft);
                }
                else
                {
                    if(chessBoard[oneUp][twoLeft].getAllegiance()!=allegiance)
                    {
                        Coordinate oneUpTwoLeft = new Coordinate(oneUp,twoLeft);
                        threatening.add(oneUpTwoLeft);
                    }
                }
            }
            if(oneDown<=7)
            {
                if(chessBoard[oneDown][twoLeft]==null)
                {
                    Coordinate oneDownTwoLeft=new Coordinate(oneDown,twoLeft);
                    threatening.add(oneDownTwoLeft);
                }
                else
                {
                    if(chessBoard[oneDown][twoLeft].getAllegiance()!=allegiance)
                    {
                        Coordinate oneDownTwoLeft=new Coordinate(oneDown,twoLeft);
                        threatening.add(oneDownTwoLeft);
                    }
                }
            }
        }
    }

    private void calculateRight(ChessPiece[][] chessBoard)
    {
        int twoRight = location.getCol() + 2;
        if(twoRight<=7)
        {
            int oneUp = location.getRow() - 1;
            int oneDown = location.getRow() + 1;
            if(oneUp>=0)
            {
                if(chessBoard[oneUp][twoRight]==null)
                {
                    Coordinate oneUpTwoLeft = new Coordinate(oneUp,twoRight);
                    threatening.add(oneUpTwoLeft);
                }
                else
                {
                    if(chessBoard[oneUp][twoRight].getAllegiance()!=allegiance)
                    {
                        Coordinate oneUpTwoLeft = new Coordinate(oneUp,twoRight);
                        threatening.add(oneUpTwoLeft);
                    }
                }
            }
            if(oneDown<=7)
            {
                if(chessBoard[oneDown][twoRight]==null)
                {
                    Coordinate oneDownTwoLeft=new Coordinate(oneDown,twoRight);
                    threatening.add(oneDownTwoLeft);
                }
                else
                {
                    if(chessBoard[oneDown][twoRight].getAllegiance()!=allegiance)
                    {
                        Coordinate oneDownTwoLeft=new Coordinate(oneDown,twoRight);
                        threatening.add(oneDownTwoLeft);
                    }
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
        return new Knight(location.getRow(),location.getCol(),allegiance);
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

}
