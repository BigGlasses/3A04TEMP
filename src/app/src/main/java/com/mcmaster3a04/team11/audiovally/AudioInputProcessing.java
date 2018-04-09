package com.mcmaster3a04.team11.audiovally;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gracenote.gnsdk.GnDataLevel;
import com.gracenote.gnsdk.GnError;
import com.gracenote.gnsdk.GnException;
import com.gracenote.gnsdk.GnImageSize;
import com.gracenote.gnsdk.GnLookupData;
import com.gracenote.gnsdk.GnMic;
import com.gracenote.gnsdk.GnMusicIdStream;
import com.gracenote.gnsdk.GnMusicIdStreamIdentifyingStatus;
import com.gracenote.gnsdk.GnMusicIdStreamPreset;
import com.gracenote.gnsdk.GnMusicIdStreamProcessingStatus;
import com.gracenote.gnsdk.GnResponseAlbums;
import com.gracenote.gnsdk.GnStatus;
import com.gracenote.gnsdk.IGnAudioSource;
import com.gracenote.gnsdk.IGnCancellable;
import com.gracenote.gnsdk.IGnMusicIdStreamEvents;
import com.mcmaster3a04.team11.audiovally.Data.Constants;
import com.mcmaster3a04.team11.audiovally.Data.IDResultFragment;
import com.mcmaster3a04.team11.audiovally.EntityClasses.AudioData;
import com.mcmaster3a04.team11.audiovally.EntityClasses.GenericExpertProcessData;
import com.mcmaster3a04.team11.audiovally.controllerClasses.Expert1Controller;
import com.mcmaster3a04.team11.audiovally.controllerClasses.Expert2Controller;
import com.mcmaster3a04.team11.audiovally.controllerClasses.Expert3Controller;
import com.mcmaster3a04.team11.audiovally.controllerClasses.GenericExpertController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.io.UniversalAudioInputStream;
import io.grpc.netty.shaded.io.netty.util.Constant;

public class AudioInputProcessing extends AppCompatActivity {

    GenericExpertController[] expertControllers;
    GnMusicIdStream gnMusicIdStream;
    private IGnAudioSource gnMicrophone;
    Thread audioProcessThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_audio_input_processing);
        Constants.init(this);
        GatherTask g = new GatherTask();
        g.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        ArrayList<Integer> a = new ArrayList();
        for (int i = 0; i < 8; i++)
            a.add(i);

//        UsersAdapter adapter = new UsersAdapter(this, a);
//        // Attach the adapter to a ListView
//        ListView listView = (ListView) findViewById(R.id.listholder);
//        listView.setAdapter(adapter);

        //gnMicrophone = new AudioVisualizeAdapter( new GnMic() );

    }

    public class UsersAdapter extends ArrayAdapter<Integer> {
        public UsersAdapter(Context context, ArrayList<Integer> users) {
            super(context, 0, users);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Integer user = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.id_fragment_list_item, parent, false);
            }
            // Lookup view for data population
            TextView idNumber = (TextView) convertView.findViewById(R.id.name);
            // Populate the data into the template view using the data object
            idNumber.setText(user.toString());
            // Return the completed view to render on screen
            return convertView;
        }
    }

    private class GatherTask extends AsyncTask<Void ,Long, Long> {


        protected Long doInBackground(Void... voids) {

            ProgressBar pb1 = AudioInputProcessing.this.findViewById(R.id.expertProgressBar1);
            ProgressBar pb2 = AudioInputProcessing.this.findViewById(R.id.expertProgressBar2);
            ProgressBar pb3 = AudioInputProcessing.this.findViewById(R.id.expertProgressBar3);

            String mFileName = getExternalCacheDir().getAbsolutePath();
            mFileName += AudioData.directory + AudioData.latest;

            FileInputStream fs = null;
            try {
                fs = new FileInputStream(mFileName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            expertControllers = new GenericExpertController[3];
            expertControllers[0] = new Expert1Controller(mFileName, pb1);
            expertControllers[1] = new Expert2Controller(mFileName, pb2);
            expertControllers[2] = new Expert3Controller(mFileName, pb3);

            for (int i = 0; i < expertControllers.length; i++){
                expertControllers[i].processAudioData();
            }

            for (int i = 0; i < expertControllers.length; i++){
                while( expertControllers[i].finished() == false )
                {
                    try
                    {
                        //Log.d("myApp", "Waiting for Task");
                        Thread.sleep(200);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    TextView genretext1 = findViewById(R.id.genre_textview_1);
                    TextView genretext2 = findViewById(R.id.genre_textview_2);
                    TextView genretext3 = findViewById(R.id.genre_textview_3);
                    String genre1 = Constants.getAlbum().genre(GnDataLevel.kDataLevel_1);
                    String genre2 = Constants.getAlbum().genre(GnDataLevel.kDataLevel_2);
                    String genre3 = Constants.getAlbum().genre(GnDataLevel.kDataLevel_3);
                    genretext1.setText(genre1);
                    if (!genre1.equals(genre2))
                        genretext2.setText(genre2);
                    if (!genre2.equals(genre3))
                        genretext3.setText(genre3);

                }
            });




            return null;
        }

        protected void onPostExecute(Long aLong) {
//            Intent myIntent = new Intent(getApplicationContext(), AudioInput.class);
//            startActivityForResult(myIntent, 0);
        }
    }

}
