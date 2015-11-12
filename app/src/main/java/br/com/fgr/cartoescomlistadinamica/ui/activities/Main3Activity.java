package br.com.fgr.cartoescomlistadinamica.ui.activities;

import android.content.ClipData;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import br.com.fgr.cartoescomlistadinamica.R;
import butterknife.Bind;
import butterknife.ButterKnife;

public class Main3Activity extends AppCompatActivity implements View.OnTouchListener,
        View.OnDragListener, View.OnLongClickListener, View.OnClickListener {

    private final int TAM_LIST = 3;

    private int height;
    private Drawable enterShape;
    private Drawable normalShape;

    @Bind(R.id.rl_t)
    RelativeLayout rl;
    @Bind(R.id.rl1)
    RelativeLayout pos1;
    @Bind(R.id.rl2)
    RelativeLayout pos2;
    @Bind(R.id.rl3)
    RelativeLayout pos3;
    @Bind(R.id.img1)
    ImageView cartao1;
    @Bind(R.id.img2)
    ImageView cartao2;
    @Bind(R.id.img3)
    ImageView cartao3;

    boolean isDraggingInTouch = false;
    float lastXTouch;
    float lastYTouch;
    float deltaXTouch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        ButterKnife.bind(this);
        pos1.setOnDragListener(this);
        pos2.setOnDragListener(this);
        pos3.setOnDragListener(this);

        cartao1.setOnTouchListener(this);
        cartao2.setOnTouchListener(this);
        cartao3.setOnTouchListener(this);

        cartao1.setOnLongClickListener(this);
        cartao2.setOnLongClickListener(this);
        cartao3.setOnLongClickListener(this);

        cartao1.setOnClickListener(this);
        cartao2.setOnClickListener(this);
        cartao3.setOnClickListener(this);

        enterShape = getResources().getDrawable(R.drawable.bg_over);
        normalShape = getResources().getDrawable(R.drawable.bg);

    }

    @Override
    protected void onResume() {

        super.onResume();

        ViewTreeObserver observer = pos1.getViewTreeObserver();

        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {

                height = pos1.getMeasuredHeight();

                pos1.getLayoutParams().height = height;
                pos2.getLayoutParams().height = height;
                pos3.getLayoutParams().height = height;

            }

        });

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
                String tagsChild[] = ((String) view.getTag()).split("_");
                owner.removeView(view);
                RelativeLayout container = (RelativeLayout) v;
                container.addView(view);
                String tagsOwner[] = ((String) container.getTag()).split("_");
                view.setVisibility(View.VISIBLE);
                view.setTag(tagsChild[0] + "_" + tagsOwner[1] + "_" + tagsChild[2]);
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

    @Override
    public boolean onLongClick(View v) {

        ClipData data = ClipData.newPlainText("", "");
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);

        v.startDrag(data, shadowBuilder, v, 0);
        v.setVisibility(View.INVISIBLE);

        return true;

    }

    @Override
    public void onClick(View v) {

        ViewGroup root = (ViewGroup) findViewById(R.id.rl_t);
        String tags[] = ((String) v.getTag()).split("_");
        int viewIndex = Integer.parseInt(tags[1]);
        String status = tags[2];
        View parentBelow = root.findViewWithTag("box_" + (viewIndex + 1));

        while (viewIndex <= 3) {

            if (parentBelow != null) {

                int height = cartao1.getHeight();
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) parentBelow.getLayoutParams();

                if (viewIndex != TAM_LIST) {

                    if (status.equals("close"))
                        params.topMargin = params.topMargin + height;
                    else
                        params.topMargin = params.topMargin - height;

                }

                parentBelow.setLayoutParams(params);

            }

            viewIndex++;
            parentBelow = root.findViewWithTag("box_" + (viewIndex + 1));

        }

        if (status.equals("close"))
            v.setTag(tags[0] + "_" + tags[1] + "_open");
        else
            v.setTag(tags[0] + "_" + tags[1] + "_close");

    }

}