package com.cu.gerbagecollection.citizen;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cu.gerbagecollection.R;
import com.cu.gerbagecollection.SignInActivity;
import com.cu.gerbagecollection.model.DateTimeItem;
import com.cu.gerbagecollection.utils.AppContants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ScheduleFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    Calendar c = Calendar.getInstance();
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    ProgressDialog dialog;
    ArrayList<DateTimeItem> dateTimeItems;
    //////////////////////////////////////////
    private TextView day_a, day_b, day_c, day_d, day_e, day_f, day_g;
    private TextView time_a, time_b, time_c, time_d, time_e, time_f, time_g;
    private TextView charges;
    private ImageView card;
    private EditText location;
    private Spinner type;
    private Button post;
    private int rate = 0, totalbill = 0;
    private View root;
    private int mYear, mMonth, mDay;
    private int mHr, mMin, mSec;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_schedule, container, false);
        initView();
        ////////////////////////////////////////////////////
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHr = c.get(Calendar.HOUR_OF_DAY);
        mMin = c.get(Calendar.MINUTE);
        mSec = c.get(Calendar.SECOND);
        ////////////////////////////////////////////////////
        return root;
    }

    private void initView() {
        card = root.findViewById(R.id.card);
        type = root.findViewById(R.id.type);
        location = root.findViewById(R.id.location);
        charges = root.findViewById(R.id.charges);
        day_a = root.findViewById(R.id.day_a);
        day_b = root.findViewById(R.id.day_b);
        day_c = root.findViewById(R.id.day_c);
        day_d = root.findViewById(R.id.day_d);
        day_e = root.findViewById(R.id.day_e);
        day_f = root.findViewById(R.id.day_f);
        day_g = root.findViewById(R.id.day_g);
        time_a = root.findViewById(R.id.time_a);
        time_b = root.findViewById(R.id.time_b);
        time_c = root.findViewById(R.id.time_c);
        time_d = root.findViewById(R.id.time_d);
        time_e = root.findViewById(R.id.time_e);
        time_f = root.findViewById(R.id.time_f);
        time_g = root.findViewById(R.id.time_g);
        post = root.findViewById(R.id.post);
        type.setSelection(0,false);
        type.setOnItemSelectedListener(this);
        card.setOnClickListener(this);
        day_a.setOnClickListener(this);
        day_b.setOnClickListener(this);
        day_c.setOnClickListener(this);
        day_d.setOnClickListener(this);
        day_e.setOnClickListener(this);
        day_f.setOnClickListener(this);
        day_g.setOnClickListener(this);
        time_a.setOnClickListener(this);
        time_b.setOnClickListener(this);
        time_c.setOnClickListener(this);
        time_d.setOnClickListener(this);
        time_e.setOnClickListener(this);
        time_f.setOnClickListener(this);
        time_g.setOnClickListener(this);
        post.setOnClickListener(this);
        card.setVisibility(View.INVISIBLE);
        location.setText(getAddress(CitizenActivity.clat,CitizenActivity.clng));
    }

    @Override
    public void onClick(View view) {
        if (!type.getSelectedItem().toString().equals("Select Request Type")) {
            switch (view.getId()) {
                case R.id.card:
                    if (Integer.parseInt(charges.getText().toString()) > 0) {
                        Intent intent = new Intent(getActivity(), PaymentActivity.class);
                        intent.putExtra("charges", charges.getText().toString());
                        startActivity(intent);
                    }
                    break;
                case R.id.post:
                    if ((!day_a.getText().toString().contains("Day") ||
                            !day_b.getText().toString().contains("Day") ||
                            !day_c.getText().toString().contains("Day") ||
                            !day_d.getText().toString().contains("Day") ||
                            !day_e.getText().toString().contains("Day") ||
                            !day_f.getText().toString().contains("Day") ||
                            !day_g.getText().toString().contains("Day"))
                            && location.getText().toString().length() > 0)
                        postRequest();
                    else {
                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom).create();
                        alertDialog.setTitle(R.string.app_name);
                        alertDialog.setMessage("Incomplete Request");
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    }
                    break;
                case R.id.day_a:
                    datePickerDialog = new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int yearTx,
                                                      int monthOfYear, int dayOfMonth) {
                                    day_a.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + yearTx);
                                    totalbill += rate;
                                    charges.setText(String.valueOf(totalbill));
                                    time_a.setText("08:00:00");
                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    datePickerDialog.show();
                    break;
                case R.id.day_b:
                    datePickerDialog = new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int yearTx,
                                                      int monthOfYear, int dayOfMonth) {
                                    day_b.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + yearTx);
                                    totalbill += rate;
                                    charges.setText(String.valueOf(totalbill));
                                    time_b.setText("08:00:00");
                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    datePickerDialog.show();
                    break;
                case R.id.day_c:
                    datePickerDialog = new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int yearTx,
                                                      int monthOfYear, int dayOfMonth) {
                                    day_c.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + yearTx);
                                    totalbill += rate;
                                    charges.setText(String.valueOf(totalbill));
                                    time_c.setText("08:00:00");
                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    datePickerDialog.show();
                    break;
                case R.id.day_d:
                    datePickerDialog = new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int yearTx,
                                                      int monthOfYear, int dayOfMonth) {
                                    day_d.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + yearTx);
                                    totalbill += rate;
                                    charges.setText(String.valueOf(totalbill));
                                    time_d.setText("08:00:00");
                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    datePickerDialog.show();
                    break;
                case R.id.day_e:
                    datePickerDialog = new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int yearTx,
                                                      int monthOfYear, int dayOfMonth) {
                                    day_e.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + yearTx);
                                    totalbill += rate;
                                    charges.setText(String.valueOf(totalbill));
                                    time_e.setText("08:00:00");
                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    datePickerDialog.show();
                    break;
                case R.id.day_f:
                    datePickerDialog = new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int yearTx,
                                                      int monthOfYear, int dayOfMonth) {
                                    day_f.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + yearTx);
                                    totalbill += rate;
                                    charges.setText(String.valueOf(totalbill));
                                    time_f.setText("08:00:00");
                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    datePickerDialog.show();
                    break;
                case R.id.day_g:
                    datePickerDialog = new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int yearTx,
                                                      int monthOfYear, int dayOfMonth) {
                                    day_g.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + yearTx);
                                    totalbill += rate;
                                    charges.setText(String.valueOf(totalbill));
                                    time_g.setText("08:00:00");
                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    datePickerDialog.show();
                    break;
                case R.id.time_a:
                    timePickerDialog = new TimePickerDialog(getActivity(),
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                    time_a.setText(sHour + ":" + sMinute + ":" + "00");
                                }
                            }, mHr, mMin, true);
                    timePickerDialog.show();
                    break;
                case R.id.time_b:
                    timePickerDialog = new TimePickerDialog(getActivity(),
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                    time_b.setText(sHour + ":" + sMinute + ":" + "00");
                                }
                            }, mHr, mMin, true);
                    timePickerDialog.show();
                    break;
                case R.id.time_c:
                    timePickerDialog = new TimePickerDialog(getActivity(),
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                    time_c.setText(sHour + ":" + sMinute + ":" + "00");
                                }
                            }, mHr, mMin, true);
                    timePickerDialog.show();
                    break;
                case R.id.time_d:
                    timePickerDialog = new TimePickerDialog(getActivity(),
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                    time_d.setText(sHour + ":" + sMinute + ":" + "00");
                                }
                            }, mHr, mMin, true);
                    timePickerDialog.show();
                    break;
                case R.id.time_e:
                    timePickerDialog = new TimePickerDialog(getActivity(),
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                    time_e.setText(sHour + ":" + sMinute + ":" + "00");
                                }
                            }, mHr, mMin, true);
                    timePickerDialog.show();
                    break;
                case R.id.time_f:
                    timePickerDialog = new TimePickerDialog(getActivity(),
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                    time_f.setText(sHour + ":" + sMinute + ":" + "00");
                                }
                            }, mHr, mMin, true);
                    timePickerDialog.show();
                    break;
                case R.id.time_g:
                    timePickerDialog = new TimePickerDialog(getActivity(),
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                    time_g.setText(sHour + ":" + sMinute + ":" + "00");
                                }
                            }, mHr, mMin, true);
                    timePickerDialog.show();
                    break;
            }
        }
        else{
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom).create();
            alertDialog.setTitle(R.string.app_name);
            alertDialog.setMessage("Select Request Type");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        getRateData(type.getSelectedItem().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void getRateData(final String selected) {
        dialog = new ProgressDialog(getActivity(), R.style.AlertDialogCustom);
        dialog.setMessage("Getting Rates...");
        dialog.show();
        String url = AppContants.baseUrl + "ListofRateForClient";
        Log.e("my URL: ", url);
        RequestQueue r = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        Log.e("Response : ", response);
                        try {
                            JSONArray list = new JSONArray(response);
                            for (int k = 0; k < list.length(); k++) {
                                JSONObject reader = list.getJSONObject(k);
                                String Title = reader.getString("Title");
                                if (Title.equals(selected)) {
                                    rate = (int) Double.parseDouble(reader.getString("Rate"));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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

    private void postRequest() {
        dialog = new ProgressDialog(getActivity(), R.style.AlertDialogCustom);
        dialog.setMessage("Posting Schedule...");
        dialog.show();
        String url = AppContants.baseUrl + "InsertRequestByClient";
        /////////////////////////////////////////
        getSchedule();
        /////////////////////////////////////////
        JSONArray array = new JSONArray();
        JSONObject jsonObject=new JSONObject();
        try {
            for (int i = 0; i < dateTimeItems.size(); i++) {
                JSONObject obj = new JSONObject();
                obj.put("Lat", String.valueOf(CitizenActivity.clat));
                obj.put("Long", String.valueOf(CitizenActivity.clng));
                obj.put("TotalAmount", String.valueOf(totalbill));
                obj.put("UserId", String.valueOf(SignInActivity.profileItem.getUserId()));
                obj.put("Address", location.getText().toString());
                obj.put("GarbageType", type.getSelectedItem().toString());
                obj.put("Time", dateTimeItems.get(i).getTime());
                obj.put("Date", dateTimeItems.get(i).getDate());
                array.put(obj);
            }
            jsonObject.put("CleintRequestDetailList",array);
//            url+="?CleintRequestDetailList="+array;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ////////////////////////////////////////
        Log.e("my URL: ", url);
        RequestQueue r = Volley.newRequestQueue(getActivity());
        JsonObjectRequest stringRequest = new JsonObjectRequest (Request.Method.POST, url,jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.dismiss();
                        Log.e("Response : ", response.toString());
                        msg();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                msg();
                Log.e("Response Error : ", error.toString());
            }
        });
        r.add(stringRequest);
    }

    private void getSchedule() {
        dateTimeItems = new ArrayList<>();
        for (int k = 0; k < 7; k++) {
            switch (k) {
                case 0:
                    if (!day_a.getText().toString().contains("Day"))
                        dateTimeItems.add(new DateTimeItem(day_a.getText().toString(), time_a.getText().toString()));
                    break;
                case 1:
                    if (!day_b.getText().toString().contains("Day"))
                        dateTimeItems.add(new DateTimeItem(day_b.getText().toString(), time_b.getText().toString()));
                    break;
                case 2:
                    if (!day_c.getText().toString().contains("Day"))
                        dateTimeItems.add(new DateTimeItem(day_c.getText().toString(), time_c.getText().toString()));
                    break;
                case 3:
                    if (!day_d.getText().toString().contains("Day"))
                        dateTimeItems.add(new DateTimeItem(day_d.getText().toString(), time_d.getText().toString()));
                    break;
                case 4:
                    if (!day_e.getText().toString().contains("Day"))
                        dateTimeItems.add(new DateTimeItem(day_e.getText().toString(), time_e.getText().toString()));
                    break;
                case 5:
                    if (!day_f.getText().toString().contains("Day"))
                        dateTimeItems.add(new DateTimeItem(day_f.getText().toString(), time_f.getText().toString()));
                    break;
                case 6:
                    if (!day_g.getText().toString().contains("Day"))
                        dateTimeItems.add(new DateTimeItem(day_g.getText().toString(), time_g.getText().toString()));
                    break;
            }
        }
    }
    private String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                result.append(address.getAddressLine(0));
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return result.toString();
    }

    private void msg(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom);
        alertDialog.setTitle(R.string.app_name);
        alertDialog.setMessage("Schedule Posted.");
        alertDialog.setPositiveButton("Pay Now", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                FragmentTransaction fragmentTransaction;
                Fragment fragment;
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragment = new MyRequestFragment();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.commit();
                dialog.dismiss();
            }
        });
        alertDialog.setNegativeButton("Pay Later", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.create().show();
    }
}