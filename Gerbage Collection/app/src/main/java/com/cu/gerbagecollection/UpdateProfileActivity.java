package com.cu.gerbagecollection;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cu.gerbagecollection.utils.AppContants;
import com.rengwuxian.materialedittext.MaterialEditText;

public class UpdateProfileActivity extends AppCompatActivity {

    MaterialEditText name, phone;
    TextView email, role;
    Button create;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_update_citizen);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        role = findViewById(R.id.role);
        create = findViewById(R.id.register);
        name.setText(SignInActivity.profileItem.getName());
        phone.setText(SignInActivity.profileItem.getContactNo());
        email.setText(SignInActivity.profileItem.getEmail());
        role.setText(SignInActivity.profileItem.getRoleName());
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }

    private void signup() {
        progressDialog = new ProgressDialog(UpdateProfileActivity.this, R.style.AlertDialogCustom);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Updating Profile...");
        progressDialog.show();

        String nametext = name.getText().toString();
        String phonetext = phone.getText().toString();
        ///////////////////////////////////////////////////////////
        String url = AppContants.baseUrl + "AllTypeofUserUpdateProfile?Name=" + nametext.replaceAll(" ", "%20") +
                "&ContactNo=" + phonetext.replaceAll(" ", "")+
                "&UserId="+ SignInActivity.profileItem.getUserId();
        Log.e("URL", url);
        RequestQueue r = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            Log.e("URL: ", response);
                            if (Integer.parseInt(response)>0) {
                                AlertDialog alertDialog = new AlertDialog.Builder(UpdateProfileActivity.this, R.style.AlertDialogCustom).create();
                                alertDialog.setTitle(R.string.app_name);
                                alertDialog.setMessage("Profile Updated");
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
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