package br.com.fgr.cartoescomlistadinamica.ui.custom_views;

import android.content.ClipData;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import br.com.fgr.cartoescomlistadinamica.R;

public class OverlapListView extends RelativeLayout {

    Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
    Drawable normalShape = getResources().getDrawable(R.drawable.shape);

    public OverlapListView(Context context) {
        super(context);
    }

    public OverlapListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OverlapListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public <T extends BaseAdapter> void setAdapter(T adapter) {

        for (int i = 0; i < adapter.getCount(); i++) {

            View v = adapter.getView(i, null, null);

            v.setOnDragListener(new OnDragListener() {

                @Override
                public boolean onDrag(View v, DragEvent event) {

                    int action = event.getAction();

                    switch (action) {
                        case DragEvent.ACTION_DRAG_STARTED:
                            // do nothing
                            break;
                        case DragEvent.ACTION_DRAG_ENTERED:
                            v.setBackgroundDrawable(enterShape);
                            break;
                        case DragEvent.ACTION_DRAG_EXITED:
                            v.setBackgroundDrawable(normalShape);
                            break;
                        case DragEvent.ACTION_DROP:
                            // Dropped, reassign View to ViewGroup
                            View view = (View) event.getLocalState();
                            ViewGroup owner = (ViewGroup) view.getParent();
                            owner.removeView(view);
                            RelativeLayout container = (RelativeLayout) v;
                            container.addView(view);
                            view.setVisibility(View.VISIBLE);
                            break;
                        case DragEvent.ACTION_DRAG_ENDED:
                            v.setBackgroundDrawable(normalShape);
                        default:
                            break;
                    }
                    return true;
                }

            });

            v.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {

                        ClipData data = ClipData.newPlainText("", "");
                        DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                        v.startDrag(data, shadowBuilder, v, 0);
                        v.setVisibility(View.INVISIBLE);

                        return true;

                    } else
                        return false;

                }

            });

            RelativeLayout.LayoutParams lp =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);

            lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            lp.topMargin = (i * 360);

            this.addView(v, lp);

        }

    }

}