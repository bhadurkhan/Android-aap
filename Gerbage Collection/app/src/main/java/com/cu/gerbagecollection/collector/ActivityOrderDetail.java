package com.cu.gerbagecollection.collector;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cu.gerbagecollection.R;
import com.cu.gerbagecollection.SignInActivity;
import com.cu.gerbagecollection.adapter.OrderAdapter;
import com.cu.gerbagecollection.adapter.OrderDetailAdapter;
import com.cu.gerbagecollection.model.OrderDetailItem;
import com.cu.gerbagecollection.model.OrderItem;
import com.cu.gerbagecollection.utils.AppContants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class ActivityOrderDetail extends AppCompatActivity {
    private ListView listView;
    private ProgressDialog pdialog;
    private ArrayList<OrderDetailItem> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_duty);
        listView = findViewById(R.id.list);
        //////////////////////////////////////////
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        //////////////////////////////////////////////////
        getData(getIntent().getStringExtra("id"));
        ////////////////////////////////////////////////
    }



    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void getData(String ID) {
        pdialog = new ProgressDialog(ActivityOrderDetail.this, R.style.AlertDialogCustom);
        pdialog.setMessage("Loading...");
        pdialog.show();
        pdialog.show();
        ////////////////////////////
        list=new ArrayList<>();
        String url = AppContants.baseUrl + "ListOfRequestDetailForEmployee?RequestId=" + ID;
        Log.e("url",url);
        /////////////////////////////////////////
        RequestQueue r = Volley.newRequestQueue(ActivityOrderDetail.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("url",response);
                        pdialog.dismiss();
                        try {
                            JSONArray lists = new JSONArray(response);
                            for (int i = 0; i < lists.length(); i++) {
                                JSONObject reader = lists.getJSONObject(i);
                                String RequestId = reader.getString("RequestId");
                                String UserId = reader.getString("UserId");
                                String Lat = reader.getString("Lat");
                                String Long = reader.getString("Long");
                                String Address = reader.getString("Address");
                                String GarbageType = reader.getString("GarbageType");
                                String TotalAmount = reader.getString("TotalAmount");
                                String RequestStatus = reader.getString("RequestStatus");
                                String CreatedDate = reader.getString("CreatedDate");
                                String Name = reader.getString("Name");
                                String ContactNo = reader.getString("ContactNo");
                                String Date = reader.getString("Date");
                                String RequestDetailId = reader.getString("RequestDetailId");
                                String RequestDetailStatus = reader.getString("RequestDetailStatus");
                                String Time = reader.getString("Time");
                                list.add(new OrderDetailItem(RequestId,
                                        UserId,
                                        Lat,
                                        Long,
                                        Address,
                                        GarbageType,
                                        TotalAmount,
                                        RequestStatus,
                                        CreatedDate,
                                        Name,
                                        ContactNo,Date,
                                        RequestDetailId,
                                        RequestDetailStatus,
                                        Time));
                            }
                            listView.setAdapter(new OrderDetailAdapter(ActivityOrderDetail.this, list));
                        } catch (Exception e) {
                            Log.e("Error", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pdialog.dismiss();
            }
        });
        r.add(stringRequest);
    }
}
