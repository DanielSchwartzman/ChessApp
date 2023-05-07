package model.chess_pieces.AbstractClasses;

import java.util.ArrayList;
import model.Coordinate.Coordinate;
import model.chess_pieces.King;
import model.chess_pieces.Pawn;

public abstract class ChessPiece
{

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Variables

    protected ArrayList<Coordinate> threatening;
    protected Coordinate location;
    protected int allegiance;
    protected int image;
    protected int imageTargeted;

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Constructor

    public ChessPiece(int row,int col,int allegiance)
    {
        location=new Coordinate(row,col);
        this.allegiance=allegiance;
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Inline Functions

    public int getAllegiance() {return allegiance;}
    public int getRow(){return location.getRow();}
    public int getCol(){return location.getCol();}

    public int getImage() {return image;}
    public int getImageTargeted() {return imageTargeted;}

    public ArrayList<Coordinate> getThreatening(){ return threatening;}
    public Coordinate getLocation(){return location;}
    public void setLocation(int row,int col){location=new Coordinate(row,col);}
    public void setFocusOnCoordinate(Coordinate toFocus){threatening.add(toFocus);}

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //General Functions

    public boolean isChessPieceThreateningCoordinate(Coordinate toCheck)
    {
        for (int i=0; i<threatening.size(); i++)
        {
            if(toCheck.equals(threatening.get(i)))
            {
                return true;
            }
        }
        return false;
    }

    protected void removeIllegalMoves(ChessPiece[][] chessBoard)
    {
        for (int i = 0; i < threatening.size(); i++)
        {
            ChessPiece[][] copy=copyChessBoard(chessBoard);
            int row=threatening.get(i).getRow();
            int col=threatening.get(i).getCol();
            copy[location.getRow()][location.getCol()]=null;
            copy[row][col]=this;
            calculateRegularMovesForAllEnemies(copy);
            if(isKingThreatened(copy))
            {
                threatening.remove(i);
                i--;
            }
        }
    }

    protected void calculateRegularMovesForAllEnemies(ChessPiece[][] chessBoard)
    {
        for (int i = 0; i < 8 ; i++)
        {
            for (int j = 0; j < 8 ; j++)
            {
                if(chessBoard[i][j]!=null)
                    if(chessBoard[i][j].getAllegiance()!=allegiance)
                        chessBoard[i][j].calculateThreatening(chessBoard,0,-1,-1);
            }
        }
    }

    protected boolean isKingThreatened(ChessPiece[][] chessBoard)
    {
        for (int i = 0; i < 8 ; i++)
        {
            for (int j = 0; j < 8 ; j++)
            {
                if(chessBoard[i][j]!=null)
                {
                    if((chessBoard[i][j].getAllegiance()!=allegiance)&&(chessBoard[i][j].getThreatening()!=null))
                    {
                        for (int k = 0; k < chessBoard[i][j].getThreatening().size() ; k++)
                        {
                            int row=chessBoard[i][j].getThreatening().get(k).getRow();
                            int col=chessBoard[i][j].getThreatening().get(k).getCol();
                            if(chessBoard[row][col] instanceof King)
                            {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    protected ChessPiece[][] copyChessBoard(ChessPiece[][] chessBoard)
    {
        ChessPiece[][] copy=new ChessPiece[8][8];
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if(chessBoard[i][j]!=null)
                    copy[i][j]=chessBoard[i][j].generateCopyChessPiece();
            }
        }
        return copy;
    }

    public void nullifyThreatening()
    {
        threatening=new ArrayList<>();
        if(this instanceof Pawn)
            ((Pawn)this).nullifyMovementAndEnPassant();
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Abstract Functions

    public abstract void  calculateThreatening(ChessPiece[][] chessBoard, int calcType, int lastMoveRow,int lastMoveCol);
    public abstract ChessPiece generateCopyChessPiece();

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

}
