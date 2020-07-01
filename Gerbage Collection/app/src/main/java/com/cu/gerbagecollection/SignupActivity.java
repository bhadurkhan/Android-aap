package com.cu.gerbagecollection;

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
import com.cu.gerbagecollection.utils.AppContants;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignupActivity extends AppCompatActivity {

    MaterialEditText name, phone, email, password, repassword;
    Button create;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_register);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        password = findViewById(R.id.passowrd);
        repassword = findViewById(R.id.repassowrd);
        create = findViewById(R.id.register);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nametext = name.getText().toString();
                String phonetext = phone.getText().toString();
                String emailtxt = email.getText().toString();
                String passwordtext = password.getText().toString();
                String repasswordtext = repassword.getText().toString();
                String error="";
                if(nametext.length()<1)
                    error+="\nInvalid Name";
                if(!AppContants.isPhoneNumberValid(phonetext))
                    error+="\nInvalid Contact Number";
                if(!AppContants.isValidMail(emailtxt))
                    error+="\nInvalid Email";
                if(passwordtext.length()<7)
                    error+="\nInvalid Password";
                if(!passwordtext.equals(repasswordtext))
                    error+="\nPassword & Re-Password Must Same";
                if (nametext.length() > 1 &&
                        AppContants.isPhoneNumberValid(phonetext) &&
                        AppContants.isValidMail(emailtxt) &&
                        passwordtext.equals(repasswordtext) &&
                        passwordtext.length() > 6)
                    signup();
                else{
                    AlertDialog alertDialog = new AlertDialog.Builder(SignupActivity.this, R.style.AlertDialogCustom).create();
                    alertDialog.setTitle(R.string.app_name);
                    alertDialog.setMessage(error);
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

    private void signup() {
        progressDialog = new ProgressDialog(SignupActivity.this, R.style.AlertDialogCustom);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String nametext = name.getText().toString();
        String phonetext = phone.getText().toString();
        String emailtxt = email.getText().toString();
        String passwordtext = password.getText().toString();
        ///////////////////////////////////////////////////////////
        String url = AppContants.baseUrl + "ClientRegistration?Name=" + nametext.replaceAll(" ", "%20") +
                "&Email=" + emailtxt.replaceAll(" ", "") +
                "&Password=" + passwordtext.replaceAll(" ", "") +
                "&ContactNo=" + phonetext.replaceAll(" ", "");
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
                                AlertDialog alertDialog = new AlertDialog.Builder(SignupActivity.this, R.style.AlertDialogCustom).create();
                                alertDialog.setTitle(R.string.app_name);
                                alertDialog.setMessage("Registered Successfully, Login & Move Forward.");
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