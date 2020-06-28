package com.longynuss.chart;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;


public class ChartFragment extends Fragment {

    private OnChartFragmentChangedListener mCallback;

    private static final String TIME = "timeLetter";
    private static final String NUM_LETTERS = "numLetters";

    private Thread chart = null;

    private ArrayList<String> mainLetters;
    private final String[] subLetters = new String[]{"d","e","j"};
    private boolean isPlaying;
    private TextView startTextTip;
    private TextView mainLetter;
    private TextView subLetter;

    private int lettersCont;
    private float time;
    private int letterNum;


    public ChartFragment() {
        // Required empty public constructor
    }
    public static ChartFragment newInstance(float letterTime , int letterNum) {
        Log.i("LifeCircle","ChartFragment - New instance made");
        ChartFragment chart = new ChartFragment();
        Bundle args = new Bundle();
        args.putFloat(TIME,letterTime);
        args.putInt(NUM_LETTERS,letterNum);
        chart.setArguments(args);
        return chart;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i("LifeCircle","ChartFragment - Created");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            time = getArguments().getFloat(TIME);
            letterNum = getArguments().getInt(NUM_LETTERS);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("LifeCircle","ChartFragment - View Created");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chart, container, false);

        //initializing variables
        mainLetters = new ArrayList<>();

        chart = new ChartThread();

        startTextTip = v.findViewById(R.id.startText);
        startTextTip.setVisibility(View.VISIBLE);

        mainLetter = v.findViewById(R.id.mainLetterText);
        mainLetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaying){
                    restart();
                }else{
                    Log.i("GameCircle","Game Started" + this);
                    isPlaying = true;
                    startTextTip.setVisibility(View.INVISIBLE);
                    chart.start();
                }

            }
        });

        subLetter = v.findViewById(R.id.subLetterText);

        initialization();

        return v;
    }

    private void restart() {
        Log.i("GameCircle","Restarting Game" + this);

        chart.interrupt();
        chart = new ChartThread();

        //reset everything
        initialization();

        isPlaying=true;
        chart.start();
    }

    private void initialization() {
        initializeAlphabet(mainLetters);
        lettersCont=0;
        isPlaying = false;

        mainLetter.setText("A");
        subLetter.setText("j");
    }

    private void initializeAlphabet(ArrayList<String> letters){
        letters.clear();
        letters.add("A");
        letters.add("B");
        letters.add("C");
        letters.add("D");
        letters.add("E");
        letters.add("F");
        letters.add("G");
        letters.add("H");
        letters.add("I");
        letters.add("J");
        letters.add("K");
        letters.add("L");
        letters.add("M");
        letters.add("N");
        letters.add("O");
        letters.add("P");
        letters.add("Q");
        letters.add("R");
        letters.add("S");
        letters.add("T");
        letters.add("U");
        letters.add("V");
        letters.add("W");
        letters.add("X");
        letters.add("Y");
        letters.add("Z");
    }

    /**chartLogic - Contains the logic of the chart, change randomly the mainLetters
     * (Only make sense in this context as it uses global variables)
     * @return boolean that represents if the exercise is over - False means is over*/
    private boolean chartLogic(){
        if(lettersCont == letterNum){
            //exercise finished
            return false;
        }else if (mainLetters.size()==0){
            //Reset mainLetters - because letterNum is greater than the whole alphabet
            initializeAlphabet(mainLetters);
        }

        //get a random mainLetter from the list
        Random r = new Random();
        String nextMainLetter = mainLetters.get(r.nextInt(mainLetters.size()));
        mainLetters.remove(nextMainLetter);
        mainLetter.setText(nextMainLetter);

        //get a random sub letter from the list
        String nextSubLetter = subLetters[r.nextInt(subLetters.length)];
        subLetter.setText(nextSubLetter);

        //cont the number of mainLetters displayed
        lettersCont++;
        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("LifeCircle","ChartFragment - Attached");
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnChartFragmentChangedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("LifeCircle","ChartFragment - Started");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("LifeCircle","ChartFragment - Resumed" + this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("LifeCircle","ChartFragment - Paused");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("LifeCircle","ChartFragment - Stopped");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("LifeCircle","ChartFragment - View Destroyed");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("LifeCircle","ChartFragment - Destroyed");
        if(chart!=null){
            Log.i("GameCircle","Stopping thread of game (OnDestroy)");
            chart.interrupt();
            chart = null;
        }
    }

    @Override
    public void onDetach() {
        Log.i("LifeCircle","ChartFragment - Detached");
        super.onDetach();
        mCallback = null;
    }

    // Container Activity must implement this interface
    public interface OnChartFragmentChangedListener {
        /**chartFinished - update UI when the exercise is finished*/
        void chartFinished();
    }


    private class ChartThread extends Thread{
        @Override
        public void run() {
            super.run();
            try{
                Log.i("GameCircle","time: " + time);
                int timeInt= (int) (time*1000.0f);
                Log.i("GameCircle","timeInt: " + timeInt);

                while(!isInterrupted()){
                    Thread.sleep(timeInt);
                    if(getActivity()!=null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(!chartLogic()){
                                    //Exercise finished
                                    mCallback.chartFinished();
                                    //Stop thread
                                    Log.i("GameCircle","Stopping Thread (While)");
                                    chart.interrupt();
                                }
                            }
                        });
                    }
                }
            }catch(InterruptedException e){
                Log.i("GameCircle","Stopping Thread (Catch)");
                Thread.currentThread().interrupt();
            }
        }
    }

}


