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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kyser.demosuite.R;
import com.kyser.demosuite.service.model.FeaturedModel;
import com.kyser.demosuite.service.model.ListingModel;
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

    public Synopsis() {}

    public static Synopsis newInstance(String param1, String param2) {
        Synopsis fragment = new Synopsis();
        return fragment;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.play_video){
           Intent intent =  new Intent(getContext(), Player.class);
           startActivity(intent);
        }
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
        ImageButton playVideo = (ImageButton) view.findViewById(R.id.play_video);
        playVideo.setOnClickListener(this);
        return view;
    }

    static void setListener(OnFragmentInteractionListener listener){mListener = listener;}
    void setSynopsisDetails( ListingModel model, List<ListingModel> listingmodel){
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
                .into(poster);
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
