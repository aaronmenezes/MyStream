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

import com.kyser.demosuite.R;
import com.kyser.demosuite.service.model.ListingModel;
import com.kyser.demosuite.view.ui.adaptor.MediaListAdaptor;
import com.kyser.demosuite.view.ui.components.SpaceItemDecoration;
import com.kyser.demosuite.viewmodel.HistoryListModel;
import com.kyser.demosuite.viewmodel.MediaListModel;

import java.util.List;

public class HistoryActivity extends AppCompatActivity implements MediaListAdaptor.ItemSelection , Synopsis.OnFragmentInteractionListener {

    private HistoryListModel mListviewModel;
    private RecyclerView mMediaListView;
    private MediaListAdaptor mMediaListAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideNavBars();
        setContentView(R.layout.activity_history);
        findViewById(R.id.history_synopsis).setVisibility(View.GONE);
        setListAdaptor();
        initViewModel();
        Synopsis.setListener(this);
    }

    private void setListAdaptor() {
        mMediaListView = findViewById(R.id.history_grid);
        mMediaListView.setLayoutManager(new GridLayoutManager(this,3));
        mMediaListAdaptor = new MediaListAdaptor(this,this);
        mMediaListView.addItemDecoration(new SpaceItemDecoration(30,1));
        mMediaListView.setAdapter(mMediaListAdaptor);
    }
    private void initViewModel(){
        mListviewModel =  ViewModelProviders.of(this).get(HistoryListModel.class);
        observeViewModel(mListviewModel);
    }

    private void observeViewModel(HistoryListModel viewModel) {
        if(viewModel.getHistoryListObservable()!=null)
            viewModel.getHistoryListObservable().observe(this, projects -> {
                if (projects != null) {
                    mMediaListAdaptor.setCategoryList(projects);
                }
            });
    }

    @Override
    public void onItemSelection(ListingModel categoryModel, int position) {
        findViewById(R.id.history_synopsis).setVisibility(View.VISIBLE);
        Synopsis fragment =(Synopsis) getSupportFragmentManager().findFragmentById(R.id.history_synopsis);
        fragment.setSynopsisDetails(categoryModel,mListviewModel.getHistoryListObservable().getValue());
    }

    @Override
    public void onMoreSelected(ListingModel model) {
        Synopsis fragment =(Synopsis) getSupportFragmentManager().findFragmentById(R.id.history_synopsis);
        fragment.setSynopsisDetails(model,mListviewModel.getHistoryListObservable().getValue());
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
    public void onBackPressed() {
        if(findViewById(R.id.history_synopsis).getVisibility()==View.VISIBLE)
            findViewById(R.id.history_synopsis).setVisibility(View.GONE);
        else {
            super.onBackPressed();
        }
    }
}
