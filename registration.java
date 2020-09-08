package riss.com.blindapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class registration extends AppCompatActivity implements JsonResponse {
    EditText e1, e2, e3, e4, e5, e6;
    Button b1;
    String imei;
    public static String cph;
    String bfname, blname, bphone, cfname, clname, cphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        TextView marque = (TextView) this.findViewById(R.id.marque);
        marque.setSelected(true);
        e1 = (EditText) findViewById(R.id.etcfirstname);
        e2 = (EditText) findViewById(R.id.etclastname);
        e3 = (EditText) findViewById(R.id.etcphnnum);
        e4 = (EditText) findViewById(R.id.etbfirstname);
        e5 = (EditText) findViewById(R.id.etblastname);
        e6 = (EditText) findViewById(R.id.etbphnnum);
        b1 = (Button) findViewById(R.id.btregister);

        TelephonyManager tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        imei = tel.getDeviceId();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cfname = e1.getText().toString();
                clname = e2.getText().toString();
                cphone = e3.getText().toString();
                bfname = e4.getText().toString();
                blname = e5.getText().toString();
                bphone = e6.getText().toString();

                if (cfname.equalsIgnoreCase("")) {
                    e1.setError("please fill this field");
                    e1.setFocusable(true);
                } else if (clname.equalsIgnoreCase("")) {
                    e2.setError("please fill this field");
                    e2.setFocusable(true);
                } else if (cphone.equalsIgnoreCase("")) {
                    e3.setError("please fill this field");
                    e3.setFocusable(true);
                } else if (!Patterns.PHONE.matcher(cphone).matches()) {
                    e3.setError("please enter valid phone number");
                    e3.setFocusable(true);
                } else if (cphone.length() != 10) {
                    e3.setError("please enter valid phone number");
                    e3.setFocusable(true);
                } else if (bfname.equalsIgnoreCase("")) {
                    e4.setError("please fill this field");
                    e4.setFocusable(true);
                } else if (blname.equalsIgnoreCase("")) {
                    e5.setError("please fill this field");
                    e5.setFocusable(true);
                } else if (bphone.equalsIgnoreCase("")) {
                    e6.setError("please fill this field");
                    e6.setFocusable(true);
                } else if (!Patterns.PHONE.matcher(cphone).matches()) {
                    e6.setError("please enter valid phone number");
                    e6.setFocusable(true);
                } else if (cphone.length() != 10) {
                    e6.setError("please enter valid phone number");
                    e6.setFocusable(true);
                } else {
                    JsonReq JR = new JsonReq(getApplicationContext());
                    JR.json_response = (JsonResponse) registration.this;
                    String q = "/register/?cfirst_name=" + cfname + "&clast_name=" + clname + "&cphone_no=" + cphone + "&bfirst_name=" + bfname + "&blast_name=" + blname + "&bphone_no=" + bphone + "&imei=" + imei;
                    // q=q.replace(" ", "%20");
                    JR.execute(q);
//                Log.d("pearl",q);
                }
            }
        });
    }

    @Override
    public void response(JSONObject jo) {
        try {
            String status = jo.getString("status");
            Log.d("pearl", status);

            if (status.equalsIgnoreCase("success")) {
//                JSONArray ja=(JSONArray)jo.getJSONArray("result");
//                cph=ja.getJSONObject(0).getString("cphone_no");
                Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), Home.class));
            } else {
                Toast.makeText(getApplicationContext(), "Registration failed.TRY AGAIN!!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), registration.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Connection problem", Toast.LENGTH_LONG).show();
        }
    }
}