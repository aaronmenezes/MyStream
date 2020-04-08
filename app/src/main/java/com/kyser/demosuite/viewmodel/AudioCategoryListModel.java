package com.kyser.demosuite.viewmodel;

import android.app.Application;

import com.kyser.demosuite.service.model.CategoryModel;
import com.kyser.demosuite.service.streamservice.StreamService;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


public class AudioCategoryListModel extends AndroidViewModel {

    private final MutableLiveData<List<CategoryModel>> audioCategoryObservable;

    public AudioCategoryListModel(Application application) {
        super(application);
        audioCategoryObservable = StreamService.getInstance().getAudioCategory();
    }
    public LiveData<List<CategoryModel>> getAudioCatObservable() {
        return audioCategoryObservable;
    }
}
