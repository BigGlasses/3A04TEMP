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

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.io.UniversalAudioInputStream;

public class AudioInputProcessing extends AppCompatActivity {

    GenericExpertController[] expertControllers;
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
            UniversalAudioInputStream Uas = new UniversalAudioInputStream(fs,new TarsosDSPAudioFormat(22050,16,1,true,true));
            AudioDispatcher ad = new AudioDispatcher(Uas,1024,512);

            mFileName = getExternalCacheDir().getAbsolutePath();
            mFileName += AudioData.directory + AudioData.latest;
            mFileName = getExternalCacheDir().getAbsolutePath();
            mFileName += "/test.mp3";
            Log.d("Pizza", mFileName);
            //mFileName = "//android_asset/test.mp3";
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

            return null;
        }

        protected void onPostExecute(Long aLong) {
//            Intent myIntent = new Intent(getApplicationContext(), AudioInput.class);
//            startActivityForResult(myIntent, 0);
        }
    }
}
