package com.animal.scale.hodoo.activity.pet.regist.fragment.type;

import android.content.Context;

import com.animal.scale.hodoo.common.CommonModel;
import com.animal.scale.hodoo.domain.PetBasicInfo;

/**
 * Created by SongSeokwoo on 2019-04-09.
 */
public class PetTypePresenter implements PetTypeIn.Presenter {
    private PetTypeIn.View view;
    private PetTypeModel model;
    PetTypePresenter ( PetTypeIn.View view ) {
        this.view = view;
        model = new PetTypeModel();
    }
    /**
     * 펫의 타입을 가져온다
     *
     * @param petIdx   펫의 인덱스 값
     * @return
    */
    @Override
    public void getType(int petIdx) {
        model.getPetType(petIdx, new CommonModel.DomainCallBackListner<Integer>() {
            @Override
            public void doPostExecute(Integer petType) {
                if ( petType != null )
                    view.setType(petType);
            }

            @Override
            public void doPreExecute() {

            }

            @Override
            public void doCancelled() {

            }
        });
    }

    @Override
    public void loadData(Context context) {
        model.loadData(context);
    }

    @Override
    public void setNavigation() {
        view.setNavigation();
    }
    @Override
    public void getPetBasicInformation(String location, int petIdx) {
        model.getPetBasicInformation(location, petIdx, new CommonModel.DomainCallBackListner<PetBasicInfo>() {
            @Override
            public void doPostExecute(PetBasicInfo basicInfo) {
                view.setBasicInfo(basicInfo);
            }

            @Override
            public void doPreExecute() {
                //view.showErrorToast();
            }
            @Override
            public void doCancelled() {

            }
        });
    }
}
