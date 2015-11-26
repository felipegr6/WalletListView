package br.com.fgr.walletlistview.ui.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class AbstractCardAdapter extends BaseAdapter {

    private Context context;
    private int resId;

    public AbstractCardAdapter(Context context, @LayoutRes int resId) {
        this.context = context;
        this.resId = resId;
    }

    @Override
    public abstract int getCount();

    @Override
    public abstract Object getItem(int position);

    @Override
    public abstract long getItemId(int position);

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(resId, parent, false);

        convertView.setTag(String.format("view_%s", String.valueOf(position)));

        return convertView;

    }

    public abstract boolean reorderList(int index1, int index2);

}