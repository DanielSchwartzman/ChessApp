package view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
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

        if(!isNetworkConnected())
        {
            SignalGenerator.getInstance().toast("No internet Connection");
            finish();
        }

        hostGameFunctionality();
        joinGameFunctionality();
    }

    private boolean isNetworkConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void initViews()
    {
        hostGame=findViewById(R.id.hostGame);
        gameNumberInput=findViewById(R.id.gameNumber);
        joinGame=findViewById(R.id.joinGame);
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
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(gameNumberInput.getText().toString());
            myRef.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    String value = snapshot.getValue(String.class);
                    if(value!=null)
                    {
                        if(value.equals("OnePlayer"))
                        {
                            Intent joinGame = new Intent(getApplicationContext(), ChessBoardActivity.class);
                            joinGame.putExtra("GameMode", "InternetPlay");
                            joinGame.putExtra("GameNumber", gameNumberInput.getText().toString());
                            joinGame.putExtra("Host", "1");
                            startActivity(joinGame);
                            finish();
                        }
                        else if(value.equals("TwoPlayers"))
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
        });
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

}