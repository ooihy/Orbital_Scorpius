package com.example.shanna.orbital2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class SectionPagerAdapter extends FragmentPagerAdapter{

    public SectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                RequestFragment requestsFragment = new RequestFragment();
                return requestsFragment;
            case 1:
                ChatsFragment chatsFragment = new ChatsFragment();
                return chatsFragment;

            case 2:
                PartnersFragment partnersFragment = new PartnersFragment();
                return partnersFragment;

            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 3; //3 tabs
    }
}
