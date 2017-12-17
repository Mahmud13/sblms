package com.example.kaziorin.sblms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.kaziorin.sblms.ServiceHandler.response;

public class AdminUserListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String MyPREFERENCESuser = "UserPrefs" ;
    public static final String usermail = "Usermail";
    public static final String userType = "userType";
    public static final String AdminT = "admin";
    public static final String OperatorT = "user";
    public static final String TokenPreference = "tokenT";
    RequestQueue queue;
    public static final String NamePreference = "nameU";
    int code;
    SharedPreferences sharedpreferences;
    TextView Hname,Hemail;
    String Uemail,Type, TokenString ,Name;
    View header;

    Activity ac;


    String phone_array[],type_array[] ,name_array[],address_array[],email_array[],id_array[];
    ArrayList<String> phone_list = new ArrayList<String>();
    ArrayList<String> Name_list = new ArrayList<String>();
    ArrayList<String> address_list = new ArrayList<String>();
    ArrayList<String> eamil_list = new ArrayList<String>();
    ArrayList<String> type_list = new ArrayList<String>();
    ArrayList<String> id_list = new ArrayList<String>();

    AdminlistAdapter amAdapter;

    Context con;




    private static final int PERMISSION_REQUEST_CODE = 200;
    SwipeMenuListView listView;;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        sharedpreferences = getSharedPreferences(MyPREFERENCESuser, Context.MODE_PRIVATE);
        queue = Volley.newRequestQueue(this);

        Intent intent = getIntent();
        Uemail = intent.getStringExtra("email");
        Type = intent.getStringExtra(userType);
        TokenString = intent.getStringExtra("token");
        Name = intent.getStringExtra("Name");

        Log.d("ALactivity","Token String"+TokenString);
        Log.d("ALactivity","email"+Uemail);
        Log.d("ALactivity","type"+Type);


        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString(NamePreference, Name);
        editor.putString(TokenPreference, TokenString);
        editor.putString(userType, Type);
        editor.putString(usermail, Uemail);
        editor.commit();
        Log.d("ALactivity","email"+sharedpreferences.getString(usermail,""));
        Log.d("ALactivity","type_pre"+sharedpreferences.getString(userType,""));
        Log.d("ALactivity","Token_Pre"+sharedpreferences.getString(TokenPreference,""));


        if(isOnline()==true)
        {
            new AdminUserList(this).execute();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Please check internet connection",Toast.LENGTH_SHORT).show();
        }



        listView = (SwipeMenuListView) findViewById(R.id.listView);
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

        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        //Toast.makeText(getApplicationContext(),"Delete",Toast.LENGTH_SHORT).show();
                        Log.d("ok", "onMenuItemClick: clicked item " + id_array[position]);

                        new DeleteUser(id_array[position],AdminUserListActivity.this).execute();

                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
