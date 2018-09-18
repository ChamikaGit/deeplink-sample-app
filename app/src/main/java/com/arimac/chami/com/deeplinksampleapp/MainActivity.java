package com.arimac.chami.com.deeplinksampleapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;

public class MainActivity extends AppCompatActivity {

    TextView tvVerificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvVerificationCode = findViewById(R.id.tv_deeplink);
    }

    @Override
    public void onStart() {
        super.onStart();

        try {
            Branch branch = Branch.getInstance();
            branch.initSession(new Branch.BranchReferralInitListener() {
                @Override
                public void onInitFinished(JSONObject referringParams, BranchError error) {

                    JSONObject sessionParams = Branch.getInstance().getLatestReferringParams();
                    Log.e("BranchLog",sessionParams.toString());
                    try {
                        String url = sessionParams.getString("~referring_link");
                        String[] partOne = url.split("//");
                        String[] partTwo = partOne[1].split("/");
                        String urlToken =partTwo[1];

                        if (urlToken != null) {
                            tvVerificationCode.setText(urlToken);
                        } else {
                            Toast.makeText(getApplicationContext(),"Verification Code Null",Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {

                        tvVerificationCode.setText(sessionParams.toString());
                    }
                }
            }, this.getIntent().getData(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);
    }
}

