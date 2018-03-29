package com.mcmaster3a04.team11.audiovally;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Brandon on 2018-03-29.
 */


public class PlayButton extends AppCompatButton {
    boolean mStartPlaying = true;

    public PlayButton(Context ctx) {
        super(ctx);
        setText("Start playing");
    }
    public PlayButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setText("Start playing");
    }
    public PlayButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setText("Start playing");
    }
}