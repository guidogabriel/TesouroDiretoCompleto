package br.com.guidogabriel.tesourodiretocompleto;

import com.orm.SugarRecord;

/**
 * Created by guido_000 on 01/05/2015.
 */
public class TituloTesouro extends SugarRecord<TituloTesouro> {


    private Long idRemoto;
    private String nome;
    private String precoCompra;
    private String taxaCompra;
    private String dataUltimaAtualizacao;
    private String precoVenda;
    private String taxaVenda;

    //region Contrutores
    public TituloTesouro() {
    }

    public TituloTesouro(long idRemoto, String nome, String precoCompra, String taxaCompra) {
        this.idRemoto = idRemoto;
        this.nome = nome;
        this.precoCompra = precoCompra;
        this.taxaCompra = taxaCompra;
    }
    //endregion

    @Override
    public String toString() {
        return nome;
    }

    //region Getters and Setters
    public String getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(String precoVenda) {
        this.precoVenda = precoVenda;
    }

    public String getTaxaVenda() {
        return taxaVenda;
    }

    public void setTaxaVenda(String taxaVenda) {
        this.taxaVenda = taxaVenda;
    }

    public Long getIdRemoto() {
        return idRemoto;
    }

    public void setIdRemoto(Long idRemoto) {
        this.idRemoto = idRemoto;
    }

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

    public String getDataUltimaAtualizacao() {
        return dataUltimaAtualizacao;
    }

    public void setDataUltimaAtualizacao(String dataUltimaAtualizacao) {
        this.dataUltimaAtualizacao = dataUltimaAtualizacao;
    }
    //endregion
}
