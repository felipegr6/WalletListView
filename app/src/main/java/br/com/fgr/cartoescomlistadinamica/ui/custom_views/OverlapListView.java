package br.com.fgr.cartoescomlistadinamica.ui.custom_views;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;

public class OverlapListView extends AbsListView {

    private Context context;
    private RelativeLayout relativeLayout;

    public OverlapListView(Context context, RelativeLayout relativeLayout) {

        super(context);

        this.context = context;
        this.relativeLayout = relativeLayout;

    }

    public <T extends BaseAdapter> void setAdapter(T adapter) {

        for (int i = 0; i < adapter.getCount(); i++) {

            View v = adapter.getView(i, null, null);

            RelativeLayout.LayoutParams lp =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);

            lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            lp.topMargin = (i * 360);

            relativeLayout.addView(v, lp);

        }

    }

    @Override
    public ListAdapter getAdapter() {
        return null;
    }

    @Override
    public void setSelection(int position) {

    }

}