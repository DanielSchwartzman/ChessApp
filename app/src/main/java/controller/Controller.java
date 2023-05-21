package controller;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import model.chess_pieces.AbstractClasses.ChessPiece;
import view.ChessBoardActivity;
import model.ChessBoard.ChessBoard;

public class Controller
{

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Variables

    //General variables
    ChessBoard gameBoard;
    ChessBoardActivity view;

    //Internet play variables
    String gameMode;
    String host;
    DatabaseReference roomInfoRef;

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Constructor

    public Controller(String gameMode,String chessBoardName,String host,ChessBoardActivity view)
    {
        this.gameMode=gameMode;
        this.view=view;
        gameBoard =new ChessBoard(gameMode,host);
        if(gameMode.equals("InternetPlay"))
        {
            this.host=host;
            roomInfoRef = FirebaseDatabase.getInstance().getReference(chessBoardName);
            listenForInfoChange();
        }
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Make turn methods

    public void askControllerToMakeTurn(int fromRow,int fromCol,int toRow,int toCol,String moveType)
    {
        if(gameMode.equals("TwoPlayers"))
        {
            sameDeviceMakeTurn(fromRow,fromCol,toRow,toCol,moveType);
        }
        if(gameMode.equals("InternetPlay"))
        {
            internetPlayMakeTurn(fromRow,fromCol,toRow,toCol,moveType);
        }
    }

    private void sameDeviceMakeTurn(int fromRow, int fromCol, int toRow, int toCol, String moveType)
    {
        String result = gameBoard.makeTurn(fromRow, fromCol, toRow, toCol, moveType);
        if (result.equals("victory"))
        {
            view.displayVictory(gameBoard.getVictoryTurn());
        }
        else
        {
            view.startPlayerInteraction();
        }
        view.displayChessBoard();
    }

    private void internetPlayMakeTurn(int fromRow, int fromCol, int toRow, int toCol, String moveType)
    {
        String result= gameBoard.makeTurn(fromRow,fromCol,toRow,toCol,moveType);
        roomInfoRef.setValue(host+","+Math.abs(fromRow-7)+","+Math.abs(fromCol-7)+","+Math.abs(toRow-7)+","+Math.abs(toCol-7)+","+moveType);
        if (result.equals("victory"))
        {
            view.displayVictory(gameBoard.getVictoryTurn());
        }
        view.displayChessBoard();
        view.connectionTimerStart();
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //ChessBoard info listener method

    private void listenForInfoChange()
    {
        roomInfoRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String value = dataSnapshot.getValue(String.class);
                if(value!=null)
                {
                    String[] valueSplit=value.split(",",6);
                    if(!valueSplit[0].equals(host))
                    {
                        view.connectionTimerStop();
                        updateGameBoardAccordingToOpponentMove(valueSplit[1],valueSplit[2],valueSplit[3],valueSplit[4],valueSplit[5]);
                        view.startPlayerInteraction();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error){}
        });
    }

    private void updateGameBoardAccordingToOpponentMove(String fromRow, String fromCol, String toRow, String toCol, String moveType)
    {
        int fromRowChange=Integer.parseInt(fromRow);
        int fromColChange=Integer.parseInt(fromCol);
        int toRowChange=Integer.parseInt(toRow);
        int toColChange=Integer.parseInt(toCol);
        String result = gameBoard.makeTurn(fromRowChange,fromColChange,toRowChange,toColChange,moveType);
        if (result.equals("victory"))
        {
            view.displayChessBoard();
            view.displayVictory(gameBoard.getVictoryTurn());
        }
        else
        {
            view.displayChessBoard();
        }
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //View to controller methods

    public boolean checkIfPieceExists(int row, int col)
    {
        return gameBoard.checkIfCoordinateNotNull(row,col);
    }

    public int getPieceImage(int row, int col)
    {
        return gameBoard.getImage(row,col);
    }

    public int getPieceSelectedImage(int row, int col)
    {
        return gameBoard.getSelectedImage(row,col);
    }

    public int getKingCheckedImage(int row, int col)
    {
        return gameBoard.getKingCheckImage(row,col);
    }

    public boolean checkIfPieceAllowedToMove(int row, int col)
    {
        return gameBoard.checkIfPieceAllowedToMove(row,col);
    }

    public ChessPiece getChessPiece(int row, int col)
    {
        return gameBoard.getChessPiece(row,col);
    }

    public int[] getCheckArray()
    {
        return gameBoard.getCurrentCheckLocation();
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

}
