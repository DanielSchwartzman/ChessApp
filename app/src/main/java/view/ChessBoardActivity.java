package view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.chessapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import controller.Controller;
import model.Coordinate.Coordinate;
import model.chess_pieces.AbstractClasses.ChessPiece;
import model.chess_pieces.King;
import model.chess_pieces.Pawn;

public class ChessBoardActivity extends AppCompatActivity
{

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Variables

    ImageView[][] imageViews;
    TextView gameNumberText;
    Controller controller;
    String gameMode;
    String host;
    String gameNumber;

    DatabaseReference gameNumberRef;
    DatabaseReference roomInfoRef;


    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Constructor

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess_board);

        initializeStrings();
        initializeViewNodes();

        controller = new Controller(gameMode,gameNumber,host, this);
        askViewToDisplayChessBoard();

        if(gameMode.equals("TwoPlayers"))
        {
            playerMove();
        }
        else if(gameMode.equals("InternetPlay"))
        {
            if(host.equals("0"))
            {
                internetPlayHost();
            }
            else
            {
                internetPlayClient();
            }
            listenForRoomChanges();
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if((gameNumberRef!=null)&&(roomInfoRef!=null))
        {
            gameNumberRef.removeValue();
            roomInfoRef.removeValue();
        }
    }

    private void initializeStrings()
    {
        gameMode = getIntent().getStringExtra("GameMode");
        gameNumber = getIntent().getStringExtra("GameNumber");
        host = getIntent().getStringExtra("Host");
        if(gameNumber!=null)
        {
            gameNumberRef = FirebaseDatabase.getInstance().getReference(gameNumber);
            roomInfoRef = FirebaseDatabase.getInstance().getReference(gameNumber+"-ChessBoard");
        }
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Connection methods

    private void internetPlayHost()
    {
        gameNumberRef.setValue("OnePlayer");
    }

    private void internetPlayClient()
    {
        gameNumberRef.setValue("TwoPlayers");
        Toast.makeText(getApplicationContext(), "Connected to game", Toast.LENGTH_SHORT).show();
    }

    private void listenForRoomChanges()
    {
        gameNumberRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String value = dataSnapshot.getValue(String.class);
                if(value==null)
                {
                    Toast.makeText(getApplicationContext(), "Game closed", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    if (value.equals("TwoPlayers")&&host.equals("0"))
                    {
                        Toast.makeText(getApplicationContext(), "Opponent connected", Toast.LENGTH_SHORT).show();
                        playerMove();
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
    //Two players game mode

    public void playerMove()
    {
        for (int i = 0; i < 8 ; i++)
        {
            for (int j = 0; j < 8 ; j++)
            {
                int row=i;
                int col=j;
                if(controller.checkIfPieceExists(row,col))
                {
                    if(controller.checkIfPieceAllowedToMove(row,col))
                    {
                        imageViews[row][col].setOnClickListener(view ->
                                chooseWhereToMove(row,col));
                    }
                    else
                    {
                        imageViews[row][col].setOnClickListener(view -> {});
                    }
                }
                else
                {
                    imageViews[row][col].setOnClickListener(view -> {});
                }
            }
        }
    }

    private void chooseWhereToMove(int fromRow, int fromCol)
    {
        for (int i = 0; i < 8 ; i++)
        {
            for (int j = 0; j < 8 ; j++)
            {
                imageViews[i][j].setOnClickListener(view->{});
            }
        }
        int image=controller.getPieceSelectedImage(fromRow,fromCol);
        if(image!=0)
        {
            imageViews[fromRow][fromCol].setBackgroundResource(image);
        }
        imageViews[fromRow][fromCol].setOnClickListener(view ->
        {
            playerMove();
            askViewToDisplayChessBoard();
        });
        ChessPiece piece=controller.getChessPiece(fromRow,fromCol);
        if(piece instanceof Pawn)
        {
            highlightTargetedForPawn(fromRow,fromCol,piece);
        }
        else if(piece instanceof King)
        {
            highlightTargetedForKing(fromRow,fromCol,piece);
        }
        else
        {
            highlightTargeted(fromRow,fromCol,piece);
        }
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Highlight threatened coordinates

    private void highlightTargetedForPawn(int fromRow, int fromCol, ChessPiece pawn)
    {
        ArrayList<Coordinate> movement=((Pawn)pawn).getMovement();

        if(movement!=null)
        {
            for (int i = 0; i < movement.size(); i++)
            {
                int toRow = movement.get(i).getRow();
                int toCol = movement.get(i).getCol();
                int image=controller.getPieceSelectedImage(toRow,toCol);
                if(image==0)
                {
                    imageViews[toRow][toCol].setBackgroundResource(R.drawable.null_targeted);
                }
                imageViews[toRow][toCol].setOnClickListener(view ->
                {
                    disableClicks();
                    controller.askControllerToMakeTurn(fromRow, fromCol, toRow, toCol,"regularMove");
                });
            }
        }
        if(((Pawn)pawn).getEnPassant()!=null)
        {
            for (int i = 0; i < ((Pawn)pawn).getEnPassant().size() ; i++)
            {
                int toRow=((Pawn)pawn).getEnPassant().get(i).getRow();
                int toCol=((Pawn)pawn).getEnPassant().get(i).getCol();
                int image=controller.getPieceSelectedImage(toRow,toCol);
                if(image==0)
                {
                    imageViews[toRow][toCol].setBackgroundResource(R.drawable.null_targeted);
                }
                imageViews[toRow][toCol].setOnClickListener(view ->
                {
                    disableClicks();
                    controller.askControllerToMakeTurn(fromRow,fromCol,toRow,toCol,"enPassant");
                });
            }
        }
        if(pawn.getThreatening()!=null)
        {
            for (int i = 0; i < pawn.getThreatening().size() ; i++)
            {
                int toRow=pawn.getThreatening().get(i).getRow();
                int toCol=pawn.getThreatening().get(i).getCol();
                if(controller.checkIfPieceExists(toRow,toCol))
                {
                    int image = controller.getPieceSelectedImage(toRow, toCol);
                    if (image == 0)
                    {
                        imageViews[toRow][toCol].setBackgroundResource(R.drawable.null_targeted);
                    }
                    imageViews[toRow][toCol].setOnClickListener(view ->
                    {
                        disableClicks();
                        controller.askControllerToMakeTurn(fromRow, fromCol, toRow, toCol, "regularMove");
                    });
                }
            }
        }
    }

    private void highlightTargeted(int fromRow, int fromCol, ChessPiece piece)
    {
        for (int i = 0; i < piece.getThreatening().size(); i++)
        {
            int toRow=piece.getThreatening().get(i).getRow();
            int toCol=piece.getThreatening().get(i).getCol();
            int image=controller.getPieceSelectedImage(toRow,toCol);
            if(image==0)
            {
                imageViews[toRow][toCol].setBackgroundResource(R.drawable.null_targeted);
            }
            imageViews[toRow][toCol].setOnClickListener(view ->
            {
                disableClicks();
                controller.askControllerToMakeTurn(fromRow,fromCol,toRow,toCol,"regularMove");
            });
        }
    }

    private void highlightTargetedForKing(int fromRow, int fromCol, ChessPiece piece)
    {
        ArrayList<Coordinate> castling=((King)piece).getCastling();
        for (int i = 0; i < castling.size(); i++)
        {
            int toRow=castling.get(i).getRow();
            int toCol=castling.get(i).getCol();
            imageViews[toRow][toCol].setBackgroundResource(R.drawable.null_targeted);
            imageViews[toRow][toCol].setOnClickListener(view ->
            {
                disableClicks();
                controller.askControllerToMakeTurn(fromRow,fromCol,toRow,toCol,"castling");
            });
        }
        for (int i = 0; i < piece.getThreatening().size(); i++)
        {
            int toRow=piece.getThreatening().get(i).getRow();
            int toCol=piece.getThreatening().get(i).getCol();
            int image=controller.getPieceSelectedImage(toRow,toCol);
            if(image==0)
            {
                imageViews[toRow][toCol].setBackgroundResource(R.drawable.null_targeted);
            }
            imageViews[toRow][toCol].setOnClickListener(view ->
            {
                disableClicks();
                controller.askControllerToMakeTurn(fromRow,fromCol,toRow,toCol,"regularMove");
            });
        }
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Disable clicks

    private void disableClicks()
    {
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                imageViews[i][j].setOnClickListener(view->{});
            }
        }
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //initializing view nodes

    public void initializeViewNodes()
    {
        imageViews=new ImageView[8][8];
        initializeRow0(imageViews);
        initializeRow1(imageViews);
        initializeRow2(imageViews);
        initializeRow3(imageViews);
        initializeRow4(imageViews);
        initializeRow5(imageViews);
        initializeRow6(imageViews);
        initializeRow7(imageViews);

        if(gameNumber!=null)
        {
            gameNumberText=findViewById(R.id.chessBoardGameNumber);
            gameNumberText.setText(gameNumber);
        }

    }

    private void initializeRow0(ImageView[][] imageViews)
    {
        imageViews[0][0]=findViewById(R.id.row0col0);
        imageViews[0][1]=findViewById(R.id.row0col1);
        imageViews[0][2]=findViewById(R.id.row0col2);
        imageViews[0][3]=findViewById(R.id.row0col3);
        imageViews[0][4]=findViewById(R.id.row0col4);
        imageViews[0][5]=findViewById(R.id.row0col5);
        imageViews[0][6]=findViewById(R.id.row0col6);
        imageViews[0][7]=findViewById(R.id.row0col7);
    }

    private void initializeRow1(ImageView[][] imageViews)
    {
        imageViews[1][0]=findViewById(R.id.row1col0);
        imageViews[1][1]=findViewById(R.id.row1col1);
        imageViews[1][2]=findViewById(R.id.row1col2);
        imageViews[1][3]=findViewById(R.id.row1col3);
        imageViews[1][4]=findViewById(R.id.row1col4);
        imageViews[1][5]=findViewById(R.id.row1col5);
        imageViews[1][6]=findViewById(R.id.row1col6);
        imageViews[1][7]=findViewById(R.id.row1col7);
    }

    private void initializeRow2(ImageView[][] imageViews)
    {
        imageViews[2][0]=findViewById(R.id.row2col0);
        imageViews[2][1]=findViewById(R.id.row2col1);
        imageViews[2][2]=findViewById(R.id.row2col2);
        imageViews[2][3]=findViewById(R.id.row2col3);
        imageViews[2][4]=findViewById(R.id.row2col4);
        imageViews[2][5]=findViewById(R.id.row2col5);
        imageViews[2][6]=findViewById(R.id.row2col6);
        imageViews[2][7]=findViewById(R.id.row2col7);
    }

    private void initializeRow3(ImageView[][] imageViews)
    {
        imageViews[3][0]=findViewById(R.id.row3col0);
        imageViews[3][1]=findViewById(R.id.row3col1);
        imageViews[3][2]=findViewById(R.id.row3col2);
        imageViews[3][3]=findViewById(R.id.row3col3);
        imageViews[3][4]=findViewById(R.id.row3col4);
        imageViews[3][5]=findViewById(R.id.row3col5);
        imageViews[3][6]=findViewById(R.id.row3col6);
        imageViews[3][7]=findViewById(R.id.row3col7);
    }

    private void initializeRow4(ImageView[][] imageViews)
    {
        imageViews[4][0]=findViewById(R.id.row4col0);
        imageViews[4][1]=findViewById(R.id.row4col1);
        imageViews[4][2]=findViewById(R.id.row4col2);
        imageViews[4][3]=findViewById(R.id.row4col3);
        imageViews[4][4]=findViewById(R.id.row4col4);
        imageViews[4][5]=findViewById(R.id.row4col5);
        imageViews[4][6]=findViewById(R.id.row4col6);
        imageViews[4][7]=findViewById(R.id.row4col7);
    }

    private void initializeRow5(ImageView[][] imageViews)
    {
        imageViews[5][0]=findViewById(R.id.row5col0);
        imageViews[5][1]=findViewById(R.id.row5col1);
        imageViews[5][2]=findViewById(R.id.row5col2);
        imageViews[5][3]=findViewById(R.id.row5col3);
        imageViews[5][4]=findViewById(R.id.row5col4);
        imageViews[5][5]=findViewById(R.id.row5col5);
        imageViews[5][6]=findViewById(R.id.row5col6);
        imageViews[5][7]=findViewById(R.id.row5col7);
    }

    private void initializeRow6(ImageView[][] imageViews)
    {
        imageViews[6][0]=findViewById(R.id.row6col0);
        imageViews[6][1]=findViewById(R.id.row6col1);
        imageViews[6][2]=findViewById(R.id.row6col2);
        imageViews[6][3]=findViewById(R.id.row6col3);
        imageViews[6][4]=findViewById(R.id.row6col4);
        imageViews[6][5]=findViewById(R.id.row6col5);
        imageViews[6][6]=findViewById(R.id.row6col6);
        imageViews[6][7]=findViewById(R.id.row6col7);
    }

    private void initializeRow7(ImageView[][] imageViews)
    {
        imageViews[7][0]=findViewById(R.id.row7col0);
        imageViews[7][1]=findViewById(R.id.row7col1);
        imageViews[7][2]=findViewById(R.id.row7col2);
        imageViews[7][3]=findViewById(R.id.row7col3);
        imageViews[7][4]=findViewById(R.id.row7col4);
        imageViews[7][5]=findViewById(R.id.row7col5);
        imageViews[7][6]=findViewById(R.id.row7col6);
        imageViews[7][7]=findViewById(R.id.row7col7);
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Controller to View methods

    public void askViewToDisplayChessBoard()
    {
        for (int i = 0; i < 8 ; i++)
        {
            for (int j = 0; j < 8 ; j++)
            {
                int image=controller.getPieceImage(i,j);
                if(image!=0)
                {
                    imageViews[i][j].setBackgroundResource(image);
                }
                else
                {
                    imageViews[i][j].setBackgroundResource(android.R.color.transparent);
                }
            }
        }
        int[] checkArray=controller.getCheckArray();
        if(checkArray[0]!=-1 && checkArray[1]!=-1)
        {
            int image=controller.getKingCheckedImage(checkArray[0],checkArray[1]);
            imageViews[checkArray[0]][checkArray[1]].setBackgroundResource(image);
        }
    }

    public void askViewToDisplayVictory(int allegiance)
    {
        for (int i = 0; i < 8 ; i++)
        {
            for (int j = 0; j < 8 ; j++)
            {
                imageViews[i][j].setOnClickListener(view->{});
            }
        }
        if(allegiance==0)
        {
            Toast.makeText(getApplicationContext(),"Victory for Whites",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Victory for Blacks",Toast.LENGTH_SHORT).show();
        }
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

}