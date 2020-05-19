package com.kyser.demosuite.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kyser.demosuite.service.model.ListingModel
import com.kyser.demosuite.service.streamservice.StreamService

class SearchListModel : ViewModel() {

  private var mediaListObservable: MutableLiveData<List<ListingModel>>? = null

    fun getSearchList(title: String) {
        mediaListObservable = StreamService.getInstance().getSearchTitles(title)
    }

    fun getSearchListModel () : MutableLiveData<List<ListingModel>>? {return mediaListObservable}
}