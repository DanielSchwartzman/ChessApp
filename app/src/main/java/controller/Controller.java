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

    ChessBoard model;
    ChessBoardActivity view;
    String gameMode;
    String host;
    String gameNumber;

    DatabaseReference roomInfoRef;

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Constructor

    public Controller(String gameMode,String gameNumber,String host,ChessBoardActivity view)
    {
        this.gameMode=gameMode;
        this.view=view;
        model=new ChessBoard();
        if(gameMode.equals("InternetPlay"))
        {
            this.host=host;
            this.gameNumber=gameNumber;
            roomInfoRef = FirebaseDatabase.getInstance().getReference(gameNumber+"-ChessBoard");
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
        String result=model.askModelToMakeTurn(fromRow,fromCol,toRow,toCol,moveType);
        if (result.equals("victory"))
        {
            view.askViewToDisplayVictory(model.getVictoryTurn());
        }
        else
        {
            view.askViewToDisplayChessBoard();
            if(gameMode.equals("TwoPlayers"))
                view.playerMove();
        }
        if(gameMode.equals("InternetPlay"))
        {
            roomInfoRef.setValue(host+","+fromRow+","+fromCol+","+toRow+","+toCol+","+moveType);
        }
    }

    private void updateGameBoard(String fromRow,String fromCol,String toRow,String toCol,String moveType)
    {
        int fromRowChange=Integer.parseInt(fromRow);
        int fromColChange=Integer.parseInt(fromCol);
        int toRowChange=Integer.parseInt(toRow);
        int toColChange=Integer.parseInt(toCol);
        String result = model.askModelToMakeTurn(fromRowChange,fromColChange,toRowChange,toColChange,moveType);
        if (result.equals("victory"))
        {
            view.askViewToDisplayVictory(model.getVictoryTurn());
        }
        else
        {
            view.askViewToDisplayChessBoard();
        }
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
                        updateGameBoard(valueSplit[1],valueSplit[2],valueSplit[3],valueSplit[4],valueSplit[5]);
                        view.playerMove();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error){}
        });
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //View to controller methods

    public boolean checkIfPieceExists(int row, int col)
    {
        return model.askModelIfNotNull(row,col);
    }

    public int getPieceImage(int row, int col)
    {
        return model.askModelForImage(row,col);
    }

    public int getPieceSelectedImage(int row, int col)
    {
        return model.askModelForSelectedImage(row,col);
    }

    public int getKingCheckedImage(int row, int col)
    {
        return model.askModelForCheckImage(row,col);
    }

    public boolean checkIfPieceAllowedToMove(int row, int col)
    {
        return model.askModelIfPieceAllowedToMove(row,col);
    }

    public ChessPiece getChessPiece(int row, int col)
    {
        return model.askModelForChessPiece(row,col);
    }

    public int[] getCheckArray()
    {
        return model.getCheckedKingLocation();
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

}
