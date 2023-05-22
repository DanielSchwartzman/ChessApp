package view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import com.example.chessapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import Singleton.SignalGenerator;

public class InternetPlayActivity extends AppCompatActivity
{

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Variables

    Button hostGame;
    Button joinGame;
    EditText gameNumberInput;

    String userName;

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Constructor

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_play);
        initViews();

        hostGameFunctionality();
        joinGameFunctionality();
    }

    private void initViews()
    {
        userName=getIntent().getStringExtra("UserName");
        hostGame=findViewById(R.id.internetPlay_BTN_hostGame);
        gameNumberInput=findViewById(R.id.internetPlay_TXTE_gameNum);
        joinGame=findViewById(R.id.internetPlay_BTN_joinGame);
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Multiplayer methods

    private void hostGameFunctionality()
    {
        hostGame.setOnClickListener(view ->
        {
            int gameNumber=(int) ((Math.random() * (99999999 - 10000000)) + 10000000);
            Intent hostGame=new Intent(getApplicationContext(), ChessBoardActivity.class);
            hostGame.putExtra("UserName",userName);
            hostGame.putExtra("GameMode", "InternetPlay");
            hostGame.putExtra("GameNumber",gameNumber+"");
            hostGame.putExtra("Host","0");
            startActivity(hostGame);
            finish();
        });
    }

    private void joinGameFunctionality()
    {
        joinGame.setOnClickListener(view ->
        {
            String gameNumber=gameNumberInput.getText().toString();
            if(gameNumber.equals("")||gameNumber.contains(".")||gameNumber.contains(","))
            {
                SignalGenerator.getInstance().toast("Invalid game number");
            }
            else
            {
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(gameNumberInput.getText().toString());
                myRef.addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        String value = snapshot.getValue(String.class);
                        if (value != null)
                        {
                            if (value.equals("OnePlayer"))
                            {
                                Intent joinGame = new Intent(getApplicationContext(), ChessBoardActivity.class);
                                joinGame.putExtra("UserName", userName);
                                joinGame.putExtra("GameMode", "InternetPlay");
                                joinGame.putExtra("GameNumber", gameNumberInput.getText().toString());
                                joinGame.putExtra("Host", "1");
                                startActivity(joinGame);
                                finish();
                            }
                            else if (value.equals("TwoPlayers"))
                            {
                                SignalGenerator.getInstance().toast("Game room is full");
                            }
                        }
                        else
                        {
                            SignalGenerator.getInstance().toast("Given room number doesn't exist");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            }
        });
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

}