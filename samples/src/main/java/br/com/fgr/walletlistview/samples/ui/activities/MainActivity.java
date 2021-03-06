package br.com.fgr.walletlistview.samples.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.fgr.walletlistview.model.AbstractCard;
import br.com.fgr.walletlistview.samples.R;
import br.com.fgr.walletlistview.samples.ui.adapters.ImplCardAdapter;
import br.com.fgr.walletlistview.ui.adapters.AbstractCardAdapter;
import br.com.fgr.walletlistview.ui.custom_views.CardListView;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.card_list)
    CardListView cardListView;

    @SuppressWarnings("unchecked")
    @Override
    protected void onResume() {

        super.onResume();

        List<AbstractCard> abstractCards = new ArrayList<>();

        abstractCards.add(new AbstractCard() {

            @Override
            public int getBackground() {
                return R.drawable.credit_card1;
            }

        });

        abstractCards.add(new AbstractCard() {

            @Override
            public int getBackground() {
                return R.drawable.credit_card2;
            }

        });

        abstractCards.add(new AbstractCard() {

            @Override
            public int getBackground() {
                return R.drawable.credit_card3;
            }

        });

        AbstractCardAdapter cardAdapter = new ImplCardAdapter(this, abstractCards,
                R.layout.layout_example);

        cardListView.setAdapter(cardAdapter);

        cardListView.setSideDraggable(new CardListView.SideDraggable<AbstractCard>() {

            @Override
            public void draggableToLeft(AbstractCard card) {
                Log.d("dragLeft", String.valueOf(card.getBackground()));
            }

            @Override
            public void draggableToRight(AbstractCard card) {
                Log.d("dragRight", String.valueOf(card.getBackground()));
            }

        });

        cardListView.setActionOnClick(new CardListView.ActionOnClick<AbstractCard>() {

            @Override
            public void onOpen(AbstractCard card) {

                Log.e("onOpen", "onOpen");

                startActivity(new Intent(MainActivity.this, Main2Activity.class));

            }

            @Override
            public void onClose(AbstractCard card) {
                Log.e("onClose", "onClose");
            }

        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

    }

}