package com.example.amareelez.db_launcher.slide;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.amareelez.db_launcher.R;
import com.example.amareelez.db_launcher.applist.AppListActivity;
import com.example.amareelez.db_launcher.applist.ThemeType;


public class FinalFragment extends Fragment {
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("size", ((RadioGroup) getView().findViewById(R.id.sizeChoosing)).getCheckedRadioButtonId());
        outState.putInt("theme", ((RadioGroup) getView().findViewById(R.id.themeChoosing)).getCheckedRadioButtonId());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_final_page, container, false);

        if (savedInstanceState != null) {
            ((RadioGroup) view.findViewById(R.id.sizeChoosing)).check(savedInstanceState.getInt("size"));
            ((RadioGroup) view.findViewById(R.id.themeChoosing)).check(savedInstanceState.getInt("theme"));
        } else {
            ((RadioGroup) view.findViewById(R.id.sizeChoosing)).check(R.id.size57);
            ((RadioGroup) view.findViewById(R.id.themeChoosing)).check(R.id.lightTheme);
        }

        ((RadioGroup) view.findViewById(R.id.sizeChoosing)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.size57) {
                    AppListActivity.tableSizes[0] = 5;
                    AppListActivity.tableSizes[1] = 7;
                } else if (checkedId == R.id.size46) {
                    AppListActivity.tableSizes[0] = 4;
                    AppListActivity.tableSizes[1] = 6;
                }
            }
        });

        ((RadioGroup) view.findViewById(R.id.themeChoosing)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.lightTheme) {
                    AppListActivity.themeType = ThemeType.LIGHT;
                } else if (checkedId == R.id.darkTheme) {
                    AppListActivity.themeType = ThemeType.DARK;
                }
            }
        });

        return view;
    }
}
