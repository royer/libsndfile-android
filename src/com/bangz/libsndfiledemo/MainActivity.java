package com.bangz.libsndfiledemo;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.meganerd.sndfile.CArrayShort;
import com.meganerd.sndfile.SF_INFO;
import com.meganerd.sndfile.SWIGTYPE_p_SNDFILE_tag;
import com.meganerd.sndfile.libsndfile;

import java.io.File;
import java.io.IOException;

public class MainActivity extends Activity
        implements View.OnClickListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    private static final  String TAG = "sndfile.demo.main";

    private Button btnRecord ;
    private Button btnPlay ;


    private int iState ;
    private static final int STATE_NONE = 0;
    private static final int STATE_RECORDING = 1;
    private static final int STATE_PLAYING = 2 ;

    /**
     * bRecording is used to control RecordThread stop record.
     */
    boolean bRecording ;

    /**
     * bRecorded means have successful record and can play.
     */
    private boolean bRecorded = false ;

    int[] mainfmts;
    int[] subfmts ;
    String[] exts ;

    private static final String SNDFILENAME = "sndfiletest" ;

    private String filepath = null ;
    private String filename = SNDFILENAME ;

    private static final int DEFAULT_SAMPLERATE = 44100 ;
    private static final int DEFAULT_CHANNELS = AudioFormat.CHANNEL_IN_MONO ;
    private static final int DEFAULT_AUDIOFORMAT = AudioFormat.ENCODING_PCM_16BIT ;

    private int nBuffersize ;

    private int nSampleRate = DEFAULT_SAMPLERATE;
    private int nChannels = DEFAULT_CHANNELS;
    private int nAudioFormat = DEFAULT_AUDIOFORMAT ;
    private int sndfileFormat = 0 ;


    AudioRecord recorder ;
    RecordThread recordThread;

    MediaPlayer mediaplayer = null ;

    protected String errormsg ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRecord = (Button)findViewById(R.id.recordandstop);
        btnPlay = (Button)findViewById(R.id.playandstop);

        btnRecord.setOnClickListener(this);
        btnPlay.setOnClickListener(this);


        Spinner spinnerMain = (Spinner)findViewById(R.id.spinMainFMT);
        ArrayAdapter<CharSequence> adaptermain =
                ArrayAdapter.createFromResource(this,
                        R.array.main_format_name,
                        android.R.layout.simple_spinner_item);
        adaptermain.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMain.setAdapter(adaptermain);

        Spinner spinnerSub = (Spinner)findViewById(R.id.spinSubFMT);
        ArrayAdapter<CharSequence> adaptersub =
                ArrayAdapter.createFromResource(this,
                        R.array.sub_format_name,
                        android.R.layout.simple_spinner_item);
        adaptersub.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSub.setAdapter(adaptersub);

        mainfmts = getResources().getIntArray(R.array.main_format);
        subfmts = getResources().getIntArray(R.array.sub_format);
        exts = getResources().getStringArray(R.array.ext);

        updateButtonTextAndStatus();
    }

    private void updateButtonTextAndStatus() {

        if (iState == STATE_RECORDING) {
            btnRecord.setText(R.string.stoprecord);
        } else {
            btnRecord.setText(R.string.startrecord);
        }

        if(iState == STATE_PLAYING) {
            btnPlay.setText(R.string.stopplay);
        } else {
            btnPlay.setText(R.string.startplay);
        }

        btnRecord.setEnabled(iState == STATE_NONE || iState == STATE_RECORDING);
        btnPlay.setEnabled(bRecorded == true && (iState == STATE_NONE || iState == STATE_PLAYING));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (iState == STATE_RECORDING) {
            stopRecord();
        } else if (iState == STATE_PLAYING) {
            stopPlaying();
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.recordandstop) {
            onRecordandStopButtonClick();
        } else if (v.getId() == R.id.playandstop) {
            onPlayandStopButtonClick();
        }
    }

    private void onRecordandStopButtonClick() {

        if (iState == STATE_RECORDING) {
            stopRecord();
        } else if (iState == STATE_NONE) {

            startRecord();
        }

    }


    private boolean startRecord() {

        if (isWritableExternalStorage() == false) {
            Toast.makeText(this, getString(R.string.storage_unwriteable), 4000).show();
            return false ;
        }

        nBuffersize = AudioRecord.getMinBufferSize(nSampleRate, nChannels, nAudioFormat);
        if (nBuffersize == AudioRecord.ERROR_BAD_VALUE || nBuffersize == AudioRecord.ERROR) {
            Toast.makeText(this, "AUdio Format not support or hardware not ready", 4000).show();
            return false;
        }

        nBuffersize = Math.max(nBuffersize, 4096) ;

        filepath = getfilepath();

        getSavedFormat();

        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                nSampleRate, nChannels, nAudioFormat,
                nBuffersize);

        errormsg = null ;

        recordThread = new RecordThread();
        bRecording = true ;
        iState = STATE_RECORDING ;
        updateButtonTextAndStatus();

        recorder.startRecording();
        recordThread.start();
        return true ;
    }

    private void stopRecord() {
        bRecording = false;
    }

    private void getSavedFormat() {

        Spinner spinnerMain = (Spinner)findViewById(R.id.spinMainFMT);
        Spinner spinnerSub = (Spinner)findViewById(R.id.spinSubFMT);

        sndfileFormat =
                mainfmts[spinnerMain.getSelectedItemPosition()]
                        | subfmts[spinnerSub.getSelectedItemPosition()];
    }

    private void onPlayandStopButtonClick() {

        if (iState == STATE_PLAYING) {
            stopPlaying();

        } else if (iState == STATE_NONE ) {
            startPlaying();
        }

        updateButtonTextAndStatus();
    }

    private void startPlaying() {
        if (mediaplayer != null) {
            mediaplayer.release();
        }

        mediaplayer = new MediaPlayer();
        mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaplayer.setOnPreparedListener(this);
        mediaplayer.setOnCompletionListener(this);
        mediaplayer.setOnErrorListener(this);

        try {
            mediaplayer.setDataSource(filepath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        mediaplayer.prepareAsync();

        iState = STATE_PLAYING;
        updateButtonTextAndStatus();
    }

    private void stopPlaying() {
         if (mediaplayer != null) {
             mediaplayer.stop();
             mediaplayer.release();
             mediaplayer = null;
         }
        iState = STATE_NONE;

        updateButtonTextAndStatus();
    }

    private boolean isWritableExternalStorage() {

        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            return true;
        } else
            return false;
    }

    private String getfilepath() {

        Spinner spinner = (Spinner)findViewById(R.id.spinMainFMT);
        String ext = exts[spinner.getSelectedItemPosition()];
        filename = SNDFILENAME + ext ;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            // Android version >= 8
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS);
            File file = new File(path, filename) ;
            path.mkdirs();

            return file.getAbsolutePath() ;
        } else {
            // Android version < 8

            File path = Environment.getExternalStorageDirectory() ;
            File podcastpath = new File(path, "Podcasts/") ;
            podcastpath.mkdirs();
            File file =  new File(podcastpath, filename) ;

            return file.getAbsolutePath();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaplayer.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

        mediaplayer.stop();
        mediaplayer.release();
        mediaplayer = null ;
        iState = STATE_NONE ;

        updateButtonTextAndStatus();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.release();
        if (mp == mediaplayer) {
            mediaplayer = null ;
            iState = STATE_NONE ;
            updateButtonTextAndStatus();
        }
        return false;
    }

    private class RecordThread extends Thread {

        @Override
        public void run() {
            super.run();

            short[] buffer = new short[nBuffersize / 2];

            SF_INFO sfInfo = new SF_INFO();
            sfInfo.setSamplerate(nSampleRate);
            sfInfo.setChannels(nChannels == AudioFormat.CHANNEL_IN_MONO?1:2);
            sfInfo.setFormat(sndfileFormat);

            SWIGTYPE_p_SNDFILE_tag sndfile = libsndfile.sf_open(filepath, libsndfile.SFM_WRITE, sfInfo);
            if (sndfile == null) {
                bRecording = false;
                errormsg = libsndfile.sf_strerror(null);

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onRecordAbort();
                    }
                });
                return ;
            }

            long totalwrited = 0;

            CArrayShort tosndbuff = new CArrayShort(nBuffersize/2);
            while (bRecording) {

                int reads = recorder.read(buffer, 0, nBuffersize/2) ;
                if (reads == AudioRecord.ERROR_INVALID_OPERATION || reads == AudioRecord.ERROR_BAD_VALUE) {
                    bRecording = false;
                    errormsg = new String("AudioRecord.read fialed.") ;
                    break;
                }

                //write to file
                for (int i = 0; i < reads; i++) {
                    tosndbuff.setitem(i, buffer[i]);
                }

                long w = libsndfile.sf_write_short(sndfile, tosndbuff.cast(), reads);
                if (w != reads) {
                    errormsg = libsndfile.sf_strerror(sndfile);
                    bRecording = false ;
                    break;
                }
                totalwrited += w ;

            }

            //save file
            libsndfile.sf_close(sndfile);
            if (totalwrited > 0) {
                bRecorded = true ;
            } else {
                Log.d(TAG, "not audio data be writed!.");
                File f = new File(filepath) ;
                f.delete();
                bRecorded = false;
            }

            recorder.stop();
            recorder.release();
            recorder = null ;

            // Noitfy GUI thread record abort
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onRecordAbort();
                }
            });
        }
    }

    private void onRecordAbort() {

        if (errormsg != null && errormsg.length() != 0) {
            Toast.makeText(this, errormsg, 4000).show();
        }
        iState = STATE_NONE ;

        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null ;
        }

        if (recordThread == null) {
            recordThread = null ;
        }

        updateButtonTextAndStatus();
    }

    static {
        System.loadLibrary("ogg");
        System.loadLibrary("FLAC");
        System.loadLibrary("vorbis");
        System.loadLibrary("sndfile");
        System.loadLibrary("sndfile_wrap");
    }

}