package br.com.fgr.cartoescomlistadinamica.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.fgr.cartoescomlistadinamica.R;
import br.com.fgr.cartoescomlistadinamica.model.AbstractCard;
import br.com.fgr.cartoescomlistadinamica.ui.adapters.AbstractCardAdapter;
import br.com.fgr.cartoescomlistadinamica.ui.adapters.ImplCardAdapter;
import br.com.fgr.cartoescomlistadinamica.ui.custom_views.CardListView;
import butterknife.Bind;
import butterknife.ButterKnife;

public class Main4Activity extends AppCompatActivity {

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
                return R.drawable.cartao_ta_linhas;
            }

        });

        abstractCards.add(new AbstractCard() {

            @Override
            public int getBackground() {
                return R.drawable.cartao_tcar_linhas;
            }

        });

        abstractCards.add(new AbstractCard() {

            @Override
            public int getBackground() {
                return R.drawable.cartao_ta_linhas;
            }

        });

        abstractCards.add(new AbstractCard() {

            @Override
            public int getBackground() {
                return R.drawable.cartao_tcar_linhas;
            }

        });

        abstractCards.add(new AbstractCard() {

            @Override
            public int getBackground() {
                return R.drawable.cartao_ta_linhas;
            }

        });

        abstractCards.add(new AbstractCard() {

            @Override
            public int getBackground() {
                return R.drawable.cartao_tcar_linhas;
            }

        });

        abstractCards.add(new AbstractCard() {

            @Override
            public int getBackground() {
                return R.drawable.cartao_ta_linhas;
            }

        });

        abstractCards.add(new AbstractCard() {

            @Override
            public int getBackground() {
                return R.drawable.cartao_tcar_linhas;
            }

        });

        abstractCards.add(new AbstractCard() {

            @Override
            public int getBackground() {
                return R.drawable.cartao_ta_linhas;
            }

        });

        abstractCards.add(new AbstractCard() {

            @Override
            public int getBackground() {
                return R.drawable.cartao_tcar_linhas;
            }

        });

        abstractCards.add(new AbstractCard() {

            @Override
            public int getBackground() {
                return R.drawable.cartao_ta_linhas;
            }

        });

        abstractCards.add(new AbstractCard() {

            @Override
            public int getBackground() {
                return R.drawable.cartao_tcar_linhas;
            }

        });

        AbstractCardAdapter cardAdapter = new ImplCardAdapter(this, R.layout.layout_example,
                abstractCards);

        cardListView.setAdapter(cardAdapter);

        cardListView.setSideDraggable(new CardListView.SideDraggable() {

            @Override
            public void draggableToLeft(AbstractCard card) {
                Log.d("dragLeft", String.valueOf(card.getBackground()));
            }

            @Override
            public void draggableToRight(AbstractCard card) {
                Log.d("dragRight", String.valueOf(card.getBackground()));
            }

        });

        cardListView.setActionOnClick(new CardListView.ActionOnClick() {

            @Override
            public void onOpen(AbstractCard card) {

            }

            @Override
            public void onClose(AbstractCard card) {

            }

        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        ButterKnife.bind(this);

    }

}