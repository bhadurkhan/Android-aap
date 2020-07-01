package com.cu.gerbagecollection.collector;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cu.gerbagecollection.R;
import com.cu.gerbagecollection.SignInActivity;
import com.cu.gerbagecollection.adapter.OrderAdapter;
import com.cu.gerbagecollection.model.OrderItem;
import com.cu.gerbagecollection.utils.AppContants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class CollectionFragment extends Fragment {
    View views;
    ListView listView;
    ArrayList<OrderItem> list;
    TextView from, to;
    ImageView search;
    private int mYear, mMonth, mDay;
    private ProgressDialog pdialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.fragment_collection, container, false);
        listView = views.findViewById(R.id.list);
        from = views.findViewById(R.id.from);
        to = views.findViewById(R.id.to);
        search = views.findViewById(R.id.search);
        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int yearTx,
                                                  int monthOfYear, int dayOfMonth) {
                                from.setText(yearTx + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int yearTx,
                                                  int monthOfYear, int dayOfMonth) {
                                to.setText(yearTx + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });
        return views;
    }

    void getData() {
        pdialog = new ProgressDialog(getActivity(), R.style.AlertDialogCustom);
        pdialog.setMessage("Loading...");
        pdialog.show();
        list = new ArrayList<>();
        String url = AppContants.baseUrl + "FromToReportofRequestForEmployee?UserId=" + SignInActivity.profileItem.getUserId() +
                "&DateFrom=" + from.getText().toString() +
                "&DateTo=" + to.getText().toString();
        Log.e("url",url);
        /////////////////////////////////////////
        RequestQueue r = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                                list.add(new OrderItem(RequestId,
                                        UserId,
                                        Lat,
                                        Long,
                                        Address,
                                        GarbageType,
                                        TotalAmount,
                                        RequestStatus,
                                        CreatedDate,
                                        Name,
                                        ContactNo));
                            }
                            listView.setAdapter(new OrderAdapter(getActivity(), list));
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