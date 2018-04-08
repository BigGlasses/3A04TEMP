package com.mcmaster3a04.team11.audiovally.controllerClasses;

import android.widget.ProgressBar;

import com.mcmaster3a04.team11.audiovally.EntityClasses.Expert2ProcessData;

/**
 * Created by Brandon on 2018-03-30.
 */

public class Expert2Controller extends GenericExpertController{
    public Expert2Controller(String audioPath, ProgressBar pb) {
        super(audioPath, pb);
        epd = new Expert2ProcessData(pb, audioPath);
    }

    @Override
    public void init() {

    }
}
