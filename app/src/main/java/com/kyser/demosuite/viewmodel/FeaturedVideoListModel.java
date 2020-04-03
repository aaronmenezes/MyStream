package com.kyser.demosuite.viewmodel;

import android.app.Application;

import com.kyser.demosuite.service.model.FeaturedModel;
import com.kyser.demosuite.service.model.ListingModel;
import com.kyser.demosuite.service.streamservice.StreamService;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class FeaturedVideoListModel extends AndroidViewModel {

    private MutableLiveData<List<FeaturedModel>> mediaListObservable;

    public FeaturedVideoListModel(Application application) {
        super(application);
        getFeatureList();
    }
    public void getFeatureList(){
        mediaListObservable = StreamService.getInstance().getFeaturedList();
    }
    public LiveData<List<FeaturedModel>> getFeaturedListObservable() {
        return mediaListObservable;
    }
}
