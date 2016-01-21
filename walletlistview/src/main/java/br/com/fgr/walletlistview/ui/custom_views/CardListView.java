package br.com.fgr.walletlistview.ui.custom_views;

import android.content.ClipData;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
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

public class CardListView<T extends AbstractCardAdapter> extends ScrollView implements
        View.OnTouchListener, View.OnDragListener, View.OnLongClickListener, View.OnClickListener {

    private Context context;

    private RelativeLayout mainLayout;
    private Drawable enterShape;
    private Drawable normalShape;

    private SideDraggable draggable;
    private ActionOnClick actionOnClick;
    private OnReorderList reorderList;

    boolean isDraggingInTouch = false;
    float lastXTouch;
    float lastYTouch;
    float deltaXTouch;

    private T baseAdapter;

    private boolean isAppear = false;
    private int distanceBetweenCards;
    private int marginBetweenCards;
    private boolean closeOnOpenOtherCard;
    private boolean onClickDoNotWork;

    public CardListView(Context context) {

        super(context);
        this.context = context;

        enterShape = getResources().getDrawable(R.drawable.bg_over);
        normalShape = getResources().getDrawable(R.drawable.bg);
        mainLayout = new RelativeLayout(context);
        onClickDoNotWork = false;

        mainLayout.setId(R.id.rlInsideSv);
        this.addView(mainLayout);

    }

    public CardListView(Context context, AttributeSet attrs) {

        super(context, attrs);
        this.context = context;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CardListView, 0, 0);

        try {

            distanceBetweenCards =
                    (int) ta.getDimension(R.styleable.CardListView_distanceBetweenCards, 80.0f);
            marginBetweenCards =
                    (int) ta.getDimension(R.styleable.CardListView_marginBetweenCards, 50.0f);
            closeOnOpenOtherCard =
                    ta.getBoolean(R.styleable.CardListView_closeOnOpenOtherCard, false);

        } finally {
            ta.recycle();
        }

        enterShape = getResources().getDrawable(R.drawable.bg_over);
        normalShape = getResources().getDrawable(R.drawable.bg);
        mainLayout = new RelativeLayout(context, attrs);
        onClickDoNotWork = false;
        mainLayout.setId(R.id.rlInsideSv);

        this.addView(mainLayout);

    }

    public void setAdapter(T baseAdapter) {

        this.baseAdapter = baseAdapter;

        inflateViews();

    }

    public void setReorderList(OnReorderList reorderList) {
        this.reorderList = reorderList;
    }

    public void setSideDraggable(SideDraggable draggable) {
        this.draggable = draggable;
    }

    public void setActionOnClick(ActionOnClick actionOnClick) {
        this.actionOnClick = actionOnClick;
    }

    private void reorderList(int oldPos, int newPos) {

        mainLayout.removeAllViews();
        baseAdapter.reorderList(oldPos, newPos);

        if (reorderList != null)
            reorderList.onReorder();

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

            params.topMargin = i * distanceBetweenCards;

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

    @SuppressWarnings("unchecked")
    @Override
    public void onClick(View v) {

        if (onClickDoNotWork)
            return;

        String tags[] = ((String) ((View) v.getParent()).getTag()).split("_");
        int viewIndex = Integer.parseInt(tags[1]);
        int auxIndex = viewIndex;
        final String status = tags[2];

        if (closeOnOpenOtherCard)
            inflateViews();

        while (auxIndex < baseAdapter.getCount()) {

            View parentBelow = this.findViewWithTag("box_" + (auxIndex + 1) + "_open") != null
                    ? this.findViewWithTag("box_" + (auxIndex + 1) + "_open")
                    : this.findViewWithTag("box_" + (auxIndex + 1) + "_close");

            if (parentBelow != null) {

                int height = marginBetweenCards;
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) parentBelow.getLayoutParams();

                switch (status) {

                    case "close":
                        params.topMargin = params.topMargin + height;
                        break;
                    case "open":
                        if (!closeOnOpenOtherCard)
                            params.topMargin = params.topMargin - height;
                        break;

                }

                parentBelow.setLayoutParams(params);

            }

            auxIndex++;

        }

        switch (status) {

            case "close":

                View vAuxClose = findViewWithTag(String.format("%s_%s_close", tags[0], tags[1]));

                if (vAuxClose != null)
                    vAuxClose.setTag(String.format("%s_%s_open", tags[0], tags[1]));

                if (actionOnClick != null)
                    actionOnClick.onOpen(baseAdapter.getItem(viewIndex));

                break;

            case "open":

                if (viewIndex != baseAdapter.getCount() - 1) {

                    View vAuxOpen = findViewWithTag(String.format("%s_%s_open", tags[0], tags[1]));

                    if (vAuxOpen != null)
                        vAuxOpen.setTag(String.format("%s_%s_close", tags[0], tags[1]));

                }

                if (actionOnClick != null)
                    actionOnClick.onClose(baseAdapter.getItem(viewIndex));

                break;

        }

    }

    @Override
    public boolean onDrag(View v, DragEvent event) {

        onClickDoNotWork = false;

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

        onClickDoNotWork = false;

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

                if (v.getX() >= 5.0f || v.getX() <= -5.0f)
                    onClickDoNotWork = true;

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

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onClickDoNotWork = false;
                    }
                }, 500);

                return false;

            } else if (action == MotionEvent.ACTION_CANCEL) {

                v.setX(lastXTouch);
                v.setY(lastYTouch);
                isDraggingInTouch = false;

                v.setX(0);
                v.setY(0);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onClickDoNotWork = false;
                    }
                }, 500);

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

    public interface OnReorderList {
        void onReorder();
    }

    static class SavedState extends BaseSavedState {

        int rlViewId;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.rlViewId = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {

            super.writeToParcel(out, flags);

            out.writeInt(this.rlViewId);

        }

        //required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

}