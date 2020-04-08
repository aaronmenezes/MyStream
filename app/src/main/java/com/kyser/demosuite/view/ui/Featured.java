package com.kyser.demosuite.view.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.kyser.demosuite.R;
import com.kyser.demosuite.service.downloadservice.DirectoryHelper;
import com.kyser.demosuite.service.model.FeaturedModel;
import com.kyser.demosuite.service.model.ListingModel;
import com.kyser.demosuite.service.streamservice.StreamService;
import com.kyser.demosuite.view.ui.adaptor.FeaturedAdaptor;
import com.kyser.demosuite.view.ui.adaptor.MediaListAdaptor;
import com.kyser.demosuite.view.ui.components.SpaceItemDecoration;
import com.kyser.demosuite.viewmodel.FeaturedVideoListModel;
import com.kyser.demosuite.viewmodel.MediaListModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Featured extends AppCompatActivity implements MediaListAdaptor.ItemSelection, FeaturedAdaptor.ItemSelection, Synopsis.OnFragmentInteractionListener {

    private FeaturedAdaptor mFeaturedAdaptor;
    private MediaListModel mMovielistModel, mTVlistModel, mAudiolistModel;
    private List<ListingModel> mFeaturedModel;
    private RecyclerView mMovieList, mTVList, mAudioList;
    private MediaListAdaptor mMovieListAdaptor, mTVListAdaptor, mAudioListAdaptor;
    final private int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 54654;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_featured);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        hideNavBars();
        initIds();
        final FeaturedVideoListModel viewModel = ViewModelProviders.of(this).get(FeaturedVideoListModel.class);
        observeViewModel(viewModel);
        initViewModels();
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        FirebaseMessaging.getInstance().subscribeToTopic(getResources().getString(R.string.topic));
        String appLinkAction = appLinkIntent.getAction();
        if(appLinkAction != null) {
            Uri appLinkData = appLinkIntent.getData();
            Log.v("appLinkData = [", appLinkData.getLastPathSegment() + "]");
            //StreamService.getInstance().getSynopsisModel(Integer.parseInt(appLinkData.getLastPathSegment() , 10),synopsisModel -> onInfoSelection(synopsisModel,0));
        }
    }

    private void initIds() {
        findViewById(R.id.featured_synopsis).setVisibility(View.GONE);
        ViewPager viewPager = (ViewPager) findViewById(R.id.carousel);
        mFeaturedAdaptor = new FeaturedAdaptor(this,this);
        viewPager.setAdapter(mFeaturedAdaptor);

        mMovieList = findViewById(R.id.feature_movie_list);
        mMovieList.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        mMovieList.addItemDecoration(new SpaceItemDecoration(1 ,20));
        mMovieListAdaptor = new MediaListAdaptor(this,this);
        mMovieList.setAdapter(mMovieListAdaptor);

        mTVList = findViewById(R.id.feature_tv_list);
        mTVList.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        mTVList.addItemDecoration(new SpaceItemDecoration(1 ,20));
        mTVListAdaptor = new MediaListAdaptor(this,this);
        mTVList.setAdapter(mTVListAdaptor);

        mAudioList = findViewById(R.id.feature_audio_list);
        mAudioList.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        mAudioList.addItemDecoration(new SpaceItemDecoration(1 ,20));
        mAudioListAdaptor = new MediaListAdaptor(this,this);
        mAudioList.setAdapter(mAudioListAdaptor);

        findViewById(R.id.btn_movie).setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Listing.class);
            startActivity(intent);
        });
        findViewById(R.id.btn_music).setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Albums.class);
            startActivity(intent);
        });
    }

    private void observeViewModel(FeaturedVideoListModel viewModel) {
        viewModel.getFeaturedListObservable().observe(this, new Observer<List<FeaturedModel>>() {
            @Override
            public void onChanged(@Nullable List<FeaturedModel> projects) {
                if (projects != null) {
                   // mFeaturedAdaptor.setCarouselModel(projects);
                }
            }
        });
    }
    private void initViewModels(){
        mFeaturedModel = new ArrayList<ListingModel>();
        mMovielistModel =  ViewModelProviders.of(this).get(MediaListModel.class);
        mMovielistModel.setMediaType("movie");
        mMovielistModel.setMediaCategory(38);
        observeMovieModel(mMovielistModel);
        mTVlistModel =  ViewModelProviders.of(this).get(MediaListModel.class);
        mMovielistModel.setMediaType("tv");
        mTVlistModel.setMediaCategory(48);
        observeTVModel(mTVlistModel);
        mAudiolistModel =  ViewModelProviders.of(this).get(MediaListModel.class);
        mMovielistModel.setMediaType("audio");
        mAudiolistModel.setMediaCategory(57);
        observeAudioModel(mAudiolistModel);
    }
    private void observeMovieModel(MediaListModel viewModel) {
        viewModel.getMediaListObservable().observe(this, new Observer<List<ListingModel>>() {
            @Override
            public void onChanged(@Nullable List<ListingModel> projects) {
                if (projects != null) {
                    mMovieListAdaptor.setCategoryList(projects);
                    Random rand = new Random();                    ;
                    mFeaturedModel.add(projects.get(rand.nextInt(projects.size())));
                    mFeaturedModel.add(projects.get(rand.nextInt(projects.size())));
                    mFeaturedModel.add(projects.get(rand.nextInt(projects.size())));
                    mFeaturedAdaptor.setCarouselModel(mFeaturedModel);
                }
            }
        });
    }
    private void observeTVModel(MediaListModel viewModel) {
        viewModel.getMediaListObservable().observe(this, new Observer<List<ListingModel>>() {
            @Override
            public void onChanged(@Nullable List<ListingModel> projects) {
                if (projects != null) {
                    mTVListAdaptor.setCategoryList(projects);
                    Random rand = new Random();                    ;
                    mFeaturedModel.add(projects.get(rand.nextInt(projects.size())));
                    mFeaturedModel.add(projects.get(rand.nextInt(projects.size())));
                    mFeaturedModel.add(projects.get(rand.nextInt(projects.size())));
                    mFeaturedAdaptor.setCarouselModel(mFeaturedModel);
                }
            }
        });
    }
    private void observeAudioModel(MediaListModel viewModel) {
        viewModel.getMediaListObservable().observe(this, new Observer<List<ListingModel>>() {
            @Override
            public void onChanged(@Nullable List<ListingModel> projects) {
                if (projects != null) {
                    mAudioListAdaptor.setCategoryList(projects);
                    Random rand = new Random();                    ;
                  //  mFeaturedModel.add(projects.get(rand.nextInt(projects.size())));
                  //  mFeaturedModel.add(projects.get(rand.nextInt(projects.size())));
                  //  mFeaturedModel.add(projects.get(rand.nextInt(projects.size())));
                  //  mFeaturedAdaptor.setCarouselModel(mFeaturedModel);
                }
            }
        });
    }
    protected void hideNavBars() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onPlaySelection(ListingModel MediaList, int position) {
        Intent intent =  new Intent(this, Player.class);
        intent.putExtra("VIDEO_URI",getResources().getString(R.string.video_demo));
        startActivity(intent);
    }

    @Override
    public void onInfoSelection(ListingModel mediaList, int position) {
        findViewById(R.id.featured_synopsis).setVisibility(View.VISIBLE);
        Synopsis fragment =(Synopsis) getSupportFragmentManager().findFragmentById(R.id.featured_synopsis);
        fragment.setSynopsisDetails(mediaList,mFeaturedAdaptor.getCarouselModel());
    }

    @Override
    public void onItemSelection(ListingModel mediaList, int position) {
        findViewById(R.id.featured_synopsis).setVisibility(View.VISIBLE);
        Synopsis fragment =(Synopsis) getSupportFragmentManager().findFragmentById(R.id.featured_synopsis);
        fragment.setSynopsisDetails(mediaList,mMovielistModel.getMediaListObservable().getValue());
    }

    @Override
    public void onMoreSelected(ListingModel model) {
        Synopsis fragment =(Synopsis) getSupportFragmentManager().findFragmentById(R.id.featured_synopsis);
        fragment.setSynopsisDetails(model,mMovielistModel.getMediaListObservable().getValue());
    }

    @Override
    public void onBackPressed() {
        if(findViewById(R.id.featured_synopsis).getVisibility()==View.VISIBLE)
            findViewById(R.id.featured_synopsis).setVisibility(View.GONE);
        else
            super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Synopsis.setListener(this);
        hideNavBars();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                DirectoryHelper.createDirectory(this);
        }
    }
}
