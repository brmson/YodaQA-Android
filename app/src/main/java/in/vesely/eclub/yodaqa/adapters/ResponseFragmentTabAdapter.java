package in.vesely.eclub.yodaqa.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import in.vesely.eclub.yodaqa.view.ResponseFragment;

/**
 * Created by vesely on 6/16/15.
 */
public class ResponseFragmentTabAdapter extends FragmentStatePagerAdapter {

    private final List<Class<? extends ResponseFragment>> fragments;
    private final String[] titles;

    public ResponseFragmentTabAdapter(FragmentManager fm, List<Class<? extends ResponseFragment>> fragments, String[] titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public Fragment getItem(int position) {
        try {
            Fragment fragment = fragments.get(position).newInstance();
            Bundle args = new Bundle();
            fragment.setArguments(args);
            return fragment;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
