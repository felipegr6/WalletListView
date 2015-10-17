package br.com.fgr.cartoescomlistadinamica.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nhaarman.listviewanimations.itemmanipulation.expandablelistitem.ExpandableListItemAdapter;
import com.nhaarman.listviewanimations.util.Swappable;

import java.util.List;

import br.com.fgr.cartoescomlistadinamica.R;
import br.com.fgr.cartoescomlistadinamica.model.Cartao;
import br.com.fgr.cartoescomlistadinamica.model.TipoCartao;

public class ExpandableCartaoAdapter extends ExpandableListItemAdapter implements Swappable {

    private Context context;
    private List<Cartao> items;

    public ExpandableCartaoAdapter(@NonNull Context context, List<Cartao> items) {

        super(context, items);

        this.context = context;
        this.items = items;

    }

    @NonNull
    @Override
    public View getTitleView(int position, View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {

            Cartao cartao = items.get(position);
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, null);

            TextView text1 = (TextView) convertView.findViewById(android.R.id.text1);

            text1.setText(cartao.getNome());

        }

        return convertView;
    }

    @NonNull
    @Override
    public View getContentView(int position, View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {

            Cartao cartao = items.get(position);
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cartao_adapter, null);

            TextView textNome = (TextView) convertView.findViewById(R.id.nome);
            TextView textNumero = (TextView) convertView.findViewById(R.id.numero);
            RelativeLayout imgCartao = (RelativeLayout) convertView.findViewById(R.id.rl_cartao);

            textNome.setText(cartao.getNome());
            textNumero.setText(cartao.getNumero());
            imgCartao.setBackgroundResource(cartao.getTipoCartao().getTipoCartao());

        }

        return convertView;

    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void swapItems(int positionOne, int positionTwo) {

        Cartao temp = getItem(positionOne);

        set(positionOne, getItem(positionTwo));
        set(positionTwo, temp);

    }

    @Override
    public Cartao getItem(int position) {
        return items != null ? items.get(position) : new Cartao("invalido", "invalido", new TipoCartao() {
            @Override
            public int getTipoCartao() {
                return 0;
            }
        });
    }

    private void set(final int location, final Cartao object) {

        items.remove(object);
        items.add(location, object);

        notifyDataSetChanged();

    }

}