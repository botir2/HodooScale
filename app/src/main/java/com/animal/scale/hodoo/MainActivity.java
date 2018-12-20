package com.animal.scale.hodoo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.animal.scale.hodoo.activity.home.fragment.welcome.WelcomeFirstFragment;
import com.animal.scale.hodoo.activity.home.fragment.welcome.WelcomeHomeFragment;
import com.animal.scale.hodoo.activity.home.fragment.welcome.WelcomeSecondFragment;
import com.animal.scale.hodoo.activity.home.fragment.welcome.WelcomeThirdFragment;
import com.animal.scale.hodoo.activity.user.login.LoginActivity;
import com.animal.scale.hodoo.activity.user.signup.SignUpActivity;
import com.animal.scale.hodoo.activity.user.signup.SignUpIn;
import com.animal.scale.hodoo.common.SharedPrefVariable;
import com.animal.scale.hodoo.custom.view.WelcomeViewPager;
import com.animal.scale.hodoo.util.CheckConnect;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import butterknife.ButterKnife;
import butterknife.OnClick;
import lombok.NonNull;

public class
MainActivity extends AppCompatActivity {

    private ProgressBar bar;

    private static final String CONNECTION_CONFIRM_CLIENT_URL = "http://clients3.google.com/generate_204";

    public String store_version;

    public String device_version;

    private WelcomeViewPager mSlideView;

    Intent intent;

    private boolean isCreated = false;
    private boolean logoutState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logoutState = getIntent().getBooleanExtra(SharedPrefVariable.LOGOUT_INTENT_KEY, false);
        //FirebaseInstanceId.getInstance().getToken();
        /*if (FirebaseInstanceId.getInstance().getToken() != null) {
            Log.e("HJLEE", "token = " + FirebaseInstanceId.getInstance().getToken());
        } else {
            Log.e("HJLEE", "asdasdasds");
        }*/
//        ButterKnife.bind(this);
        mSlideView = findViewById(R.id.slide_view);
//        bar = (ProgressBar) findViewById(R.id.progress_loader);
//        bar.setVisibility(View.GONE);
//        if (!isOnline()) {
//            Toast.makeText(getApplicationContext(), R.string.not_connected_to_the_Internet, Toast.LENGTH_LONG).show();
//            // new ServiceCheckTask().execute();
//        }
    }
//    @OnClick({R.id.signup_btn, R.id.login_btn})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.signup_btn:
//                intent = new Intent(getApplicationContext(), SignUpActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.login_btn:
//                intent = new Intent(getApplicationContext(), LoginActivity.class);
//                startActivity(intent);
//                break;
//        }
//    }

   /* private boolean isOnline() {
        CheckConnect cc = new CheckConnect(CONNECTION_CONFIRM_CLIENT_URL);
        cc.start();
        try {
            cc.join();
            return cc.isSuccess();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }*/

//    private class ServiceCheckTask extends AsyncTask<Void, Void, String> {
//        @Override
//        protected void onPreExecute() {
//            bar.setVisibility(View.VISIBLE);
//            super.onPreExecute();
//        }
//
//        @Override
//        protected String doInBackground(Void... arg0) {
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            //Intent intent = new Intent(getApplicationContext(), WifiSearchActivity.class);
//            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//            startActivity(intent);
//            finish();
//            super.onPostExecute(result);
//        }
//    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!isCreated) {
            mSlideView.setFragments(getSupportFragmentManager(), WelcomeHomeFragment.newInstance(), WelcomeFirstFragment.newInstance(), WelcomeSecondFragment.newInstance(), WelcomeThirdFragment.newInstance());
            isCreated = true;
            if ( logoutState ) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        }
    }
}
