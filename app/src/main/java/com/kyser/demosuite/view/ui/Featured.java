package com.kyser.demosuite.view.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.kyser.demosuite.R;
import com.kyser.demosuite.service.model.FeaturedModel;
import com.kyser.demosuite.service.model.ListingModel;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_featured);
        hideNavBars();
        initIds();
        final FeaturedVideoListModel viewModel =  ViewModelProviders.of(this).get(FeaturedVideoListModel.class);
        observeViewModel(viewModel);
        initViewModels();
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
}
