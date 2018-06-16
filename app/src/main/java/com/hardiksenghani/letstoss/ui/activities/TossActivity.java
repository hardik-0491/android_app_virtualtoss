package com.hardiksenghani.letstoss.ui.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.hardiksenghani.letstoss.R;
import com.hardiksenghani.letstoss.controller.TossManager;
import com.hardiksenghani.letstoss.db.AppDatabase;
import com.hardiksenghani.letstoss.db.DBUtils;
import com.hardiksenghani.letstoss.model.Toss;
import com.hardiksenghani.letstoss.ui.fragments.TossFragment;
import com.hardiksenghani.letstoss.ui.viewmodel.TossActivityViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TossActivity extends AppCompatActivity
        implements DBUtils.DBTask {

    ViewPager viewPager;
    TabLayout tabLayout;
    TossActivityViewModel tossActivityViewModel;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toss);

        Toolbar toolbar = findViewById(R.id.activity_toss_toolbar);
        setSupportActionBar(toolbar);

        tossActivityViewModel = ViewModelProviders.of(this)
                .get(TossActivityViewModel.class);

        tossActivityViewModel.getAllTossLiveData().observe(this,
                new Observer<ArrayList<Toss>>() {
                    @Override
                    public void onChanged(@Nullable ArrayList<Toss> tosses) {
                        if (viewPagerAdapter != null) {
                            viewPagerAdapter.updateValues(tosses);
                        }
                    }
                });

        LiveData<List<Integer>> allTossNamesLiveData
                = tossActivityViewModel.getAllTossNamesLiveData();
        if (allTossNamesLiveData == null) {
            HashMap<String, Object> map = new HashMap<>();
            new DBUtils.DBAsyncTask(this, map).execute();
        } else {
            observeAllTossNameLiveData();
        }


        tabLayout = findViewById(R.id.activity_toss_tab_layout);
        viewPager = findViewById(R.id.activity_toss_view_pager);

        initializeViewPager();
        tabLayout.setupWithViewPager(viewPager);
    }

    private void observeAllTossNameLiveData() {
        tossActivityViewModel.getAllTossNamesLiveData().observe(this,
                new Observer<List<Integer>>() {
                    @Override
                    public void onChanged(@Nullable List<Integer> tossIds) {
                        tossActivityViewModel.updateTossData(tossIds);
                    }
                });
    }

    private void initializeViewPager() {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void performTask(AppDatabase appDatabase, HashMap<String, Object> objectMap) {
        tossActivityViewModel
                .setAllTossNamesLiveData(TossManager.getAllTossWithLiveData(appDatabase));
        observeAllTossNameLiveData();
    }

    @Override
    public void taskCompleted() {

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        HashMap<String, TossFragment> tossFragmentHashMap;
        ArrayList<Toss> tosses;

        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            tossFragmentHashMap = new HashMap<>();
            tosses = new ArrayList<>();
        }

        public void updateValues(ArrayList<Toss> tosses) {
            this.tosses.clear();
            this.tosses.addAll(tosses);

            for (Toss toss : this.tosses) {
                if (!tossFragmentHashMap.containsKey(toss.getName())) {
                    TossFragment tossFragment = new TossFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt(TossFragment.ARG_KEY_TOSS, toss.getTossId());
                    tossFragment.setArguments(bundle);
                    tossFragmentHashMap.put(toss.getName(), tossFragment);
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            return tossFragmentHashMap.get(tosses.get(position).getName());
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (getCount() > 0) {
                return tosses.get(position).getName();
            } else {
                return "";
            }
        }

        @Override
        public int getCount() {
            return tosses.size();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_toss_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_setting:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}
