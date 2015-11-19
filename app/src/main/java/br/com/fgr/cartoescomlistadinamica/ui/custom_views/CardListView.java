package br.com.fgr.cartoescomlistadinamica.ui.custom_views;

import android.content.ClipData;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import br.com.fgr.cartoescomlistadinamica.R;
import br.com.fgr.cartoescomlistadinamica.ui.adapters.AbstractCardAdapter;
import br.com.fgr.cartoescomlistadinamica.utils.GeneratorId;
import br.com.fgr.cartoescomlistadinamica.utils.Measure;

public class CardListView<T extends AbstractCardAdapter> extends RelativeLayout implements
        View.OnTouchListener, View.OnDragListener, View.OnLongClickListener, View.OnClickListener {

    private Context context;

    private Drawable enterShape;
    private Drawable normalShape;

    private SideDraggable draggable;

    private List<RelativeLayout> relativeLayouts;

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

    }

    public CardListView(Context context, AttributeSet attrs) {

        super(context, attrs);
        this.context = context;

        enterShape = getResources().getDrawable(R.drawable.bg_over);
        normalShape = getResources().getDrawable(R.drawable.bg);

    }

    public void setAdapter(@NonNull T baseAdapter) {

        this.baseAdapter = baseAdapter;
        relativeLayouts = new ArrayList<>();

        inflateViews();

    }

    public void setSideDraggable(@NonNull SideDraggable draggable) {
        this.draggable = draggable;
    }

    private void reorderList(int oldPos, int newPos) {

        RelativeLayout rl = relativeLayouts.remove(oldPos);

        relativeLayouts.add(newPos, rl);
        inflateViews();

    }

    private void inflateViews() {

        int size = baseAdapter.getCount();
        relativeLayouts.clear();
        removeAllViews();

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
            addView(rl);
            relativeLayouts.add(rl);

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
                        break;
                    case "open":
                        params.topMargin = params.topMargin - height;
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
                ViewGroup owner = (ViewGroup) view.getParent();
                RelativeLayout container = (RelativeLayout) v;
                View viewTo = container.getChildAt(0);

                int indexFrom = Integer.parseInt(((String) view.getTag()).split("_")[1]);
                int indexTo = Integer.parseInt(((String) viewTo.getTag()).split("_")[1]);

                ViewGroup root = (ViewGroup) findViewById(R.id.rl_t);

                if (!owner.getTag().equals(container.getTag())) {

                    owner.removeView(view);
                    container.removeView(viewTo);
                    owner.addView(viewTo);
                    container.addView(view);

                }

                view.setVisibility(View.VISIBLE);
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

        if (action == MotionEvent.ACTION_DOWN && !isDraggingInTouch) {

            isDraggingInTouch = true;
            deltaXTouch = event.getX();

            return false;

        } else if (isDraggingInTouch) {

            if (action == MotionEvent.ACTION_MOVE) {

                v.setX(v.getX() + event.getX() - deltaXTouch);
                v.setY(v.getY());

                Log.e("getX", String.valueOf(v.getX()));

                if (v.getX() < -500 && !isAppear) {

//                    isAppear = true;

                    draggable.draggableToLeft();
//                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                    builder.setMessage("Alguma coisa aqui.")
//                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    isAppear = false;
//                                }
//                            })
//                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    isAppear = false;
//                                }
//                            });
//
//                    builder.create().show();

                } else
                    draggable.draggableToRight();

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

    public interface SideDraggable {

        void draggableToLeft();

        void draggableToRight();

    }

}