package com.example.kaziorin.sblms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import java.util.ArrayList;
import java.util.List;

public class NewSurveyActivity2 extends AppCompatActivity {

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
    TextView Hname,Hemail;
    View header;

    EditText other ,Bcom;
    String AgreeText ,ThinkTest,Interesttext,OtherText,FinalText;
    Spinner agree , think, interest;
    ArrayAdapter<String> agreeAdapter,thinkAdapter,interestAdapter;
    boolean agreeB , thinkB , interestB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_survey2);

        Intent intent = getIntent();
        Uemail = intent.getStringExtra("email");
        Type = intent.getStringExtra(userType);
        TokenString = intent.getStringExtra("token");
        Name = intent.getStringExtra("Name");
        Log.d("NSactivity2","name"+ Name);
        Log.d("NSactivity2","email"+ Uemail);
        Log.d("NSactivity2","Type"+ Type);
        Log.d("NSactivity2","email"+Uemail);

        sharedpreferences = getSharedPreferences(MyPREFERENCESuser, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString(NamePreference, Name);
        editor.putString(TokenPreference, TokenString);
        editor.putString(userType, Type);
        editor.putString(usermail, Uemail);
        editor.commit();
        Log.d("NSactivity2","email"+sharedpreferences.getString(usermail,""));
        Log.d("NSactivity2","type_pre"+sharedpreferences.getString(userType,""));
        Log.d("NSactivity2","Token_Pre"+sharedpreferences.getString(TokenPreference,""));


        agree = (Spinner) findViewById(R.id.Spinner_agree);
        think = (Spinner) findViewById(R.id.Spinner_think);
        interest = (Spinner) findViewById(R.id.Spinnerinterest);

        other = (EditText)findViewById(R.id.other);
        Bcom =(EditText)findViewById(R.id.Bcomment);

        List<String> intL = new ArrayList<String>();
        intL.add("Not Interest");
        intL.add("Yes");
        intL.add("No");
        // Creating adapter for spinner
        interestAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,intL);
        // Drop down layout style - list view with radio button
        interestAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        interest.setAdapter(interestAdapter);

        // Spinner click listener
        List<String> thinkL = new ArrayList<String>();
        thinkL.add("Will Think");
        thinkL.add("Yes");
        thinkL.add("No");
        // Creating adapter for spinner
        thinkAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,thinkL);
        // Drop down layout style - list view with radio button
        thinkAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        think.setAdapter(thinkAdapter);

        // Spinner Drop down elements
        List<String> agreeL = new ArrayList<String>();
        agreeL.add("Agreed");
        agreeL.add("Yes");
        agreeL.add("No");
        // Creating adapter for spinner
        agreeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, agreeL);
        // Drop down layout style - list view with radio button
        agreeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        agree.setAdapter(agreeAdapter);


    }
    public void saveFinal(View v)
    {
        AgreeText = agree.getSelectedItem().toString();
        ThinkTest = think.getSelectedItem().toString();
        Interesttext = interest.getSelectedItem().toString();
        OtherText = other.getText().toString();
        FinalText = Bcom.getText().toString();

        if(agree.getSelectedItemPosition()==1)
        {
            agreeB = true;
        }else if(agree.getSelectedItemPosition()==2)
        {
            agreeB = false;
        }

        if(think.getSelectedItemPosition()==1)
        {
            thinkB = true;
        }else if(agree.getSelectedItemPosition()==2)
        {
            thinkB = false;
        }
        if(interest.getSelectedItemPosition()==1)
        {
            interestB = true;
        }else if(agree.getSelectedItemPosition()==2)
        {
            interestB = false;
        }

        Intent intent = new Intent(NewSurveyActivity2.this, NewSurveyActivity.class);
        intent.putExtra("email", sharedpreferences.getString(usermail,""));
        intent.putExtra("token", sharedpreferences.getString(TokenPreference,""));
        intent.putExtra("Name", sharedpreferences.getString(NamePreference,""));
        intent.putExtra(userType,OperatorT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

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
