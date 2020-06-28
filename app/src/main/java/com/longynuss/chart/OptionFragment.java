package com.longynuss.chart;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;


public class OptionFragment extends Fragment {

    private static final int MIN_LETTERS = 26;
    private static final int INC_LETTERS = 26;

    private static final float MAX_TIME = 2.5f; //Value in seconds
    private static final float INC_TIME = 0.2f;

    private static final String TIME_TEXT_KEY = "gameTimeText";
    private static final String NUM_LETTERBAR_KEY = "numLetterProgress";
    private static final String TIME_LETTERBAR_KEY = "changeLetterProgress";

    private OnOptionFragmentChangedListener mCallback;

    private TextView gameTime;
    private SeekBar numLettersBar;
    private SeekBar changeLetterBar;

    private MyPreferences preferences;

    public OptionFragment() {
        // Required empty public constructor
    }

    public static OptionFragment newInstance() {
        Log.i("LifeCicle","OptionFragment - New instance made");
        return new OptionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        Log.i("LifeCicle","OptionFragment - Created");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("LifeCicle","OptionFragment - View Created");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_option, container, false);

        gameTime = v.findViewById(R.id.gameTimeTextview);

        changeLetterBar = v.findViewById(R.id.seekBarVelocityLetter);
        changeLetterBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //progress from 0 to 10
                float letterTime = calculateTimeLetter(seekBar.getProgress());
                int letterNum = calculateNumberLetter(numLettersBar.getProgress());
                notifyGameTime(gameTime,letterTime, letterNum);
                if(mCallback!=null){
                    mCallback.onGameOptionsChanged(letterTime,letterNum);
                }

            }
        });

        numLettersBar = v.findViewById(R.id.min);
        numLettersBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //progress from 0 to 3
                float letterTime = calculateTimeLetter(changeLetterBar.getProgress());
                int letterNum = calculateNumberLetter(seekBar.getProgress());
                notifyGameTime(gameTime,letterTime, letterNum);
                if(mCallback!=null){
                    mCallback.onGameOptionsChanged(letterTime,letterNum);
                }
            }

        });

        preferences = new MyPreferences(v.getContext());

        if(preferences.optionStatesExist()){
            int numLetterProgress = preferences.getNumLettersBarProgress();
            int changeBarProgress = preferences.getVelocityBarProgress();
            String gameTimeText = preferences.getGameTime();

            Log.i("SaveData","Data restored");

            changeLetterBar.setProgress(changeBarProgress);
            numLettersBar.setProgress(numLetterProgress);
            gameTime.setText(gameTimeText);

        }else{
            Log.i("SaveData","No Data to restore");
            //There are no Option States, set default states.
            // If changed must be changed in MainActivity
            String defaultText = String.format(getResources().getString(R.string.estimatedGameLenghtText),78);
            gameTime.setText(defaultText);
        }

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.i("LifeCicle","OptionFragment - Activity Created");
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null){
            gameTime.setText(savedInstanceState.getString(TIME_TEXT_KEY));
            changeLetterBar.setProgress(savedInstanceState.getInt(TIME_LETTERBAR_KEY));
            numLettersBar.setProgress(savedInstanceState.getInt(NUM_LETTERBAR_KEY));
        }
    }

    private float calculateTimeLetter(int progress){
        return MAX_TIME-(INC_TIME*progress);
    }

    private int calculateNumberLetter(int progress){
        return MIN_LETTERS+(INC_LETTERS*progress);
    }

    private void notifyGameTime(TextView gameTimeText, float letterTime , int letterNum) {
        //proximate time that one letter stays on the screen
        int gameTime = (int)(letterTime*letterNum);

        String text = String.format(getResources().getString(R.string.estimatedGameLenghtText),gameTime);
        gameTimeText.setText(text);
    }

    @Override
    public void onAttach(Context context) {
        Log.i("LifeCicle","OptionFragment - Attached");
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnOptionFragmentChangedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("LifeCicle","OptionFragment - Started");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("LifeCicle","OptionFragment - Resumed");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("LifeCicle","OptionFragment - Paused");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("LifeCicle","OptionFragment - Stopped");

        preferences.saveOptionStates(gameTime.getText().toString(),
                changeLetterBar.getProgress(),numLettersBar.getProgress());
        Log.i("SaveData","Data saved");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("LifeCicle","OptionFragment - View Destroyed");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("LifeCicle","OptionFragment - Destroyed");
    }

    @Override
    public void onDetach() {
        Log.i("LifeCicle","OptionFragment - Detached");
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TIME_TEXT_KEY ,gameTime.getText().toString());
        outState.putInt(NUM_LETTERBAR_KEY, numLettersBar.getProgress());
        outState.putInt(TIME_LETTERBAR_KEY,changeLetterBar.getProgress());
    }

    // Container Activity must implement this interface
    public interface OnOptionFragmentChangedListener {
        void onGameOptionsChanged(float letterTime , int letterNum);
    }
}
