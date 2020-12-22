package com.kyser.demosuite.view.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kyser.demosuite.R;
import com.kyser.demosuite.service.downloadservice.DirectoryHelper;
import com.kyser.demosuite.service.model.ListingModel;
import com.kyser.demosuite.service.preferences.HistoryService;
import com.kyser.demosuite.view.ui.adaptor.AlbumListAdaptor;
import com.kyser.demosuite.view.ui.adaptor.FeaturedAdaptor;
import com.kyser.demosuite.view.ui.adaptor.MediaListAdaptor;
import com.kyser.demosuite.view.ui.components.SpaceItemDecoration;
import com.kyser.demosuite.viewmodel.FeaturedVideoListModel;
import com.kyser.demosuite.viewmodel.MediaListModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Featured extends AppCompatActivity implements FeaturedAdaptor.ItemSelection, Synopsis.OnFragmentInteractionListener, View.OnClickListener {

    private FeaturedAdaptor mFeaturedAdaptor;
    private MediaListModel mMovielistModel, mTVlistModel, mAudiolistModel;
    private List<ListingModel> mFeaturedModel;
    private RecyclerView mMovieList, mTVList, mAudioList;
    private MediaListAdaptor mMovieListAdaptor, mTVListAdaptor;
    private AlbumListAdaptor mAudioListAdaptor;
    final private int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 54654;
    final private int ACCESS_COARSE_LOCATION_REQUEST_CODE = 54754;
    final private int ACCESS_FINE_LOCATION_REQUEST_CODE = 54854;

    // keytool -keystore path-to-debug-or-production-keystore -list -v
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_featured);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);

        findViewById(R.id.album_sysnopsis).setVisibility(View.GONE);
        hideNavBars();
        initIds();
        final FeaturedVideoListModel viewModel = ViewModelProviders.of(this).get(FeaturedVideoListModel.class);
        observeViewModel(viewModel);
        initViewModels();
        setAccountUi();
        Intent appLinkIntent = getIntent();
        FirebaseMessaging.getInstance().subscribeToTopic(getResources().getString(R.string.topic));
        String appLinkAction = appLinkIntent.getAction();
        if(appLinkAction != null) {
            Uri appLinkData = appLinkIntent.getData();
            Log.v("appLinkData = [", appLinkData.getLastPathSegment() + "]");
            //StreamService.getInstance().getSynopsisModel(Integer.parseInt(appLinkData.getLastPathSegment() , 10),synopsisModel -> onInfoSelection(synopsisModel,0));
        }
        PackageManager manager = this.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), PackageManager.GET_ACTIVITIES);
            ((TextView)findViewById(R.id.version_lbl)).setText(String.format("ver %s", info.versionName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setAccountUi() {
        ((TextView)findViewById(R.id.profile_name)).setText(getIntent().getStringExtra(getString(R.string.account_name)));
        ((TextView)findViewById(R.id.panel_profile_name)).setText(new StringBuilder().append(getIntent().getStringExtra(getString(R.string.account_given_name))).append(" ").append(getIntent().getStringExtra(getString(R.string.account_family_name))).toString());
        ((TextView)findViewById(R.id.panel_profile_mail)).setText(getIntent().getStringExtra(getString(R.string.account_mail)));
        ImageView im =findViewById(R.id.profile_picture);
        Glide.with(im)
                .load(getIntent().getStringExtra(getString(R.string.account_face)))
                .fitCenter()
                .placeholder(R.drawable.user_picture)
                .into(im);
        im =findViewById(R.id.panel_profile_picture);
        Glide.with(im)
                .load(getIntent().getStringExtra(getString(R.string.account_face)))
                .fitCenter()
                .placeholder(R.drawable.user_picture)
                .into(im);
    }

    private void initIds() {
        findViewById(R.id.featured_synopsis).setVisibility(View.GONE);
        ViewPager viewPager = (ViewPager) findViewById(R.id.carousel);
        mFeaturedAdaptor = new FeaturedAdaptor(this,this);
        viewPager.setAdapter(mFeaturedAdaptor);
        startCarousel(viewPager,mFeaturedAdaptor);
        mMovieList = findViewById(R.id.feature_movie_list);
        mMovieList.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        mMovieList.addItemDecoration(new SpaceItemDecoration(1 ,20));
        mMovieListAdaptor = new MediaListAdaptor(this,mMovieSelected);
        mMovieList.setAdapter(mMovieListAdaptor);

        mTVList = findViewById(R.id.feature_tv_list);
        mTVList.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        mTVList.addItemDecoration(new SpaceItemDecoration(1 ,20));
        mTVListAdaptor = new MediaListAdaptor(this,mTvSelected);
        mTVList.setAdapter(mTVListAdaptor);

        mAudioList = findViewById(R.id.feature_audio_list);
        mAudioList.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        mAudioList.addItemDecoration(new SpaceItemDecoration(1 ,10));
        mAudioListAdaptor = new AlbumListAdaptor(this,mAlbumSelection);
        mAudioList.setAdapter(mAudioListAdaptor);

        findViewById(R.id.history_menu).setOnClickListener(this);
        findViewById(R.id.navbar_overlay).setOnClickListener(this);
        findViewById(R.id.btn_movie).setOnClickListener(this);
        findViewById(R.id.btn_music).setOnClickListener(this);
        findViewById(R.id.btn_tv).setOnClickListener(this);
        findViewById(R.id.btn_radio ).setOnClickListener(this);
        findViewById(R.id.movie_history).setOnClickListener(this);
        findViewById(R.id.tv_history).setOnClickListener(this);
        findViewById(R.id.album_history).setOnClickListener(this);
        findViewById(R.id.update_media).setOnClickListener(this);
        findViewById(R.id.gmap_demo).setOnClickListener(this);
        findViewById(R.id.app_demo).setOnClickListener(this);
        findViewById(R.id.insights).setOnClickListener(this);
        findViewById(R.id.profile_stage).setOnClickListener(this);
        findViewById(R.id.panel_close).setOnClickListener(this);
        findViewById(R.id.panel_sign_out).setOnClickListener(this);
    }

    public void startCarousel(ViewPager viewPager, FeaturedAdaptor mFeaturedAdaptor){
        new Handler().postDelayed(() -> {
            if(viewPager.getCurrentItem() +1 < mFeaturedAdaptor.getCount())
                viewPager.setCurrentItem(viewPager.getCurrentItem() +1 ,true);
            else
                viewPager.setCurrentItem(0, true );
            startCarousel(viewPager, mFeaturedAdaptor);
        }, 10*1000);
    }

    private void observeViewModel(FeaturedVideoListModel viewModel) {
        viewModel.getFeaturedListObservable().observe(this, projects -> {
            if (projects != null) {
               // mFeaturedAdaptor.setCarouselModel(projects);
            }
        });
    }
    private void initViewModels(){
        mFeaturedModel = new ArrayList<>();
        mMovielistModel =  ViewModelProviders.of(this).get(MediaListModel.class);
        mMovielistModel.setMediaType("movie");
        mMovielistModel.setMediaCategory(38);
        observeMovieModel(mMovielistModel);
        mTVlistModel =  ViewModelProviders.of(this).get(MediaListModel.class);
        mTVlistModel.setMediaType("tv");
        mTVlistModel.setMediaCategory(48);
        observeTVModel(mTVlistModel);
        mAudiolistModel =  ViewModelProviders.of(this).get(MediaListModel.class);
        mAudiolistModel.setMediaType("audio");
        mAudiolistModel.setMediaCategory(57);
        observeAudioModel(mAudiolistModel);

    }
    private void observeMovieModel(MediaListModel viewModel) {
        viewModel.getMediaListObservable().observe(this, projects -> {
            if (projects != null) {
                mMovieListAdaptor.setCategoryList(projects);
                Random rand = new Random();
                mFeaturedModel.add(projects.get(rand.nextInt(projects.size())));
                mFeaturedModel.add(projects.get(rand.nextInt(projects.size())));
                mFeaturedModel.add(projects.get(rand.nextInt(projects.size())));
                mFeaturedAdaptor.setCarouselModel(mFeaturedModel);
                findViewById(R.id.shimmer_view_container).setVisibility(View.GONE);
                findViewById(R.id.main_content_scroll).setVisibility(View.VISIBLE);
            }
        });
    }
    private void observeTVModel(MediaListModel viewModel) {
        viewModel.getMediaListObservable().observe(this, projects -> {
            if (projects != null) {
                mTVListAdaptor.setCategoryList(projects);
                Random rand = new Random();
                mFeaturedModel.add(projects.get(rand.nextInt(projects.size())));
                mFeaturedModel.add(projects.get(rand.nextInt(projects.size())));
                mFeaturedModel.add(projects.get(rand.nextInt(projects.size())));
                mFeaturedAdaptor.setCarouselModel(mFeaturedModel);
                findViewById(R.id.shimmer_view_container).setVisibility(View.GONE);
                findViewById(R.id.main_content_scroll).setVisibility(View.VISIBLE);
            }
        });
    }
    private void observeAudioModel(MediaListModel viewModel) {
        viewModel.getMediaListObservable().observe(this, projects -> {
            if (projects != null) {
                mAudioListAdaptor.setCategoryList(projects);
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
        HistoryService.getInstance().logMovieItem(this, MediaList);
        Intent intent =  new Intent(this, Player.class);
        intent.putExtra("VIDEO_URI",getResources().getString(R.string.video_demo));
        startActivity(intent);
    }

    @Override
    public void onInfoSelection(ListingModel mediaList, int position) {
        findViewById(R.id.featured_synopsis).setVisibility(View.VISIBLE);
        Synopsis fragment =(Synopsis) getSupportFragmentManager().findFragmentById(R.id.featured_synopsis);
        fragment.setSynopsisDetails( mediaList, 27 , 38 ,mFeaturedAdaptor.getCarouselModel() );
        //fragment.setSynopsisDetails(mediaList,mFeaturedAdaptor.getCarouselModel());
    }


    AlbumListAdaptor.ItemSelection mAlbumSelection = new AlbumListAdaptor.ItemSelection() {
        @Override
        public void onItemSelection(ListingModel MediaList, int position) {
            findViewById(R.id.album_sysnopsis).setVisibility(View.VISIBLE);
            AlbumSynopsis fragment =(AlbumSynopsis) getSupportFragmentManager().findFragmentById(R.id.album_sysnopsis);
            fragment.setAlbumSynopsisDetails(MediaList,mAudiolistModel.getMediaListObservable().getValue());
        }
    };

    MediaListAdaptor.ItemSelection mMovieSelected = new MediaListAdaptor.ItemSelection() {
        @Override
        public void onItemSelection(ListingModel MediaList, int position) {
            findViewById(R.id.featured_synopsis).setVisibility(View.VISIBLE);
            Synopsis fragment =(Synopsis) getSupportFragmentManager().findFragmentById(R.id.featured_synopsis);
            fragment.setSynopsisDetails( MediaList, 27 , 38 ,mMovielistModel.getMediaListObservable().getValue() );
        }
    };

    MediaListAdaptor.ItemSelection mTvSelected = new MediaListAdaptor.ItemSelection() {
        @Override
        public void onItemSelection(ListingModel MediaList, int position) {
            findViewById(R.id.featured_synopsis).setVisibility(View.VISIBLE);
            Synopsis fragment =(Synopsis) getSupportFragmentManager().findFragmentById(R.id.featured_synopsis);
            fragment.setSynopsisDetails(MediaList,27 , 48 ,mTVlistModel.getMediaListObservable().getValue());
        }
    };

    @Override
    public void onMoreSelected(ListingModel model) {
        Synopsis fragment =(Synopsis) getSupportFragmentManager().findFragmentById(R.id.featured_synopsis);
        fragment.setSynopsisDetails(model,mMovielistModel.getMediaListObservable().getValue());
    }

    @Override
    public void onBackPressed() {
        if(findViewById(R.id.featured_synopsis).getVisibility()==View.VISIBLE)
            findViewById(R.id.featured_synopsis).setVisibility(View.GONE);
        else if (findViewById(R.id.album_sysnopsis).getVisibility()==View.VISIBLE)
            findViewById(R.id.album_sysnopsis).setVisibility(View.GONE);
        else if(findViewById(R.id.portal_view).getVisibility() == View.VISIBLE)
            findViewById(R.id.portal_view).setVisibility(View.GONE);
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.history_menu || v.getId() == R.id.navbar_overlay) {
            if (findViewById(R.id.profile_panel).getVisibility() == View.VISIBLE) {
                findViewById(R.id.profile_panel).setVisibility(View.GONE);
                findViewById(R.id.navbar_overlay).setVisibility(View.GONE);
            } else toggleNavbar();
        } else if (v.getId() == R.id.btn_movie) {
            Intent intent = new Intent(getApplicationContext(), Listing.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_music) {
            Intent intent = new Intent(getApplicationContext(), Albums.class);
            startActivity(intent);
        }else if (v.getId() == R.id.btn_tv || v.getId() == R.id.btn_radio){
            View contextView = findViewById(R.id.btn_tv);
            Snackbar.make(contextView,"Not Available", Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(getResources().getColor(R.color.snackbar_bg))
                    .setActionTextColor((getResources().getColor(R.color.white)))
//                    .setAction("Use Prime", v1 -> {})
                    .show();
        }else if (v.getId() == R.id.app_demo){
            WebView portal = (WebView) findViewById(R.id.portal_view);
            portal.getSettings().setAppCacheEnabled(false);
            portal.clearCache(true);
            portal.setVisibility(View.VISIBLE);
            portal.loadUrl(getResources().getString(R.string.demo_website_url));
        }
        else if(v.getId() == R.id.update_media){
            toggleNavbar();
            Intent intent = new Intent(this,MediaUpdate.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.movie_history){
            toggleNavbar();
            Intent intent = new Intent(getApplicationContext(), BKHistoryActivity.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.gmap_demo){
            toggleNavbar();
            Intent it = new Intent(this, MapsActivity.class);
            startActivity(it);
        }
        else if(v.getId() == R.id.insights){
            toggleNavbar();
            Intent it = new Intent(this, Insights.class);
            startActivity(it);
        }
        else if(v.getId() == R.id.tv_history){
            toggleNavbar();
            Intent it = new Intent(this, MediaHistory.class);
            startActivity(it);
        }
        else if(v.getId() == R.id.profile_stage){
            findViewById(R.id.profile_panel).setVisibility(View.VISIBLE);
            findViewById(R.id.navbar_overlay).setVisibility(View.VISIBLE);
            toggleNavbar();
        }
        else if(v.getId() == R.id.panel_close){
            findViewById(R.id.profile_panel).setVisibility(View.GONE);
            findViewById(R.id.navbar_overlay).setVisibility(View.GONE);
        }
        else if(v.getId() == R.id.panel_sign_out) {
            Intent intent = new Intent(this, Splash2.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        }else if(v.getId() == R.id.album_history){}

    }

    private void toggleNavbar() {
        RelativeLayout navbar = findViewById(R.id.navbar);
        if(navbar.getVisibility() == View.VISIBLE){
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) { }
                @Override
                public void onAnimationEnd(Animation animation) {
                    if(findViewById(R.id.profile_panel).getVisibility() != View.VISIBLE)
                        findViewById(R.id.navbar_overlay).setVisibility(View.GONE);
                    navbar.setVisibility(View.GONE);
                }
                @Override
                public void onAnimationRepeat(Animation animation) { }
            });
            navbar.startAnimation(animation);


        }else{
            navbar.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    navbar.setVisibility(View.VISIBLE);
                    findViewById(R.id.navbar_overlay).setVisibility(View.VISIBLE);
                }
                @Override
                public void onAnimationEnd(Animation animation) { }
                @Override
                public void onAnimationRepeat(Animation animation) { }
            });
            navbar.startAnimation(animation);
        }
    }
}
