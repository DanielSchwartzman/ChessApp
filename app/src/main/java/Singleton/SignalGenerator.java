package Singleton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

public class SignalGenerator
{

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Variables

    @SuppressLint("StaticFieldLeak")
    private static SignalGenerator instance = null;
    private final Context context;

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Constructor and initializer

    private SignalGenerator(Context context)
    {
        this.context = context;
    }

    public static void init(Context context)
    {
        if (instance == null)
        {
            instance = new SignalGenerator(context);
        }
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Getter

    public static SignalGenerator getInstance()
    {
        return instance;
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //General functions

    public void toast(String text)
    {
        Toast.makeText(context,text,Toast.LENGTH_LONG).show();
    }

    //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

}