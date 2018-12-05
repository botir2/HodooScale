package com.animal.scale.hodoo.activity.user.login;

import android.content.Context;
import android.util.JsonReader;
import android.util.Log;

import com.animal.scale.hodoo.R;
import com.animal.scale.hodoo.domain.Device;
import com.animal.scale.hodoo.domain.Pet;
import com.animal.scale.hodoo.domain.ResultMessageGroup;
import com.animal.scale.hodoo.domain.User;
import com.animal.scale.hodoo.message.ResultMessage;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.StringReader;
import java.util.List;

public class LoginPresenter implements Login.Presenter {

    Login.View loginView;
    LoginModel loginModel;
    Context context;

    public LoginPresenter(Login.View loginView) {
        this.loginView = loginView;
        this.loginModel = new LoginModel();
    }

    @Override
    public void initUserData(User user, Context context) {
        this.context = context;
        loginModel.initUserData(user, context);
    }

    @Override
    public void sendServer(User user) {
        loginModel.sendServer(user, new LoginModel.DomainCallBackListner<ResultMessageGroup>() {
            @Override
            public void doPostExecute(ResultMessageGroup resultMessageGroup) {
                if(resultMessageGroup.getResultMessage().equals(ResultMessage.NOT_FOUND_EMAIL)){
                    loginView.showPopup(context.getString(R.string.not_found_email));
                }else if(resultMessageGroup.getResultMessage().equals(ResultMessage.ID_PASSWORD_DO_NOT_MATCH)){
                    loginView.showPopup(context.getString(R.string.id_password_do_not_match));
                }else if(resultMessageGroup.getResultMessage().equals(ResultMessage.FAILED)){
                    loginView.showPopup(context.getString(R.string.failed));
                }else if(resultMessageGroup.getResultMessage().equals(ResultMessage.SUCCESS)){
                    Gson gson = new Gson();
                    User user = gson.fromJson(resultMessageGroup.getDomain().toString().trim(), User.class);
                    saveUserSharedValue(user);
                    checkRegistrationStatus();
                }
            }

            @Override
            public void doPreExecute() {
                loginView.setProgress(true);
            }
        });
    }

    @Override
    public void userValidationCheck(User user) {
        sendServer(user);
        /*if (loginModel.editTextisEmptyCheck(user.getEmail())) {
            //이메일 형식에 어긋납니다.
            loginView.showPopup(context.getString(R.string.istyle_enter_the_email));
        } else if (!loginModel.editTextisValidEmail(user.getEmail())) {
            //이메일 형식에 어긋납니다.
            loginView.showPopup(context.getString(R.string.istyle_not_valid_email_format));
        } else if (loginModel.editTextisEmptyCheck(user.getPassword())) {
            loginView.showPopup(context.getString(R.string.istyle_enter_the_password));
        } else {
            sendServer();
        }*/
    }

    @Override
    public void saveUserSharedValue(User user) {
        loginModel.saveUserSharedValue(user);
    }

    @Override
    public void checkRegistrationStatus() {
        loginModel.confirmDeviceRegistration(new LoginModel.DeviceRegistrationListener() {
            @Override
            public void doPostExecute(List<Device> devices) {
                if(!devices.isEmpty()){
                    //디바이스 등록됨.
                    loginModel.confirmPetRegistration(new LoginModel.PetRegistrationListener() {
                        @Override
                        public void doPostExecute(List<Pet> pets) {
                            if(!pets.isEmpty()){
                                //PET 등록됨.
                                if(pets.size() == 1){
                                    if(pets.get(0).getBasic() == 0){
                                        loginView.goPetRegistActivity(pets.get(0).getPetIdx());
                                    }else if(pets.get(0).getDisease() == 0){
                                        loginView.goDiseasesRegistActivity(pets.get(0).getPetIdx());
                                    }else if(pets.get(0).getPhysical() == 0){
                                        loginView.goPhysicalRegistActivity(pets.get(0).getPetIdx());
                                    }else if(pets.get(0).getWeight() == 0){
                                        loginView.goWeightRegistActivity(pets.get(0).getPetIdx());
                                    }else{
                                        loginView.goHomeActivity();
                                    }
                                }else{
                                    loginView.goHomeActivity();
                                }
                            }else{
                                //PET 등록페이지 이동
                                loginView.goPetRegistActivity(0);
                            }
                        }
                        @Override
                        public void doPreExecute() {

                        }
                    });
                }else{
                    //디바이스 없음
                    loginView.goDeviceRegistActivity();
                }
            }
            @Override
            public void doPreExecute() {
                loginView.setProgress(true);
            }
        });
    }
}
