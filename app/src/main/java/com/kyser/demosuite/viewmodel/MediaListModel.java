package com.kyser.demosuite.viewmodel;

import android.app.Application;

import com.kyser.demosuite.service.model.CategoryModel;
import com.kyser.demosuite.service.model.ListingModel;
import com.kyser.demosuite.service.streamservice.StreamService;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MediaListModel extends AndroidViewModel {

    private MutableLiveData<List<ListingModel>> mediaListObservable;
    private String mediaType;

    public MediaListModel(Application application) {
        super(application);
    }
    public void setMediaCategory(int cid){
        mediaListObservable = StreamService.getInstance().getMediaList(cid);
    }

    public void setMediaType(String mediaType){this.mediaType = mediaType;}

    public LiveData<List<ListingModel>> getMediaListObservable() {
        return mediaListObservable;
    }


}
