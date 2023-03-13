package controller;

import model.chess_pieces.AbstractClasses.ChessPiece;
import view.ChessBoardActivity;
import model.ChessBoard.ChessBoard;

public class Controller
{
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Variables

    ChessBoard model;
    String gameMode;
    ChessBoardActivity view;

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Constructor

    public Controller(String gameMode, ChessBoardActivity view)
    {
        model=new ChessBoard(this,gameMode);
        this.gameMode=gameMode;
        this.view=view;
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Controller to Model methods

    public void askControllerToMakeTurn(int fromRow,int fromCol,int toRow,int toCol,String moveType,String initiator)
    {
        model.askModelToMakeTurn(fromRow,fromCol,toRow,toCol,moveType,initiator);
    }

    public boolean askControllerIfNotNull(int row, int col)
    {
        return model.askModelIfNotNull(row,col);
    }

    public int askControllerForImage(int row,int col)
    {
        return model.askModelForImage(row,col);
    }

    public int askControllerForTargetedImage(int row, int col)
    {
        return model.askModelForTargetedImage(row,col);
    }

    public int askControllerForCheckImage(int row, int col)
    {
        return model.askModelForCheckImage(row,col);
    }

    public boolean askControllerIfPieceAllowedToMove(int row, int col)
    {
        return model.askModelIfPieceAllowedToMove(row,col);
    }

    public ChessPiece askControllerForChessPiece(int row, int col)
    {
        return model.askModelForChessPiece(row,col);
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Controller to View methods

    public void askControllerToDisplayVictory(int allegiance)
    {
        view.askViewToDisplayVictory(allegiance);
    }

    public void askControllerToDisplayChessBoard()
    {
        view.askViewToDisplayChessBoard();
    }

    public void askControllerToDisplayCheck(int fromRow, int fromCol, int toRow, int toCol)
    {
        view.askViewToDisplayCheck(fromRow,fromCol,toRow,toCol);
    }

    public void askControllerToRemoveCheck()
    {
        view.askViewToRemoveCheck();
    }

    public String askControllerForPromotion()
    {
        return view.askViewForPromotion();
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

}
