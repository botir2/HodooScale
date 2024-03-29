package com.animal.scale.hodoo.service;

import com.animal.scale.hodoo.domain.CommonResponce;
import com.animal.scale.hodoo.domain.Pet;
import com.animal.scale.hodoo.domain.PetAllInfos;
import com.animal.scale.hodoo.domain.PetBasicInfo;
import com.animal.scale.hodoo.domain.PetBreed;
import com.animal.scale.hodoo.domain.PetUserSelectionQuestion;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PetService {

    @POST("pet/my/pet/list.do")
    Call<List<Pet>> getMyPetList(@Query("groupCode") String groupCode);

    @POST("pet/my/pet/listResult.do")
    Call<Integer []> getMyPetResult(@Query("groupCode") String groupCode);

    @POST("android/pet/exist/my/pet.do")
    Call<CommonResponce<Integer>> getExistMyPet(@Query("groupCode") String groupCode);

    @POST("pet/my/pet/myPetFirstIdx.do")
    Call<Integer> myPetFirstIdx(@Query("groupCode") String groupCode);

    @POST("pet/basic/get.do")
    Call<PetBasicInfo> getBasicInformation(@Query("groupCode") String groupCode, @Query("petIdx") int petIdx, @Query("location") String location);

    @POST("pet/all/infos.do")
    Call<PetAllInfos> petAllInfos(@Query("petIdx") int petIdx);

    @POST("android/pet/all/infos.do")
    Call<CommonResponce<PetAllInfos>> petAllInfosForAndroid(@Query("petIdx") int petIdx);

    @POST("android/pet/get/breed/of/type.do")
    Call<CommonResponce<List<PetBreed>>> getAllBreed( @Query("location") String location, @Query("typeIdx") int typeIdx );

    @POST("pet/make/it/invisible.do")
    Call<CommonResponce<Integer>> deletePet(@Query("petIdx") int petIdx);

    @POST("pet/get/type.do")
    Call<Integer> getPetType( @Query("petIdx") int petIdx );

    @POST("pet/{type}/petType.do")
    Call<Integer> registPetType ( @Path("type") String type, @Query("petType") int petType );

    @POST("android/pet/question/regist.do")
    Call<CommonResponce<Integer>> registPetUserSelectQuestion (@Query("petIdx") int petIdx, @Body PetUserSelectionQuestion petUserSelectionQuestion);

    @POST("android/pet/question/delete.do")
    Call<CommonResponce<Integer>> deletePetUserSelectQuestion (@Query("petIdx") int petIdx, @Query("questionIdx") int questionIdx);

}
