package com.example.smartbusinesscard2;

/**
 * Created by 현욱 on 2016-11-30.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                System.out.println("one fragment");
                OneFragment tab1 = new OneFragment();
                return tab1;
            case 1:
                System.out.println("two fragment");
                TwoFragment tab2 = new TwoFragment();
                return tab2;
            case 2:
                System.out.println("three fragment");
                ThreeFragment tab3 = new ThreeFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}