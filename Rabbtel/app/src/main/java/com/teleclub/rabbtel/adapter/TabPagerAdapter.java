package com.teleclub.rabbtel.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.teleclub.rabbtel.fragment.CallFragment;
import com.teleclub.rabbtel.fragment.CallbackFragment;
import com.teleclub.rabbtel.fragment.TopUpFragment;
import com.teleclub.rabbtel.fragment.VoipFragment;

public class TabPagerAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;
    private CallFragment m_callFragment;
    private TopUpFragment m_topupFragment;
    private CallbackFragment m_callbackFragment;
    private VoipFragment m_voipfragment;

    public TabPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;

        m_callFragment = CallFragment.newInstance();
        m_topupFragment = TopUpFragment.newInstance();
        m_callbackFragment = CallbackFragment.newInstance();
        m_voipfragment  = VoipFragment.newInstance();
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return m_callbackFragment;
            case 1:
                return m_callFragment;
            case 2:
                return m_voipfragment;
            case 3:
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
