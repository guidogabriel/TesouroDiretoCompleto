package br.com.guidogabriel.tesourodiretocompleto;

import com.orm.SugarRecord;

/**
 * Created by guido_000 on 01/05/2015.
 */
public class TituloTesouro extends SugarRecord<TituloTesouro>{
    private String nome;
    private String precoCompra;
    private String taxaCompra;

    //Testando no Github

    public TituloTesouro(String nome, String precoCompra, String taxaCompra) {
        this.nome = nome;
        this.precoCompra = precoCompra;
        this.taxaCompra = taxaCompra;
    }

    public TituloTesouro() {}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPrecoCompra() {
        return precoCompra;
    }

    public void setPrecoCompra(String precoCompra) {
        this.precoCompra = precoCompra;
    }

    public String getTaxaCompra() {
        return taxaCompra;
    }

    public void setTaxaCompra(String taxaCompra) {
        this.taxaCompra = taxaCompra;
    }
}
