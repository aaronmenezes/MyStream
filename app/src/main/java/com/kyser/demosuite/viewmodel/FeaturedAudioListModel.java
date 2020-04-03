package com.kyser.demosuite.viewmodel;

import android.app.Application;

import com.kyser.demosuite.service.model.ListingModel;
import com.kyser.demosuite.service.streamservice.StreamService;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class FeaturedAudioListModel extends AndroidViewModel {

    private MutableLiveData<List<ListingModel>> mediaListObservable;

    public FeaturedAudioListModel(Application application) {
        super(application);
    }
    public void setMediaCategory(int cid){
        mediaListObservable = StreamService.getInstance().getMediaList(cid);
    }
    public LiveData<List<ListingModel>> getMediaListObservable() {
        return mediaListObservable;
    }
}
