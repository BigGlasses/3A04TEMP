package com.mcmaster3a04.team11.audiovally.controllerClasses;

import com.mcmaster3a04.team11.audiovally.EntityClasses.GenericExpertProcessData;

/**
 * Created by Brandon on 2018-03-30.
 */

public abstract class GenericExpertController {
    static GenericExpertProcessData epd;
    public static void feedAudioData(byte[] w){
        epd.feedAudioData(w);
    }

    public abstract void init ();

}
