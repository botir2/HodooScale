package com.animal.scale.hodoo.activity.meal.regist;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.animal.scale.hodoo.R;
import com.animal.scale.hodoo.activity.meal.detail.IngredientsOfMealActivity;
import com.animal.scale.hodoo.activity.pet.regist.basic.BasicInformationRegistActivity;
import com.animal.scale.hodoo.base.BaseActivity;
import com.animal.scale.hodoo.common.SharedPrefVariable;
import com.animal.scale.hodoo.custom.dialog.IngredientsOfMealDialog;
import com.animal.scale.hodoo.custom.view.seekbar.ProgressItem;
import com.animal.scale.hodoo.databinding.ActivityMealRegistrationBinding;
import com.animal.scale.hodoo.db.DBHandler;
import com.animal.scale.hodoo.domain.ActivityInfo;
import com.animal.scale.hodoo.domain.Feed;
import com.animal.scale.hodoo.domain.MealHistory;
import com.animal.scale.hodoo.domain.PetAllInfos;
import com.animal.scale.hodoo.domain.SearchHistory;
import com.animal.scale.hodoo.util.DateUtil;
import com.animal.scale.hodoo.util.RER;
import com.github.javiersantos.bottomdialogs.BottomDialog;

import java.util.ArrayList;

public class MealRegistrationActivity extends BaseActivity<MealRegistrationActivity> implements MealRegistrationIn.View {

    ActivityMealRegistrationBinding binding;

    MealRegistrationIn.Presenter presenter;

    private ArrayList<ProgressItem> progressItemList;

    private ProgressItem mProgressItem;

    private String[] decimalArray = {".0", ".25", ".5", ".75"};

    private String[] unitArray;

    private String[] doubleUnitArray = {"g", getString(R.string.cup)};

    private String[] singleUnitArray = {getString(R.string.ea)};

    private int feedId;

    private float darkGreySpan;

    private float rer;

    private DBHandler dbHandler;

    IngredientsOfMealDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_meal_registration);
        binding.setActivity(this);
        binding.setActivityInfo(new ActivityInfo(getString(R.string.food)));
        presenter = new MealRegistrationPresenter(this);
        presenter.loadData(this);
        presenter.getPetAllInfo();
        feedId = intent.getIntExtra("feedId", 0);
        presenter.getFeedInfo(feedId);
        dbHandler = new DBHandler(this);
        progressItemList = new ArrayList<ProgressItem>();
        binding.seekBar.initData(progressItemList);
    }


    private void setNumberPicker(NumberPicker numberPicker) {
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(1000);
        numberPicker.setWrapSelectorWheel(true);
        //Set a value change listener for NumberPicker
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            }
        });
    }

    private void setDecimalNumberPicker(NumberPicker numberPicker) {
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(3);
        numberPicker.setDisplayedValues(decimalArray);
        //Set a value change listener for NumberPicker
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            }
        });
    }

    private void setStringNumberPicker(NumberPicker numberPicker, Feed feed) {
        numberPicker.setMinValue(0);
        if (feed.getTag().equals("D")) {
            numberPicker.setMaxValue(1);
            numberPicker.setDisplayedValues(doubleUnitArray);
            unitArray = doubleUnitArray;
        } else if (feed.getTag().equals("S")) {
            numberPicker.setMaxValue(0);
            numberPicker.setDisplayedValues(singleUnitArray);
            unitArray = singleUnitArray;
        }
        //Set a value change listener for NumberPicker
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            }
        });
    }

    @Override
    protected BaseActivity<MealRegistrationActivity> getActivityClass() {
        return MealRegistrationActivity.this;
    }

    public void initDataToSeekbar(float rer, float kcal) {
        mProgressItem = new ProgressItem();
        mProgressItem.progressItemPercentage = (float) (((rer * 0.7) / kcal) * 100);
        mProgressItem.color = R.color.seek_bar_gray;
        progressItemList.add(mProgressItem);

        mProgressItem = new ProgressItem();
        mProgressItem.progressItemPercentage = (float) (((rer * 0.3) / kcal) * 100);
        mProgressItem.color = R.color.grey;
        progressItemList.add(mProgressItem);

        mProgressItem = new ProgressItem();
        mProgressItem.progressItemPercentage = (darkGreySpan / kcal) * 100;
        mProgressItem.color = R.color.red;
        progressItemList.add(mProgressItem);

        binding.seekBar.invalidate();
    }

    public void initDataToSeekbar(float rer) {
        mProgressItem = new ProgressItem();
        mProgressItem.progressItemPercentage = (float) (((rer * 0.7) / rer) * 100);
        mProgressItem.color = R.color.seek_bar_gray;
        progressItemList.add(mProgressItem);
        // greyspan

        mProgressItem = new ProgressItem();
        mProgressItem.progressItemPercentage = (darkGreySpan / rer) * 100;
        mProgressItem.color = R.color.grey;
        progressItemList.add(mProgressItem);

        binding.seekBar.invalidate();
    }

    @Override
    public void setFeedInfo(Feed feed) {
        binding.setDomain(feed);
        setNumberPicker(binding.jungsu);
        setDecimalNumberPicker(binding.umsu);
        setStringNumberPicker(binding.unit, feed);

        dialog = IngredientsOfMealDialog.newInstance(feed);
    }

    @Override
    public void setInsertResult(Integer result) {
        if (result == 1)
            dbHandler.insertFeed(new SearchHistory(binding.getDomain().getName(), mSharedPrefManager.getIntExtra(SharedPrefVariable.USER_UNIQUE_ID), binding.getDomain().getId(),  DateUtil.getCurrentDatetime()));
            finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void setPetAllInfo(PetAllInfos petAllInfos) {
        rer = new RER(5, petAllInfos.getFactor()).getRER();
        presenter.getTodaySumCalorie();
    }

    @Override
    public void setTodaySumCalorie(MealHistory mealHistory) {
        if (mealHistory != null) {
            if (rer > mealHistory.getCalorie()) {
                binding.seekBar.setMax((int) rer);
                initDataToSeekbar(rer);
            } else {
                binding.seekBar.setMax((int) mealHistory.getCalorie());
                initDataToSeekbar(rer, mealHistory.getCalorie());
            }
            binding.seekBar.setProgress((int) mealHistory.getCalorie());
        } else {
            binding.seekBar.setMax((int) rer);
            initDataToSeekbar(rer);
            binding.seekBar.setProgress(0);
        }
        binding.seekBar.setEnabled(true);
    }

    public void onClickDetailBtn(View view) {
        dialog.show(getFragmentManager(), "dialog");

    }

    public void onClickSaveBtn(View view) {
        StringBuilder AmountOfFeed = new StringBuilder();
        AmountOfFeed.append(binding.jungsu.getValue());
        AmountOfFeed.append(decimalArray[binding.umsu.getValue()]);

        MealHistory mealHistory = MealHistory.builder()
                .calorie(Float.parseFloat(AmountOfFeed.toString()))
                .unitIndex(binding.unit.getValue())
                .unitString(unitArray[binding.unit.getValue()])
                .feedIdx(feedId)
                .petIdx(mSharedPrefManager.getIntExtra(SharedPrefVariable.CURRENT_PET_IDX))
                .groupId(mSharedPrefManager.getStringExtra(SharedPrefVariable.GROUP_CODE))
                .userIdx(mSharedPrefManager.getIntExtra(SharedPrefVariable.USER_UNIQUE_ID))
                .build();
        presenter.saveMeal(mealHistory);
    }
}