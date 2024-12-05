package com.example.expense.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.expense.Fragment.DailyFragment;
import com.example.expense.Fragment.MonthlyFragment;
import com.example.expense.Fragment.NoteFragment;
import com.example.expense.Fragment.YearlyFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {


    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new NoteFragment();
            case 1:
                return new DailyFragment();
            case 2:
                return new MonthlyFragment();
            case 3:
                return new YearlyFragment();
            default:
                throw new IllegalStateException("Invalid position: " + position);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "NOTES";
            case 1:
                return "DAILY";
            case 2:
                return "MONTHLY";
            case 3:
                return "YEARLY";
            default:
                return ""; // Hoặc trả về một chuỗi mặc định
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
