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
        initializeInternetPlayButton();
        initializeTwoPlayerButton();
    }

    private void initializeInternetPlayButton()
    {
        Button internetPlayButton=findViewById(R.id.activateInternetPlay);
        internetPlayButton.setOnClickListener(view ->
        {
            Intent switchToInternetPlay=new Intent(getApplicationContext(), InternetPlayActivity.class);
            startActivity(switchToInternetPlay);
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