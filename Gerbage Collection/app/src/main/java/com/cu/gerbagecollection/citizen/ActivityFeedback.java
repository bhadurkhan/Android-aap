package com.cu.gerbagecollection.citizen;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cu.gerbagecollection.R;
import com.cu.gerbagecollection.SignInActivity;
import com.cu.gerbagecollection.utils.AppContants;
import com.rengwuxian.materialedittext.MaterialEditText;

public class ActivityFeedback extends AppCompatActivity {
    MaterialEditText feedback;
    Button post;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedbackform);
        feedback = findViewById(R.id.feedback);
        post = findViewById(R.id.post);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (feedback.getText().length() > 2)
                    postData();
                else {
                    AlertDialog alertDialog = new AlertDialog.Builder(ActivityFeedback.this).create();
                    alertDialog.setTitle(getString(R.string.app_name));
                    alertDialog.setMessage("Please Provide Feedback");
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
            }
        });
    }

    void postData() {
        dialog = new ProgressDialog(ActivityFeedback.this);
        dialog.setMessage("Recording Feedback...");
        dialog.show();
        String feedbacktxt=feedback.getText().toString().replaceAll(" ", "%20");
        String url = AppContants.baseUrl + "InsertFeedbackByClient?FeedBack=" + feedbacktxt.replaceAll("\n", "%20")
                + "&UserId=" + SignInActivity.profileItem.getUserId();
        Log.e("URL", url);
        /////////////////////////////////////////
        RequestQueue r = Volley.newRequestQueue(ActivityFeedback.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.dismiss();
                            if (Integer.parseInt(response) > 0) {
                                AlertDialog alertDialog = new AlertDialog.Builder(ActivityFeedback.this).create();
                                alertDialog.setTitle(getString(R.string.app_name));
                                alertDialog.setMessage("Thanks for Feedback.");
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        dialog.dismiss();
                                    }
                                });
                                alertDialog.show();
                            }
                        } catch (Exception e) {
                            dialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        });
        r.add(stringRequest);
    }
}
