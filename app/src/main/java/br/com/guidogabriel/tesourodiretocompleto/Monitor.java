package br.com.guidogabriel.tesourodiretocompleto;

/**
 * Created by guido_000 on 23/06/2015.
 */
public class Monitor {
    private Long idMonitorRemoto;
    private String nomeTitulo;
    private Long idTituloRemoto;
    private Long idUsuario;
    private String taxaCompraMenorQue;
    private String taxaCompraMaiorQue;

    //region Getters and Setters
    public Long getIdMonitorRemoto() {
        return idMonitorRemoto;
    }

    public void setIdMonitorRemoto(Long idMonitorRemoto) {
        this.idMonitorRemoto = idMonitorRemoto;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNomeTitulo() {
        return nomeTitulo;
    }

    public void setNomeTitulo(String nomeTitulo) {
        this.nomeTitulo = nomeTitulo;
    }

    public Long getIdTituloRemoto() {
        return idTituloRemoto;
    }

    public void setIdTituloRemoto(Long idTituloRemoto) {
        this.idTituloRemoto = idTituloRemoto;
    }

    public String getTaxaCompraMenorQue() {
        return taxaCompraMenorQue;
    }

    public void setTaxaCompraMenorQue(String taxaCompraMenorQue) {
        this.taxaCompraMenorQue = taxaCompraMenorQue;
    }

    public String getTaxaCompraMaiorQue() {
        return taxaCompraMaiorQue;
    }

    public void setTaxaCompraMaiorQue(String taxaCompraMaiorQue) {
        this.taxaCompraMaiorQue = taxaCompraMaiorQue;
    }
    //endregion
}
