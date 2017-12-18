package com.example.kaziorin.sblms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import java.util.Random;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.listeners.TableHeaderClickListener;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

import static com.example.kaziorin.sblms.ServiceHandler.response;

public class NewSurveyActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public static final String MyPREFERENCESuser = "UserPrefs" ;
    public static final String usermail = "Usermail";
    public static final String userType = "userType";
    public static final String AdminT = "admin";
    public static final String OperatorT = "user";
    public static final String SurveyID = "surveyid";
    public static final String TokenPreference = "tokenT";
    RequestQueue queue;
    public static final String NamePreference = "nameU";

    String Uemail,Type,TokenString,Name;
    SharedPreferences sharedpreferences;
    TextView Hname,Hemail;
    View header;

    Boolean check;
    int code;

    Spinner ProductType,Shopname,TableSpinner;
    String shopname,productType;

    EditText bname,pprice,spric ,comm;
    String Bname,Pprice,Spric ,Com;

    String name_array[] ,pprice_arry[],sprice_array[],comment_array[];
    String name_array2[] ,pprice_arry2[],sprice_array2[],comment_array2[];

    String name_array3[] ,pprice_arry3[],sprice_array3[],comment_array3[];

ArrayList<String> Shoplist = new ArrayList<String>();
ArrayList<String> Shop_id_list = new ArrayList<String>();
ArrayList<String> ListItemShop = new ArrayList<String>();

ArrayList<BrandModel> brands = new ArrayList<BrandModel>();
    ArrayList<ProductModel> products = new ArrayList<ProductModel>();
