package br.com.fgr.cartoescomlistadinamica.ui.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import br.com.fgr.cartoescomlistadinamica.R;
import br.com.fgr.cartoescomlistadinamica.model.AbstractCard;

public class ImplCardAdapter extends AbstractCardAdapter {

    private List<AbstractCard> abstractCards;

    public ImplCardAdapter(Context context, @LayoutRes int resId, @NonNull List<AbstractCard> abstractCards) {
        super(context, resId);
        this.abstractCards = abstractCards;
    }

    @Override
    public int getCount() {
        return abstractCards.size();
    }

    @Override
    public AbstractCard getItem(int position) {
        return abstractCards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return abstractCards.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = super.getView(position, convertView, parent);

        ImageView imageView = (ImageView) v.findViewById(R.id.image);
        AbstractCard abstractCard = abstractCards.get(position);

        imageView.setBackgroundResource(abstractCard.getBackground());

        return v;

    }

    @Override
    public boolean reorderList(int index1, int index2) {

        if (index1 < abstractCards.size() && index2 < abstractCards.size()) {

            AbstractCard abstractCard = abstractCards.remove(index1);

            abstractCards.add(index2, abstractCard);

            return true;

        }

        return false;

    }

}