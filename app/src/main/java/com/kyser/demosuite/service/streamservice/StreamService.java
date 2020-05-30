package com.kyser.demosuite.service.streamservice;

import android.util.Log;

import com.kyser.demosuite.service.model.CategoryModel;
import com.kyser.demosuite.service.model.FeaturedModel;
import com.kyser.demosuite.service.model.ListingModel;
import com.kyser.demosuite.service.model.TrackModel;
import com.kyser.demosuite.service.model.UploadModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    private SearchService mSearchService;

    public interface SynopsisCallback { void onSynopsisReady(ListingModel synopsisModel);}
    public interface ServiceTest { void onTestResponse(String response);}
    public interface TracklistCallback { void onTracklistReady(List<TrackModel> tracklist);}
    public interface UploadCallback { void onUploadResult(UploadModel result);}

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
                .baseUrl("https://stream-a1.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mVideoService = mRetrofit.create(VideoService.class);
        mAudioService = mRetrofit.create(AudioService.class);
        mFeaturedService = mRetrofit.create(FeaturedService.class);
        mSearchService = mRetrofit.create(SearchService.class);
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

    public void getSynopsisModel(int mid,int cid ,int scid, SynopsisCallback mcallback){
        System.out.println("mid = [" + mid + "], cid = [" + cid + "], scid = [" + scid + "], mcallback = [" + mcallback + "]");
        getFeaturedService().getSynopsisModel(mid,cid,scid).enqueue(new Callback<List<ListingModel>>() {
            @Override
            public void onResponse(Call<List<ListingModel>> call, Response<List<ListingModel>> response) {
              //  System.out.println("call = [" + call + "], response = [" + response.body().get(0).getDescription() + "]");
               // mcallback.onSynopsisReady(response.body().get(0));
            }

            @Override
            public void onFailure(Call<List<ListingModel>> call, Throwable t) {
                System.out.println("call = [" + call + "], t = [" + t + "]");
            }
        });
    }

    public void getTracklist(int aggmid, TracklistCallback mTracklistCallback){
        getAudioService().getTracklistModel(aggmid).enqueue(new Callback<List<TrackModel>>() {
            @Override
            public void onResponse(Call<List<TrackModel>> call, Response<List<TrackModel>> response) {
                mTracklistCallback.onTracklistReady(response.body());
            }

            @Override
            public void onFailure(Call<List<TrackModel>> call, Throwable t) {

            }
        });
    }
    MutableLiveData<List<ListingModel>> searchList  =new MutableLiveData<>();
    public MutableLiveData<List<ListingModel>> getSearchTitles(String title){

        mSearchService.getSearchMedia(title).enqueue(new Callback<List<ListingModel>>() {
            @Override
            public void onResponse(Call<List<ListingModel>> call, Response<List<ListingModel>> response) {
                Log.v("=====onResponse======= ","================");
                searchList.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<ListingModel>> call, Throwable t) {
                Log.v("=======error========== ","================");
            }
        });
        return searchList;
    }

    public void uploadPoster(String uri , String mid , UploadCallback uploadCallback){
        MultipartBody.Part filePart;
        File file = new File(uri);
        filePart = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

        mSearchService.posterUpdate(filePart,MultipartBody.Part.createFormData("mid",mid)).enqueue(new Callback<UploadModel>() {
            @Override
            public void onResponse(Call<UploadModel> call, Response<UploadModel> response) {
                uploadCallback.onUploadResult(response.body());
            }

            @Override
            public void onFailure(Call<UploadModel> call, Throwable t) {

            }
        });
    }
}
