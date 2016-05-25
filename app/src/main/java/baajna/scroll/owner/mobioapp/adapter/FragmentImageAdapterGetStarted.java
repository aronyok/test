package baajna.scroll.owner.mobioapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mobioapp.baajna.R;
import baajna.scroll.owner.mobioapp.fragment.FragmentImageGetStarted;

public class FragmentImageAdapterGetStarted extends FragmentPagerAdapter {
    
    private int[] offerImages = {
    		R.drawable.img_getstarted_1,
            R.drawable.img_getstarted_2,
            R.drawable.img_getstarted_3,
            R.drawable.img_getstarted_4
            

            
            

    };

    private int mCount = offerImages.length;

    public FragmentImageAdapterGetStarted(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new FragmentImageGetStarted(offerImages[position]);
    }

    @Override
    public int getCount() {
        return mCount;
    }

    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }
}


