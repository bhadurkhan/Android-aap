package com.cu.gerbagecollection.citizen;

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
import com.cu.gerbagecollection.R;
import com.cu.gerbagecollection.utils.AppContants;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PaymentActivity extends AppCompatActivity {

    private TextView charges, card_holder_expiry;
    private Button post;
    MaterialEditText card_holder_name, card_holder_number, card_holder_cvv, card_holder_postal, card_holder_mobile;
    private ProgressDialog progressDialog;
    private String requestID;
    Calendar today = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_payment);
        /////////////////////////////////////////////
        card_holder_expiry = findViewById(R.id.card_holder_expiry);
        card_holder_name = findViewById(R.id.card_holder_name);
        card_holder_number = findViewById(R.id.card_holder_number);
        card_holder_cvv = findViewById(R.id.card_holder_cvv);
        card_holder_postal = findViewById(R.id.card_holder_postal);
        card_holder_mobile = findViewById(R.id.card_holder_mobile);
        charges = findViewById(R.id.charges);
        post = findViewById(R.id.post);
        charges.setText(getIntent().getStringExtra("charges"));
        requestID = getIntent().getStringExtra("id");
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCardDetail();
            }
        });
        card_holder_expiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectExpiryDate();
            }
        });
        /////////////////////////////////////////////
    }

    private void checkCardDetail() {
        String nametxt = card_holder_name.getText().toString();
        String cardnumbertxt = card_holder_number.getText().toString();
        String expirytxt = card_holder_expiry.getText().toString();
        String mobiletxt = card_holder_mobile.getText().toString();
        String cvctxt = card_holder_cvv.getText().toString();
        String postaltxt = card_holder_postal.getText().toString();

        if (nametxt.length() > 3
                && cardnumbertxt.length() == 16
                && !expirytxt.equals("Card Expiry Date")
                && cvctxt.length() == 3
                && postaltxt.length() > 3
                && AppContants.isPhoneNumberValid(mobiletxt))
            payRequest();
        else {
            String error = "";
            if (postaltxt.length() <= 3)
                error += "\nInvalid Postal Code";
            if (nametxt.length() <= 3)
                error += "\nInvalid Cardholder Name";
            if (cardnumbertxt.length() != 16)
                error += "\nInvalid Card Number";
            if (expirytxt.equals("Card Expiry Date"))
                error += "\nInvalid Expiry Date";
            if (cvctxt.length() != 3)
                error += "\nInvalid CVV";
            if (!AppContants.isPhoneNumberValid(mobiletxt))
                error += "\nInvalid Mobile Number";

            AlertDialog alertDialog = new AlertDialog.Builder(PaymentActivity.this, R.style.AlertDialogCustom).create();
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

    //    @Override
//    public void onCardFormSubmit() {
//        if (mCardForm.isValid()) {
//            Toast.makeText(this, R.string.valid, Toast.LENGTH_SHORT).show();
//            check=true;
//        } else {
//            mCardForm.validate();
//            Toast.makeText(this, R.string.invalid, Toast.LENGTH_SHORT).show();
//            check=false;
//        }
//    }
    private void payRequest() {
        progressDialog = new ProgressDialog(PaymentActivity.this, R.style.AlertDialogCustom);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Paying...");
        progressDialog.show();
        ///////////////////////////////////////////////////////////
        String url = AppContants.baseUrl + "ConfirmRequestAfterPayementByClient?RequestId=" + requestID;
        Log.e("URL", url);
        RequestQueue r = Volley.newRequestQueue(PaymentActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            Log.e("URL: ", response);
                            if (Integer.parseInt(response) > 0) {
                                AlertDialog alertDialog = new AlertDialog.Builder(PaymentActivity.this, R.style.AlertDialogCustom).create();
                                alertDialog.setTitle(R.string.app_name);
                                alertDialog.setMessage("Paid, Our Collector will collect Garbage on Schedule Timing and Location.");
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        dialog.dismiss();
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

    private void selectExpiryDate() {
        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(PaymentActivity.this,
                new MonthPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int selectedMonth, int selectedYear) {
                        try {
                            if (new SimpleDateFormat("MM/yyyy").parse((selectedMonth +1) +"/" + selectedYear).before(new Date())) {
                                card_holder_expiry.setText("Card Expiry Date");
                            }else
                                card_holder_expiry.setText((selectedMonth +1) +"/" + selectedYear);
                        } catch (ParseException exception) {
                        }
                    }
                }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));

        builder.setTitle("Select Card Expiry Date")
                .setMinYear(today.get(Calendar.YEAR))
                .setMaxYear(today.get(Calendar.YEAR) + 10)
//                .setMonthAndYearRange(today.get(Calendar.MONTH), today.get(Calendar.MONTH),
//                        today.get(Calendar.YEAR), today.get(Calendar.YEAR) + 10)
                .build()
                .show();

    }
}
