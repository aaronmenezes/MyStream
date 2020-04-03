package com.kyser.demosuite.viewmodel;

import android.app.Application;

import com.kyser.demosuite.service.model.CategoryModel;
import com.kyser.demosuite.service.streamservice.StreamService;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class CategoryListModel extends AndroidViewModel {

    private final MutableLiveData<List<CategoryModel>> videoCategoryObservable;

    public CategoryListModel(Application application) {
        super(application);
        videoCategoryObservable = StreamService.getInstance().getVideoCategory();
    }
    public LiveData<List<CategoryModel>> getVideoCatObservable() {
        return videoCategoryObservable;
    }
}
