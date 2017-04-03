package com.example.amareelez.db_launcher.applist;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amareelez.db_launcher.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.TreeMap;

class AppsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Drawable> apps;
    private Context context;
    private TreeMap<CharSequence, Double> counts = new TreeMap<>();
    private int width;

    AppsAdapter(ArrayList<Drawable> apps, ArrayList<Integer> news, Context context, int width) {
        this.apps = apps;
        this.context = context;
        this.width = width;

        for (int i = 3 * width + 4; i < 4 * width + 4; i++)
            counts.put(Integer.toHexString(i - 2 * width - 2), 0.5);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return 0;
        else if (position == width + 1 || position == 2 * width + 3)
            return 1;
        else if (position == width + 2)
            return 3;
        else
            return 2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case 0: {
                viewHolder = new ViewHolderPopular(inflater.inflate(R.layout.app_popular, parent, false));
                break;
            }
            case 1:
                viewHolder = new ViewHolderDivider(inflater.inflate(R.layout.app_divider, parent, false));
                break;
            case 2:
                viewHolder = new ViewHolderUsual(inflater.inflate(R.layout.app, parent, false), context);
                break;
            case 3:
                viewHolder = new ViewHolderNew(inflater.inflate(R.layout.app_new, parent, false));
                break;
        }

        if (viewType == 2)
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CharSequence s = ((TextView) v.findViewById(R.id.appName)).getText();
                    Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                    if (counts.containsKey(s)) {
                        double n = counts.get(s);
                        counts.remove(s);
                        counts.put(s, n + 1);
                    } else
                        counts.put(s, 1.);
                    notifyItemRangeChanged(1, width);
                }
            });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 0: {
                break;
            }
            case 1: {
                break;
            }
            case 2: {
                if (position > 2 * width + 3) {
                    ((ViewHolderUsual) holder).mTextView.setText(Integer.toHexString(position - 2 * width - 2));
                    ((ViewHolderUsual) holder).mImageView.setImageDrawable(apps.get(position - 2 * width - 2));
                } else if (position < width + 1) {
                    ArrayList<CharSequence> entries = new ArrayList<>(counts.keySet());
                    CharSequence[] temp = new CharSequence[entries.size()];
                    entries.toArray(temp);
                    Arrays.sort(temp, new Comparator<CharSequence>() {
                        @Override
                        public int compare(CharSequence o1, CharSequence o2) {
                            return -1 * counts.get(o1).compareTo(counts.get(o2));
                        }
                    });
                    Integer pos = Integer.valueOf((String) temp[position - 1], 16);
                    ((ViewHolderUsual) holder).mTextView.setText(Integer.toHexString(pos));
                    ((ViewHolderUsual) holder).mImageView.setImageDrawable(apps.get(pos));
                } else {
                    for (int i = 1; i <= width; i++) {
                        ((ViewHolderUsual) holder).mTextView.setText(Integer.toHexString(new Random().nextInt()));
                        ((ViewHolderUsual) holder).mImageView.setImageDrawable(apps.get(new Random().nextInt(apps.size())));
                    }
                }
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    private class ViewHolderUsual extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private final MenuItem.OnMenuItemClickListener onRemoveMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int pos = getAdapterPosition();
                apps.remove(pos - 2 * width - 3);
                //notifyItemRemoved(pos);
                notifyItemRangeChanged(pos - 2 * width - 3, getItemCount());
                return true;
            }
        };
        ImageView mImageView;
        TextView mTextView;
        Context context;
        private final MenuItem.OnMenuItemClickListener onInfoMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(context, mTextView.getText(), Toast.LENGTH_SHORT).show();
                return true;
            }
        };

        ViewHolderUsual(View itemView, Context context) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.appIcon);
            mTextView = (TextView) itemView.findViewById(R.id.appName);
            this.context = context;

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Actions");
            MenuItem Edit = menu.add(Menu.NONE, 1, 1, "Info");
            MenuItem Delete = menu.add(Menu.NONE, 2, 2, "Delete");
            Edit.setOnMenuItemClickListener(onInfoMenu);
            Delete.setOnMenuItemClickListener(onRemoveMenu);
        }
    }

    private class ViewHolderPopular extends RecyclerView.ViewHolder {

        TextView mTextView;

        ViewHolderPopular(View itemView) {
            super(itemView);

            mTextView = (TextView) itemView.findViewById(R.id.popular);
        }
    }

    private class ViewHolderDivider extends RecyclerView.ViewHolder {

        ViewHolderDivider(View itemView) {
            super(itemView);
        }
    }

    private class ViewHolderNew extends RecyclerView.ViewHolder {

        TextView mTextView;

        ViewHolderNew(View itemView) {
            super(itemView);

            mTextView = (TextView) itemView.findViewById(R.id.newApp);
        }
    }
}
