package com.mcmaster3a04.team11.audiovally.controllerClasses;

import android.widget.ProgressBar;

import com.mcmaster3a04.team11.audiovally.EntityClasses.Expert3ProcessData;

/**
 * Created by Brandon on 2018-03-30.
 */

public class Expert3Controller extends GenericExpertController{
    public Expert3Controller(String audioPath, ProgressBar pb) {
        super(audioPath, pb);
        epd = new Expert3ProcessData(pb, audioPath);
    }

    @Override
    public void init() {

    }
}
