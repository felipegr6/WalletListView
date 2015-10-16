package br.com.fgr.cartoescomlistadinamica.model;

import br.com.fgr.cartoescomlistadinamica.R;

public class CartaoRestaurante implements TipoCartao {

    @Override
    public int getTipoCartao() {
        return R.drawable.cartao_tr_linhas;
    }

}