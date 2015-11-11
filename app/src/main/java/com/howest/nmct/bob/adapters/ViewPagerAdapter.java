package com.howest.nmct.bob.adapters;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.howest.nmct.bob.R;

/**
 * illyism
 * 11/11/15
 *
 * http://github.com/rohaanhamid/ScrollableItemList
 */
public class ViewPagerAdapter extends PagerAdapter {

    public Object instantiateItem(ViewGroup collection, int position) {

        int resId = 0;
        switch (position) {
            case 0:
                resId = R.id.cardView;
                break;
            case 1:
                resId = R.id.cardOptionsView;
                break;
        }
        return collection.findViewById(resId);
    }


    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }
}
