package com.cu.gerbagecollection.citizen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

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
import com.cu.gerbagecollection.adapter.FeedbackAdapter;
import com.cu.gerbagecollection.model.FeebackItem;
import com.cu.gerbagecollection.utils.AppContants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FeedbackFragment extends Fragment {
    private ListView listView;
    private ProgressDialog pdialog;
    private ArrayList<FeebackItem> list;
    private ImageView add;
    private View views;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.fragment_feedback, container, false);
        listView = views.findViewById(R.id.list);
        add = views.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),ActivityFeedback.class));
            }
        });
        return views;
    }

    private void getData() {
        pdialog = new ProgressDialog(getActivity(), R.style.AlertDialogCustom);
        pdialog.setMessage("Loading...");
        pdialog.show();
        pdialog.show();
        ////////////////////////////
        list = new ArrayList<>();
        String url = AppContants.baseUrl + "ListofFeedbackForClient?UserId=" + SignInActivity.profileItem.getUserId();
        Log.e("url", url);
        /////////////////////////////////////////
        RequestQueue r = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("url", response);
                        pdialog.dismiss();
                        try {
                            JSONArray lists = new JSONArray(response);
                            for (int i = 0; i < lists.length(); i++) {
                                JSONObject reader = lists.getJSONObject(i);
                                String FeedbackId = reader.getString("FeedbackId");
                                String FeedbackBy = reader.getString("FeedbackBy");
                                String Feedback = reader.getString("Feedback");
                                String CreatedDate = reader.getString("CreatedDate");
                                list.add(new FeebackItem(FeedbackId,
                                        Feedback,
                                        FeedbackBy,
                                        CreatedDate));
                            }
                            listView.setAdapter(new FeedbackAdapter(getActivity(), list));
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

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }
}