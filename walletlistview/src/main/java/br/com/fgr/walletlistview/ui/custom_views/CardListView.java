package br.com.fgr.walletlistview.ui.custom_views;

import android.content.ClipData;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import br.com.fgr.walletlistview.R;
import br.com.fgr.walletlistview.ui.adapters.AbstractCardAdapter;
import br.com.fgr.walletlistview.utils.GeneratorId;
import br.com.fgr.walletlistview.utils.Measure;

public class CardListView<T extends AbstractCardAdapter> extends ScrollView implements
        View.OnTouchListener, View.OnDragListener, View.OnLongClickListener, View.OnClickListener {

    private Context context;

    private RelativeLayout mainLayout;
    private Drawable enterShape;
    private Drawable normalShape;

    private SideDraggable draggable;
    private ActionOnClick actionOnClick;

    boolean isDraggingInTouch = false;
    float lastXTouch;
    float lastYTouch;
    float deltaXTouch;

    private T baseAdapter;

    private boolean isAppear = false;

    public CardListView(Context context) {

        super(context);
        this.context = context;

        enterShape = getResources().getDrawable(R.drawable.bg_over);
        normalShape = getResources().getDrawable(R.drawable.bg);
        mainLayout = new RelativeLayout(context);

        this.addView(mainLayout);

    }

    public CardListView(Context context, AttributeSet attrs) {

        super(context, attrs);
        this.context = context;

        enterShape = getResources().getDrawable(R.drawable.bg_over);
        normalShape = getResources().getDrawable(R.drawable.bg);
        mainLayout = new RelativeLayout(context, attrs);

        this.addView(mainLayout);

    }

    public void setAdapter(@NonNull T baseAdapter) {

        this.baseAdapter = baseAdapter;

        inflateViews();

    }

    public void setSideDraggable(@NonNull SideDraggable draggable) {
        this.draggable = draggable;
    }

    public void setActionOnClick(@NonNull ActionOnClick actionOnClick) {
        this.actionOnClick = actionOnClick;
    }

    private void reorderList(int oldPos, int newPos) {

        mainLayout.removeAllViews();
        baseAdapter.reorderList(oldPos, newPos);

        inflateViews();

    }

    private void inflateViews() {

        int size = baseAdapter.getCount();

        mainLayout.removeAllViews();

        for (int i = 0; i < size; i++) {

            RelativeLayout rl = new RelativeLayout(context);
            RelativeLayout.LayoutParams params = new RelativeLayout
                    .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            if (i != size - 1)
                rl.setTag(String.format("box_%s_close", String.valueOf(i)));
            else
                rl.setTag(String.format("box_%s_open", String.valueOf(i)));

            params.topMargin = i * Measure.getPixelsFromDP(context, 80);

            rl.setLayoutParams(params);

            if (Build.VERSION.SDK_INT >= 17)
                rl.setId(generateViewId());
            else
                rl.setId(GeneratorId.generate());

            View cardView = baseAdapter.getView(i, null, this);

            cardView.setOnTouchListener(this);
            cardView.setOnClickListener(this);
            cardView.setOnLongClickListener(this);

            rl.addView(cardView);
            rl.setOnDragListener(this);
            mainLayout.addView(rl);

        }

    }

    @Override
    public void onClick(View v) {

        String tags[] = ((String) ((View) v.getParent()).getTag()).split("_");
        int viewIndex = Integer.parseInt(tags[1]);
        String status = tags[2];

        while (viewIndex < baseAdapter.getCount()) {

            View parentBelow = this.findViewWithTag("box_" + (viewIndex + 1) + "_open") != null
                    ? this.findViewWithTag("box_" + (viewIndex + 1) + "_open")
                    : this.findViewWithTag("box_" + (viewIndex + 1) + "_close");

            if (parentBelow != null) {

                int height = 5 * v.getHeight() / 8;
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) parentBelow.getLayoutParams();

                switch (status) {

                    case "close":
                        params.topMargin = params.topMargin + height;
                        if (actionOnClick != null)
                            actionOnClick.onClose(baseAdapter.getItem(viewIndex));
                        break;
                    case "open":
                        params.topMargin = params.topMargin - height;
                        if (actionOnClick != null)
                            actionOnClick.onOpen(baseAdapter.getItem(viewIndex));
                        break;

                }

                parentBelow.setLayoutParams(params);

            }

            viewIndex++;

        }

        switch (status) {

            case "close":
                ((View) v.getParent()).setTag(tags[0] + "_" + tags[1] + "_open");
                break;
            case "open":
                ((View) v.getParent()).setTag(tags[0] + "_" + tags[1] + "_close");
                break;

        }

    }

    @Override
    public boolean onDrag(View v, DragEvent event) {

        switch (event.getAction()) {

            case DragEvent.ACTION_DRAG_ENTERED:
                // Ao entrar na área que pode fazer o drop
                v.setBackgroundDrawable(enterShape);
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                // Ao sair da área que pode fazer o drop
                v.setBackgroundDrawable(normalShape);
                break;
            case DragEvent.ACTION_DROP:
                // Ao fazer o drop
                View view = (View) event.getLocalState();

                RelativeLayout container = (RelativeLayout) v;
                View viewTo = container.getChildAt(0);

                int indexFrom = Integer.parseInt(((String) view.getTag()).split("_")[1]);
                int indexTo = Integer.parseInt(((String) viewTo.getTag()).split("_")[1]);

                reorderList(indexFrom, indexTo);

                break;
            case DragEvent.ACTION_DRAG_ENDED:
                // Ao terminar de arrastar
                v.setBackgroundDrawable(normalShape);
                View view2 = (View) event.getLocalState();
                view2.setVisibility(View.VISIBLE);
            default:
                break;

        }

        return true;

    }

    @Override
    public boolean onLongClick(View v) {

        ClipData data = ClipData.newPlainText("", "");
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);

        v.startDrag(data, shadowBuilder, v, 0);
        v.setVisibility(View.INVISIBLE);

        return true;

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int action = event.getAction();
        int indice = Integer.parseInt(((String) v.getTag()).split("_")[1]);

        if (action == MotionEvent.ACTION_DOWN && !isDraggingInTouch) {

            isDraggingInTouch = true;
            deltaXTouch = event.getX();

            return false;

        } else if (isDraggingInTouch) {

            if (action == MotionEvent.ACTION_MOVE) {

                v.setX(v.getX() + event.getX() - deltaXTouch);
                v.setY(v.getY());

                if (v.getX() < -500 && !isAppear) {

                    isAppear = true;

                    if (draggable != null) {

                        draggable.draggableToLeft(baseAdapter.getItem(indice));
                        isAppear = false;

                    }

                } else {

                    isAppear = true;

                    if (draggable != null) {

                        draggable.draggableToRight(baseAdapter.getItem(indice));
                        isAppear = false;

                    }

                }

                return false;

            } else if (action == MotionEvent.ACTION_UP) {

                isDraggingInTouch = false;
                lastXTouch = event.getX();
                lastYTouch = event.getY();

                v.setX(0);
                v.setY(0);

                return false;

            } else if (action == MotionEvent.ACTION_CANCEL) {

                v.setX(lastXTouch);
                v.setY(lastYTouch);
                isDraggingInTouch = false;

                v.setX(0);
                v.setY(0);

                return false;

            }

        }

        return false;

    }

    public interface SideDraggable<T> {

        void draggableToLeft(T card);

        void draggableToRight(T card);

    }

    public interface ActionOnClick<T> {

        void onOpen(T card);

        void onClose(T card);

    }

}