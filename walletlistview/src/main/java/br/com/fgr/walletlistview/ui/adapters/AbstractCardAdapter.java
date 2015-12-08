package br.com.fgr.walletlistview.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractCardAdapter<T> extends BaseAdapter {

    private Context context;
    private List<Integer> listResId;
    private List<T> list;

    public AbstractCardAdapter(Context context, List<T> list, Integer... resId) {

        this.context = context;
        this.list = list;
        this.listResId = new ArrayList<>();

        listResId.addAll(Arrays.asList(resId));

    }

    @Override
    public abstract int getCount();

    @Override
    public abstract T getItem(int position);

    @Override
    public abstract long getItemId(int position);

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int layoutId = listResId.get(getItemViewType(position));

        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(layoutId, parent, false);

        convertView.setTag(String.format("view_%s", String.valueOf(position)));

        return convertView;

    }

    @Override
    public abstract int getItemViewType(int position);

    public boolean reorderList(int index1, int index2) {

        if (index1 < list.size() && index2 < list.size()) {

            T abstractCard = list.remove(index1);

            list.add(index2, abstractCard);

            return true;

        }

        return false;

    }

}