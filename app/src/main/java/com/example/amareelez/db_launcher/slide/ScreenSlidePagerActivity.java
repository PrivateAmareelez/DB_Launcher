package com.example.amareelez.db_launcher.slide;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.amareelez.db_launcher.R;
import com.example.amareelez.db_launcher.applist.AppListActivity;

import java.util.ArrayList;

public class ScreenSlidePagerActivity extends FragmentActivity {

    private static int sNUM_PAGES = 4;
    private static ArrayList<Fragment> mFragments = new ArrayList<>();
    private ViewPager pager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        pager = (ViewPager) findViewById(R.id.viewPager);
        PagerAdapter mPagerAdapter = new ScreenSlidePagedAdapter(getSupportFragmentManager());

        mFragments.add(new WelcomeFragment());
        mFragments.add(new BaseSlideOneFragment());
        mFragments.add(new BaseSlideTwoFragment());
        mFragments.add(new FinalFragment());

        pager.setAdapter(mPagerAdapter);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void switchToNext(View view) {
        pager.setCurrentItem(pager.getCurrentItem() + 1, true);
    }

    public void switchToActivity(View view) {
        Intent appListActivity = new Intent(getBaseContext(), AppListActivity.class);
        startActivity(appListActivity);
    }

    private class ScreenSlidePagedAdapter extends FragmentStatePagerAdapter {

        ScreenSlidePagedAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return sNUM_PAGES;
        }
    }
}
