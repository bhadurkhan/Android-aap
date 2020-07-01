package com.cu.gerbagecollection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cu.gerbagecollection.R;
import com.cu.gerbagecollection.model.FeebackItem;

import java.util.ArrayList;

public class FeedbackAdapter extends ArrayAdapter {
    private ArrayList<FeebackItem> arrayList;
    private Context ctx;
    private FeebackItem item;

    public FeedbackAdapter(Context context, ArrayList<FeebackItem> arrayList) {
        super(context, R.layout.faadback_item, arrayList);
        this.arrayList = arrayList;
        ctx = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.faadback_item, null);
        }
        TextView feedback = convertView.findViewById(R.id.feedback);
        TextView datetime = convertView.findViewById(R.id.datetime);
        item = arrayList.get(position);
        feedback.setText(item.getFeedback());
        datetime.setText(item.getCreatedDate());
        return convertView;
    }
}
