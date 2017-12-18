package com.example.kaziorin.sblms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.kaziorin.sblms.ServiceHandler.response;

public class TABActivity extends AppCompatActivity {

    public static final String MyPREFERENCESuser = "UserPrefs" ;
    public static final String usermail = "Usermail";
    public static final String userType = "userType";
    public static final String AdminT = "admin";
    public static final String ShopID = "sid";
    public static final String OperatorT = "user";
    public static final String TokenPreference = "tokenT";

    ArrayList<String> typename = new ArrayList<String>();
    RequestQueue queue;
    public static final String NamePreference = "nameU";

    TextView ComapanyName,OwenrName,Division,District,Address,Contract,Email,Website,ShopName,Agree,Think,Interest,Other,Comment;

    ViewModel model;

    String Uemail,Type,TokenString,Name,ShopId;
    SharedPreferences sharedpreferences;

    Spinner ProductType,Shopname,TableSpinner;
    ArrayAdapter adapterProduct;
    CustomAdapter adapterProductList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        ComapanyName = (TextView)findViewById(R.id.CName);
        OwenrName = (TextView)findViewById(R.id.pname);
        Division = (TextView)findViewById(R.id.division);
        District = (TextView)findViewById(R.id.District);
        Address = (TextView)findViewById(R.id.add);
        Contract = (TextView)findViewById(R.id.cont);
        Email = (TextView)findViewById(R.id.email);
        Website = (TextView)findViewById(R.id.website);

        Agree = (TextView)findViewById(R.id.agree);
        Think = (TextView)findViewById(R.id.think);
        Interest = (TextView)findViewById(R.id.inter);
        Other = (TextView)findViewById(R.id.other);
        Comment= (TextView)findViewById(R.id.bcom);

        queue = Volley.newRequestQueue(this);
        Intent intent = getIntent();
        ShopId = intent.getStringExtra(ShopID);
        Uemail = intent.getStringExtra("email");
        Type = intent.getStringExtra(userType);
        TokenString = intent.getStringExtra("token");
        Name = intent.getStringExtra("Name");
        Log.d("TABctivity","shopid"+ ShopId);
        Log.d("TABctivity","name"+ Name);
        Log.d("TABtivity","email"+ Uemail);
        Log.d("TABtivity","Type"+ Type);
        Log.d("TABtivity","email"+Uemail);

        sharedpreferences = getSharedPreferences(MyPREFERENCESuser, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(ShopID, ShopId);
        editor.putString(NamePreference, Name);
        editor.putString(TokenPreference, TokenString);
        editor.putString(userType, Type);
        editor.putString(usermail, Uemail);
        editor.commit();
        Log.d("TABtivity","email"+sharedpreferences.getString(usermail,""));
        Log.d("TABtivity","type_pre"+sharedpreferences.getString(userType,""));
        Log.d("TABtivity","Token_Pre"+sharedpreferences.getString(TokenPreference,""));

        ProductType = (Spinner)findViewById(R.id.Spinner_ptype);
        TableSpinner =(Spinner)findViewById(R.id.Spinner_sname);


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

                    String [] name_array =new String [10],pprice_arry =new String [10],sprice_array = new String [10],comment_array = new String [10];
                    ArrayList<BrandModel> brands = model.products.get(0).brands;

                    for(int i = 0; i< brands.size(); i++){
                        name_array[i] = brands.get(i).name;
                        pprice_arry[i] = String.valueOf(brands.get(i).purchasePrice);
                        sprice_array[i] = String.valueOf(brands.get(i).salePrice);
                        comment_array[i] = brands.get(i).comment;
                    }
                    adapterProductList= new CustomAdapter(getApplicationContext(),name_array,pprice_arry,sprice_array,comment_array);

                    Log.d("","");
                    TableSpinner.setAdapter(adapterProductList);
                    adapterProductList.notifyDataSetChanged();
                }
                else if(ProductType.getSelectedItemPosition()==2)
                {
                    String [] name_array =new String [10],pprice_arry =new String [10],sprice_array = new String [10],comment_array = new String [10];
                    ArrayList<BrandModel> brands = model.products.get(1).brands;

                    for(int i = 0; i< brands.size(); i++){
                        name_array[i] = brands.get(i).name;
                        pprice_arry[i] = String.valueOf(brands.get(i).purchasePrice);
                        sprice_array[i] = String.valueOf(brands.get(i).salePrice);
                        comment_array[i] = brands.get(i).comment;
                    }
                    adapterProductList= new CustomAdapter(getApplicationContext(),name_array,pprice_arry,sprice_array,comment_array);

                    Log.d("","");
                    TableSpinner.setAdapter(adapterProductList);
                    adapterProductList.notifyDataSetChanged();
                }
                else if(ProductType.getSelectedItemPosition()==3)
                {
                    String [] name_array =new String [10],pprice_arry =new String [10],sprice_array = new String [10],comment_array = new String [10];
                    ArrayList<BrandModel> brands = model.products.get(2).brands;

                    for(int i = 0; i< brands.size(); i++){
                        name_array[i] = brands.get(i).name;
                        pprice_arry[i] = String.valueOf(brands.get(i).purchasePrice);
                        sprice_array[i] = String.valueOf(brands.get(i).salePrice);
                        comment_array[i] = brands.get(i).comment;
                    }
                    adapterProductList= new CustomAdapter(getApplicationContext(),name_array,pprice_arry,sprice_array,comment_array);

                    Log.d("","");
                    TableSpinner.setAdapter(adapterProductList);
                    adapterProductList.notifyDataSetChanged();
                }

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


        new ShopViewList(ShopId).execute();
    }

    private class ShopViewList extends AsyncTask<Void, Void, String> {

        String url = VariableClass.surveyed_view;
        InputStream inputStream = null;
        String result = "";
        String id;

        ShopViewList(String id)
        {
            this.id = id;
        }

        @Override
        protected String doInBackground(Void... arg0) {

            StringRequest postRequest = new StringRequest(Request.Method.GET, url+"/"+id,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("TABResponse", response);

                            try {

                                JSONObject obj = new JSONObject(response);

                                Gson gson = new Gson();
                                model = gson.fromJson(response,ViewModel.class);
                                Log.d("model","model"+model.isAgreed);

                                Agree.setText(model.isAgreed ? "Yes" : "No");

                                Think.setText(model.isThink ? "Yes" : "No");

                                Interest.setText(model.isNotInterested ? "Yes" : "No");

                                Other.setText(model.others);

                                Comment.setText(model.comment);

                                ShopModel shop =model.shop;

                                ComapanyName.setText(shop.name);

                                OwenrName.setText(shop.ownerName);

                                District.setText(shop.districtName);

                                Division.setText(shop.divisionName);

                                Address.setText(shop.address);

                                Contract.setText(shop.phone);

                                Email.setText(shop.email);

                                Website.setText(shop.website);

                                String products = obj.getString("products");

                                typename.add("Select Product Type");
                                for(int i=0;i<model.products.size();i++)
                                {
                                    typename.add(model.products.get(i).name);
                                }

                                Log.d("typename","Type"+typename);




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
