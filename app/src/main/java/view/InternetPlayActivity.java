package view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.chessapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InternetPlayActivity extends AppCompatActivity
{

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Variables

    Button hostGame;
    EditText gameNumberInput;
    Button joinGame;

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
            Toast.makeText(getApplicationContext(),"No internet Connection",Toast.LENGTH_LONG).show();
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
            Intent hostGame=new Intent(getApplicationContext(), ChessBoardActivity.class);
            hostGame.putExtra("GameMode", "InternetPlay");
            hostGame.putExtra("GameNumber","1");
            hostGame.putExtra("Host","0");
            startActivity(hostGame);
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
                        }
                        else if(value.equals("TwoPlayers"))
                        {
                            Toast.makeText(getApplicationContext(),"Game room is full",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Given room number doesn't exist",Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error)
                {
                    Toast.makeText(getApplicationContext(),"Given room number doesn't exist",Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        });
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

}