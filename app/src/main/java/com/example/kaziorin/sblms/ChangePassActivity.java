package com.example.kaziorin.sblms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.Map;

import static com.example.kaziorin.sblms.ServiceHandler.response;

public class ChangePassActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


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
    EditText Pass ,Cpass;
    String pass,cpass;
    int code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        queue = Volley.newRequestQueue(this);
        Intent intent = getIntent();
        Uemail = intent.getStringExtra("email");
        Type = intent.getStringExtra(userType);
        TokenString = intent.getStringExtra("token");
        Name = intent.getStringExtra("Name");
        Log.d("Pctivity","name"+ Name);
        Log.d("Ptivity","email"+ Uemail);
        Log.d("Ptivity","Type"+ Type);
        Log.d("Ptivity","email"+Uemail);

        sharedpreferences = getSharedPreferences(MyPREFERENCESuser, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString(NamePreference, Name);
        editor.putString(TokenPreference, TokenString);
        editor.putString(userType, Type);
        editor.putString(usermail, Uemail);
        editor.commit();
        Log.d("Ptivity","email"+sharedpreferences.getString(usermail,""));
        Log.d("Ptivity","type_pre"+sharedpreferences.getString(userType,""));
        Log.d("Ptivity","Token_Pre"+sharedpreferences.getString(TokenPreference,""));


        Pass =(EditText)findViewById(R.id.ppass);
        Cpass = (EditText)findViewById(R.id.cpass);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    public void PassChange(View v)
    {
        pass = Pass.getText().toString();
        cpass = Cpass.getText().toString();
        if(pass.equals(cpass))
        {
            if(isOnline()==true)
            {
                new ChangePass(pass).execute();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Please check internet connection",Toast.LENGTH_SHORT).show();
            }



        }else
        {
            Toast.makeText(getApplicationContext(),"Password not match",Toast.LENGTH_SHORT).show();
        }
    }

    private class ChangePass extends AsyncTask<String, Void, String> {

        String url = VariableClass.user_list;
        InputStream inputStream = null;
        String result = "";
        String password;

        ChangePass(String password)
        {
            this.password = password;
        }

        @Override
        protected String doInBackground(String... arg0) {

            StringRequest postRequest = new StringRequest(Request.Method.PUT, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("Response", response);

                            try {

                                JSONObject obj = new JSONObject(response);
                                String r = obj.getString("user");
                                JSONObject mainOb = new JSONObject(r);

                                code = obj.getInt("code");
                                //JSONObject messageOb = new JSONObject(message);
                                Log.d("oba","code:"+code);
                                if(code==0)
                                {
                                    Pass.setText("");
                                    Cpass.setText("");
                                    Toast.makeText(getApplicationContext(),"Passwod Change",Toast.LENGTH_SHORT).show();
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

                   // UserModel user = new UserModel(Password);
                    UserModel user = new UserModel();
                    user.setPass(password);

                    Gson gson = new Gson();
                    //gson.toJson(user);
                    String mContent = gson.toJson(user);
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



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.change_pass, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Add) {
            Intent intent = new Intent(ChangePassActivity.this, UserActivity.class);
            intent.putExtra("email", sharedpreferences.getString(usermail, ""));
            intent.putExtra("token", sharedpreferences.getString(TokenPreference, ""));
            intent.putExtra("Name", sharedpreferences.getString(NamePreference, ""));
            intent.putExtra(userType, OperatorT);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        } else if (id == R.id.nav_newsurvay) {
            Intent intent = new Intent(ChangePassActivity.this, NewSurveyActivity.class);
            intent.putExtra("email", sharedpreferences.getString(usermail, ""));
            intent.putExtra("token", sharedpreferences.getString(TokenPreference, ""));
            intent.putExtra("Name", sharedpreferences.getString(NamePreference, ""));
            intent.putExtra(userType, OperatorT);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);


        } else if (id == R.id.nav_view) {
            Intent intent = new Intent(ChangePassActivity.this, ViewSurveyActivity.class);
            intent.putExtra("email", sharedpreferences.getString(usermail, ""));
            intent.putExtra("token", sharedpreferences.getString(TokenPreference, ""));
            intent.putExtra("Name", sharedpreferences.getString(NamePreference, ""));
            intent.putExtra(userType, OperatorT);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        } else if (id == R.id.nav_signout) {
            sharedpreferences = getSharedPreferences(MyPREFERENCESuser, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.clear();
            editor.commit();
            finish();
            Intent intent = new Intent(ChangePassActivity.this, LoginActivity.class);
            startActivity(intent);

        }else if (id == R.id.nav_profile) {
            Toast.makeText(getApplicationContext(),"nav_profile",Toast.LENGTH_SHORT).show();

        }else if (id == R.id.nav_pass) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
