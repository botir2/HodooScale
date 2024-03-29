package com.animal.scale.hodoo.activity.pet.regist.fragment.physique;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.animal.scale.hodoo.HodooApplication;
import com.animal.scale.hodoo.R;
import com.animal.scale.hodoo.activity.pet.regist.activity.PetRegistActivity;
import com.animal.scale.hodoo.base.BaseFragment;
import com.animal.scale.hodoo.base.PetRegistFragment;
import com.animal.scale.hodoo.databinding.FragmentPhysiqueInfomationBinding;
import com.animal.scale.hodoo.domain.PetPhysicalInfo;
import com.animal.scale.hodoo.util.MathUtil;
import com.animal.scale.hodoo.util.ValidationUtil;
import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.tistory.dwfox.dwrulerviewlibrary.utils.DWUtils;
import com.tistory.dwfox.dwrulerviewlibrary.view.DWRulerSeekbar;
import com.tistory.dwfox.dwrulerviewlibrary.view.ObservableHorizontalScrollView;
import com.tistory.dwfox.dwrulerviewlibrary.view.ScrollingValuePicker;

/**
 * Created by SongSeokwoo on 2019-04-02.
 */
public class PhysiqueInfomationRegistFragment extends PetRegistFragment implements PhysiqueInformationRegistIn.View {
    private FragmentPhysiqueInfomationBinding binding;
    private PhysiqueInformationRegistPresenter presenter;

    public static Context mContext;

    public int petId;

    BottomDialog builder;

    private ScrollingValuePicker myScrollingValuePicker;
    private DWRulerSeekbar dwRulerSeekbar;

    private static final float MIN_VALUE = 0;
    private static final float MAX_VALUE = 30;
    private static final float LINE_RULER_MULTIPLE_SIZE = 3.5f;

    private int petIdx;

    private boolean deleteAllPet;
    private boolean state = false;
    private final String suffixStr = "kg";
    private int type;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_physique_infomation, container, false);
        binding.setActivity(this);

        if ( getArguments() != null ) {
            petIdx = getArguments().getInt("petIdx");
            deleteAllPet = getArguments().getBoolean("deleteAllPet", false);
        }
        presenter = new PhysiqueInformationRegistPresenter(this);
        presenter.loadData(getContext());
        binding.unitStr.setText( getContext().getResources().getStringArray(R.array.height_unit)[presenter.getUnitIdx()] );
//        presenter.setNavigation();

        presenter.getPhysiqueInformation(petIdx);
        setTextWatcher(binding.editWidth, binding.editHeight);
        return binding.getRoot();
    }
    public static PetRegistFragment newInstance() {
        return new PhysiqueInfomationRegistFragment();
    }

    @Override
    public void setDiseaseInfo(PetPhysicalInfo petPhysicalInfo) {
        if ( presenter.getUnitIdx() == 1 && petPhysicalInfo != null ) {
            petPhysicalInfo.setWidth( String.valueOf(  MathUtil.cmToInch( Double.parseDouble( petPhysicalInfo.getWidth() ) ) ) );
            petPhysicalInfo.setHeight( String.valueOf( MathUtil.cmToInch( Double.parseDouble( petPhysicalInfo.getHeight() ) ) ) );
        }
        if (petPhysicalInfo != null) {
            binding.setDomain(petPhysicalInfo);
            if ( Float.parseFloat(petPhysicalInfo.getWeight()) > 0 )
                state = true;
            else
                state = false;
            binding.setState(state);
            validation();
        } else {
            PetPhysicalInfo info = new PetPhysicalInfo();
            info.setWeight("0");
            binding.setDomain(info);
        }
    }

    public void onClickWidthEt(PetPhysicalInfo petPhysicalInfo) {
        if (petPhysicalInfo != null) {
            if (petPhysicalInfo.getWidth() != null) {
//                presenter.showRulerBottomDlg(binding.editWidth, petPhysicalInfo.getWidth());
            } else {
//                presenter.showRulerBottomDlg(binding.editWidth, "0");
            }
        } else {
//            presenter.showRulerBottomDlg(binding.editWidth, "0");
        }
    }

    public void onClickHightEt(PetPhysicalInfo petPhysicalInfo) {
        if (petPhysicalInfo != null) {
            if (petPhysicalInfo.getHeight() != null) {
                presenter.showRulerBottomDlg(binding.editHeight, petPhysicalInfo.getHeight());
            } else {
                presenter.showRulerBottomDlg(binding.editHeight, "0");
            }
        } else {
            presenter.showRulerBottomDlg(binding.editHeight, "0");
        }

    }

    public void onClickWeightEt(PetPhysicalInfo petPhysicalInfo) {

    }

    @Override
    public void showRulerBottomDlg(final EditText editText, String value) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.dialog_custom_ruler_popup, null);
        builder = new BottomDialog.Builder(getContext())
                .setCustomView(customView)
                .setNegativeText(getString(R.string.confirm))
                .show();

