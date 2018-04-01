package com.mcmaster3a04.team11.audiovally.EntityClasses;

import com.mcmaster3a04.team11.audiovally.Data.IDResultFragment;

/**
 * Created by Brandon on 2018-03-30.
 */


public abstract class GenericExpertProcessData {
    protected byte[] audioData;
    protected IDResultFragment outID;

    public GenericExpertProcessData(){
        outID = new IDResultFragment();
    }

    public synchronized void feedAudioData(byte[] w){
        //Make a copy of the audio data.
        audioData = new byte[w.length];
        for (int i = 0; i < w.length; i ++)
            audioData[i] = w[i];
        processAudioData();
    }

    public abstract void processAudioData(); //The magic happens here.

    public  abstract void init ();

    public synchronized IDResultFragment getProcessed(){
        return outID;
    };

}