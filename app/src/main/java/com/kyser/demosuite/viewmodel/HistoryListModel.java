package com.kyser.demosuite.viewmodel;

import android.app.Application;

import com.kyser.demosuite.service.model.ListingModel;
import com.kyser.demosuite.service.preferences.HistoryService;
import com.kyser.demosuite.service.streamservice.StreamService;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class HistoryListModel extends AndroidViewModel {

    private MutableLiveData<List<ListingModel>> mediaListObservable;
    private String mediaType;

    public HistoryListModel(Application application) {
        super(application);
        mediaListObservable = HistoryService.getInstance().getMovieHistory();
    }

    public LiveData<List<ListingModel>> getHistoryListObservable() {
        return mediaListObservable;
    }


}
