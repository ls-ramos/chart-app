package com.longynuss.chart;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TutorialFragment extends Fragment {

    private int contador;
    private OnTutorialFragmentChangedListener listener;

    public TutorialFragment() {
        // Required empty public constructor
    }

    public static TutorialFragment newInstance() {
        return new TutorialFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tutorial, container, false);

        contador = 0;
        final TextView tutorialTextTop = v.findViewById(R.id.tutorialTextTop);
        final TextView mainLetter = v.findViewById(R.id.tutorialMainLetter);
        final TextView smallLetter = v.findViewById(R.id.tutorialSubLetter);
        final TextView tutorialTextMiddle = v.findViewById(R.id.tutorialTextMiddle);
        final TextView tutorialTextSubLetter = v.findViewById(R.id.tutorialTextSubLetter);
        final TextView tutorialTextBellow = v.findViewById(R.id.tutorialTextBelow);

        mainLetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contador == 0){
                    tutorialTextTop.setText(R.string.tutorialTextTop1);
                    tutorialTextMiddle.setText(R.string.tutorialTextMiddle1);
                    tutorialTextSubLetter.setText(R.string.tutorialTextSmallLetter1);
                    tutorialTextBellow.setText(R.string.tutorialTexBellow1);

                    mainLetter.setText(R.string.tutorialMainLetterB);
                    smallLetter.setText(R.string.tutorialSmallLetterD);
                    contador = 1;
                }else if(contador == 1){
                    tutorialTextTop.setText(R.string.tutorialTextTop2);
                    tutorialTextMiddle.setText(R.string.tutorialTextMiddle1);
                    tutorialTextSubLetter.setText(R.string.tutorialTextSmallLetter2);
                    tutorialTextBellow.setText(R.string.tutorialTexBellow2);

                    mainLetter.setText(R.string.tutorialMainLetterC);
                    smallLetter.setText(R.string.tutorialSmallLetterE);
                    contador = 2;
                }else if (contador==2) {
                    tutorialTextTop.setText(R.string.tutorialTextTop3);
                    tutorialTextMiddle.setText(R.string.tutorialTextMiddle1);
                    tutorialTextSubLetter.setText(R.string.tutorialTextSmallLetter3);
                    tutorialTextBellow.setText(R.string.tutorialTexBellow3);

                    mainLetter.setText(R.string.tutorialMainLetterD);
                    smallLetter.setText(R.string.tutorialSmallLetterJ);
                    contador = 3;
                }else if (contador ==3) {
                    tutorialTextTop.setText(R.string.tutorialTextTop4);
                    tutorialTextMiddle.setText(R.string.tutorialTextMiddle2);
                    tutorialTextSubLetter.setText(R.string.tutorialTextSmallLetter4);
                    tutorialTextBellow.setText(R.string.tutorialTexBellow4);

                    mainLetter.setText(R.string.tutorialMainLetterH);
                    smallLetter.setText(R.string.tutorialSmallLetterE);
                    contador = 4;
                }else if (contador==4) {
                    tutorialTextTop.setText(R.string.tutorialTextTop0);
                    tutorialTextMiddle.setText(R.string.tutorialTextMiddle0);
                    tutorialTextSubLetter.setText(R.string.tutorialTextSmallLetter0);
                    tutorialTextBellow.setText(R.string.tutorialTexBellow0);

                    mainLetter.setText(R.string.tutorialMainLetterA);
                    smallLetter.setText(R.string.tutorialSmallLetterJ);
                    contador = 0;

                    //go to charFragment
                    listener.tutorialEnded();
                }
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            listener = (OnTutorialFragmentChangedListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                    + "must implement OnTutorialFragmentChangedListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnTutorialFragmentChangedListener{
        void tutorialEnded();
    }
}
