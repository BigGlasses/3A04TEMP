package com.mcmaster3a04.team11.audiovally.EntityClasses;

import android.util.Log;
import android.widget.ProgressBar;
import com.gracenote.gnsdk.GnDataLevel;
import com.mcmaster3a04.team11.audiovally.Data.Constants;

/**
 * Created by Brandon on 2018-03-30.
 */

public class Expert2ProcessData extends GenericExpertProcessData {
    public Expert2ProcessData(ProgressBar pb, String w) {
        super(pb, w);
    }

    @Override
    public void processAudioData() {
        String genre = Constants.getAlbum().genre(GnDataLevel.kDataLevel_2);
        Log.d("Expert2", "Genre: " + genre);

    }

    @Override
    public void init() {

    }
}
