package com.animal.scale.hodoo.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetRetrofit {

    private static NetRetrofit ourInstance = new NetRetrofit();

    public static NetRetrofit getInstance() {
        return ourInstance;
    }

    private NetRetrofit() {

    }

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://121.183.234.14:7171/")
//            .baseUrl("http://192.168.0.49:8181/")
            .addConverterFactory(GsonConverterFactory.create()) // 파싱등록
            .build();

    UserService service = retrofit.create(UserService.class);
    PetBasicInfoService petBasicInfoService = retrofit.create(PetBasicInfoService.class);
    PetChronicDiseaseService petChronicDiseaseService = retrofit.create(PetChronicDiseaseService.class);
    PetPhysicalInfoService petPhysicalInfoService = retrofit.create(PetPhysicalInfoService.class);
    PetWeightInfoService petWeightInfoService = retrofit.create(PetWeightInfoService.class);
    RealTimeWeightService realTimeWeightService = retrofit.create(RealTimeWeightService.class);
    PetGroupMappingService petGroupMappingService = retrofit.create(PetGroupMappingService.class);
    DeviceService getDeviceService = retrofit.create(DeviceService.class);
    PetService getPetService = retrofit.create(PetService.class);

    FeedService feedService = retrofit.create(FeedService.class);

    MealHistoryService mealHistoryService = retrofit.create(MealHistoryService.class);

    public UserService getUserService() {
        return service;
    }

    public PetBasicInfoService getPetBasicInfoService() {
        return petBasicInfoService;
    }

    public PetChronicDiseaseService getPetChronicDiseaseService() { return petChronicDiseaseService; }

    public PetPhysicalInfoService getPetPhysicalInfoService() { return petPhysicalInfoService; }

    public PetWeightInfoService getPetWeightInfoService() { return petWeightInfoService; }

    public RealTimeWeightService getRealTimeWeightService() { return realTimeWeightService; }

    public PetGroupMappingService getPetGroupMappingService() { return petGroupMappingService; }

    public DeviceService getDeviceService() { return getDeviceService; }

    public PetService getPetService() { return getPetService; }

    public FeedService getFeedService() { return feedService; }

    public MealHistoryService getMealHistoryService() { return mealHistoryService; }

}

