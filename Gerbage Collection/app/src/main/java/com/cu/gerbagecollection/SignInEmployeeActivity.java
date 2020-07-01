package com.cu.gerbagecollection;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cu.gerbagecollection.citizen.CitizenActivity;
import com.cu.gerbagecollection.collector.CollectorActivity;
import com.cu.gerbagecollection.model.ProfileItem;
import com.cu.gerbagecollection.utils.AppContants;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONObject;

public class SignInEmployeeActivity extends AppCompatActivity {
    Button loginbtn;
    MaterialEditText textEmailAddress, textPassword;
    ProgressDialog dialog;
    ///////////////////////////////
    CheckBox rememberMe;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor prefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_employee);
        loginbtn = findViewById(R.id.loginbtn);
        textEmailAddress = findViewById(R.id.editTextEmailAddress);
        textPassword = findViewById(R.id.editText_Password);
        rememberMe = findViewById(R.id.remember);
        ////////Initialize Prefrence///////////////
        sharedPreferences = getSharedPreferences("MySharedPrefs", MODE_PRIVATE);
        prefEditor = sharedPreferences.edit();
        //////////////////////////////////////////
        String username = sharedPreferences.getString("mail", "");
        String password = sharedPreferences.getString("password", "");
        if (username.length() > 0 && password.length() > 0)
            login(username, password);
        //////////////////////////////////////////
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailtxt = textEmailAddress.getText().toString();
                String passwordtext = textPassword.getText().toString();
                String error="";
                if(!AppContants.isValidCNIC(emailtxt))
                    error+="\nInvalid CNIC";
                if(passwordtext.length()<7)
                    error+="\nInvalid Password";
                if (AppContants.isValidCNIC(emailtxt) &&passwordtext.length() > 6){
                    if (rememberMe.isChecked()) {
                        prefEditor.putString("mail", textEmailAddress.getText().toString()); //Save user data
                        prefEditor.putString("password", textPassword.getText().toString()); //Save password data
                        prefEditor.apply();
                    }
                    ///////////////Get Data//////////////////
                    login(emailtxt, passwordtext);
                }
                else{
                    AlertDialog alertDialog = new AlertDialog.Builder(SignInEmployeeActivity.this, R.style.AlertDialogCustom).create();
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

    private void login(String mailTxt, String passwordTxt) {
        dialog = new ProgressDialog(SignInEmployeeActivity.this, R.style.AlertDialogCustom);
        dialog.setMessage("Loging In...");
        dialog.show();
        String url = AppContants.baseUrl + "AllTypeofUserLogin?Email=" + mailTxt.replace(" ", "%20") + "&Password=" + passwordTxt.replace(" ", "%20");
        Log.e("my URL: ", url);
        RequestQueue r = Volley.newRequestQueue(SignInEmployeeActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.dismiss();
                            Log.e("Response : ", response);
                            JSONObject reader = new JSONObject(response);
                            String UserId = reader.getString("UserId");
                            String Name = reader.getString("Name");
                            String Email = reader.getString("Email");
                            String Status = reader.getString("Status");
                            String RoleId = reader.getString("RoleId");
                            String Password = reader.getString("Password");
                            String RoleName = reader.getString("RoleName");
                            String ContactNo = reader.getString("ContactNo");
                            String Available = reader.getString("Available");
                            SignInActivity.profileItem = new ProfileItem(UserId,
                                    Name,
                                    Email,
                                    Password,
                                    Status,
                                    RoleId,
                                    RoleName,
                                    ContactNo,
                                    Available);
                            if (RoleId.equals("3")) {
                                startActivity(new Intent(SignInEmployeeActivity.this, CitizenActivity.class));
                                finish();
                            }
                            else if (RoleId.equals("2")) {
                                startActivity(new Intent(SignInEmployeeActivity.this, CollectorActivity.class));
                                finish();
                            }
                            else {
                                showMsg();
                            }
                        } catch (Exception e) {
                            Toast.makeText(SignInEmployeeActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
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

    void showMsg() {
        AlertDialog alertDialog = new AlertDialog.Builder(SignInEmployeeActivity.this, R.style.AlertDialogCustom).create();
        alertDialog.setTitle(R.string.app_name);
        alertDialog.setMessage("Invalid Credentitials");
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
