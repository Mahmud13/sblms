package com.example.kaziorin.sblms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.kaziorin.sblms.ServiceHandler.response;

public class NewSurveyActivity2 extends AppCompatActivity {

    public static final String MyPREFERENCESuser = "UserPrefs" ;
    public static final String usermail = "Usermail";
    public static final String userType = "userType";
    public static final String AdminT = "admin";
    public static final String OperatorT = "user";
    public static final String TokenPreference = "tokenT";
    RequestQueue queue;
    public static final String NamePreference = "nameU";
    public static final String SurveyID = "surveyid";
    int code;

    String Uemail,Type,TokenString,Name;
    SharedPreferences sharedpreferences;
    TextView Hname,Hemail;
    View header;

    EditText other ,Bcom;
    String AgreeText ,ThinkTest,Interesttext,OtherText,FinalText;
    Spinner agree , think, interest;
    ArrayAdapter<String> agreeAdapter,thinkAdapter,interestAdapter;
    boolean agreeB , thinkB , interestB;
    int SID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_survey2);

        queue = Volley.newRequestQueue(this);

        Intent intent = getIntent();
        Uemail = intent.getStringExtra("email");
        Type = intent.getStringExtra(userType);
        TokenString = intent.getStringExtra("token");
        SID = intent.getIntExtra(SurveyID,0);
        Name = intent.getStringExtra("Name");
        Log.d("NSactivity2","SID"+ SID);
        Log.d("NSactivity2","name"+ Name);
        Log.d("NSactivity2","email"+ Uemail);
        Log.d("NSactivity2","Type"+ Type);
        Log.d("NSactivity2","email"+Uemail);

        sharedpreferences = getSharedPreferences(MyPREFERENCESuser, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putInt(SurveyID, SID);
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

        Log.d("okkk","id"+sharedpreferences.getInt(SurveyID,0)+"-a-"+agreeB+"-i-"+interestB+"-t-"+thinkB+"-c-"+FinalText+"-o-"+OtherText);

        new surveyInsert2(sharedpreferences.getInt(SurveyID,0)).execute();

    }


    private class surveyInsert2 extends AsyncTask<String, Void, String> {

        String url = VariableClass.surveyed;
        InputStream inputStream = null;
        String result = "";
        int sid;

        surveyInsert2(int sid )
        {
            this.sid = sid;
        }

        @Override
        protected String doInBackground(String... arg0) {

            StringRequest postRequest = new StringRequest(Request.Method.PUT, url+"/"+sid,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("New2Response", response);

                            try {
                                //JSONObject obj = new JSONObject(response);
//                                String r = obj.getString("user");
//                                JSONObject mainOb = new JSONObject(r);
//
//                                code = obj.getInt("code");
//                                //JSONObject messageOb = new JSONObject(message);
//                                Log.d("oba","code:"+code);


                                JSONObject obj = new JSONObject(response);
                                code = obj.getInt("code");
                                Log.d("codeC","C"+code);

                                if(code==0)
                                {
                                    agree.setSelection(0);
                                    think.setSelection(0);
                                    interest.setSelection(0);
                                    other.setText("");
                                    Bcom.setText("");
                                    Intent intent = new Intent(NewSurveyActivity2.this, NewSurveyActivity.class);
                                    intent.putExtra("email", sharedpreferences.getString(usermail,""));
                                    intent.putExtra("token", sharedpreferences.getString(TokenPreference,""));
                                    intent.putExtra("Name", sharedpreferences.getString(NamePreference,""));
                                    intent.putExtra(userType,OperatorT);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                                else if(code ==1){ Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_SHORT).show();}
                                else if(code ==2){ Toast.makeText(getApplicationContext(),"Unauthorized",Toast.LENGTH_SHORT).show();}
                                else if(code ==3){ Toast.makeText(getApplicationContext(),"System Error",Toast.LENGTH_SHORT).show();}
                                else if(code ==4){ Toast.makeText(getApplicationContext(),"Require Field",Toast.LENGTH_SHORT).show();}
                                else if(code ==5){Toast.makeText(getApplicationContext(),"User Already Exist",Toast.LENGTH_SHORT).show();}
                                else if(code ==6){Toast.makeText(getApplicationContext(),"No user found",Toast.LENGTH_SHORT).show();}



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("Error.Response", response);
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("token", sharedpreferences.getString(TokenPreference,""));
                    Log.d("header","Token:Header"+sharedpreferences.getString(TokenPreference,""));
                    return headers;
                }

                @Override
                public byte[] getBody() throws AuthFailureError {

                    // ShopModel smodel = new ShopModel(cname,pname,addrss,phone,email,website,district,Lat,Long,image);

                    NewSurveyModel model =new NewSurveyModel();

                    model.isAgreed = agreeB;
                    model.isNotInterested = interestB;
                    model.isThink = thinkB;
                    model.comment = OtherText;
                    model.others = FinalText;

                    Gson gson = new Gson();
                    //gson.toJson(user);
                    String mContent = gson.toJson(model);
                    Log.d("mcontent","content:"+mContent);
                    byte[] body = new byte[0];
                    try {
                        body = mContent.getBytes("UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        Log.e("pp", "Unable to gets bytes from JSON", e.fillInStackTrace());
                    }
                    return body;
                }


                @Override
                public String getBodyContentType() {
                    return "application/json";
                }
            };
            queue.add(postRequest);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }

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
