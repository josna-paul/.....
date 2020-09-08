package riss.com.blindapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

public class EditCareTaker extends AppCompatActivity implements JsonResponse {

    String imei;
    EditText ed_fname, ed_lname, ed_phone;
    Button bt_update;
    SharedPreferences sh;
    String fname = "", lname = "", phone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_care_taker);

        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        TelephonyManager tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        imei = tel.getDeviceId();

        ed_fname = findViewById(R.id.ed_cfirstname);
        ed_lname = findViewById(R.id.ed_clastname);
        ed_phone = findViewById(R.id.ed_cphnnum);
        bt_update = findViewById(R.id.bt_update);

        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fname = ed_fname.getText().toString();
                lname = ed_lname.getText().toString();
                phone = ed_phone.getText().toString();

                if (fname.equals("")) {
                    ed_fname.setError("Please fill the field");
                    ed_fname.setFocusable(true);
                } else if (lname.equals("")) {
                    ed_lname.setError("Please fill the field");
                    ed_lname.setFocusable(true);
                } else if (phone.length() != 10) {
                    ed_phone.setError("Please enter valid number");
                    ed_phone.setFocusable(true);
                } else {
                    SharedPreferences.Editor ed = sh.edit();
                    ed.putString("emergency_no", phone);
                    ed.commit();

                    JsonReq JR = new JsonReq(getApplicationContext());
                    JR.json_response = (JsonResponse) EditCareTaker.this;
                    String q = "/update/?cfirst_name=" + fname + "&clast_name=" + lname + "&cphone_no=" + phone + "&imei=" + imei;
                    // q=q.replace(" ", "%20");
                    JR.execute(q);
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
                SharedPreferences.Editor ed = sh.edit();
                ed.putString("emergency_no", phone);
                ed.commit();
                Toast.makeText(getApplicationContext(), "Updated successful", Toast.LENGTH_LONG).show();
            }
            startActivity(new Intent(getApplicationContext(), Home.class));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Connection problem", Toast.LENGTH_LONG).show();
        }
    }
}