String ShopName_Array[],ShopId_array[];
    ArrayAdapter<String> adapterShop;


    String[] spaceProbeHeaders={"ID","Barnd Name","Purchase Price","Sale Price","Customer_Comment"};
    String[][] spaceProbes;
    TableView<String[]> tb;

    ArrayList<String> name1 = new ArrayList<String>();
    ArrayList<String> pprice1 = new ArrayList<String>();
    ArrayList<String> sprice1 = new ArrayList<String>();
    ArrayList<String> comm1 = new ArrayList<String>();
    ArrayList<String> Type1 = new ArrayList<String>();

    ArrayList<String> name2 = new ArrayList<String>();
    ArrayList<String> pprice2 = new ArrayList<String>();
    ArrayList<String> sprice2 = new ArrayList<String>();
    ArrayList<String> comm2 = new ArrayList<String>();
    ArrayList<String> Type2 = new ArrayList<String>();

    ArrayList<String> name3 = new ArrayList<String>();
    ArrayList<String> pprice3 = new ArrayList<String>();
    ArrayList<String> sprice3 = new ArrayList<String>();
    ArrayList<String> comm3 = new ArrayList<String>();
    ArrayList<String> Type3 = new ArrayList<String>();

    ArrayList<String> nameF = new ArrayList<String>();
    ArrayList<String> ppriceF = new ArrayList<String>();
    ArrayList<String> spriceF = new ArrayList<String>();
    ArrayList<String> commF = new ArrayList<String>();
    ArrayList<String> TypeF = new ArrayList<String>();

    ArrayList<Integer> num1 = new ArrayList<Integer>();
    int count=0;
    ArrayAdapter<CharSequence> adapterProduct;
    CustomAdapter adapterProductList;

    TextView nametext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_survey);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        queue = Volley.newRequestQueue(this);
        Intent intent = getIntent();
        Uemail = intent.getStringExtra("email");
        Type = intent.getStringExtra(userType);
        TokenString = intent.getStringExtra("token");
        Name = intent.getStringExtra("Name");
        Log.d("NSactivity","name"+ Name);
        Log.d("NSactivity","email"+ Uemail);
        Log.d("NSactivity","Type"+ Type);
        Log.d("NSactivity","email"+Uemail);

        products.add(new ProductModel("SOLAR PANEL"));
        products.add(new ProductModel("SOLAR CHARGE CONTROLLER"));//
        products.add(new ProductModel("SOLAR CABLES"));
        sharedpreferences = getSharedPreferences(MyPREFERENCESuser, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString(NamePreference, Name);
        editor.putString(TokenPreference, TokenString);
        editor.putString(userType, Type);
        editor.putString(usermail, Uemail);
        editor.commit();
        Log.d("NSactivity","email"+sharedpreferences.getString(usermail,""));
        Log.d("NSactivity","type_pre"+sharedpreferences.getString(userType,""));
        Log.d("NSactivity","Token_Pre"+sharedpreferences.getString(TokenPreference,""));


        bname = (EditText)findViewById(R.id.Bname);
        pprice = (EditText)findViewById(R.id.pprice);
        spric = (EditText)findViewById(R.id.cprice);
        comm = (EditText)findViewById(R.id.com);
        nametext = (TextView)findViewById(R.id.textname);

        Shopname = (Spinner)findViewById(R.id.Spinner_sname);
        ProductType = (Spinner)findViewById(R.id.Spinner_ptype);
        TableSpinner =(Spinner)findViewById(R.id.Table_spinner);


        if(isOnline()==true)
        {
            new shoplist().execute();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Please check internet connection",Toast.LENGTH_SHORT).show();
        }
        adapterShop = new ArrayAdapter<String>(this, R.layout.spinner_item_post, R.id.txt, ListItemShop);
        Shopname.setAdapter(adapterShop);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        header = navigationView.getHeaderView(0);
        Hname = (TextView)header.findViewById(R.id.header_name);
        Hemail = (TextView)header.findViewById(R.id.header_email);

        Hemail.setText(sharedpreferences.getString(usermail,""));
        Hname.setText(sharedpreferences.getString(NamePreference,""));


        adapterProduct = ArrayAdapter.createFromResource(this,R.array.Product_type_array, android.R.layout.simple_spinner_item);
        adapterProduct.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ProductType.setAdapter(adapterProduct);

        ProductType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if(ProductType.getSelectedItemPosition()==1)
                {
                    pro1Add();
                    resetField();

                    nametext.setText(selectedItem);

                }
                else if(ProductType.getSelectedItemPosition()==2)
                {
                    pro2Add();
                    resetField();
                    nametext.setText(selectedItem);
                }
                else if(ProductType.getSelectedItemPosition()==3)
                {
                      pro3Add();
                     resetField();
                     nametext.setText(selectedItem);
                }

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });



    }
    public void Insert(View v) {
        Bname = bname.getText().toString();
        Pprice = pprice.getText().toString();
        Spric = spric.getText().toString();
        Com = comm.getText().toString();



    //    shopname = Shopname.getSelectedItem().toString();
        productType = ProductType.getSelectedItem().toString();


        if(ProductType.getSelectedItemPosition()==1)
        {
            if(Type1.size()<4) {

                String BnameCheck,CommentCheck,PPriceCheck, SPriceCheck;

                name1.add(Bname);
                pprice1.add(Pprice);
                sprice1.add(Spric);
                comm1.add(Com);
                Type1.add(productType);

                    if (Bname.equals(""))
                    {
                        Toast.makeText(getApplicationContext(),"Brand name empty",Toast.LENGTH_SHORT).show();
                    }
                    else if((Pprice.equals("")||Spric.equals("")) && !Bname.equals(""))
                    {
                        Toast.makeText(getApplicationContext(),"Price field empty",Toast.LENGTH_SHORT).show();
                    }
                    else if((!Bname.equals("")&& !Pprice.equals("")||!Spric.equals("")) &&Com.equals(""))
                    {
                        BrandModel m = new BrandModel(Bname,Double.valueOf(Pprice),Double.valueOf(Spric));
                        products.get(0).brands.add(m);
                        brands.add(m);
                        pro1Add();
                        resetField();

                    }else if(!Bname.equals("")&& !Pprice.equals("")&&!Spric.equals("") &&!Com.equals(""))
                    {
                        BrandModel m = new BrandModel(Bname,Com,Double.valueOf(Pprice),Double.valueOf(Spric));
                        products.get(0).brands.add(m);
                        brands.add(m);
                        pro1Add();
                        resetField();
                    }

            }else
            {
                resetField();
                Toast.makeText(getApplicationContext(),"Table Completed",Toast.LENGTH_SHORT).show();
            }
        }
        else if(ProductType.getSelectedItemPosition()==2)
        {
            if(Type2.size()<4) {
                name2.add(Bname);
                pprice2.add(Pprice);
                sprice2.add(Spric);
                comm2.add(Com);
                Type2.add(productType);
                if (Bname.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Brand name empty",Toast.LENGTH_SHORT).show();
                }
                else if((Pprice.equals("")||Spric.equals("")) && !Bname.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Price field empty",Toast.LENGTH_SHORT).show();
                }
                else if((!Bname.equals("")&& !Pprice.equals("")||!Spric.equals("")) &&Com.equals(""))
                {
                    BrandModel m = new BrandModel(Bname,Double.valueOf(Pprice),Double.valueOf(Spric));
                    products.get(1).brands.add(m);
                    brands.add(m);
                    pro1Add();
                    resetField();

                }else if(!Bname.equals("")&& !Pprice.equals("")&&!Spric.equals("") &&!Com.equals(""))
                {
                    BrandModel m = new BrandModel(Bname,Com,Double.valueOf(Pprice),Double.valueOf(Spric));
                    products.get(1).brands.add(m);
                    brands.add(m);
                    pro1Add();
                    resetField();
                }
            }else
            {
                resetField();
                Toast.makeText(getApplicationContext(),"Table Completed",Toast.LENGTH_SHORT).show();
            }
        }
        else if(ProductType.getSelectedItemPosition()==3)
        {
            if(Type3.size()<4) {
                name3.add(Bname);
                pprice3.add(Pprice);
                sprice3.add(Spric);
                comm3.add(Com);
                Type3.add(productType);

                if (Bname.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Brand name empty",Toast.LENGTH_SHORT).show();
                }
                else if((Pprice.equals("")||Spric.equals("")) && !Bname.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Price field empty",Toast.LENGTH_SHORT).show();
                }
                else if((!Bname.equals("")&& !Pprice.equals("")||!Spric.equals("")) &&Com.equals(""))
                {
                    BrandModel m = new BrandModel(Bname,Double.valueOf(Pprice),Double.valueOf(Spric));
                    products.get(2).brands.add(m);
                    brands.add(m);
                    pro1Add();
                    resetField();

                }else if(!Bname.equals("")&& !Pprice.equals("")&&!Spric.equals("") &&!Com.equals(""))
                {
                    BrandModel m = new BrandModel(Bname,Com,Double.valueOf(Pprice),Double.valueOf(Spric));
                    products.get(2).brands.add(m);
                    brands.add(m);
                    pro1Add();
                    resetField();
                }
                pro3Add();
                resetField();
            }else
            {
                resetField();
                Toast.makeText(getApplicationContext(),"Table Completed",Toast.LENGTH_SHORT).show();
            }

        }else if(ProductType.getSelectedItemPosition()==0)
        {
            Toast.makeText(getApplicationContext(),"Select Product Type",Toast.LENGTH_SHORT).show();
        }


    }
    public void arraycust()
    {
        for(int i = 0;i<Type1.size();i++)
        {
            nameF.add(name1.get(i));
            spriceF.add(sprice1.get(i));
            ppriceF.add(pprice1.get(i));
            commF.add(comm1.get(i));
            TypeF.add(Type1.get(i));
        }
        for(int i = 0;i<Type2.size();i++)
        {
            nameF.add(name2.get(i));
            spriceF.add(sprice2.get(i));
            ppriceF.add(pprice2.get(i));
            commF.add(comm2.get(i));
            TypeF.add(Type2.get(i));
        }
        for(int i = 0;i<Type3.size();i++)
        {
            nameF.add(name3.get(i));
            spriceF.add(sprice3.get(i));
            ppriceF.add(pprice3.get(i));
            commF.add(comm3.get(i));
            TypeF.add(Type3.get(i));
        }
        Log.d("array","array"+nameF);
    }
    public void resetField(){
        bname.setText("");
        pprice.setText("");
        spric.setText("");
        comm.setText("");
    }
    public void pro1Add()
    {
        name_array = name1.toArray(new String[name1.size()]);
        pprice_arry = pprice1.toArray(new String[pprice1.size()]);
        sprice_array = sprice1.toArray(new String[sprice1.size()]);
        comment_array = comm1.toArray(new String[comm1.size()]);
        adapterProductList= new CustomAdapter(this,name_array,pprice_arry,sprice_array,comment_array);
        TableSpinner.setAdapter(adapterProductList);
        adapterProductList.notifyDataSetChanged();
    }
    public void pro2Add()
    {
        name_array = name2.toArray(new String[name2.size()]);
        pprice_arry = pprice2.toArray(new String[pprice2.size()]);
        sprice_array = sprice2.toArray(new String[sprice2.size()]);
        comment_array = comm2.toArray(new String[comm2.size()]);
        adapterProductList= new CustomAdapter(this,name_array,pprice_arry,sprice_array,comment_array);
        TableSpinner.setAdapter(adapterProductList);
        adapterProductList.notifyDataSetChanged();
    }
    public void pro3Add()
    {
        name_array = name3.toArray(new String[name3.size()]);
        pprice_arry = pprice3.toArray(new String[pprice3.size()]);
        sprice_array = sprice3.toArray(new String[sprice3.size()]);
        comment_array = comm3.toArray(new String[comm3.size()]);
        adapterProductList= new CustomAdapter(this,name_array,pprice_arry,sprice_array,comment_array);
        TableSpinner.setAdapter(adapterProductList);
        adapterProductList.notifyDataSetChanged();
    }

    public void save(View v)
    {
        check = true;

        if(Shopname.getSelectedItemPosition()==0)
        {
             Toast.makeText(getApplicationContext(),"Please Select Shop",Toast.LENGTH_SHORT).show();
        }else
        {
                int id =Integer.parseInt (ShopId_array[Shopname.getSelectedItemPosition()-1]);

            Log.d("shopid","shopid"+ShopId_array[Shopname.getSelectedItemPosition()-1]);
            //Log.d("nameFid","name:"+id );
            if(isOnline()==true)
            {
               new surveyInsert1(id).execute();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Please Connect Internet",Toast.LENGTH_SHORT).show();
            }


        }
    }
    public void Allclear()
    {
        Shopname.setSelection(0);
        ProductType.setSelection(0);
        TableSpinner.setAdapter(null);
        nametext.setText("");
        name1.clear();
        pprice1.clear();
        sprice1.clear();
        comm1.clear();
        Type1.clear();
        name2.clear();
        pprice2.clear();
        sprice2.clear();
        comm2.clear();
        Type2.clear();
        name3.clear(); pprice3.clear();
        sprice3.clear();
        comm3.clear();
        Type3.clear();

        ArrayList<String> nameF = new ArrayList<String>();
        ArrayList<String> ppriceF = new ArrayList<String>();
        ArrayList<String> spriceF = new ArrayList<String>();
        commF.clear();
       TypeF.clear();
    }
    public void Next(View v) {
        check = false;

        if (Shopname.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), "Please Select Shop", Toast.LENGTH_SHORT).show();
        } else {
            int id = Integer.parseInt(ShopId_array[Shopname.getSelectedItemPosition() - 1]);

            Log.d("shopid", "shopid" + ShopId_array[Shopname.getSelectedItemPosition() - 1]);
            //Log.d("nameFid","name:"+id );
            if (isOnline() == true) {
                new surveyInsert1(id).execute();
            } else {
                Toast.makeText(getApplicationContext(), "Please Connect Internet", Toast.LENGTH_SHORT).show();
            }


        }
    }
    public void location(View v) {
        Toast.makeText(getApplicationContext(),"location",Toast.LENGTH_SHORT).show();
    }

    private class surveyInsert1 extends AsyncTask<String, Void, String> {

        String url = VariableClass.surveyed;
        InputStream inputStream = null;
        String result = "";
        int id;
        ShopModel smodel2;
        surveyInsert1(int id )
        {
            this.id = id;
        }

        @Override
        protected String doInBackground(String... arg0) {

            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("NewResponse", response);

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
                                code = obj.getInt("code");

                                String r = obj.getString("survey");
                                JSONObject mainOb = new JSONObject(r);
                                int surveyID = mainOb.getInt("id");
                                Log.d("main","r:"+r);
                                Log.d("main","main:"+mainOb);
                                Log.d("main","main:"+surveyID);

                                if(code==0)
                                {
                                    if(check ==true)
                                    {
                                        // save
                                        Toast.makeText(getApplication(),"Save",Toast.LENGTH_SHORT).show();
                                        Allclear();
                                        resetField();
                                        new shoplist().execute();

                                    }
                                    else
                                    {
                                        Intent intent = new Intent(NewSurveyActivity.this, NewSurveyActivity2.class);
                                        intent.putExtra("email", sharedpreferences.getString(usermail,""));
                                        intent.putExtra("token", sharedpreferences.getString(TokenPreference,""));
                                        intent.putExtra("Name", sharedpreferences.getString(NamePreference,""));
                                        intent.putExtra(SurveyID ,surveyID);
                                        intent.putExtra(userType,OperatorT);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
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
                model.shopId = id;
                model.products = products;
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

    private class shoplist extends AsyncTask<Void, Void, String> {

        String url = VariableClass.surveyed_shop_list;
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
                            Log.d("AoooResponse", response);

                            Gson gson = new Gson();
                            UserModel[] user= gson.fromJson(response,UserModel[].class);

                            Shoplist.add("Shop Name");
                            for(int i = 0; i<user.length;i++)
                            {
                                Log.d("",""+user[i].longitude);
                                Shoplist.add(user[i].getName());
                                Shop_id_list.add(user[i].getId());

                            }

                            ShopName_Array = Shoplist.toArray(new String[Shoplist.size()]);
                            ShopId_array = Shop_id_list.toArray(new String[Shop_id_list.size()]);

                            ListItemShop.addAll( Shoplist);
                            adapterShop.notifyDataSetChanged();

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
        getMenuInflater().inflate(R.menu.new_survey, menu);
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
            Intent intent = new Intent(NewSurveyActivity.this, UserActivity.class);
            intent.putExtra("email", sharedpreferences.getString(usermail,""));
            intent.putExtra("token", sharedpreferences.getString(TokenPreference,""));
            intent.putExtra("Name", sharedpreferences.getString(NamePreference,""));
            intent.putExtra(userType, OperatorT);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        } else if (id == R.id.nav_newsurvay) {
            Toast.makeText(getApplicationContext(),"nav_newsurvay",Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_view) {

            Intent intent = new Intent(NewSurveyActivity.this, ViewSurveyActivity.class);

            intent.putExtra("email", sharedpreferences.getString(usermail,""));
            intent.putExtra("token", sharedpreferences.getString(TokenPreference,""));
            intent.putExtra("Name", sharedpreferences.getString(NamePreference,""));
            intent.putExtra(userType, OperatorT);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        } else if (id == R.id.nav_signout) {

            sharedpreferences = getSharedPreferences(MyPREFERENCESuser, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.clear();
            editor.commit();
            finish();
            Intent intent = new Intent(NewSurveyActivity.this,LoginActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_profile) {
            Toast.makeText(getApplicationContext(),"nav_profile",Toast.LENGTH_SHORT).show();

        }else if (id == R.id.nav_pass) {

            Intent intent = new Intent(NewSurveyActivity.this, ChangePassActivity.class);

            intent.putExtra("email", sharedpreferences.getString(usermail,""));
            intent.putExtra("token", sharedpreferences.getString(TokenPreference,""));
            intent.putExtra("Name", sharedpreferences.getString(NamePreference,""));
            intent.putExtra(userType, OperatorT);
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
