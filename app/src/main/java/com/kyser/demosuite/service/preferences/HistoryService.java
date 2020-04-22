package com.kyser.demosuite.service.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.kyser.demosuite.service.model.ListingModel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.MutableLiveData;

public class HistoryService {

    static HistoryService __instance;
    private ArrayList<ListingModel> mMovieHistory;

    public HistoryService() {}
    public static HistoryService getInstance(){
        if(__instance == null)
            __instance =  new HistoryService();
        return  __instance;
    }

    public void logMovieItem(Context context , ListingModel model){
        SharedPreferences pref = context.getSharedPreferences("MovieHistory", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        StringBuilder st = new StringBuilder(model.getMid().toString());
        st.append("|");
        st.append(model.getTitle());
        st.append("|");
        st.append(model.getPoster());
        st.append("|");
        st.append(model.getDescription());
        editor.putString(model.getMid().toString(), st.toString());
        editor.apply();
        getHistory(context);
    }

    public void getHistory(Context context){
        SharedPreferences pref = context.getSharedPreferences("MovieHistory", Context.MODE_PRIVATE);
        Map<String, ?> history_map = pref.getAll();
        mMovieHistory = new ArrayList<ListingModel>();
        for( Map.Entry<String,?> entry :history_map.entrySet()){
            Log.d("map values",entry.getKey() + ": " +   entry.getValue().toString());
            String [] st =  entry.getValue().toString().split("\\|");
            ListingModel lt = new ListingModel();
            lt.setId(Integer.parseInt(entry.getKey()) ); lt.setMid(Integer.parseInt(entry.getKey()));
            lt.setTitle(st[1]);lt.setPoster(st[2]);lt.setDescription(st[3]);
            mMovieHistory.add(lt);
        }
    }

    public MutableLiveData<List<ListingModel>> getMovieHistory(){
        final MutableLiveData<List<ListingModel>> data = new MutableLiveData<>();
        data.setValue(mMovieHistory);
        return data;
    }
}
