package view;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.chessapp.R;

public class MainActivity extends AppCompatActivity
{
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Constructor

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeButtons();
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Buttons

    private void initializeButtons()
    {
        initializeSinglePlayerButton();
        initializeTwoPlayerButton();
    }

    private void initializeSinglePlayerButton()
    {
        Button singlePlayerButton=findViewById(R.id.activateSinglePlayer);
        singlePlayerButton.setOnClickListener(view ->
        {
            Intent switchToChessBoard=new Intent(getApplicationContext(), ChessBoardActivity.class);
            switchToChessBoard.putExtra("GameMode", "SinglePlayer");
            startActivity(switchToChessBoard);
        });
    }

    private void initializeTwoPlayerButton()
    {
        Button twoPlayerButton=findViewById(R.id.activateTwoPlayers);
        twoPlayerButton.setOnClickListener(view ->
        {
            Intent switchToChessBoard=new Intent(getApplicationContext(), ChessBoardActivity.class);
            switchToChessBoard.putExtra("GameMode", "TwoPlayers");
            startActivity(switchToChessBoard);
        });
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
}