//                Intent intent = new Intent(ViewSurveyActivity.this, TABActivity.class);
//                intent.putExtra("email", "O");
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Sweep the view", Toast.LENGTH_SHORT).show();
            }
        });


    }
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {

            // create "delete" item
            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    getApplicationContext());
            // set item background
            deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                    0x3F, 0x25)));
            // set item width
            deleteItem.setWidth(170);
            // set a icon
            deleteItem.setIcon(R.drawable.ic_delete_white_24dp);
            // add to menu
            menu.addMenuItem(deleteItem);
        }
    };

    private class AdminUserList extends AsyncTask<String, Void, String> {

        String url = VariableClass.user_list;
        InputStream inputStream = null;
        String result = "";


        AdminUserList(Activity a)
        {
            ac = a;
        }

        @Override
        protected String doInBackground(String... arg0) {

            StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("AResponse", response);

                            Gson gson = new Gson();
                            UserModel[] user= gson.fromJson(response,UserModel[].class);


                            for(int i = 0; i<user.length;i++)
                            {
                                Log.d("user","userN"+user[i].getName());
                                Log.d("user","userE"+user[i].getEmail());
                                Log.d("user","userT"+user[i].getType());
                                Log.d("user","userA"+user[i].getAddress());
                                Log.d("user","userP"+user[i].getPhone());
                                Log.d("user","userP"+user[i].getId());

                                eamil_list.add(user[i].getEmail());
                                Name_list.add(user[i].getName());
                                address_list.add(user[i].getAddress());
                                phone_list.add(user[i].getPhone());
                                type_list.add(user[i].getType());
                                id_list.add(user[i].getId());
                                Log.d("user","userE-"+eamil_list);
                                Log.d("user","userN-"+Name_list);
                                Log.d("user","userA-"+address_list);
                                Log.d("user","userP-"+phone_list);
                                Log.d("user","userT-"+type_list);
                                Log.d("user","useri-"+id_list);
                            }

                            email_array = eamil_list.toArray(new String[eamil_list.size()]);
                            name_array = Name_list.toArray(new String[Name_list.size()]);
                            address_array = address_list.toArray(new String[address_list.size()]);
                            phone_array = phone_list.toArray(new String[phone_list.size()]);
                            type_array = type_list.toArray(new String[type_list.size()]);
                            id_array = id_list.toArray(new String[id_list.size()]);
                            amAdapter = new AdminlistAdapter(ac,name_array,phone_array,address_array,email_array,type_array);
                            listView.setAdapter(amAdapter);

                            Log.d("email_array","email_array"+email_array);
                            Log.d("email_array","ad_array"+address_array);
                            Log.d("email_array","nm_array"+name_array);
                            Log.d("email_array","ph_array"+phone_array);
                            Log.d("email_array","ty_array"+type_array);



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
    private class DeleteUser extends AsyncTask<String, Void, String> {

        String id;

        DeleteUser(String id ,Activity b)
        {
            this.id = id;
            ac = b;
        }

        String url = VariableClass.user_list;
        InputStream inputStream = null;
        String result = "";

        @Override
        protected String doInBackground(String... arg0) {

            StringRequest postRequest = new StringRequest(Request.Method.DELETE, url+"/"+id,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("AResponse", response);

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
                   // headers.put("id",);
                    Log.d("header","Token:Header"+sharedpreferences.getString(TokenPreference,""));
                    return headers;
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


                phone_list.clear();
                Name_list.clear();
                address_list.clear();
                eamil_list.clear();
                type_list.clear();
                id_list.clear();
                if(isOnline()==true)
                { new AdminUserList(ac).execute();}
                else
                {
                    Toast.makeText(getApplicationContext(),"Please check internet connection",Toast.LENGTH_SHORT).show();
                }

                amAdapter = new AdminlistAdapter(ac,name_array,phone_array,address_array,email_array,type_array);
                listView.setAdapter(amAdapter);

                Toast.makeText(getApplicationContext(),"Delete User",Toast.LENGTH_SHORT).show();
            }
            else if(code ==1){ Toast.makeText(getApplicationContext(),"Delete Fail",Toast.LENGTH_SHORT).show();}
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
        getMenuInflater().inflate(R.menu.admin_user_list, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_adduser)
        {
            Intent intent = new Intent(AdminUserListActivity.this, AdminActivity.class);
            intent.putExtra("email", sharedpreferences.getString(usermail,""));
            intent.putExtra("token", sharedpreferences.getString(TokenPreference,""));
            intent.putExtra("Name", sharedpreferences.getString(NamePreference,""));
            intent.putExtra(userType,AdminT);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_userList) {

        } else if (id == R.id.nav_map) {
            if (!checkPermission()) {

                requestPermission();

            } else {
                Intent intent = new Intent(AdminUserListActivity.this, getLocationActivity.class);
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
            Intent intent = new Intent(AdminUserListActivity.this,LoginActivity.class);
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
        new AlertDialog.Builder(AdminUserListActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
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
