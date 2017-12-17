package com.example.kaziorin.sblms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.test.mock.MockPackageManager;
import android.util.Base64;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.listeners.TableHeaderClickListener;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.example.kaziorin.sblms.ServiceHandler.response;

public class UserActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EditText Cname, Pname, Addrss, Phone, Email, Website;
    Spinner District, Division;
    boolean Blocation, Bimage;
    ImageView upload, location;
    Bitmap mainImage;

    ShopModel smodel;

    String id_district[];

    ArrayList<String> id_list = new ArrayList<String>();
    ArrayList<String> div_list = new ArrayList<String>();
    ArrayList<String> dis_list = new ArrayList<String>();
    ArrayList<String> dis_id_list = new ArrayList<String>();

    ArrayAdapter<String> adapterDiv, adapterDis;
    ArrayList<String> listItemsDis = new ArrayList<>();
    ArrayList<String> listItemsDiv = new ArrayList<>();

    private static final int STORAGE_PERMISSION_CODE = 123;

    private static final int PERMISSION_REQUEST_CODE = 200;

    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = android.Manifest.permission.ACCESS_FINE_LOCATION;




    String cname, pname, addrss, phone, email, website, district, division,Lat ,Long;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    GPSTracker gps;

    public static final String MyPREFERENCESuser = "UserPrefs";
    public static final String usermail = "Usermail";
    public static final String userType = "userType";
    public static final String AdminT = "admin";
    public static final String OperatorT = "user";
    public static final String TokenPreference = "tokenT";
    RequestQueue queue;
    public static final String NamePreference = "nameU";
    public static final String IDPreference = "uid";
    int code;

    String Uemail, Type, TokenString, Name,UserId;
    SharedPreferences sharedpreferences;
    TextView Hname, Hemail;
    View header;
    String id_array[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        queue = Volley.newRequestQueue(this);

        Intent intent = getIntent();
        Uemail = intent.getStringExtra("email");
        Type = intent.getStringExtra(userType);
        TokenString = intent.getStringExtra("token");
        Name = intent.getStringExtra("Name");
        UserId =intent.getStringExtra("ID");

        Log.d("Uactivity", "ID" + UserId);
        Log.d("Uactivity", "name" + Name);
        Log.d("Uactivity", "email" + Uemail);
        Log.d("Uactivity", "Type" + Type);
        Log.d("Uactivity", "email" + Uemail);

        sharedpreferences = getSharedPreferences(MyPREFERENCESuser, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString(IDPreference, UserId);
        editor.putString(NamePreference, Name);
        editor.putString(TokenPreference, TokenString);
        editor.putString(userType, OperatorT);
        editor.putString(usermail, Uemail);
        editor.commit();
        Log.d("Uactivity", "email" + sharedpreferences.getString(usermail, ""));
        Log.d("Uactivity", "type_pre" + sharedpreferences.getString(userType, ""));
        Log.d("Uactivity", "Token_Pre" + sharedpreferences.getString(TokenPreference, ""));

        Cname = (EditText) findViewById(R.id.sname);
        Pname = (EditText) findViewById(R.id.pname);
        Addrss = (EditText) findViewById(R.id.address);
        Phone = (EditText) findViewById(R.id.phone);
        Email = (EditText) findViewById(R.id.email);
        Website = (EditText) findViewById(R.id.web);


        District = (Spinner) findViewById(R.id.Sp_dis);
        Division = (Spinner) findViewById(R.id.Sp_div);

        new divtList().execute();
        adapterDiv = new ArrayAdapter<String>(this, R.layout.spinner_item_post, R.id.txt, listItemsDiv);
        Division.setAdapter(adapterDiv);

//
        Division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);

              //  new distList(id_array[pos]).execute();
                if(Division.getSelectedItemPosition()==0)
                {

                }else
                {
                    Log.d("ID","Id: "+id_array[pos]);
                     new distList(id_array[pos]).execute();

                    adapterDis = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item_post, R.id.txt, listItemsDis);
                    District.setAdapter(adapterDis);
                }

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        upload = (ImageView) findViewById(R.id.uploadImage);
        location = (ImageView) findViewById(R.id.locationImage);


        upload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }

            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (!checkPermission()) {

                    requestPermission();

                } else {

                    gps = new GPSTracker(UserActivity.this);

                    // check if GPS enabled
                    if (gps.canGetLocation()) {


                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();
                        if (latitude == 0 && longitude == 0) {
                            Blocation = false;
                        } else {
                            Blocation = true;
                            sharedpreferences = getSharedPreferences(MyPREFERENCESuser, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();

                            Lat= String.valueOf(latitude);
                            Long = String.valueOf(longitude);
                            editor.putString("Lat",Lat);
                            editor.putString("Long",Long);
                            editor.commit();
                        }

                        // \n is for new line
                        Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    } else {
                        // can't get location
                        // GPS or Network is not enabled
                        // Ask user to enable GPS/network in settings
                        gps.showSettingsAlert();
                    }

                }


            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        header = navigationView.getHeaderView(0);
        Hname = (TextView) header.findViewById(R.id.header_name);
        Hemail = (TextView) header.findViewById(R.id.header_email);

        Hemail.setText(sharedpreferences.getString(usermail, ""));
        Hname.setText(sharedpreferences.getString(NamePreference, ""));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mainImage = Bitmap.createScaledBitmap(imageBitmap, 800, 950, true);
            upload.setImageBitmap(mainImage);

            sharedpreferences = getSharedPreferences(MyPREFERENCESuser, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            String im = BitmapToString(mainImage);
            editor.putString("image",im);
            editor.commit();
            Bimage = true;
        }
    }
    public String BitmapToString(Bitmap bp)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bp.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String imageString = Base64.encodeToString(b, Base64.DEFAULT);
        return imageString;
    }
    public void ok(View v) {

        String im2 =sharedpreferences.getString("image","");
        cname = Cname.getText().toString();
        pname = Pname.getText().toString();
        addrss = Addrss.getText().toString();
        phone = Phone.getText().toString();
        email = Email.getText().toString();
        website = Website.getText().toString();

        String latatute = sharedpreferences.getString("Lat","");
        String Longatuate = sharedpreferences.getString("Long","");

        district = District.getSelectedItem().toString();
        division = Division.getSelectedItem().toString();
        int districtid = Integer.parseInt(id_district[District.getSelectedItemPosition()]);
        smodel = new ShopModel(cname,pname,addrss,phone,email,website,districtid,latatute,Longatuate,im2);

        Log.d("ok","cname"+cname);
        Log.d("ok","pnm:"+pname);
        Log.d("ok","ph:"+phone);
        Log.d("ok","ad:"+addrss);
        Log.d("ok","em:"+email);
        Log.d("ok","web:"+website);
        Log.d("ok","Lat"+latatute);
        Log.d("ok","Long"+Longatuate);
        Log.d("ok","DisId :"+districtid);

       // Log.d("ok","");

        if (cname.equals("") || pname.equals("") || addrss.equals("") || phone.equals("") || email.equals("") ||District.getSelectedItemPosition()==0||Division.getSelectedItemPosition()==0) {
            Toast.makeText(getApplicationContext(), "Input Field empty", Toast.LENGTH_SHORT).show();
        } else if (Blocation == false) {
            Toast.makeText(getApplicationContext(), "Shop Location Required", Toast.LENGTH_SHORT).show();
        } else if (Bimage == false) {
            Toast.makeText(getApplicationContext(), "Shop Image Required", Toast.LENGTH_SHORT).show();
        } else {

            new ShopInsert(this.smodel).execute();


            reset();
        }

    }

    public void reset() {
        Bimage = false;
        Blocation = false;
        Cname.setText("");
        Pname.setText("");
        Addrss.setText("");
        Phone.setText("");
        Email.setText("");
        Website.setText("");
//
        District.setSelection(0);
        Division.setSelection(0);
        //upload.setImageBitmap();
        upload.setImageBitmap(null);
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
        getMenuInflater().inflate(R.menu.user, menu);
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
            // Handle the camera action
        } else if (id == R.id.nav_newsurvay) {
            Intent intent = new Intent(UserActivity.this, NewSurveyActivity.class);
            intent.putExtra("email", sharedpreferences.getString(usermail, ""));
            intent.putExtra("token", sharedpreferences.getString(TokenPreference, ""));
            intent.putExtra("Name", sharedpreferences.getString(NamePreference, ""));
            intent.putExtra(userType, OperatorT);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        } else if (id == R.id.nav_view) {

            Intent intent = new Intent(UserActivity.this, ViewSurveyActivity.class);
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
            Intent intent = new Intent(UserActivity.this, LoginActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_profile) {
            Toast.makeText(getApplicationContext(), "nav_profile", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_pass) {
            Intent intent = new Intent(UserActivity.this, ChangePassActivity.class);
            intent.putExtra("email", sharedpreferences.getString(usermail, ""));
            intent.putExtra("token", sharedpreferences.getString(TokenPreference, ""));
            intent.putExtra("Name", sharedpreferences.getString(NamePreference, ""));
            intent.putExtra(userType, OperatorT);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private class divtList extends AsyncTask<String, Void, String> {

        String url = VariableClass.division;
        InputStream inputStream = null;
        String result = "";


        @Override
        protected String doInBackground(String... arg0) {

            StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("UResponse", response);

                            Gson gson = new Gson();
                            ZoneModel[] user = gson.fromJson(response, ZoneModel[].class);


                            div_list.add("Division List");
                            for (int i = 0; i < user.length; i++) {

                                id_list.add(user[i].id);
                                div_list.add(user[i].name);

                            }
                            Log.d("Uback", "id" + id_list);
                            Log.d("Uback", "id" + div_list);

                            id_array = id_list.toArray(new String[id_list.size()]);

                            listItemsDiv.addAll(div_list);
                            adapterDiv.notifyDataSetChanged();


                        }
                    },
                    new Response.ErrorListener() {
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
                    headers.put("token", sharedpreferences.getString(TokenPreference, ""));
                    Log.d("header", "Token:Header" + sharedpreferences.getString(TokenPreference, ""));
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
    private class ShopInsert extends AsyncTask<String, Void, String> {

        String url = VariableClass.shop;
        InputStream inputStream = null;
        String result = "";
         ShopModel smodel2;
        ShopInsert(ShopModel smodel)
        {
            smodel2= smodel;
        }

        @Override
        protected String doInBackground(String... arg0) {

            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
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

                   // ShopModel smodel = new ShopModel(cname,pname,addrss,phone,email,website,district,Lat,Long,image);


                    Gson gson = new Gson();
                    //gson.toJson(user);
                    String mContent = gson.toJson(smodel2);
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
                reset();
                Intent intent = new Intent(UserActivity.this, NewSurveyActivity.class);
                intent.putExtra("email", sharedpreferences.getString(usermail, ""));
                intent.putExtra("token", sharedpreferences.getString(TokenPreference, ""));
                intent.putExtra("Name", sharedpreferences.getString(NamePreference, ""));
                intent.putExtra(userType, OperatorT);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"Insert",Toast.LENGTH_SHORT).show();
            }
            else if(code ==1){ Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_SHORT).show();}
            else if(code ==2){ Toast.makeText(getApplicationContext(),"Unauthorized",Toast.LENGTH_SHORT).show();}
            else if(code ==3){ Toast.makeText(getApplicationContext(),"System Error",Toast.LENGTH_SHORT).show();}
            else if(code ==4){ Toast.makeText(getApplicationContext(),"Require Field",Toast.LENGTH_SHORT).show();}
            else if(code ==5){Toast.makeText(getApplicationContext(),"User Already Exist",Toast.LENGTH_SHORT).show();}
            else if(code ==6){Toast.makeText(getApplicationContext(),"No user found",Toast.LENGTH_SHORT).show();}
        }

    }
    private class distList extends AsyncTask<String, Void, String> {

        String id;
        String url = VariableClass.districts;
        InputStream inputStream = null;

        distList(String id)
        {
            this.id = id;
            Log.d("ID","id"+this.id);
        }

        String result = "";


        @Override
        protected String doInBackground(String... arg0) {

            StringRequest postRequest = new StringRequest(Request.Method.GET, url+"/"+id,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("UResponse", response);

                            Gson gson = new Gson();
                            ZoneModel[] dis= gson.fromJson(response,ZoneModel[].class);


                            dis_list.add("District List");
                            for(int i = 0; i<dis.length;i++)
                            {

                                dis_id_list.add(dis[i].getId());
                                dis_list.add(dis[i].name);

                            }
                           // Log.d("Uback","id"+id_list);
                            Log.d("Uback","id"+dis_list);

                            //id_array = div_list.toArray(new String[div_list.size()]);

                            id_district = dis_id_list.toArray(new String[dis_id_list.size()]);
                            listItemsDis.addAll(dis_list);
                            adapterDis.notifyDataSetChanged();



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
        new AlertDialog.Builder(UserActivity.this)
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
