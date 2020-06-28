package com.longynuss.chart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements
        OptionFragment.OnOptionFragmentChangedListener,
        ChartFragment.OnChartFragmentChangedListener,
        TutorialFragment.OnTutorialFragmentChangedListener{

    private static final String IS_DISPLAYING_KEY = "isDisplaying";

    //default values - if changed must change default text in OPTIONFRAGMENT
    private static float gameLetterTime =2.5f; // Value in seconds
    private static int gameLetterNum =26;

    //Those constants must be the same of the constants in OPTIONFRAGMENT
    private static final int MIN_LETTERS = 26;
    private static final int INC_LETTERS = 26;
    private static final float MAX_TIME = 2.5f; //Value in seconds
    private static final float INC_TIME = 0.2f;

    private final static String CHART_FRAGMENT__KEY = "chartFragment";
    private final static String TUTORIAL_FRAGMENT__KEY = "tutorialFragment";
    private final static String OPTION_FRAGMENT__KEY = "optionFragment";

    private Fragment chartFragment;
    private Fragment tutorialFragment;
    private Fragment optionFragment;
    private FragmentManager fm;

    private boolean isDisplaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("LifeCicle","MainActivity - Created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        MyPreferences pref = new MyPreferences(getApplicationContext());
        if(pref.isFirstAcess()){

            //ask user to see the tutorial
            showMessage(MainActivity.this,R.string.askTutorialTitle,
                    R.string.askTutorial,R.string.yesTutorial, R.string.noTutorial);

            pref.doFirstAcess();
        }

        //Restore option saved in preferences
        if(pref.optionStatesExist()){
            int numLetterProgress = pref.getNumLettersBarProgress();
            int changeBarProgress = pref.getVelocityBarProgress();

            gameLetterTime = calculateTimeLetter(changeBarProgress);
            gameLetterNum = calculateNumberLetter(numLetterProgress);
        }

        fm = getSupportFragmentManager();

        if(savedInstanceState != null){
            chartFragment = fm.getFragment(savedInstanceState,CHART_FRAGMENT__KEY);
            tutorialFragment = fm.getFragment(savedInstanceState,TUTORIAL_FRAGMENT__KEY);
            optionFragment = fm.getFragment(savedInstanceState,OPTION_FRAGMENT__KEY);
            isDisplaying = savedInstanceState.getBoolean(IS_DISPLAYING_KEY);
        }else{
            tryCreateFragmentsInstances();
            isDisplaying = false;
        }

        if(!isDisplaying){
            showFragment(chartFragment);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Log.i("Screen","Tutorial clicked");
                    if(tutorialFragment.isHidden()){
                        Log.i("Screen","Tutorial displaying");
                        destroyChartFragment();
                        showFragment(tutorialFragment);
                    }
                    return true;
                case R.id.navigation_dashboard:
                    Log.i("Screen","Chart clicked");
                    if(chartFragment==null){
                        Log.i("Screen","Chart displaying");
                        recreateChartFragment();
                    }
                    return true;
                case R.id.navigation_notifications:
                    Log.i("Screen","Option clicked");
                    if(optionFragment.isHidden()){
                        Log.i("Screen","Option displaying");
                        destroyChartFragment();
                        showFragment(optionFragment);
                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onGameOptionsChanged(float letterTime, int letterNum) {
        gameLetterTime = letterTime;
        gameLetterNum = letterNum;
        Log.i("LifeCicle","New LetterTime:" + gameLetterTime +
                                    "New LetterNum:" + gameLetterNum);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i("LifeCicle","MainActivity - Saving States");
        super.onSaveInstanceState(outState);

        if(chartFragment != null && chartFragment.isAdded()){
            fm.putFragment(outState,CHART_FRAGMENT__KEY,chartFragment);
            Log.i("LifeCicle","MainActivity - Chart fragment saved");
        }if(tutorialFragment.isAdded()){
            fm.putFragment(outState,TUTORIAL_FRAGMENT__KEY,tutorialFragment);
            Log.i("LifeCicle","MainActivity - Turotial fragment saved");
        }if(optionFragment.isAdded()){
            fm.putFragment(outState,OPTION_FRAGMENT__KEY,optionFragment);
            Log.i("LifeCicle","MainActivity - Option fragment saved");
        }

        outState.putBoolean(IS_DISPLAYING_KEY,isDisplaying);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("LifeCicle","MainActivity - Restoring States");

    }

    private void tryCreateFragmentsInstances(){
        if(chartFragment == null){
            chartFragment = ChartFragment.newInstance(gameLetterTime,gameLetterNum);
            fm.beginTransaction().add(R.id.displayPanel,chartFragment).commit();
        }if(tutorialFragment==null){
            tutorialFragment = TutorialFragment.newInstance();
            fm.beginTransaction().add(R.id.displayPanel,tutorialFragment).commit();
        }if(optionFragment==null){
            optionFragment = OptionFragment.newInstance();
            fm.beginTransaction().add(R.id.displayPanel,optionFragment).commit();
        }
        hideAllFragments();
    }

    /**showFragment - Hide all fragments that are being displayed and show that one requested*/
    private void showFragment(Fragment fragment){
        hideAllFragments();

        Log.i("LifeCicle","Displaying Fragment: "+fragment);
        fm.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out).
                show(fragment).commit();

        isDisplaying=true;
        fm.popBackStack();
    }

    /**hideAllFragments - hide all fragments that exists in the project
     * @see OptionFragment,ChartFragment,TutorialFragment*/
    private  void hideAllFragments(){
        Log.i("LifeCicle","Hiding all fragments");
        if(!optionFragment.isHidden()){
            fm.beginTransaction().hide(optionFragment).commit();
        }if(!tutorialFragment.isHidden()){
            fm.beginTransaction().hide(tutorialFragment).commit();
        }
    }

    private void recreateChartFragment(){
        chartFragment = ChartFragment.newInstance(gameLetterTime,gameLetterNum);
        fm.beginTransaction().add(R.id.displayPanel,chartFragment).commit();
        showFragment(chartFragment);
    }

    private void destroyChartFragment(){
        if(chartFragment != null){
            fm.beginTransaction().remove(chartFragment).commit();
            chartFragment = null;
        }
    }

    @Override
    public void chartFinished() {
        //Tell user the chart is finished
        Toast.makeText(getApplicationContext(),R.string.chartFinished,Toast.LENGTH_SHORT).show();

        //Reset the instance of charFragment
        destroyChartFragment();
        recreateChartFragment();
    }


    /**
     * showMessage - Display {@link AlertDialog} with two options to the user choose.
     *
     * title,message,yesText,noText are of the type: int because you are supposed to use
     * a text defined in strings.xml
     * */
    private void showMessage(Context context,int title, int message ,int yesText,int noText){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);

        builder.setTitle(getResources().getString(title))
                .setMessage(getResources().getString(message))
                .setPositiveButton(getResources().getString(yesText), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //yesText - clicked
                        //go to tutorial
                        Log.i("Screen","Tutorial displaying");
                        destroyChartFragment();
                        showFragment(tutorialFragment);
                    }
                })
                .setNegativeButton(getResources().getString(noText), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //noText - clicked
                        //nothing to do
                    }
                }).show();
    }


    private float calculateTimeLetter(int progress){
        return MAX_TIME-(INC_TIME*progress);
    }

    private int calculateNumberLetter(int progress){
        return MIN_LETTERS+(INC_LETTERS*progress);
    }

    @Override
    public void tutorialEnded() {
        if(chartFragment==null){
            Log.i("Screen","Chart displaying");
            recreateChartFragment();
        }
    }
}
