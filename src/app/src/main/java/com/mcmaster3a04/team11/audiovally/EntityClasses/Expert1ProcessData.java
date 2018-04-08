package com.mcmaster3a04.team11.audiovally.EntityClasses;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gracenote.gnsdk.GnAlbum;
import com.gracenote.gnsdk.GnAlbumIterator;
import com.gracenote.gnsdk.GnAudioFile;
import com.gracenote.gnsdk.GnDataMatch;
import com.gracenote.gnsdk.GnDataMatchIterator;
import com.gracenote.gnsdk.GnError;
import com.gracenote.gnsdk.GnException;
import com.gracenote.gnsdk.GnLog;
import com.gracenote.gnsdk.GnLookupData;
import com.gracenote.gnsdk.GnLookupMode;
import com.gracenote.gnsdk.GnManager;
import com.gracenote.gnsdk.GnMusicId;
import com.gracenote.gnsdk.GnMusicIdFile;
import com.gracenote.gnsdk.GnMusicIdFileCallbackStatus;
import com.gracenote.gnsdk.GnMusicIdFileInfo;
import com.gracenote.gnsdk.GnMusicIdFileInfoManager;
import com.gracenote.gnsdk.GnMusicIdFileProcessType;
import com.gracenote.gnsdk.GnMusicIdFileResponseType;
import com.gracenote.gnsdk.GnMusicIdStream;
import com.gracenote.gnsdk.GnMusicIdStreamPreset;
import com.gracenote.gnsdk.GnResponseAlbums;
import com.gracenote.gnsdk.GnResponseDataMatches;
import com.gracenote.gnsdk.GnStatus;
import com.gracenote.gnsdk.GnTrackIterator;
import com.gracenote.gnsdk.GnUser;
import com.gracenote.gnsdk.IGnAudioSource;
import com.gracenote.gnsdk.IGnCancellable;
import com.gracenote.gnsdk.IGnMusicIdFileEvents;
import com.mcmaster3a04.team11.audiovally.Data.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.UniversalAudioInputStream;

/**
 * Created by Brandon on 2018-03-30.
 */

public class Expert1ProcessData extends GenericExpertProcessData {
    public Expert1ProcessData(ProgressBar pb, String w) {
        super(pb, w);
    }

    // set these values before running the sample
    static final String gnsdkClientId = "1615624510";
    static final String gnsdkClientTag = "0CCF7D3095456B67254AC5C58A2E221E";
    static final String gnsdkLicenseFilename = "license.txt";    // app expects this file as an "asset"
    private static final String gnsdkLogFilename = "sample.log";
    private static final String appString = "GFM Sample";

    private Activity activity;
    private Context context;

    // ui objects
    private TextView statusText;
    private Button buttonSettings;
    private Button buttonIDNow;
    private Button buttonTextSearch;
    private Button buttonHistory;
    private Button buttonLibraryID;
    private Button buttonCancel;
    private Button buttonVisShowHide;
    private LinearLayout linearLayoutHomeContainer;
    private LinearLayout linearLayoutVisContainer;
    private boolean visShowing;

    protected ViewGroup metadataListing;
    private final int metadataMaxNumImages = 10;

    // Gracenote objects
    private GnManager gnManager;
    private GnUser gnUser;
    private GnMusicIdStream gnMusicIdStream;
    private IGnAudioSource gnMicrophone;
    private GnLog gnLog;
    private List<GnMusicId> idObjects = new ArrayList<GnMusicId>();
    private List<GnMusicIdFile> fileIdObjects = new ArrayList<GnMusicIdFile>();
    private List<GnMusicIdStream> streamIdObjects = new ArrayList<GnMusicIdStream>();

    // store some tracking info about the most recent MusicID-Stream lookup
    protected volatile boolean lastLookup_local = false;    // indicates whether the match came from local storage
    protected volatile long lastLookup_matchTime = 0;        // total lookup time for query
    protected volatile long lastLookup_startTime;                // start time of query
    private volatile boolean audioProcessingStarted = false;
    private volatile boolean analyzingCollection = false;
    private volatile boolean analyzeCancelled = false;

