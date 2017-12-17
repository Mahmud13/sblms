package com.example.kaziorin.sblms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.AdapterView;
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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.kaziorin.sblms.ServiceHandler.response;

public class ViewSurveyActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SwipeMenuListView listView;;
    listAdapter mAdapter;
    Activity ac;

    ArrayList<String> orderdate_list = new ArrayList<String>();
    ArrayList<String> Name_list = new ArrayList<String>();
    ArrayList<String> address_list = new ArrayList<String>();
    ArrayList<String> zone_list = new ArrayList<String>();
    ArrayList<String> id_list = new ArrayList<String>();


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



    String orderdate_array[] ,name_array[],address_array[],zone_array[],id_array[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_survey);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        queue = Volley.newRequestQueue(this);
        Intent intent = getIntent();
        Uemail = intent.getStringExtra("email");
        Type = intent.getStringExtra(userType);
        TokenString = intent.getStringExtra("token");
        Name = intent.getStringExtra("Name");
        Log.d("Vactivity","name"+ Name);
        Log.d("Vactivity","email"+ Uemail);
        Log.d("Vactivity","Type"+ Type);
        Log.d("Vactivity","email"+Uemail);

        sharedpreferences = getSharedPreferences(MyPREFERENCESuser, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString(NamePreference, Name);
        editor.putString(TokenPreference, TokenString);
        editor.putString(userType, Type);
        editor.putString(usermail, Uemail);
        editor.commit();
        Log.d("Vactivity","email"+sharedpreferences.getString(usermail,""));
        Log.d("Vactivity","type_pre"+sharedpreferences.getString(userType,""));
        Log.d("Vactivity","Token_Pre"+sharedpreferences.getString(TokenPreference,""));


        listView = (SwipeMenuListView) findViewById(R.id.listView);

        if(isOnline()==true)
        {
            new ShopViewList(this).execute();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Please check internet connection",Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        Toast.makeText(getApplicationContext(),"Delete",Toast.LENGTH_SHORT).show();
                        Log.d("ok", "onMenuItemClick: clicked item id " + id_array[position]);
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
                Intent intent = new Intent(ViewSurveyActivity.this, TABActivity.class);
                intent.putExtra("email", sharedpreferences.getString(usermail,""));
                intent.putExtra("token", sharedpreferences.getString(TokenPreference,""));
                intent.putExtra("Name", sharedpreferences.getString(NamePreference,""));
                intent.putExtra(userType,OperatorT);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), position + "item click", Toast.LENGTH_SHORT).show();
            }
        });

    }
    SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            SwipeMenuItem editItem = new SwipeMenuItem(
                    getApplicationContext());
            // set item background
            editItem.setBackground(new ColorDrawable(Color.rgb(94, 221, 77)));
            // set item width
            editItem.setWidth(170);
            // set a icon
            editItem.setIcon(R.drawable.ic_edit_white_24dp);
            // add to menu
            menu.addMenuItem(editItem);
        }
    };

    private class ShopViewList extends AsyncTask<String, Void, String> {

        String url = VariableClass.surveyed_list_view;
        InputStream inputStream = null;
        String result = "";


        ShopViewList(Activity a)
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
                            ShopModel[] user= gson.fromJson(response,ShopModel[].class);


                            for(int i = 0; i<user.length;i++)
                            {

                               // Log.d("user","userP"+user[i].getId());
                                id_list.add(user[i].id);
                                orderdate_list.add(user[i].createdAt);
                                Name_list.add(user[i].name);
                                address_list.add(user[i].ownerName);
                                zone_list.add(user[i].address);
                               // Log.d("user","useri-"+id_list);
                            }

//                            orderdate_list.add("12/12/2017 20:40");
//                            Name_list.add("Kazi Orin");
//                            address_list.add("janata co operative housing");
//                            zone_list.add("Mohammedpur");
                            id_array = id_list.toArray(new String[id_list.size()]);
                            orderdate_array = orderdate_list.toArray(new String[orderdate_list.size()]);
                            name_array = Name_list.toArray(new String[Name_list.size()]);
                            address_array = address_list.toArray(new String[address_list.size()]);
                            zone_array = zone_list.toArray(new String[zone_list.size()]);

                            mAdapter = new listAdapter(ac,name_array,zone_array,address_array,orderdate_array);
                            listView.setAdapter(mAdapter);

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
        getMenuInflater().inflate(R.menu.view_survey, menu);
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

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Add) {
            Intent intent = new Intent(ViewSurveyActivity.this, UserActivity.class);
            intent.putExtra("email", sharedpreferences.getString(usermail,""));
            intent.putExtra("token", sharedpreferences.getString(TokenPreference,""));
            intent.putExtra("Name", sharedpreferences.getString(NamePreference,""));
            intent.putExtra(userType,OperatorT);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        } else if (id == R.id.nav_newsurvay) {
            Intent intent = new Intent(ViewSurveyActivity.this, NewSurveyActivity.class);
            intent.putExtra("email", sharedpreferences.getString(usermail,""));
            intent.putExtra("token", sharedpreferences.getString(TokenPreference,""));
            intent.putExtra("Name", sharedpreferences.getString(NamePreference,""));
            intent.putExtra(userType,OperatorT);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);


        } else if (id == R.id.nav_view) {

        } else if (id == R.id.nav_signout) {
            sharedpreferences = getSharedPreferences(MyPREFERENCESuser, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.clear();
            editor.commit();
            finish();
            Intent intent = new Intent(ViewSurveyActivity.this,LoginActivity.class);
            startActivity(intent);

        }else if (id == R.id.nav_profile) {
            Toast.makeText(getApplicationContext(),"nav_profile",Toast.LENGTH_SHORT).show();

        }else if (id == R.id.nav_pass) {
            Intent intent = new Intent(ViewSurveyActivity.this, ChangePassActivity.class);
            intent.putExtra("email", sharedpreferences.getString(usermail,""));
            intent.putExtra("token", sharedpreferences.getString(TokenPreference,""));
            intent.putExtra("Name", sharedpreferences.getString(NamePreference,""));
            intent.putExtra(userType,OperatorT);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
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