//        myScrollingValuePicker = (ScrollingValuePicker) customView.findViewById(R.id.myScrollingValuePicker);
//        myScrollingValuePicker.setViewMultipleSize(LINE_RULER_MULTIPLE_SIZE);
//        myScrollingValuePicker.setMaxValue(MIN_VALUE, MAX_VALUE);
//        myScrollingValuePicker.setValueTypeMultiple(3);
//        myScrollingValuePicker.setInitValue(Integer.parseInt(value));
//        myScrollingValuePicker.getScrollView().setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    myScrollingValuePicker.getScrollView().startScrollerTask();
//                }
//                return false;
//            }
//        });
//        myScrollingValuePicker.setOnScrollChangedListener(new ObservableHorizontalScrollView.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged(ObservableHorizontalScrollView view, int l, int t) {
//            }
//
//            @Override
//            public void onScrollStopped(int l, int t) {
//                editText.setText(String.valueOf(DWUtils.getValueAndScrollItemToCenter(myScrollingValuePicker.getScrollView(), l, t, MAX_VALUE, MIN_VALUE, myScrollingValuePicker.getViewMultipleSize())));
//            }
//        });
    }

    @Override
    public void registPhysiqueInformation() {

    }

    @Override
    public void registResult(Integer result) {

    }

    /**
     * 펫의 피지컬을 액티비티에 저장한다.
     *
     * @param    
     * @return
     * @description   설정에서 단위를 인치로 바꾸면 입력된 인치를 센치로 변환해서 넣는다
    */
    public void onClickNextBtn(View view) {
        PetPhysicalInfo info = binding.getDomain();
        if ( presenter.getUnitIdx() == 1 ) {
            info.setWidth(String.valueOf(MathUtil.InchToCm( Double.parseDouble( info.getWidth() ) )));
            info.setHeight(String.valueOf(MathUtil.InchToCm( Double.parseDouble( info.getHeight() ) )));
        }

        ((PetRegistActivity) getActivity()).setPetPhysicalInfo(info);
        ((PetRegistActivity) getActivity()).nextFragment();
    }

    @Override
    public void setNavigation() {
//        binding.addPetNavigation.basicBtn.setBackgroundResource(R.drawable.rounded_pink_btn);
//        binding.addPetNavigation.diseaseBtn.setBackgroundResource(R.drawable.rounded_pink_btn);
//        binding.addPetNavigation.physiqueBtn.setBackgroundResource(R.drawable.rounded_pink_btn);
//        binding.addPetNavigation.basicBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//              /*  Intent intent = new Intent(getApplicationContext(), BasicInformationRegistActivity.class);
//                intent.putExtra("petIdx", petIdx);
//                startActivity(intent);
//                overridePendingTransition(R.anim.end_enter, R.anim.end_exit);
//                finish();*/
//            }
//        });
//        binding.addPetNavigation.diseaseBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               /* Intent intent = new Intent(getApplicationContext(), DiseaseInformationRegistActivity.class);
//                intent.putExtra("petIdx", petIdx);
//                startActivity(intent);
//                overridePendingTransition(R.anim.end_enter, R.anim.end_exit);
//                finish();*/
//            }
//        });
       /* binding.addPetNavigation.physiqueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PhysiqueInformationRegistActivity.class);
                intent.putExtra("petIdx", petIdx);
                startActivity(intent);
                overridePendingTransition(R.anim.end_enter, R.anim.end_exit);
                finish();
            }
        });
        binding.addPetNavigation.weightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WeightCheckActivity.class);
                intent.putExtra("petIdx", petIdx);
                startActivity(intent);
                overridePendingTransition(R.anim.end_enter, R.anim.end_exit);
                finish();
            }
        });*/
    }

    private void setTextWatcher(EditText... edts) {
        for (EditText edt : edts) {
            edt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    validation();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
    }

    /**
     * 검증처리
     *
     * @param
     * @return
     * @description
    */
    private void validation() {
        if ( ((HodooApplication) getActivity().getApplication()).isExperienceState() ) {
            binding.setState(true);
            binding.nextStep.setEnabled(true);
            return;
        }

        if (
                !ValidationUtil.isEmpty(binding.editWidth.getText().toString()) &&
                        !ValidationUtil.isEmpty(binding.editHeight.getText().toString()) ) {
            binding.nextStep.setEnabled(true);
        } else {
            binding.nextStep.setEnabled(false);
        }
    }
    /**
     * 펫의 인덱스 값을 가지고 등록되어있는 피지컬을 가져와 뷰에 출력한다.
     *
     * @param
     * @return
     * @description
    */
    public void updateView() {
        if ( petIdx != 0 ) {
            presenter.getPhysiqueInformation(petIdx);
        }
        validation();
    }
    /**
     * 펫 타입을 셋팅한다.
     *
     * @param type   펫 타입
     * @return
     * @description
    */
    public void setPetType ( int type ) {
        this.type = type;

        String[] physiqueStrArr = null;
        switch (type) {
            case PetRegistActivity.DOG_TYPE :
                physiqueStrArr = getActivity().getResources().getStringArray(R.array.dog_type_physique_str_arr);
                break;
            case PetRegistActivity.CAT_TYPE :
                physiqueStrArr = getActivity().getResources().getStringArray(R.array.cat_type_physique_str_arr);
                break;
            default:
                physiqueStrArr = getActivity().getResources().getStringArray(R.array.dog_type_physique_str_arr);
                break;
        }

        binding.first.setText( physiqueStrArr[0] );
        binding.second.setText( physiqueStrArr[1] );
    }
}
