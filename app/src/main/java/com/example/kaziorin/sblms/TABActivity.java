package com.example.kaziorin.sblms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class TABActivity extends AppCompatActivity {

    public static final String MyPREFERENCESuser = "UserPrefs" ;
    public static final String usermail = "Usermail";
    public static final String userType = "userType";
    public static final String AdminT = "admin";
    public static final String OperatorT = "user";
    public static final String TokenPreference = "tokenT";
    RequestQueue queue;
    public static final String NamePreference = "nameU";

    String Uemail,Type,TokenString,Name;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);


        queue = Volley.newRequestQueue(this);
        Intent intent = getIntent();
        Uemail = intent.getStringExtra("email");
        Type = intent.getStringExtra(userType);
        TokenString = intent.getStringExtra("token");
        Name = intent.getStringExtra("Name");
        Log.d("TABctivity","name"+ Name);
        Log.d("TABtivity","email"+ Uemail);
        Log.d("TABtivity","Type"+ Type);
        Log.d("TABtivity","email"+Uemail);

        sharedpreferences = getSharedPreferences(MyPREFERENCESuser, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString(NamePreference, Name);
        editor.putString(TokenPreference, TokenString);
        editor.putString(userType, Type);
        editor.putString(usermail, Uemail);
        editor.commit();
        Log.d("TABtivity","email"+sharedpreferences.getString(usermail,""));
        Log.d("TABtivity","type_pre"+sharedpreferences.getString(userType,""));
        Log.d("TABtivity","Token_Pre"+sharedpreferences.getString(TokenPreference,""));

    }
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage( getBaseContext().getPackageName() );
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
