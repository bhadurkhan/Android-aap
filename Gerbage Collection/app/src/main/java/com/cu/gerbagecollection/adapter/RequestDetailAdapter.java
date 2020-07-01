package com.cu.gerbagecollection.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cu.gerbagecollection.R;
import com.cu.gerbagecollection.citizen.HomeFragment;
import com.cu.gerbagecollection.citizen.MyRequestFragment;
import com.cu.gerbagecollection.model.OrderDetailItem;
import com.cu.gerbagecollection.utils.AppContants;

import java.util.ArrayList;

public class RequestDetailAdapter extends ArrayAdapter {
    private ArrayList<OrderDetailItem> arrayList;
    private Context ctx;
    OrderDetailItem item;
    private ProgressDialog progressDialog;

    public RequestDetailAdapter(Context context, ArrayList<OrderDetailItem> arrayList) {
        super(context, R.layout.order_detail_item, arrayList);
        this.arrayList = arrayList;
        ctx = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.order_detail_item, null);
        }
        TextView tracking_id = convertView.findViewById(R.id.tracking_id);
        TextView address = convertView.findViewById(R.id.address);
        TextView status = convertView.findViewById(R.id.status);
        TextView customer = convertView.findViewById(R.id.customer);
        TextView datetime = convertView.findViewById(R.id.datetime);
        ImageView call = convertView.findViewById(R.id.call);
        ImageView map = convertView.findViewById(R.id.map);
        ImageView update = convertView.findViewById(R.id.update);
        TextView payment = convertView.findViewById(R.id.payment);
        item = arrayList.get(position);
        tracking_id.setText("Tracking ID " + item.getRequestDetailId());
        customer.setText(item.getName() + "\nContact No: " +
                item.getContactNo());
        address.setText(item.getAddress());
        payment.setText(item.getTotalAmount());
        status.setText(item.getRequestStatus());
        datetime.setText(item.getDate() + " " + item.getTime());
        map.setVisibility(View.INVISIBLE);
        call.setVisibility(View.INVISIBLE);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getRequestStatus().equals("Collected")) {
                    closeRequest(item.getRequestDetailId());
                } else {
                    Toast.makeText(ctx, "Only Collected Request can be confirmed.", Toast.LENGTH_LONG).show();
                }
            }
        });
        return convertView;
    }

    private void closeRequest(String Id) {
        progressDialog = new ProgressDialog(ctx, R.style.AlertDialogCustom);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Confirmation...");
        progressDialog.show();
        ///////////////////////////////////////////////////////////
        String url = AppContants.baseUrl + "CloseRequestAfterCollectedByClient?RequestId=" + Id;
        Log.e("URL", url);
        RequestQueue r = Volley.newRequestQueue(ctx);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            Log.e("URL: ", response);
                            if (Integer.parseInt(response)>0) {
                                AlertDialog alertDialog = new AlertDialog.Builder(ctx, R.style.AlertDialogCustom).create();
                                alertDialog.setTitle(R.string.app_name);
                                alertDialog.setMessage("Thank you for confirmation.");
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        ((AppCompatActivity) ctx).finish();
                                        dialog.dismiss();
                                    }
                                });
                                alertDialog.show();
                            }
                        } catch (Exception e) {
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        });
        r.add(stringRequest);
    }
}

