package com.kyser.demosuite.service.streamservice;

import com.kyser.demosuite.service.model.CategoryModel;
import com.kyser.demosuite.service.model.FeaturedModel;
import com.kyser.demosuite.service.model.ListingModel;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StreamService {

    static StreamService __instaMediaServiceController;
    private Retrofit mRetrofit;
    private VideoService mVideoService;
    private FeaturedService mFeaturedService;
    private AudioService mAudioService;

    public interface SynopsisCallback { void onSynopsisReady(ListingModel synopsisModel);}
    public interface ServiceTest { void onTestResponse(String response);}

    public static StreamService getInstance(){
        if(__instaMediaServiceController==null)
            __instaMediaServiceController = new StreamService();
        return __instaMediaServiceController;
    }

    private StreamService(){
        initService();
    }
    
    private void initService() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl("https://stream-canvas-va1.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mVideoService = mRetrofit.create(VideoService.class);
        mAudioService = mRetrofit.create(AudioService.class);
        mFeaturedService = mRetrofit.create(FeaturedService.class);
    }
    private VideoService getVideoService() {
        return mVideoService;
    }
    private FeaturedService getFeaturedService() {
        return mFeaturedService;
    }
    private AudioService getAudioService() {
        return mAudioService;
    }
    public void testService(ServiceTest serviceTest){
        getFeaturedService().test().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                serviceTest.onTestResponse(response.body().toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                serviceTest.onTestResponse("ERROR");
            }
        });

    }

    public MutableLiveData<List<CategoryModel>> getVideoCategory(){
        final MutableLiveData<List<CategoryModel>> data = new MutableLiveData<>();
        getVideoService().getVideoModel(27).enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
                List<CategoryModel> lt =new ArrayList<>();
                CategoryModel cm =new CategoryModel();

                for (int i = 1;i<11;i++) {
                    cm.setTitle("Category" + i);
                    lt.add(cm);
                }
                data.setValue(lt);
            }
        });
        return data;
    }

    public MutableLiveData<List<CategoryModel>> getAudioCategory(){
        final MutableLiveData<List<CategoryModel>> data = new MutableLiveData<>();
        getAudioService().getAudioModel(29).enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                System.out.println("================="+response.body().size());
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
                List<CategoryModel> lt =new ArrayList<>();
                CategoryModel cm =new CategoryModel();

                for (int i = 1;i<11;i++) {
                    cm.setTitle("Category" + i);
                    lt.add(cm);
                }
                data.setValue(lt);
            }
        });
        return data;
    }

    public MutableLiveData<List<ListingModel>> getMediaList(int cid) {
        final MutableLiveData<List<ListingModel>> data = new MutableLiveData<>();
        getVideoService().getListingModel(cid).enqueue(new Callback<List<ListingModel>>() {
            @Override
            public void onResponse(Call<List<ListingModel>> call, Response<List<ListingModel>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<ListingModel>> call, Throwable t) {
                List<ListingModel> lt = new ArrayList<>();
                ListingModel cm = new ListingModel();

                for (int i = 1; i < 11; i++) {
                    cm.setTitle("Title" + i);
                    lt.add(cm);
                }
                data.setValue(lt);
            }
        });
        return data;
    }
    public MutableLiveData<List<FeaturedModel>> getFeaturedList(){
        final MutableLiveData<List<FeaturedModel>> data = new MutableLiveData<>();
        getFeaturedService().getFeatured().enqueue(new Callback<List<FeaturedModel>>() {
            @Override
            public void onResponse(Call<List<FeaturedModel>> call, Response<List<FeaturedModel>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<FeaturedModel>> call, Throwable t) {
                List<FeaturedModel> lt =new ArrayList<>();
                FeaturedModel cm =new FeaturedModel();

                for (int i = 1;i<11;i++) {
                    cm.setTitle("Featured" + i);
                    lt.add(cm);
                }
                data.setValue(lt);
            }
        });
        return data;
    }

    public void getSynopsisModel(int cid, SynopsisCallback mcallback){
        getFeaturedService().getSynopsisModel(cid).enqueue(new Callback<List<ListingModel>>() {
            @Override
            public void onResponse(Call<List<ListingModel>> call, Response<List<ListingModel>> response) {
                System.out.println("call = [" + call + "], response = [" + response.body().get(0).getDescription() + "]");
                mcallback.onSynopsisReady(response.body().get(0));
            }

            @Override
            public void onFailure(Call<List<ListingModel>> call, Throwable t) {
                System.out.println("call = [" + call + "], t = [" + t + "]");
            }
        });
    }
}