    @Override
    public void processAudioData() {
//        MusicIDFileEvents m  = new MusicIDFileEvents();
        //GnAudioFile gnAudioFile = new GnAudioFile( new File("Testnam.3gp"));
        GnMusicIdFileInfo fileInfo = null;
        try {

            //GnMusicIdFile a = new GnMusicIdFile(Constants.gnUser, new TrackIdCallback);
            GnMusicIdFile c = new GnMusicIdFile(Constants.gnUser, new MusicIDFileEventsExpert());
            GnMusicIdFileInfoManager cManager = c.fileInfos();
            fileInfo = cManager.add(audioPath);
            fileInfo.fileName(audioPath);
            Log.d("Pizza Lool", audioPath);
            c.options().lookupData(GnLookupData.kLookupDataContent, true);
            c.options().batchSize(10); // todo: comment
            //c.options().lookupData(GnLookupData.kLookupDataSonicData, true);
            //c.options().lookupMode(GnLookupMode.kLookupModeOnline);
            //c.doAlbumId(GnMusicIdFileProcessType.kQueryReturnAll, GnMusicIdFileResponseType.kResponseAlbums );
            //c.doLibraryId(GnMusicIdFileResponseType.kResponseAlbums);
//            c.doTrackId(GnMusicIdFileProcessType.kQueryReturnAll, GnMusicIdFileResponseType.kResponseAlbums );
            Log.d("Pizza Face", fileInfo.fingerprint());

        } catch (GnException e) {
            Log.d("Pizza pop", "lol");
            e.printStackTrace();
        }
        Log.d("App", "Finished GNSDK LOOKUP");
    }

    @Override
    public void init() {

    }

    private class MusicIDFileEventsExpert implements IGnMusicIdFileEvents {
        @Override
        public void musicIdFileStatusEvent(GnMusicIdFileInfo gnMusicIdFileInfo, GnMusicIdFileCallbackStatus gnMusicIdFileCallbackStatus, long l, long l1, IGnCancellable iGnCancellable) {

        }

        @Override
        public void gatherFingerprint(GnMusicIdFileInfo gnMusicIdFileInfo, long l, long l1, IGnCancellable iGnCancellable) {

            try {
                //Log.d("Pizza", gnMusicIdFileInfo.fileName());
                if ( GnAudioFile.isFileFormatSupported(gnMusicIdFileInfo.fileName())) {
                    GnAudioFile k = new GnAudioFile( new File(gnMusicIdFileInfo.fileName()));
                    //Log.d("Pizzaria", k.filename());
                    gnMusicIdFileInfo.fingerprintFromSource( new GnAudioFile( new File(gnMusicIdFileInfo.fileName())) );
                    Log.d("Fingerprinted", gnMusicIdFileInfo.fileName());
                }
                else{
                    Log.d("Pizza", gnMusicIdFileInfo.fileName() + " is not supported");
                }

            } catch (GnException e) {
                if ( GnError.isErrorEqual(e.errorCode(),GnError.GNSDKERR_Aborted) == false ){
                    Log.e(appString, "error in fingerprinting file: " + e.errorAPI() + ", " + e.errorModule() + ", " + e.errorDescription());
                }
            }
            Log.d("Pizza", "fingerprint ran!");
        }

        @Override
        public void gatherMetadata(GnMusicIdFileInfo gnMusicIdFileInfo, long l, long l1, IGnCancellable iGnCancellable) {

        }

        @Override
        public void musicIdFileAlbumResult(GnResponseAlbums gnResponseAlbums, long l, long l1, IGnCancellable iGnCancellable) {
            Log.d("Pizza Album", "Running");
            GnAlbumIterator a = gnResponseAlbums.albums().getIterator();
            while (a.hasNext()){
                GnAlbum g = null;
                try {
                    g = a.next();
                    Log.d("Pizza", g.toString());
                } catch (GnException e) {
                    e.printStackTrace();
                }
            }

        }


