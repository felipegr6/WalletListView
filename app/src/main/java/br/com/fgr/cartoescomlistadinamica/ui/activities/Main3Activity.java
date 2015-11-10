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
        View.OnDragListener, View.OnLongClickListener {

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
    float originalX;
    boolean jaPegou = false;

    boolean isDraggingInOnLongClick = false;
    float lastXTouchOnLongClick;
    float lastYTouchOnLongClick;
    float deltaYTouchOnLongClick;

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

                if (!jaPegou) {
                    originalX = cartao1.getX();
                    jaPegou = true;
                }

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
                owner.removeView(view);
                RelativeLayout container = (RelativeLayout) v;
                container.addView(view);
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
    public boolean onTouch(View v, MotionEvent event) {

//        int action = event.getAction();

        /*switch (action) {

            case MotionEvent.ACTION_DOWN: {

                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);

                v.startDrag(data, shadowBuilder, v, 0);
                v.setVisibility(View.INVISIBLE);

                return true;

            }

            case MotionEvent.ACTION_MOVE: {

                v.setX(event.getX());
                Log.w("x", event.getX() + "");
                Log.w("y", event.getY() + "");
                return true;

            }

            default:
                return false;

        }*/

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

                v.setX(originalX);

                return false;

            } else if (action == MotionEvent.ACTION_CANCEL) {

                v.setX(lastXTouch);
                v.setY(lastYTouch);
                isDraggingInTouch = false;

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

}