package com.kyser.demosuite.view.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.kyser.demosuite.R;
import com.kyser.demosuite.service.model.CategoryModel;
import com.kyser.demosuite.service.model.ListingModel;
import com.kyser.demosuite.service.model.TrackModel;
import com.kyser.demosuite.view.ui.adaptor.AlbumListAdaptor;
import com.kyser.demosuite.view.ui.adaptor.MediaListAdaptor;
import com.kyser.demosuite.view.ui.components.SpaceItemDecoration;
import com.kyser.demosuite.viewmodel.AudioCategoryListModel;
import com.kyser.demosuite.viewmodel.CategoryListModel;
import com.kyser.demosuite.viewmodel.MediaListModel;

import java.util.ArrayList;
import java.util.List;

public class Albums extends AppCompatActivity implements AlbumListAdaptor.ItemSelection, AdapterView.OnItemSelectedListener {

    private RecyclerView mMediaListView;
    private AlbumListAdaptor mMediaListAdaptor;
    private MediaListModel mListviewModel;
    private List<CategoryModel> mCategoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideNavBars();
        setContentView(R.layout.activity_albums);
        findViewById(R.id.album_sysnopsis).setVisibility(View.GONE);
        findViewById(R.id.audio_player).setVisibility(View.GONE);
        setListAdaptor();
        initViewModel();
        final AudioCategoryListModel mCategoryListModel =  ViewModelProviders.of(this).get(AudioCategoryListModel.class);
        observeCategoryViewModel(mCategoryListModel);
    }
    private void initViewModel(){
        mListviewModel =  ViewModelProviders.of(this).get(MediaListModel.class);
        observeViewModel(mListviewModel);
    }

    private void observeViewModel(MediaListModel viewModel) {
        if(viewModel.getMediaListObservable()!=null)
            viewModel.getMediaListObservable().observe(this, new Observer<List<ListingModel>>() {
                @Override
                public void onChanged(@Nullable List<ListingModel> projects) {
                    if (projects != null) {
                        mMediaListAdaptor.setCategoryList(projects);
                    }
                }
            });
    }

    private void setListAdaptor() {
        mMediaListView = findViewById(R.id.album_listing);
        mMediaListView.setLayoutManager(new GridLayoutManager(this,2));
        mMediaListAdaptor = new AlbumListAdaptor(this,this);
        mMediaListView.addItemDecoration(new SpaceItemDecoration(10,10));
        mMediaListView.setAdapter(mMediaListAdaptor);
    }

    private void observeCategoryViewModel(AudioCategoryListModel viewModel) {
        viewModel.getAudioCatObservable().observe(this, projects -> {
            if (projects != null) {
                mCategoryList = projects;
                setSpinnerlist();
            }
        });
    }

    private void setSpinnerlist() {
        List<String> categories = new ArrayList<String>();
        for( CategoryModel cat : mCategoryList){
            categories.add(cat.getTitle());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.category_spinner, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = findViewById(R.id.album_cat_spinner);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    protected void hideNavBars() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onItemSelection(ListingModel categoryModel, int position) {
        findViewById(R.id.album_sysnopsis).setVisibility(View.VISIBLE);
        AlbumSynopsis fragment =(AlbumSynopsis) getSupportFragmentManager().findFragmentById(R.id.album_sysnopsis);
        fragment.setAlbumSynopsisDetails(categoryModel,mListviewModel.getMediaListObservable().getValue());
    }

    public void showPlayerFragment(ListingModel currentAlbumModel,List<TrackModel>  mediaModel, int position) {
        if(findViewById(R.id.album_sysnopsis).getVisibility()==View.VISIBLE)
            findViewById(R.id.album_sysnopsis).setVisibility(View.GONE);
        findViewById(R.id.audio_player).setVisibility(View.VISIBLE);
        AudioPlayer fragment =(AudioPlayer) getSupportFragmentManager().findFragmentById(R.id.audio_player);
        fragment.setModel(currentAlbumModel,mediaModel,position);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("parent = [" + parent + "], view = [" + view + "], position = [" + position + "], id = [" + id + "]");
        mListviewModel.setMediaCategory(mCategoryList.get(position).getCid());
        observeViewModel(mListviewModel);
        hideNavBars();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        hideNavBars();
    }

    @Override
    public void onBackPressed() {
        if(findViewById(R.id.album_sysnopsis).getVisibility()==View.VISIBLE)
            findViewById(R.id.album_sysnopsis).setVisibility(View.GONE);
        else if(findViewById(R.id.audio_player).getVisibility()==View.VISIBLE)
            findViewById(R.id.audio_player).setVisibility(View.GONE);
        else {
            super.onBackPressed();
        }
    }
}
