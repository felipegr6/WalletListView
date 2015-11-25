package br.com.fgr.cartoescomlistadinamica.model;

import br.com.fgr.cartoescomlistadinamica.R;

public class CartaoAlimentacao implements TipoCartao {

    @Override
    public int getTipoCartao() {
        return R.drawable.cartao_ta_linhas;
    }

}