package model.Bot;

import java.util.ArrayList;

import model.ChessBoard.ChessBoard;
import model.chess_pieces.AbstractClasses.ChessPiece;
import model.chess_pieces.King;
import model.chess_pieces.Pawn;

public class Bot
{

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Variables

    ChessBoard chessBoard;
    ArrayList<ChessPiece> allAvailablePieces;

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Constructor

    public Bot(ChessBoard chessBoard)
    {
        this.chessBoard=chessBoard;
        allAvailablePieces=new ArrayList<>();
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Bot move method

    public void askBotToMakeMove(ChessPiece[][] chessBoard)
    {
        addAllAvailablePieces(chessBoard);
        int fromRow=-1;
        int fromCol=-1;
        int toRow=-1;
        int toCol=-1;

    }

    private void addAllAvailablePieces(ChessPiece[][] chessBoard)
    {
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if(chessBoard[i][j]!=null)
                {
                    if(chessBoard[i][j].getAllegiance()==1)
                    {
                        if(chessBoard[i][j].getThreatening().size()>0)
                        {
                            allAvailablePieces.add(chessBoard[i][j]);
                        }
                        else if(chessBoard[i][j] instanceof Pawn)
                        {
                            if((((Pawn)chessBoard[i][j]).getEnPassant().size()>0)||(((Pawn)chessBoard[i][j]).getMovement().size()>0))
                            {
                                allAvailablePieces.add(chessBoard[i][j]);
                            }
                        }
                        else if(chessBoard[i][j] instanceof King)
                        {
                            if(((King)chessBoard[i][j]).getCastling().size()>0)
                            {
                                allAvailablePieces.add(chessBoard[i][j]);
                            }
                        }
                    }
                }
            }
        }
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

}
