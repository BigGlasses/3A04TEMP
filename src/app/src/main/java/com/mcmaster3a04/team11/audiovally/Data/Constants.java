package com.mcmaster3a04.team11.audiovally.Data;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import com.gracenote.gnsdk.GnException;
import com.gracenote.gnsdk.GnLicenseInputMode;
import com.gracenote.gnsdk.GnLog;
import com.gracenote.gnsdk.GnLookupLocalStream;
import com.gracenote.gnsdk.GnManager;
import com.gracenote.gnsdk.GnMusicIdStream;
import com.gracenote.gnsdk.GnStorageSqlite;
import com.gracenote.gnsdk.GnUser;
import com.gracenote.gnsdk.GnUserStore;
import com.gracenote.gnsdk.IGnAudioSource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Brandon on 2018-03-30.
 */

public class Constants {
    public static final String [] GENRES_STRING_LIST = {"Rock", "Blues", "Pop", "Jazz"};
    public static final int GENRE_CODE_ROCK  = 0;
    public static final int GENRE_CODE_BLUES = 1;
    public static final int GENRE_CODE_POP   = 2;
    public static final int GENRE_CODE_JAZZ  = 3;
    public static final int GENRE_NUM  = 4;
    public static final int MAX_SAMPLE_LENGTH = 10000;

    // set these values before running the sample
    private static GnManager 					gnManager;
    public static GnUser             			gnUser;
    private static GnMusicIdStream gnMusicIdStream;
    private static IGnAudioSource gnMicrophone;
    private static GnLog gnLog;
    private static Context context;
    static final String 				gnsdkClientId 			= "1615624510";
    static final String 				gnsdkClientTag 			= "0CCF7D3095456B67254AC5C58A2E221E";
    static final String 				gnsdkLicenseFilename 	= "license.txt";	// app expects this file as an "asset"
    private static final String    		gnsdkLogFilename 		= "sample.log";
    private static final String 		appString				= "GFM Sample";

    private static boolean inited = false;
    public static  void init(Context scontext){
        if (inited)
            return;
        inited = true;
        context = scontext;
        copyAssets();
        try {
            Log.d("pizza", "initing!");

            // get the gnsdk license from the application assets
            String gnsdkLicense = null;
            if ( (gnsdkLicenseFilename == null) || (gnsdkLicenseFilename.length() == 0) ){
                //showError( "License filename not set" );
            } else {
                gnsdkLicense = getAssetAsString( gnsdkLicenseFilename );
                if ( gnsdkLicense == null ){
                    //showError( "License file not found: " + gnsdkLicenseFilename );
                    return;
                }
            }

            gnManager = new GnManager( context, gnsdkLicense, GnLicenseInputMode.kLicenseInputModeString );
            // provide handler to receive system events, such as locale update needed
            //gnManager.systemEventHandler( new GracenoteMusicID.SystemEvents() );
            gnUser = new GnUser( new GnUserStore(context), gnsdkClientId, gnsdkClientTag, appString );
            Log.d("pizza", gnUser.toString());
            // enable storage provider allowing GNSDK to use its persistent stores
            GnStorageSqlite.enable();

            // enable local MusicID-Stream recognition (GNSDK storage provider must be enabled as pre-requisite)
            GnLookupLocalStream.enable();
        } catch (GnException e) {
            e.printStackTrace();
        }
        Log.d("pizza", "Inited constants.");

    }


    /**
     * Helpers to read license file from assets as string
     */
    private static String getAssetAsString( String assetName ){

        String 		assetString = null;
        InputStream assetStream;

        try {

            assetStream = context.getApplicationContext().getAssets().open(assetName);
            if(assetStream != null){

                java.util.Scanner s = new java.util.Scanner(assetStream).useDelimiter("\\A");

                assetString = s.hasNext() ? s.next() : "";
                assetStream.close();

            }else{
                Log.e(appString, "Asset not found:" + assetName);
            }

        } catch (IOException e) {

            Log.e( appString, "Error getting asset as string: " + e.getMessage() );

        }

        return assetString;
    }

    private static void copyAssets() {
        AssetManager assetManager = context.getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        for(String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(filename);

                String outDir = context.getExternalCacheDir().getAbsolutePath() + "/";

                File outFile = new File(outDir, filename);

                out = new FileOutputStream(outFile);
                copyFile(in, out);
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            } catch(IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            }
        }
    }
    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

}
