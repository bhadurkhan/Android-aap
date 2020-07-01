package com.cu.gerbagecollection.collector;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.cu.gerbagecollection.UpdateProfileActivity;
import com.cu.gerbagecollection.model.ProfileItem;
import com.cu.gerbagecollection.utils.AppContants;

import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class HomeCFragment extends Fragment {
    private TextView name, email, phone,role;
    private ImageView update,logout;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor prefEditor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chome, container, false);
        name = root.findViewById(R.id.name);
        email = root.findViewById(R.id.email);
        phone = root.findViewById(R.id.phone);
        role = root.findViewById(R.id.role);
        update = root.findViewById(R.id.update);
        logout = root.findViewById(R.id.logout);
        ////////Initialize Prefrence///////////////
        sharedPreferences = getActivity().getSharedPreferences("MySharedPrefs", MODE_PRIVATE);
        prefEditor = sharedPreferences.edit();
        //////////////////////////////////////////
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UpdateProfileActivity.class));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom);
                alertDialog.setTitle(R.string.app_name);
                alertDialog.setMessage("Are you sure to logout?");
                alertDialog.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        prefEditor.putString("mail", ""); //Save user data
                        prefEditor.putString("password", ""); //Save password data
                        prefEditor.apply();
                        dialog.dismiss();
                        getActivity().finish();
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
        getProfileData();
        return root;
    }
    @Override
    public void onResume() {
        super.onResume();
        getProfileData();
    }
    private void getProfileData() {
        String url = AppContants.baseUrl + "AllTypeofUserProfile?UserId=" + SignInActivity.profileItem.getUserId();
        Log.e("my URL: ", url);
        RequestQueue r = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
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
                            name.setText(Name);
                            phone.setText(ContactNo);
                            email.setText(Email);
                            role.setText(RoleName);
                        } catch (Exception e) {
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        r.add(stringRequest);

    }
}