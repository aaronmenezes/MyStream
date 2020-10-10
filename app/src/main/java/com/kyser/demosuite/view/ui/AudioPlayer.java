package com.kyser.demosuite.view.ui;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.kyser.demosuite.R;
import com.kyser.demosuite.service.audioplayer.AudioService;
import com.kyser.demosuite.service.audioplayer.AudioServiceBinder;
import com.kyser.demosuite.service.model.ListingModel;
import com.kyser.demosuite.service.model.TrackModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AudioPlayer extends Fragment implements View.OnClickListener {

    private AudioServiceBinder audioServiceBinder;
    private ProgressBar backgroundAudioProgress;
    private Handler audioProgressUpdateHandler = null;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            // Cast and assign background service's onBind method returned iBinder object.
            System.out.println("==============componentName = [" + componentName + "], iBinder = [" + iBinder + "]");
            audioServiceBinder = (AudioServiceBinder) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {  }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.audio_player, container, false);
        view.findViewById(R.id.player_back).setOnClickListener(this);
        backgroundAudioProgress = (ProgressBar)view.findViewById(R.id.player_progressBar);
        bindAudioService();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setModel(ListingModel currentAlbumModel, List<TrackModel> tracklist, int position){
        Toast toast = Toast.makeText(getContext(), "This Sections is a WIP. Expect some bad behaviour", Toast.LENGTH_SHORT);
        toast.show();
        ImageView poster = ((ImageView)getView().findViewById(R.id.player_poster));
        StringBuilder b_url = new StringBuilder(getContext().getString(R.string.BASE_URL));
        b_url.append("getAlbumImageAsset/");
        b_url.append(currentAlbumModel.getPoster());
        Glide.with(poster)
                .load(b_url.toString())
                .override(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL)
                .error(Glide.with(poster).load(R.drawable.album_poster2))
                .into(poster);

        ((TextView)getView().findViewById(R.id.player_album)).setText(currentAlbumModel.getTitle());
        ((TextView)getView().findViewById(R.id.player_artist)).setText(tracklist.get(position).getArtist());
        ((TextView)getView().findViewById(R.id.player_track_title)).setText(tracklist.get(position).getTitle());
        getView().findViewById(R.id.player_play).setOnClickListener(this);
        audioServiceBinder.setOnCompletionListener();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unBoundAudioService();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.player_back){
            ((Albums)getActivity()).onBackPressed();
        }else if(v.getId() == R.id.player_play) {
            if (getView().findViewById(R.id.player_play).isSelected()){
                pauseAudioPlay();
                getView().findViewById(R.id.player_play).setSelected(false);
            }else{
                getView().findViewById(R.id.player_play).setSelected(true);
                startAudioPlay();
            }
        }
    }

    private void pauseAudioPlay() {
        audioServiceBinder.pauseAudio();
        Toast.makeText(getContext(), "Play web audio file is paused.", Toast.LENGTH_LONG).show();
    }

    private void stopAudioPlay() {
        audioServiceBinder.stopAudio();
        backgroundAudioProgress.setVisibility(ProgressBar.INVISIBLE);
        Toast.makeText(getContext(), "Stop play web audio file.", Toast.LENGTH_LONG).show();
    }

    private void startAudioPlay() {
        // Set web audio file url
        audioServiceBinder.setAudioFileUrl("https://stream-a1.herokuapp.com/getTestAudio");
        audioServiceBinder.setStreamAudio(true);

        // Set application context.
        audioServiceBinder.setContext(getContext());

        // Initialize audio progress bar updater Handler object.
        createAudioProgressbarUpdater();
        audioServiceBinder.setAudioProgressUpdateHandler(audioProgressUpdateHandler);

        // Start audio in background service.
        audioServiceBinder.startAudio();

        backgroundAudioProgress.setVisibility(ProgressBar.VISIBLE);

        ((ImageButton)getView().findViewById(R.id.player_play)).setSelected(true);
        Toast.makeText(getContext(), "Start play web audio file.", Toast.LENGTH_LONG).show();
    }

    // Bind background service with caller activity. Then this activity can use
    // background service's AudioServiceBinder instance to invoke related methods.
    private void bindAudioService() {
        if(audioServiceBinder == null) {
            Intent intent = new Intent((Albums)getContext(), AudioService.class);
            // Below code will invoke serviceConnection's onServiceConnected method.
            getContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    // Unbound background audio service with caller activity.
    private void unBoundAudioService()  {
        if(audioServiceBinder != null) {
            getContext().unbindService(serviceConnection);
        }
    }

    @SuppressLint("HandlerLeak")
    private void createAudioProgressbarUpdater()
    {
        /* Initialize audio progress handler. */
        if(audioProgressUpdateHandler==null) {
            audioProgressUpdateHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    // The update process message is sent from AudioServiceBinder class's thread object.
                    if (msg.what == audioServiceBinder.UPDATE_AUDIO_PROGRESS_BAR) {

                        if( audioServiceBinder != null) {
                            // Calculate the percentage.
                            int currProgress =audioServiceBinder.getAudioProgress();

                            // Update progressbar. Make the value 10 times to show more clear UI change.
                            backgroundAudioProgress.setProgress(currProgress);
                        }
                    }else if(msg.what == audioServiceBinder.EOS){
                        getView().findViewById(R.id.player_play).setSelected(false);
                    }
                }
            };
        }
    }

}
