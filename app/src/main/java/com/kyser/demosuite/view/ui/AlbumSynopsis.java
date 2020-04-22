package com.kyser.demosuite.view.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kyser.demosuite.R;
import com.kyser.demosuite.service.model.ListingModel;
import com.kyser.demosuite.service.model.TrackModel;
import com.kyser.demosuite.service.streamservice.StreamService;
import com.kyser.demosuite.view.ui.adaptor.TrackListAdaptor;
import com.kyser.demosuite.view.ui.components.SpaceItemDecoration;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//https://www.dev2qa.com/android-play-audio-file-in-background-service-example/
public class AlbumSynopsis extends Fragment implements View.OnClickListener {
    private static OnFragmentInteractionListener mListener;
    private RecyclerView mtracklistview;
    private TrackListAdaptor mTrackAdaptor;
    private ListingModel mCurrentAlbumModel;

    public AlbumSynopsis() {}



    public static AlbumSynopsis newInstance(String param1, String param2) {
        AlbumSynopsis fragment = new AlbumSynopsis();
        return fragment;
    }

    @Override
    public void onClick(View v) {
       if(v.getId() == R.id.play_all_button){
            ((Albums)getActivity()).showPlayerFragment(mCurrentAlbumModel,mTrackAdaptor.getTracklistModel(),0);
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
        View view = inflater.inflate(R.layout.album_synopsis, container, false);
        mtracklistview = view.findViewById(R.id.album_syn_tracks);
        mtracklistview.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        mtracklistview.addItemDecoration(new SpaceItemDecoration(10,1));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        mtracklistview.addItemDecoration(itemDecoration);
        mTrackAdaptor = new TrackListAdaptor(getContext(), new TrackListAdaptor.ItemSelection() {
            @Override
            public void onItemSelection(TrackModel MediaList, int position) {
                ((Albums)getActivity()).showPlayerFragment(mCurrentAlbumModel,mTrackAdaptor.getTracklistModel(),position);
            }
        });
        mtracklistview.setAdapter(mTrackAdaptor);
        view.findViewById(R.id.play_all_button).setOnClickListener(this);
        view.findViewById(R.id.start_audio_in_background).setOnClickListener(this);
        view.findViewById(R.id.stop_audio_in_background).setOnClickListener(this);
        view.findViewById(R.id.pause_audio_in_background).setOnClickListener(this);
        return view;
    }

    static void setListener(OnFragmentInteractionListener listener){
        mListener = listener;
    }

    void setAlbumSynopsisDetails( ListingModel model, List<ListingModel> listingmodel) {
        mCurrentAlbumModel = model;
        View v = this.getView();
        ((TextView) this.getView().findViewById(R.id.album_syn_title)).setText(model.getTitle());
        ((TextView) this.getView().findViewById(R.id.album_syn_genre)).setText(model.getGenre());
        ImageView poster = (ImageView) getView().findViewById(R.id.album_syn_poster);
        StringBuilder b_url = new StringBuilder(getContext().getString(R.string.BASE_URL));
        b_url.append("getImageAsset/");
        b_url.append(model.getPoster());
        Glide.with(getView())
                .load(b_url.toString())
                .fitCenter()
                .placeholder(R.drawable.poster_unavailable)
                .into(poster);
        StreamService.getInstance().getTracklist(model.getMid(), tracklist -> {
            ((TextView) v.findViewById(R.id.album_syn_artist)).setText(tracklist.get(0).getArtist());
            mTrackAdaptor.setCategoryList(tracklist);
        });
    }
}
