package com.teleclub.rajatalk.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.teleclub.rajatalk.fragment.CallFragment;
import com.teleclub.rajatalk.fragment.CallHistoryFragment;
import com.teleclub.rajatalk.fragment.CallbackFragment;
import com.teleclub.rajatalk.fragment.TopUpFragment;

public class TabPagerAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;
    private CallFragment m_callFragment;
    private TopUpFragment m_topupFragment;
    private CallbackFragment m_callbackFragment;

    public TabPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;

        m_callFragment = CallFragment.newInstance();
        m_topupFragment = TopUpFragment.newInstance();
        m_callbackFragment = CallbackFragment.newInstance();
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return m_callbackFragment;
            case 1:
                return m_callFragment;
            case 2:
                return m_topupFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    public void setCardNumber(String cardNumber) {
        m_callFragment.setCardNumber(cardNumber);
    }

    public void refresh(int position) {
        if (position == 2)
            m_topupFragment.refresh();
        else if (position == 0)
            m_callbackFragment.refresh();
    }
}
