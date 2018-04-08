package com.mcmaster3a04.team11.audiovally.controllerClasses;

import android.os.AsyncTask;
import android.widget.ProgressBar;

import com.mcmaster3a04.team11.audiovally.EntityClasses.GenericExpertProcessData;

import java.util.concurrent.ExecutionException;

/**
 * Created by Brandon on 2018-03-30.
 */

public abstract class GenericExpertController {
    protected GenericExpertProcessData epd;

    public GenericExpertController(String audioPath, ProgressBar pb){
    }

    public void processAudioData(){
        epd.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public boolean finished(){
        return epd.getStatus() == AsyncTask.Status.FINISHED;
    }

    public abstract void init ();

}
