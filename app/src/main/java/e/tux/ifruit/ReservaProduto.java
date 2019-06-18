package e.tux.ifruit;

import java.io.Serializable;
import java.text.DecimalFormat;

public class ReservaProduto extends Produto implements Serializable {

    private double precoDaCompra;
    private double quantidadeComprada;
    private String comprador;
    private DecimalFormat decimalFormat;

    public ReservaProduto(){

    }

    public ReservaProduto(String nome, double precoIndividual, String proprietario, double quantidadeDisponivel, boolean porUnidade, double precoDaCompra, double quantidadeComprada, String comprador) {
        super(nome, precoIndividual, proprietario, quantidadeDisponivel, porUnidade);
        this.precoDaCompra = precoDaCompra;
        this.quantidadeComprada = quantidadeComprada;
        this.comprador = comprador;
    }

    @Override
    public String toString() {
        decimalFormat = new DecimalFormat("0.00");
        String msg = this.getNome()+" (R$ "+decimalFormat.format(this.getPrecoIndividual())+") x "+this.getQuantidadeComprada()
                +" = R$"+decimalFormat.format(this.precoDaCompra);
        return msg;
    }

    public double getPrecoDaCompra() {
        return precoDaCompra;
    }

    public void setPrecoDaCompra(double precoDaCompra) {
        this.precoDaCompra = precoDaCompra;
    }

    public double getQuantidadeComprada() {
        return quantidadeComprada;
    }

    public void setQuantidadeComprada(double quantidadeComprada) {
        this.quantidadeComprada = quantidadeComprada;
    }

    public String getComprador() {
        return comprador;
    }

    public void setComprador(String comprador) {
        this.comprador = comprador;
    }
}
