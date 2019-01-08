package com.animal.scale.hodoo.activity.setting.user.group.list;

import android.content.Context;

import com.animal.scale.hodoo.common.CommonModel;
import com.animal.scale.hodoo.domain.InvitationUser;

import java.util.List;
import java.util.regex.Pattern;

public class UserGroupPresenter implements UserGroupList.Presenter {
    private UserGroupList.View mView;
    private UserGroupListModel mModel;
    private Context mContext;
    UserGroupPresenter (Context context, UserGroupList.View view) {
        mView = view;
        mContext = context;
        mModel = new UserGroupListModel();
        mModel.loadData(context);
    }

    @Override
    public void getInvitationList() {
        mModel.getInvitationList(new CommonModel.DomainListCallBackListner<InvitationUser>() {
            @Override
            public void doPostExecute(List<InvitationUser> users) {
                for (int i = 0; i < users.size(); i++) {
                    users.get(i).setConvertNickName( matches( users.get(i).getNickname() ) );
                }
                mView.setAdapterData(users);
            }

            @Override
            public void doPreExecute() {

            }
        });
    }

    private String matches ( String name ) {
        String convertName = "";
        int endNum = 1;
        if ( Pattern.matches("^[ㄱ-ㅎ가-힣]*$", name) ) {
            endNum = 2;
        } else if ( Pattern.matches("^[a-zA-Z]*$", name) ) {
            name = name.toUpperCase(); //세로 가운데 정렬을 위한 대문자 처리
            endNum = 1;
        }
        convertName = name.substring(0, endNum);
        return convertName;
    }
}
