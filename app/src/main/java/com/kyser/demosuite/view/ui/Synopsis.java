package com.kyser.demosuite.view.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.kyser.demosuite.R;
import com.kyser.demosuite.service.downloadservice.DirectoryHelper;
import com.kyser.demosuite.service.downloadservice.DownloadService;
import com.kyser.demosuite.service.model.FeaturedModel;
import com.kyser.demosuite.service.model.ListingModel;
import com.kyser.demosuite.service.preferences.HistoryService;
import com.kyser.demosuite.service.streamservice.StreamService;
import com.kyser.demosuite.view.ui.adaptor.FeaturedAdaptor;
import com.kyser.demosuite.view.ui.adaptor.MediaListAdaptor;
import com.kyser.demosuite.view.ui.components.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class Synopsis extends Fragment implements View.OnClickListener {
    private RecyclerView mMoreList;
    private MediaListAdaptor mMovieListAdaptor;
    private FeaturedAdaptor mfeatuiredListAdaptor;

    private static OnFragmentInteractionListener mListener;
    private ListingModel mCurrentModel;

    public Synopsis() {}

    public static Synopsis newInstance(String param1, String param2) {
        Synopsis fragment = new Synopsis();
        return fragment;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.play_video ||v.getId() ==  R.id.btn_trailer){
           HistoryService.getInstance().logMovieItem(getContext(), mCurrentModel);
           int videoURI = v.getId() == R.id.play_video? R.string.video_demo:R.string.trailer_demo;
           Intent intent =  new Intent(getContext(), Player.class);
           intent.putExtra("VIDEO_URI",getResources().getString(videoURI));
           startActivity(intent);
        }
        else if (v.getId() == R.id.btn_share){
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Watch : "+ ((TextView)this.getView().findViewById(R.id.syn_title)).getText()+" \n Story : "+((TextView)this.getView().findViewById(R.id.syn_desc)).getText();
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Watch : "+ ((TextView)this.getView().findViewById(R.id.syn_title)).getText());
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
        else if(v.getId() == R.id.btn_download){
            downloadVideo();
        }
        else if(v.getId() == R.id.btn_watchlist){
            Toast toast = Toast.makeText(getContext(), "This Sections is a WIP. Memorize the watchlist yourself.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void downloadVideo() {
        String SONG_DOWNLOAD_PATH = "https://stream-a1.herokuapp.com/getDownloadVideo";
        getContext().startService(DownloadService.getDownloadService(getContext(), SONG_DOWNLOAD_PATH, DirectoryHelper.ROOT_DIRECTORY_NAME.concat("/")));
    }

    interface OnFragmentInteractionListener {
        void onMoreSelected(ListingModel model);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.synopsis, container, false);
        mMoreList = view.findViewById(R.id.more_list);
        mMoreList.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        view.findViewById(R.id.play_video).setOnClickListener(this);
        view.findViewById(R.id.btn_download).setOnClickListener(this);
        view.findViewById(R.id.btn_share).setOnClickListener(this);
        view.findViewById(R.id.btn_trailer).setOnClickListener(this);
        view.findViewById(R.id.btn_watchlist).setOnClickListener(this);
        return view;
    }

    static void setListener(OnFragmentInteractionListener listener){mListener = listener;}
    void setSynopsisDetails( ListingModel model ,int cid , int scid , List<ListingModel> listingmodel){
        System.out.println("model = [" + model.getMid() + "], cid = [" + cid + "], scid = [" + scid + "], listingmodel = [" + listingmodel + "]");
        StreamService.getInstance().getSynopsisModel(model.getMid(), cid, scid, synopsisModel -> {

        });
        setSynopsisDetails( model, listingmodel);
    }

    void setSynopsisDetails( ListingModel model, List<ListingModel> listingmodel){
        mCurrentModel = model;
        ((TextView)this.getView().findViewById(R.id.syn_title)).setText(model.getTitle());
        ((TextView)this.getView().findViewById(R.id.syn_desc)).setText(model.getDescription());
        ImageView poster = (ImageView) getView().findViewById(R.id.syn_poster);
        StringBuilder b_url = new StringBuilder(getContext().getString(R.string.BASE_URL));
        b_url.append("getImageAsset/");
        b_url.append(model.getPoster());
        Glide.with(getView())
                .load(b_url.toString())
                .fitCenter()
                .placeholder(R.drawable.poster_unavailable) 
                .transition(GenericTransitionOptions.with(R.anim.synopsis_poster_in))
                .into(poster) ;
        List<ListingModel> moreListingModel = new ArrayList<ListingModel>() ;
        moreListingModel.addAll(listingmodel);
        for(ListingModel curModel: moreListingModel){
            if(curModel.getMid() == model.getMid()) {
                moreListingModel.remove(curModel);
                break;
            }
        }
        if(mMovieListAdaptor==null) {
            mMovieListAdaptor = new MediaListAdaptor(getContext(), (mediaList, position) -> mListener.onMoreSelected(mediaList));
            mMoreList.addItemDecoration(new SpaceItemDecoration(5, 20));
            mMoreList.setAdapter(mMovieListAdaptor);
        }
        mMovieListAdaptor.setCategoryList(moreListingModel);
    }
}
