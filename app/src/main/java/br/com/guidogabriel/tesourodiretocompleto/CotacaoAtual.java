package br.com.guidogabriel.tesourodiretocompleto;

/**
 * Created by guido_000 on 23/05/2015.
 */
public class CotacaoAtual {
    private int _id;
    private String nomeTitulo;
    private String vencimento;
    private String nomeAbreviado;
    private String taxaCompra;
    private String taxaVenda;
    private String precoCompra;
    private String precoVenda;
    private String ultimaAtualizacao;

    @Override
    public String toString() {
        return getNomeTitulo();
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getNomeTitulo() {
        return nomeTitulo;
    }

    public void setNomeTitulo(String nomeTitulo) {
        this.nomeTitulo = nomeTitulo;
    }

    public String getVencimento() {
        return vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }

    public String getNomeAbreviado() {
        return nomeAbreviado;
    }

    public void setNomeAbreviado(String nomeAbreviado) {
        this.nomeAbreviado = nomeAbreviado;
    }

    public String getTaxaCompra() {
        return taxaCompra;
    }

    public void setTaxaCompra(String taxaCompra) {
        this.taxaCompra = taxaCompra;
    }

    public String getTaxaVenda() {
        return taxaVenda;
    }

    public void setTaxaVenda(String taxaVenda) {
        this.taxaVenda = taxaVenda;
    }

    public String getPrecoCompra() {
        return precoCompra;
    }

    public void setPrecoCompra(String precoCompra) {
        this.precoCompra = precoCompra;
    }

    public String getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(String precoVenda) {
        this.precoVenda = precoVenda;
    }

    public String getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    public void setUltimaAtualizacao(String ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
    }
}
