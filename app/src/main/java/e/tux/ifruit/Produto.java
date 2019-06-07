package e.tux.ifruit;

import java.io.Serializable;

public class Produto implements Serializable {

    private String nome;
    private double precoIndividual;
    private double quantidadeDisponivel;
    private String proprietario;
    private Boolean porUnidade;

    public Produto() {

    }

    public Produto(String nome, double precoIndividual, String proprietario, double quantidadeDisponivel, boolean porUnidade) {
        this.nome = nome;
        this.precoIndividual = precoIndividual;
        this.quantidadeDisponivel = quantidadeDisponivel;
        this.proprietario = proprietario;
        this.porUnidade = porUnidade;
    }

    @Override
    public String toString() {
        String msg = this.nome+" x "+this.quantidadeDisponivel;
        return msg;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPrecoIndividual() {
        return precoIndividual;
    }

    public void setPrecoIndividual(double precoIndividual) {
        this.precoIndividual = precoIndividual;
    }

    public double getQuantidadeDisponivel() {
        return quantidadeDisponivel;
    }

    public void setQuantidadeDisponivel(double quantidadeDisponivel) {
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    public String getProprietario() {
        return proprietario;
    }

    public void setProprietario(String proprietario) {
        this.proprietario = proprietario;
    }

    public Boolean getPorUnidade() {
        return porUnidade;
    }

    public void setPorUnidade(Boolean porUnidade) {
        this.porUnidade = porUnidade;
    }
}

