package model.chess_pieces;

import com.example.chessapp.R;
import java.util.ArrayList;

import model.chess_pieces.AbstractClasses.ChessPiece;
import model.Coordinate.Coordinate;
import model.chess_pieces.AbstractClasses.SpecialChessPiece;

public class Rook extends SpecialChessPiece
{

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Constructor

    public Rook(int row,int col,int allegiance)
    {
        super(row,col,allegiance);
        setImages(allegiance);
    }

    private void setImages(int allegiance)
    {
        if(allegiance==0)
        {
            image= R.drawable.rook_white;
            imageTargeted= R.drawable.rook_white_targeted;
            imageCheck=R.drawable.rook_white_check;
        }
        else
        {
            image= R.drawable.rook_black;
            imageTargeted= R.drawable.rook_black_targeted;
            imageCheck=R.drawable.rook_black_check;
        }
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Calculate coordinates which given Rook threatens

    public void calculateThreatening(ChessPiece[][] chessBoard, int calcType,int unUsed, int unUsed2)
    {//calcType: 0=Regular calculation,1=Calculate with regards to king
        threatening= new ArrayList<>();
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
        int i=location.getRow()-1;
        for (; (i>=0) ; i--)
        {
            if(chessBoard[i][location.getCol()]==null)
            {
                Coordinate movement = new Coordinate(i, location.getCol());
                threatening.add(movement);
            }
            else
            {
                break;
            }
        }
        if(i>=0)
        {
            if(chessBoard[i][location.getCol()]!=null)
            {
                if(chessBoard[i][location.getCol()].getAllegiance()!=allegiance)
                {
                    Coordinate threaten = new Coordinate(i, location.getCol());
                    threatening.add(threaten);
                }
            }
        }
    }

    private void calculateDownwards(ChessPiece[][] chessBoard)
    {
        int i=location.getRow()+1;
        for (; (i<=7) ; i++)
        {
            if(chessBoard[i][location.getCol()]==null)
            {
                Coordinate movement = new Coordinate(i, location.getCol());
                threatening.add(movement);
            }
            else
            {
                break;
            }
        }
        if(i<=7)
        {
            if(chessBoard[i][location.getCol()]!=null)
            {
                if(chessBoard[i][location.getCol()].getAllegiance()!=allegiance)
                {
                    Coordinate threaten = new Coordinate(i, location.getCol());
                    threatening.add(threaten);
                }
            }
        }
    }

    private void calculateRight(ChessPiece[][] chessBoard)
    {
        int i=location.getCol() +1;
        for (; (i<=7) ; i++) {
            if(chessBoard[location.getRow()][i]==null)
            {
                Coordinate movement = new Coordinate(location.getRow(), i);
                threatening.add(movement);
            }
            else
            {
                break;
            }
        }
        if(i<=7)
        {
            if(chessBoard[location.getRow()][i]!=null)
            {
                if(chessBoard[location.getRow()][i].getAllegiance()!=allegiance)
                {
                    Coordinate threaten = new Coordinate(location.getRow(), i);
                    threatening.add(threaten);
                }
            }
        }
    }

    private void calculateLeft(ChessPiece[][] chessBoard)
    {
        int i=location.getCol() -1;
        for (; (i>=0) ; i--)
        {
            if(chessBoard[location.getRow()][i]==null)
            {
                Coordinate movement = new Coordinate(location.getRow(), i);
                threatening.add(movement);
            }
            else
            {
                break;
            }
        }
        if(i>=0)
        {
            if(chessBoard[location.getRow()][i]!=null)
            {
                if(chessBoard[location.getRow()][i].getAllegiance()!=allegiance)
                {
                    Coordinate threaten = new Coordinate(location.getRow(), i);
                    threatening.add(threaten);
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
        return new Rook(location.getRow(),location.getCol(),allegiance);
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

}
