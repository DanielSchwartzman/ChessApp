package view;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Button;
import com.example.chessapp.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Collections;
import java.util.List;
import Singleton.SignalGenerator;

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
        SignalGenerator.init(getApplicationContext());
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
        Button internetPlayButton=findViewById(R.id.main_BTN_internetPlay);
        internetPlayButton.setOnClickListener(view ->
        {
            if(isNetworkConnected())
            {
                login();
            }
            else
            {
                SignalGenerator.getInstance().toast("No internet Connection");
            }
        });
    }

    private void initializeTwoPlayerButton()
    {
        Button twoPlayerButton=findViewById(R.id.main_BTN_twoPlayers);
        twoPlayerButton.setOnClickListener(view ->
        {
            Intent switchToChessBoard=new Intent(getApplicationContext(), ChessBoardActivity.class);
            switchToChessBoard.putExtra("GameMode", "TwoPlayers");
            startActivity(switchToChessBoard);
        });
    }

    private boolean isNetworkConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Log in method

    private void login()
    {
        List<AuthUI.IdpConfig> providers = Collections.singletonList(
                new AuthUI.IdpConfig.EmailBuilder().build());

        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);
    }



    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            this::onSignInResult
    );
    private void onSignInResult(FirebaseAuthUIAuthenticationResult result)
    {
        if (result.getResultCode() == RESULT_OK)
        {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            assert user != null;
            moveToInternetPlayActivity(user);
        }
        else
        {
            SignalGenerator.getInstance().toast("Login aborted");
        }
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Activity Switch

    private void moveToInternetPlayActivity(FirebaseUser user)
    {
        Intent intent = new Intent(this, InternetPlayActivity.class);
        intent.putExtra("UserName", user.getDisplayName());
        startActivity(intent);
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

}