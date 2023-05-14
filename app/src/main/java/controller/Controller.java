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
            if(host.equals("0"))
            {
                internetPlayHost();
                checkForConnection();
            }
            else
            {
                internetPlayClient();
            }
        }
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Initial connection methods

    private void internetPlayHost()
    {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(gameNumber);
        myRef.setValue("Active");
    }

    private void internetPlayClient()
    {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(gameNumber);
        myRef.setValue("Connected");
    }

    private void checkForConnection()
    {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(gameNumber);
        myRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String value = dataSnapshot.getValue(String.class);
                if(value!=null) {
                    if (value.equals("Connected"))
                    {
                        view.playerMove();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Make turn methods

    public void askControllerToMakeTurn(int fromRow,int fromCol,int toRow,int toCol,String moveType)
    {
        String result=model.askModelToMakeTurn(fromRow,fromCol,toRow,toCol,moveType,0);
        if(gameMode.equals("TwoPlayers"))
        {
            if (result.equals("victory"))
            {
                view.askViewToDisplayVictory(model.getVictoryTurn());
            }
            else
            {
                view.askViewToDisplayChessBoard();
                view.playerMove();
            }
        }
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
