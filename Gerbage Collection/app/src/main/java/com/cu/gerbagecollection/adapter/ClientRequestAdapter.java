package com.cu.gerbagecollection.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cu.gerbagecollection.R;
import com.cu.gerbagecollection.citizen.ActivityRequestDetail;
import com.cu.gerbagecollection.citizen.PaymentActivity;
import com.cu.gerbagecollection.model.OrderItem;

import java.util.ArrayList;

public class ClientRequestAdapter extends ArrayAdapter {
    private ArrayList<OrderItem> arrayList;
    private Context ctx;
    OrderItem item;

    public ClientRequestAdapter(Context context, ArrayList<OrderItem> arrayList) {
        super(context, R.layout.order_item, arrayList);
        this.arrayList = arrayList;
        ctx = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.order_item, null);
        }
        TextView tracking_id = convertView.findViewById(R.id.tracking_id);
        TextView address = convertView.findViewById(R.id.address);
        TextView status = convertView.findViewById(R.id.status);
        TextView customer = convertView.findViewById(R.id.customer);
        ImageView call = convertView.findViewById(R.id.call);
        ImageView map = convertView.findViewById(R.id.map);
        ImageView update = convertView.findViewById(R.id.update);
        TextView payment = convertView.findViewById(R.id.payment);
        item = arrayList.get(position);
        tracking_id.setText("Tracking ID " + item.getRequestId());
        customer.setText(item.getName() + "\nContact No: " +
                item.getContactNo());
        address.setText(item.getAddress());
        payment.setText(item.getTotalAmount());
        status.setText(item.getRequestStatus());
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, ActivityRequestDetail.class);
                intent.putExtra("id", item.getRequestId());
                ctx.startActivity(intent);
            }
        });
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("geo:0,0?q=" + item.getLat() + "," + item.getLong() + "(" + item.getAddress() + ")"));
                ctx.startActivity(intent);
            }
        });
        if (item.getRequestStatus().equals("Pending")) {
            call.setVisibility(View.VISIBLE);
        } else {
            call.setVisibility(View.INVISIBLE);
        }
        call.setImageResource(R.drawable.id_card);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, PaymentActivity.class);
                intent.putExtra("charges", item.getTotalAmount());
                intent.putExtra("id", item.getRequestId());
                ctx.startActivity(intent);
            }
        });
        return convertView;
    }
}
