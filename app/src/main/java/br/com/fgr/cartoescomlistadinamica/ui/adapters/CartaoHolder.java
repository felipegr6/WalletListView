package br.com.fgr.cartoescomlistadinamica.ui.adapters;

import android.view.View;
import android.widget.TextView;

import br.com.fgr.cartoescomlistadinamica.R;

public class CartaoHolder {

    private TextView textNome;
    private TextView textNumero;
    private View imgCartao;

    public CartaoHolder(View parentView) {

        this.textNome = (TextView) parentView.findViewById(R.id.nome);
        this.textNumero = (TextView) parentView.findViewById(R.id.numero);
        this.imgCartao = parentView.findViewById(R.id.rl_cartao);

    }

    public TextView getTextNome() {
        return textNome;
    }

    public TextView getTextNumero() {
        return textNumero;
    }

    public View getImgCartao() {
        return imgCartao;
    }

}