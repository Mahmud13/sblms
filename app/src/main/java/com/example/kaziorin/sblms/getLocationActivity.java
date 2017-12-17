package com.example.kaziorin.sblms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.kaziorin.sblms.ServiceHandler.response;

public class getLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static final String TokenPreference = "tokenT";
    public static final String MyPREFERENCESuser = "UserPrefs" ;
    SharedPreferences sharedpreferences;
    RequestQueue queue;
    String TokenString;

    ArrayList<String> Name_list = new ArrayList<String>();
    ArrayList<String> user_list = new ArrayList<String>();
    ArrayList<String> id_list = new ArrayList<String>();
    ArrayList<String> lon_list = new ArrayList<String>();
    ArrayList<String> lan_list = new ArrayList<String>();
    String id_array[],name_array[],user_array[],lon_array[],lan_array[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);

        sharedpreferences = getSharedPreferences(MyPREFERENCESuser, Context.MODE_PRIVATE);
        queue = Volley.newRequestQueue(this);

        Intent intent = getIntent();
        TokenString = intent.getStringExtra("token");

        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString(TokenPreference, TokenString);
        editor.commit();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        new MapList().execute();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private class MapList extends AsyncTask<Void, Void, String> {

        String url = VariableClass.shop;
        InputStream inputStream = null;
        String result = "";


        @Override
        protected String doInBackground(Void... arg0) {

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
                                Log.d("",""+user[i].longitude);
                                lon_list.add(user[i].longitude);
                                lan_list.add(user[i].latitude);
                                Name_list.add(user[i].getName());
                                id_list.add(user[i].getId());

                            }

                            name_array = Name_list.toArray(new String[Name_list.size()]);
                            id_array = id_list.toArray(new String[id_list.size()]);
                            lon_array = lon_list.toArray(new String[lon_list.size()]);
                            lan_array = lan_list.toArray(new String[lan_list.size()]);

                            Log.d("map","aa"+lan_array);


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
