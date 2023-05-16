package model.chess_pieces;

import com.example.chessapp.R;
import java.util.ArrayList;
import model.Coordinate.Coordinate;
import model.chess_pieces.AbstractClasses.ChessPiece;

public class Queen extends ChessPiece
{

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Constructor

    public Queen(int row,int col,int allegiance)
    {
        super(row,col,allegiance);
        setImages(allegiance);
    }

    private void setImages(int allegiance)
    {
        if(allegiance==0)
        {
            image= R.drawable.queen_white;
            imageTargeted= R.drawable.queen_white_selected;
        }
        else
        {
            image= R.drawable.queen_black;
            imageTargeted= R.drawable.queen_black_selected;
        }
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Calculate coordinates which given Queen threatens

    public void calculateThreatening(ChessPiece[][] chessBoard, int calcType,int unUsed, int unUsed2,int currentOrientation)
    {//calcType: 0=Regular calculation,1=Calculate with regards to king
        threatening=new ArrayList<>();
        calculateUpwards(chessBoard);
        calculateDownwards(chessBoard);
        calculateLeft(chessBoard);
        calculateRight(chessBoard);
        calculateDiagonalUpLeft(chessBoard);
        calculateDiagonalUpRight(chessBoard);
        calculateDiagonalDownRight(chessBoard);
        calculateDiagonalDownLeft(chessBoard);

        if(calcType==1)
        {
            removeIllegalMoves(chessBoard,currentOrientation);
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
        return new Queen(location.getRow(),location.getCol(),allegiance);
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

}
