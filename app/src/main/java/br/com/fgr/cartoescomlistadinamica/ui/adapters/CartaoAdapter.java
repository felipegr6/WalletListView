package br.com.fgr.cartoescomlistadinamica.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.nhaarman.listviewanimations.util.Swappable;

import java.util.List;

import br.com.fgr.cartoescomlistadinamica.R;
import br.com.fgr.cartoescomlistadinamica.model.Cartao;

public class CartaoAdapter extends BaseAdapter implements Swappable {

    private Context context;
    private List<Cartao> cartaoList;

    public CartaoAdapter(Context context, List<Cartao> cartaoList) {

        this.context = context;
        this.cartaoList = cartaoList;

    }

    @Override
    public int getCount() {
        return cartaoList != null ? cartaoList.size() : 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public Cartao getItem(int position) {
        return cartaoList != null ? cartaoList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return cartaoList != null ? cartaoList.get(position).hashCode() : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Cartao cartao = cartaoList.get(position);
        CartaoHolder holder;

        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.item_cartao_adapter, null);
            holder = new CartaoHolder(convertView);

            convertView.setTag(holder);

        } else
            holder = (CartaoHolder) convertView.getTag();

        holder.getTextNome().setText(cartao.getNome());
        holder.getTextNumero().setText(cartao.getNumero());
        holder.getImgCartao().setBackgroundResource(cartao.getTipoCartao().getTipoCartao());

        return convertView;

    }

    @Override
    public void swapItems(int positionOne, int positionTwo) {

        Cartao temp = getItem(positionOne);

        set(positionOne, getItem(positionTwo));
        set(positionTwo, temp);

    }

    private void set(int location, Cartao object) {

        cartaoList.remove(object);
        cartaoList.add(location, object);

        notifyDataSetChanged();

    }

    public void remove(int position) {

        cartaoList.remove(position);

        notifyDataSetChanged();

    }

}