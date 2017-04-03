package com.example.amareelez.db_launcher.applist;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.amareelez.db_launcher.R;

import java.util.ArrayList;
import java.util.Random;

public class AppListActivity extends Activity {
    public static ThemeType themeType = ThemeType.LIGHT;
    public static int[] tableSizes = {5, 7};
    private static ArrayList<Drawable> images = new ArrayList<>();
    private static ArrayList<Integer> newApps = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(themeType == ThemeType.LIGHT ? R.style.Theme_IndigoLight : R.style.Theme_IndigoDark);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);

        RecyclerView rvItems = (RecyclerView) findViewById(R.id.recycleView);
        registerForContextMenu(rvItems);

        if (images.size() == 0) {
            int curr_id = 1, resId = 0;
            do {
                resId = getResources().getIdentifier("ic_" + curr_id, "drawable", "com.example.amareelez.db_launcher");
                if (resId != 0)
                    images.add(ResourcesCompat.getDrawable(getResources(), resId, null));
                curr_id++;
            } while (resId != 0);
        }

        int numCols;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            numCols = tableSizes[0];
        else
            numCols = tableSizes[1];

        if (newApps.size() == 0) {
            for (int i = 0; i < numCols; i++)
                newApps.add(new Random().nextInt());
        }

        final AppsAdapter appsAdapter = new AppsAdapter(images, newApps, getApplicationContext(), numCols);
        rvItems.setAdapter(appsAdapter);

        GridLayoutManager layoutManager = new WrapContentGridLayoutManager(this, numCols);
        final int width = numCols;
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (appsAdapter.getItemViewType(position)) {
                    case 0:
                        return width;
                    case 1:
                        return width;
                    case 3:
                        return width;
                    case 2:
                        return 1;
                }
                return 1;
            }
        });
        rvItems.setLayoutManager(layoutManager);

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                final int curSize = appsAdapter.getItemCount();

                for (int i = 0; i < curSize; i++)
                    images.add(images.get(i));

                view.post(new Runnable() {
                    @Override
                    public void run() {
                        appsAdapter.notifyItemRangeInserted(curSize, images.size() - 1);
                    }
                });
            }
        };
        rvItems.addOnScrollListener(scrollListener);
    }

    private class WrapContentGridLayoutManager extends GridLayoutManager {
        WrapContentGridLayoutManager(Context context, int spanCount) {
            super(context, spanCount);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("Error", "IndexOutOfBoundsException in RecyclerView happens");
            }
        }
    }

}
