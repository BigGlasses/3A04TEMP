package com.mcmaster3a04.team11.audiovally.EntityClasses;

import android.animation.ObjectAnimator;
import android.graphics.drawable.RotateDrawable;
import android.os.AsyncTask;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

import com.mcmaster3a04.team11.audiovally.Data.IDResultFragment;

/**
 * Created by Brandon on 2018-03-30.
 */
public abstract class GenericExpertProcessData extends AsyncTask<String ,Integer, IDResultFragment> {
    protected String audioPath;
    protected IDResultFragment outID;
    protected ProgressBar pb;
    public GenericExpertProcessData(ProgressBar pb, String w){
        super();
        this.pb = pb;
        outID = new IDResultFragment();
        audioPath = w;
        //Make a copy of the audio data.

    }

    public abstract void processAudioData(); //The magic happens here.

    public  abstract void init ();

    public synchronized IDResultFragment getProcessed(){
        return outID;
    };

    protected IDResultFragment doInBackground(String... data) {
        int count = data.length;
        processAudioData();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new IDResultFragment();
    }

    protected void onProgressUpdate(Integer... progress) {
        if(android.os.Build.VERSION.SDK_INT >= 11){
            // will update the "progress" propriety of seekbar until it reaches progress
            ObjectAnimator animation = ObjectAnimator.ofInt(pb, "progress", progress[0]);
            animation.setDuration(500); // 0.5 second
            animation.setInterpolator(new DecelerateInterpolator());
            animation.start();
        }
        else {
            pb.setProgress(progress[0]); // no animation on Gingerbread or lower
        }
    }


    protected void onPostExecute(IDResultFragment result) {
        publishProgress(100);
    }

}