package br.com.fgr.walletlistview.samples.ui.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import br.com.fgr.walletlistview.samples.R;
import br.com.fgr.walletlistview.model.AbstractCard;
import br.com.fgr.walletlistview.ui.adapters.AbstractCardAdapter;

public class ImplCardAdapter extends AbstractCardAdapter<AbstractCard> {

    private List<AbstractCard> abstractCards;

    public ImplCardAdapter(Context context, List<AbstractCard> list, @NonNull @LayoutRes Integer... resId) {

        super(context, list, resId);
        this.abstractCards = list;

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
    public int getItemViewType(int position) {
        return 0;
    }

}