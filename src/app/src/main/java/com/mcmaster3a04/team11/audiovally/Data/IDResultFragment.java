package com.mcmaster3a04.team11.audiovally.Data;

/**
 * Created by Brandon on 2018-03-30.
 */

public class IDResultFragment {
    public float [] probabilities = new float[Constants.GENRE_NUM];
    public IDResultFragment(){
        for (int i = 0; i < Constants.GENRE_NUM; i ++){
            probabilities[i] = 0;
        }
    }



}
