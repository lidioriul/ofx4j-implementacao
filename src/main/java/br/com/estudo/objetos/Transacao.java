package br.com.estudo.objetos;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class Transacao {

    private String idTransacao;
    private String nomeTipoTransacao;
    private Date dataTransacao;
    private BigDecimal valor;
    private String descricao;

    public String getIdTransacao() {
        return idTransacao;
    }

    public void setIdTransacao(String idTransacao) {
        this.idTransacao = idTransacao;
    }

    public String getNomeTipoTransacao() {
        return nomeTipoTransacao;
    }

    public void setNomeTipoTransacao(String nomeTipoTransacao) {
        this.nomeTipoTransacao = nomeTipoTransacao;
    }

    public Date getDataTransacao() {
        return dataTransacao;
    }

    public void setDataTransacao(Date dataTransacao) {
        this.dataTransacao = dataTransacao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