        @Override
        public void musicIdFileMatchResult(GnResponseDataMatches gnResponseDataMatches, long l, long l1, IGnCancellable iGnCancellable) {
            GnDataMatchIterator a = gnResponseDataMatches.dataMatches().getIterator();
            Log.d("Pizza Match", "Running");
            while (a.hasNext()){
                GnDataMatch g = null;
                try {
                    g = a.next();
                    Log.d("Pizza Match", g.getAsAlbum().toString());
                } catch (GnException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void musicIdFileResultNotFound(GnMusicIdFileInfo gnMusicIdFileInfo, long l, long l1, IGnCancellable iGnCancellable) {
            Log.d("Pizza Fail", "No Matches.");
        }

        @Override
        public void musicIdFileComplete(GnError gnError) {
            Log.d("Pizza Fail", "File Complete?");

        }

        @Override
        public void statusEvent( GnStatus status, long percentComplete, long bytesTotalSent, long bytesTotalReceived, IGnCancellable cancellable ) {
            Log.d("Pizza", String.format("%d%%",percentComplete) );
        }

    }
//
//    /**
//     * GNSDK MusicID-File event delegate
//     */
//    private class MusicIDFileEvents implements IGnMusicIdFileEvents {
//
//        HashMap<String, String> gnStatus_to_displayStatus;
//
//        public MusicIDFileEvents(){
//            gnStatus_to_displayStatus = new HashMap<String,String>();
//            gnStatus_to_displayStatus.put("kMusicIdFileCallbackStatusProcessingBegin", "Begin processing file");
//            gnStatus_to_displayStatus.put("kMusicIdFileCallbackStatusFileInfoQuery", "Querying file info");
//            gnStatus_to_displayStatus.put("kMusicIdFileCallbackStatusProcessingComplete", "Identification complete");
//        }
//
//
//        @Override
//        public void gatherFingerprint(GnMusicIdFileInfo fileInfo, long currentFile, long totalFiles, IGnCancellable cancelable){
//
//            // If the audio file can be decoded then provide a MusicID-File fingerprint
//            //
//            // GnAudioFile uses Gracenote's audio decoder, if your application uses a proprietary audio
//            // format you can decode the audio and provide it manually using GnMusicIdFileInfo.fingerprintBegin,
//            // fingerprintWrite and fingerprintEnd; or create your own audio file decoder class that implements
//            // IGnAudioSource.
//            try {
//
//                if ( GnAudioFile.isFileFormatSupported(fileInfo.fileName())) {
//                    fileInfo.fingerprintFromSource( new GnAudioFile( new File(fileInfo.fileName())) );
//                }
//
//            } catch (GnException e) {
//                if ( GnError.isErrorEqual(e.errorCode(),GnError.GNSDKERR_Aborted) == false )
//                {
//                    Log.e(appString, "error in fingerprinting file: " + e.errorAPI() + ", " + e.errorModule() + ", " + e.errorDescription());
//                }
//            }
//        }
//
//        @Override
//        public void gatherMetadata(GnMusicIdFileInfo fileInfo, long currentFile, long totalFiles, IGnCancellable cancelable) {
//            // Skipping this here as metadata has been previously loaded for all files
//            // You could provide metadata "just in time" instead of before invoking Track/Album/Library ID, which
//            // means you would add it in this delegate method for the file represented by fileInfo
//        }
//
//
//        @Override
//        public void statusEvent(GnStatus status, long percentComplete, long bytesTotalSent, long bytesTotalReceived, IGnCancellable cancellable ) {
//            setStatus( String.format("%d%%",percentComplete), true );
//        }
//
//        @Override
//        public void musicIdFileStatusEvent(GnMusicIdFileInfo fileinfo, GnMusicIdFileCallbackStatus midf_status, long currentFile, long totalFiles, IGnCancellable canceller){
//
//            try {
//                String status = midf_status.toString();
//                if (gnStatus_to_displayStatus.containsKey(status)) {
//                    String filename = fileinfo.identifier();
//                    if (filename != null) {
//                        status = gnStatus_to_displayStatus.get(status) + ": " + filename;
//                        setStatus(status, true);
//                    }
//
//                }
//
//            } catch (Exception e) {
//                Log.e(appString, "error in retrieving musidIdFileStatus");
//            }
//
//        }
//
//        @Override
//        public void musicIdFileAlbumResult(GnResponseAlbums albumResult, long currentAlbum, long totalAlbums, IGnCancellable cancellable  ) {
//            // match found!
//            activity.runOnUiThread( new GracenoteMusicID.UpdateResultsRunnable( albumResult ) );
//        }
//
//        @Override
//        public void musicIdFileResultNotFound( GnMusicIdFileInfo fileInfo, long currentFile, long totalFiles, IGnCancellable cancellable ) {
//            // no match found for the audio file represented by fileInfo
//            try {
//                Log.i(appString,"GnMusicIdFile no match found for " + fileInfo.identifier());
//            } catch (GnException e) {
//            }
//        }
//
//        @Override
//        public void musicIdFileComplete( GnError musicidfileCompleteError ) {
//
//            if ( musicidfileCompleteError.errorCode() == 0 ){
//                setStatus( "Success", true );
//
//            } else {
//
//                if ( musicidfileCompleteError.isCancelled() )
//                    setStatus( "Cancelled", true );
//                else
//                    setStatus(musicidfileCompleteError.errorDescription(), true );
//                Log.e(appString, musicidfileCompleteError.errorAPI() + ": " + musicidfileCompleteError.errorDescription() );
//            }
//            setUIState( GracenoteMusicID.UIState.READY );
//        }
//
//
//        @Override
//        public void musicIdFileMatchResult(GnResponseDataMatches matchResult, long currentFile, long totalFiles, IGnCancellable cancellable) {
//            // handle match result
//            // match result only received if requested match results when initiating query
//        }
//    }

}
