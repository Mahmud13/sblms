package com.example.kaziorin.sblms;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
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

public class AdminActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    ArrayAdapter<String> typeadpter;
    private static final int PERMISSION_REQUEST_CODE = 200;

    public static final String MyPREFERENCESuser = "UserPrefs" ;
    public static final String usermail = "Usermail";
    public static final String userType = "userType";
    public static final String AdminT = "admin";
    public static final String OperatorT = "user";
    public static final String TokenPreference = "tokenT";
    RequestQueue queue;
    public static final String NamePreference = "nameU";
    int code;
    View header;


    String Uemail,Type, TokenString ,EmailText,Name,FullName,User_type,Phone,Address,Password;
    SharedPreferences sharedpreferences;


    TextView Hname,Hemail;
    Spinner type;

    EditText email,name ,phone,address,passsword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedpreferences = getSharedPreferences(MyPREFERENCESuser, Context.MODE_PRIVATE);
        queue = Volley.newRequestQueue(this);


        email =(EditText)findViewById(R.id.email);
        name =(EditText)findViewById(R.id.Uname);
        phone =(EditText)findViewById(R.id.phone);
        address =(EditText)findViewById(R.id.addr);
        passsword =(EditText)findViewById(R.id.password);



        Intent intent = getIntent();
        Uemail = intent.getStringExtra("email");
        Type = intent.getStringExtra(userType);
        TokenString = intent.getStringExtra("token");
        Name = intent.getStringExtra("Name");

        Log.d("Aactivity","Token String"+TokenString);
        Log.d("Aactivity","email"+Uemail);
        Log.d("Aactivity","type"+Type);


        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString(NamePreference, Name);
        editor.putString(TokenPreference, TokenString);
        editor.putString(userType, Type);
        editor.putString(usermail, Uemail);
        editor.commit();
        Log.d("Aactivity","email"+sharedpreferences.getString(usermail,""));
        Log.d("Aactivity","type_pre"+sharedpreferences.getString(userType,""));
        Log.d("Aactivity","Token_Pre"+sharedpreferences.getString(TokenPreference,""));


        type =(Spinner)findViewById(R.id.type);

        List<String> intL = new ArrayList<String>();
        intL.add("User Type");
        intL.add("admin");
        intL.add("user");
        // Creating adapter for spinner
        typeadpter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,intL);
        // Drop down layout style - list view with radio button
        typeadpter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        type.setAdapter(typeadpter);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        header = navigationView.getHeaderView(0);
        Hname = (TextView)header.findViewById(R.id.admin_header_name);
        Hemail = (TextView)header.findViewById(R.id.admin_header_email);

        Hemail.setText(sharedpreferences.getString(usermail,""));
        Hname.setText(sharedpreferences.getString(NamePreference,""));


    }


    public void SaveAdmin(View v)
    {
        EmailText = email.getText().toString();
        FullName = name.getText().toString();
        User_type = type.getSelectedItem().toString();
        Phone= phone.getText().toString();
        Address = address.getText().toString();
        Password =passsword.getText().toString();

        if(EmailText.equals("")||FullName.equals("")|| type.getSelectedItemPosition()==0||Phone.equals("")||Password.equals(""))
        {
            Toast.makeText(getApplicationContext(),"Text Field Empty",Toast.LENGTH_SHORT).show();
        }else
        {
            if (isValidEmail( EmailText)) {

                if(isOnline()==true)
                {
                    new EmailCheck().execute();
                }else
                {
                    Toast.makeText(getApplicationContext(),"Please check internet connection",Toast.LENGTH_SHORT).show();
                }


                Log.d("Email", "Email valid");
            } else {
                Log.d("Email", "Email Invalid");

                Snackbar snackbar = Snackbar.make(header, "Invalid Email Address", Snackbar.LENGTH_LONG)
                        .setAction("Action", null);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(Color.RED);
                snackbar.show();
            }
        }


    }
    private class EmailCheck extends AsyncTask<String, Void, String> {

        String login_url = VariableClass.user_list;
        InputStream inputStream = null;
        String result = "";

        @Override
        protected String doInBackground(String... arg0) {

            StringRequest postRequest = new StringRequest(Request.Method.POST, login_url,
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

                    UserModel user = new UserModel(User_type,FullName,EmailText,Address,Phone,Password);


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

            if(code==0)
            {

                email.setText("");
                name.setText("");
                type.setSelection(0);
                phone.setText("");
                address.setText("");
                passsword.setText("");
                Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
            }
            else if(code ==1){ Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_SHORT).show();}
            else if(code ==2){ Toast.makeText(getApplicationContext(),"Unauthorized",Toast.LENGTH_SHORT).show();}
            else if(code ==3){ Toast.makeText(getApplicationContext(),"System Error",Toast.LENGTH_SHORT).show();}
            else if(code ==4){ Toast.makeText(getApplicationContext(),"Require Field",Toast.LENGTH_SHORT).show();}
            else if(code ==5){Toast.makeText(getApplicationContext(),"User Already Exist",Toast.LENGTH_SHORT).show();}
            else if(code ==6){Toast.makeText(getApplicationContext(),"No user found",Toast.LENGTH_SHORT).show();}
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
        getMenuInflater().inflate(R.menu.admin, menu);
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

        if (id == R.id.nav_adduser) {
            // Handle the camera action
        } else if (id == R.id.nav_userList) {
            Intent intent = new Intent(AdminActivity.this, AdminUserListActivity.class);
            intent.putExtra("email", sharedpreferences.getString(usermail,""));
            intent.putExtra("token", sharedpreferences.getString(TokenPreference,""));
            intent.putExtra("Name", sharedpreferences.getString(NamePreference,""));
            intent.putExtra(userType,AdminT);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        } else if (id == R.id.nav_map) {
            if (!checkPermission()) {

                requestPermission();

            } else {
                Intent intent = new Intent(AdminActivity.this, getLocationActivity.class);
                intent.putExtra("token", sharedpreferences.getString(TokenPreference,""));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

        } else if (id == R.id.nav_signout_admin) {
            sharedpreferences = getSharedPreferences(MyPREFERENCESuser, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.clear();
            editor.commit();
            finish();
            Intent intent = new Intent(AdminActivity.this,LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && cameraAccepted)
                        Toast.makeText(getApplicationContext(),"granted",Toast.LENGTH_SHORT).show();

                    else {

                        Toast.makeText(getApplicationContext(),"no",Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                                                            PERMISSION_REQUEST_CODE);

                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }
                break;
        }


    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(AdminActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
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
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
