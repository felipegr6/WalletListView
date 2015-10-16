package br.com.fgr.cartoescomlistadinamica.model;

public class Cartao {

    private String nome;
    private String numero;
    private TipoCartao tipoCartao;

    public Cartao(String nome, String numero, TipoCartao tipoCartao) {

        this.nome = nome;
        this.numero = numero;
        this.tipoCartao = tipoCartao;

    }

    public String getNome() {
        return nome;
    }

    public String getNumero() {
        return numero;
    }

    public TipoCartao getTipoCartao() {
        return tipoCartao;
    }

}