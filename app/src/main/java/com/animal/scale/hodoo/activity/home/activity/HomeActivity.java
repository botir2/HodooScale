package com.animal.scale.hodoo.activity.home.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.animal.scale.hodoo.R;
import com.animal.scale.hodoo.activity.home.fragment.meal.MealFragment;
import com.animal.scale.hodoo.activity.home.fragment.temp.TempFragment;
import com.animal.scale.hodoo.activity.home.fragment.weight.WeightFragment;
import com.animal.scale.hodoo.activity.pet.regist.basic.BasicInformationRegistActivity;
import com.animal.scale.hodoo.helper.BottomNavigationViewHelper;
import com.animal.scale.hodoo.activity.setting.list.SettingListActivity;
import com.animal.scale.hodoo.adapter.AdapterOfPets;
import com.animal.scale.hodoo.adapter.AdapterSpinner;
import com.animal.scale.hodoo.base.BaseActivity;
import com.animal.scale.hodoo.common.SharedPrefVariable;
import com.animal.scale.hodoo.databinding.ActivityHomeBinding;
import com.animal.scale.hodoo.domain.ActivityInfo;
import com.animal.scale.hodoo.domain.PetAllInfos;
import com.animal.scale.hodoo.domain.SettingMenu;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeActivity extends BaseActivity<HomeActivity> implements NavigationView.OnNavigationItemSelectedListener, HomeActivityIn.View {

    private final long FINISH_INTERVAL_TIME = 2000;

    private final int ADD_PET = 0;

    private long backPressedTime = 0;

    public ActivityHomeBinding binding;
    //Spinner adapter
    AdapterSpinner adapterSpinner;

    HomeActivityIn.Presenter presenter;

    AdapterOfPets adapter;

    public boolean isDropdonw = false;

    AlertDialog.Builder alertDialog;

    private List<PetAllInfos> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        binding.setActivity(this);
        //super.setToolbarColor();
        presenter = new HomeActivityPresenter(this);
        presenter.loadData(HomeActivity.this);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentTransaction.add(R.id.fragment_container, WeightFragment.newInstance()).commit();
        binding.setActivityInfo(new ActivityInfo(getString(R.string.weight_title)));
        presenter.loadCustomDropdownView();
        presenter.getSttingListMenu();
    }

    public void onPetImageClick(View view) {
        adapter = new AdapterOfPets(HomeActivity.this, data, mSharedPrefManager.getIntExtra(SharedPrefVariable.CURRENT_PET_IDX));
        alertDialog = super.showBasicOneBtnPopup("PET 선택", null);
        View customView = getLayoutInflater().inflate(R.layout.pet_list_dialog, null);
        ListView li = (ListView) customView.findViewById(R.id.pet_listview);
        li.setAdapter(adapter);
        li.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                getSelectedItemOfSpinner(position);
                adapter.notifyDataSetChanged();
                presenter.setCurrentPetInfos(data);
            }
        });
        alertDialog.setView(customView);
        alertDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Button addBtn = (Button) customView.findViewById(R.id.add_pet);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BasicInformationRegistActivity.class);
                intent.putExtra("petIdx", ADD_PET);
                startActivity(intent);
                //overridePendingTransition(R.anim.end_enter, R.anim.end_exit);
            }
        });
        alertDialog.show();
    }

    public void onSettingBtnClick(View view) {
        Intent intent = new Intent(getApplicationContext(), SettingListActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
    }


    @Override
    protected BaseActivity<HomeActivity> getActivityClass() {
        return HomeActivity.this;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_weight:
                    binding.setActivityInfo(new ActivityInfo(getString(R.string.weight_title)));
                    replaceFragment(WeightFragment.newInstance());
                    presenter.loadCustomDropdownView();
                    return true;
          /*      case R.id.navigation_temp:
                    binding.setActivityInfo(new ActivityInfo(getString(R.string.temp_title)));
                    replaceFragment(TempFragment.newInstance());
                    return true;*/
                case R.id.navigation_meal:
                    replaceFragment(MealFragment.newInstance());
                    binding.setActivityInfo(new ActivityInfo(getString(R.string.meal_title)));
                    return true;
            }
            return false;
        }
    };

    // Fragment 변환을 해주기 위한 부분, Fragment의 Instance를 받아서 변경
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment).commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
                super.onBackPressed();
            } else {
                backPressedTime = tempTime;
                Toast.makeText(getApplicationContext(), R.string.press_back_message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void changeBottomNavigationSize(BottomNavigationView bottomNavigationView) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }
    }

    @Override
    public void setCustomDropdownView(List<PetAllInfos> data) {
        this.data = data;
        presenter.setCurrentPetInfos(data);

    }


    @Override
    public void setCurrentPetInfos(List<PetAllInfos> data) {
        for (PetAllInfos info : data) {
            if (info.getPet().getPetIdx() == mSharedPrefManager.getIntExtra(SharedPrefVariable.CURRENT_PET_IDX)) {
                //Curcle 이미지 셋팅
                presenter.setCurcleImage(info);
                //에니메이션 돌림 이미지 셋팅
                android.support.v4.app.Fragment tf = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if(tf instanceof WeightFragment){
                    WeightFragment weightFragment = (WeightFragment) tf;
                    weightFragment.setBcsMessage(info.getPet().getBasic());
                    weightFragment.drawChart();
                }else if(tf instanceof MealFragment){
                    MealFragment mealFragment = (MealFragment) tf;
                    mealFragment.initRaderChart();
                }
            }
        }
    }

    @Override
    public void setCurcleImage(PetAllInfos info) {
        Picasso.with(this)
                .load("http://121.183.234.14:7171/hodoo/" + info.getPetBasicInfo().getProfileFilePath())
                .into(binding.appBarNavigation.petImage);
    }

    @Override
    public void setListviewAdapter(List<SettingMenu> settingList) {
        //adapter = new AdapterOfSetting(HomeActivity.this, settingList);
        //binding.appBarNavigation.customSpinnerListView.setAdapter(adapter);
    }

    public void getSelectedItemOfSpinner(int position) {
        PetAllInfos info = (PetAllInfos) adapter.getItem(position);
        presenter.setCurcleImage(info);
        mSharedPrefManager.putIntExtra(SharedPrefVariable.CURRENT_PET_IDX, info.getPet().getPetIdx());
    }


    @Override
    public void onStart() {
        super.onStart();
        presenter.loadCustomDropdownView();
        //Kcal 로리 표시
    }
}