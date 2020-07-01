package com.cu.gerbagecollection.citizen;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import com.cu.gerbagecollection.utils.AppContants;
import com.rengwuxian.materialedittext.MaterialEditText;

public class PassowrdFragment extends Fragment {
    private Button create;
    private MaterialEditText current, updated, confirm;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_passowrd, container, false);
        create = root.findViewById(R.id.name);
        current = root.findViewById(R.id.current);
        updated = root.findViewById(R.id.updated);
        confirm = root.findViewById(R.id.confirm);
        create = root.findViewById(R.id.register);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });
        return root;
    }
    private void updatePassword() {
        String url = AppContants.baseUrl + "ChangePasswordByAllTypeOfUser?NewPassword=" + updated.getText().toString().replaceAll(" ", "") +
                "&OldPassword=" + current.getText().toString().replaceAll(" ", "") +
                "&UserId=" + SignInActivity.profileItem.getUserId();
        Log.e("my URL: ", url);
        RequestQueue r = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("Response : ", response);
                            if (Integer.parseInt(response)>0) {
                                AlertDialog alertDialog = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom).create();
                                alertDialog.setTitle(R.string.app_name);
                                alertDialog.setMessage("Registered Successfully, Login & Move Forward.");
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        current.setText("");
                                        updated.setText("");
                                        confirm.setText("");
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
            }
        });
        r.add(stringRequest);
    }
}