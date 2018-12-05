package com.animal.scale.hodoo.activity.device.regist;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.animal.scale.hodoo.R;
import com.animal.scale.hodoo.activity.user.login.LoginActivity;
import com.animal.scale.hodoo.activity.wifi.WifiSearchActivity;
import com.animal.scale.hodoo.activity.wifi.find.FindHodoosActivity;
import com.animal.scale.hodoo.base.BaseActivity;
import com.animal.scale.hodoo.databinding.ActivityDeviceRegistBinding;
import com.animal.scale.hodoo.domain.ActivityInfo;

public class DeviceRegistActivity extends BaseActivity<DeviceRegistActivity> implements DeviceRegistIn.View{

    ActivityDeviceRegistBinding binding;

    DeviceRegistIn.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_device_regist);
        binding.setActivity(this);
        binding.setActivityInfo(new ActivityInfo(getString(R.string.title_device_regist)));
        super.setToolbarColor();
        presenter = new DeviceRegistPresenter(this);
        presenter.loadData(getApplicationContext());
    }

    @Override
    protected BaseActivity<DeviceRegistActivity> getActivityClass() {
        return DeviceRegistActivity.this;
    }

    public void onClickRegistBtn(View view){
        presenter.tempRegist();
    }

   /* public void onClickTestWifiBtn(View view){
        Intent intent = new Intent(getApplicationContext(), FindHodoosActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.end_enter, R.anim.end_exit);
        finish();
    }*/

    @Override
    public void setProgress(boolean play) {
        if (play) {
            binding.loginProgress.setVisibility(View.VISIBLE);
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.end_enter, R.anim.end_exit);
            finish();
            showToast(getString(R.string.success));
        } else {
            binding.loginProgress.setVisibility(View.GONE);
        }
    }

    @Override
    public void moveWIFISetting() {
        Intent intent = new Intent(getApplicationContext(), WifiSearchActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.end_enter, R.anim.end_exit);
        finish();
    }
}
