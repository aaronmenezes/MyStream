package com.kyser.demosuite.service.audioplayer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


public class AudioService extends Service {

    private AudioServiceBinder audioServiceBinder = new AudioServiceBinder();

    public AudioService() {  }

    @Override
    public IBinder onBind(Intent intent) {
        return audioServiceBinder;
    }
}
