package com.example.kaziorin.sblms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.example.kaziorin.sblms.ServiceHandler.response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    LoginModel m;
    public static final String MyPREFERENCESuser = "UserPrefs" ;
    public static final String usermail = "Usermail";
    public static final String userType = "userType";

    public static final String AdminT = "admin";
    public static final String OperatorT = "user";
    public static final String TokenPreference = "tokenT";
    public static final String NamePreference = "nameU";

    SharedPreferences sharedpreferences;


    private static final int REQUEST_READ_CONTACTS = 0;
    UserModel  userModel;
    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    //private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    String pass,email ,Token;
    RequestQueue queue;
    String responseServer;
    int code;
    String UserTypeS;
    Boolean check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.

        sharedpreferences = getSharedPreferences(MyPREFERENCESuser, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();

        String loginVerify = sharedpreferences.getString(usermail,"");
        String UType = sharedpreferences.getString(userType,"");


        if(!loginVerify.equals("")) {


                if (UType.equals(AdminT)) {
                    Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                    intent.putExtra("email", sharedpreferences.getString(usermail, ""));
                    intent.putExtra("token", sharedpreferences.getString(TokenPreference, ""));
                    intent.putExtra("Name", sharedpreferences.getString(NamePreference, ""));
                    intent.putExtra(userType, AdminT);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else if (UType.equals(OperatorT)) {
                    Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                    intent.putExtra("email", sharedpreferences.getString(usermail, ""));
                    intent.putExtra("token", sharedpreferences.getString(TokenPreference, ""));
                    intent.putExtra("Name", sharedpreferences.getString(NamePreference, ""));
                    intent.putExtra(userType, OperatorT);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

            }



        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        queue = Volley.newRequestQueue(this);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                   // attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
               // attemptLogin();


                email=  mEmailView.getText().toString();
                pass =  mPasswordView.getText().toString();


                if(pass.equals("")||email.equals(""))
                {
                  Toast.makeText(getApplication(),"Text Field Empty",Toast.LENGTH_SHORT).show();
                }else
                {
                    if(isOnline()==true)
                    {
                        new  LoginAttempt().execute();
                    }
                    else
                    {
                        Toast.makeText(getApplication(),"Please open internet connection",Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }




    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }


    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 2;
    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }



    private class LoginAttempt extends AsyncTask<String, Void, String> {

        String login_url = VariableClass.login;
        InputStream inputStream = null;
        String result = "";

        @Override
        protected String doInBackground(final String... arg0) {

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
                                Log.d("ob","code:"+code);

                                UserTypeS = mainOb.get("type").toString();
                                Log.d("ob","Type:"+UserTypeS);



                               // sharedpreferences = getSharedPreferences(MyPREFERENCESuser, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                Token = mainOb.get("token").toString();
                                Log.d("ob","Type:"+Token);

                                String name = mainOb.get("name").toString();// main object
                                Log.d("name","Name"+mainOb.get("name").toString());

                                String uid = mainOb.get("id").toString();// main object
                                Log.d("ID","ID"+mainOb.get("id").toString());

                                sharedpreferences.edit().remove(TokenPreference).commit();
                                editor.putString(NamePreference,name);
                                editor.putString("uid", uid);
                                editor.putString("user", UserTypeS);
                                editor.putString("token", Token);
                                editor.commit();

                                //............................
                                if(code==0)
                                {
                                    //  Log.d("UserType","UserType"+UserType);
                                    //sharedpreferences.edit().remove(TokenPreference).commit();
                                    String NameUi = sharedpreferences.getString(NamePreference,"");
                                    String UserTypeUI = sharedpreferences.getString("user","");
                                    String tokenPreUI = sharedpreferences.getString("token","");

                                    Log.d("UI","user type "+ UserTypeUI);
                                    Log.d("UI","Token "+ tokenPreUI);
                                    Log.d("UI","U"+UserTypeS);
                                    if(UserTypeS.equals(OperatorT))
                                    {
                                        Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                                        intent.putExtra("email", email);
                                        intent.putExtra("token", tokenPreUI);
                                        intent.putExtra("Name", NameUi);
                                        intent.putExtra("ID", sharedpreferences.getString("uid",""));
                                        intent.putExtra(userType,OperatorT);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);

                                    }
                                    else
                                    {
                                        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                                        intent.putExtra("email", email);
                                        intent.putExtra("token", tokenPreUI);
                                        intent.putExtra("Name", NameUi);
                                        intent.putExtra(userType,AdminT);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                }
                                //............................

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
                    headers.put("api-version", "1");
                    return headers;
                }

                @Override
                public byte[] getBody() throws AuthFailureError {


                    String mContent = "{\"email\":\""+email+"\"," +
                            "\"password\":\""+pass+"\"}";
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
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}