package com.hardiksenghani.letstoss.ui.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.hardiksenghani.letstoss.R;
import com.hardiksenghani.letstoss.controller.TossFlipper;
import com.hardiksenghani.letstoss.controller.TossManager;
import com.hardiksenghani.letstoss.ui.viewmodel.TossFragmentViewModel;

public class TossFragment extends Fragment {

    public static final String ARG_KEY_TOSS = "toss";

    private FloatingActionButton floatingActionButton;
    private TossFragmentViewModel tossFragmentViewModel;
    private TextView tossOutcomeText;
    private int tossId;
    private SharedPreferences sharedPreferences;
    private FirebaseAnalytics firebaseAnalytics;

    public TossFragment() {
        tossId = -1;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (tossId == -1) {
            if (getArguments() != null) {
                tossId = getArguments().getInt(ARG_KEY_TOSS);
            }
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        firebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_toss, container, false);

        tossFragmentViewModel = ViewModelProviders.of(this).get(TossFragmentViewModel.class);
        tossFragmentViewModel.getTossFlipperMutableLiveData().observe(this, new Observer<TossFlipper>() {
            @Override
            public void onChanged(@Nullable TossFlipper tossFlipper) {
                tossOutcomeText.setText(tossFlipper.getTossOutcome());
            }
        });

        tossFragmentViewModel.setCurrentToss(tossId);

        floatingActionButton = view.findViewById(R.id.fragment_toss_floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = getResources().getString(R.string.pref_key_outcome_delay_time);
                String value = sharedPreferences.getString(key, Integer.toString(TossManager.DEFAULT_OUTCOME_DELAY_TIME));
                tossFragmentViewModel.flipToss(Integer.parseInt(value));

                Bundle firebaseEventDataBundle = new Bundle();
                firebaseEventDataBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "tossed");
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, firebaseEventDataBundle);
            }
        });

        tossFragmentViewModel.getTossFlipInProgress().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isFlipInProgress) {
                if (isFlipInProgress) {
                    floatingActionButton.setVisibility(View.INVISIBLE);
                    tossOutcomeText.setTextColor(Color.GRAY);
                } else {
                    floatingActionButton.setVisibility(View.VISIBLE);
                    tossOutcomeText.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                }
            }
        });

        tossOutcomeText = view.findViewById(R.id.fragment_toss_result);
        tossOutcomeText.setTextColor(Color.GRAY);
        tossOutcomeText.setText(R.string.fragment_toss_helptext);

        MobileAds.initialize(getContext());
        AdView adView = view.findViewById(R.id.fragment_toss_banner_ad);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        return view;
    }

}
