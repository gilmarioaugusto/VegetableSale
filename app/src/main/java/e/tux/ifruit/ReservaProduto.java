package e.tux.ifruit;

import java.io.Serializable;
import java.text.DecimalFormat;

public class ReservaProduto extends Produto implements Serializable {

    private double precoDaCompra;
    private double quantidadeComprada;
    private DecimalFormat decimalFormat;

    public ReservaProduto(){

    }

    public ReservaProduto(String nome, double precoIndividual, String proprietario, int quantidadeDisponivel,
                          boolean porUnidade, double precoDaCompra, double quantidadeComprada) {
        super(nome, precoIndividual, proprietario, quantidadeDisponivel, porUnidade);
        this.precoDaCompra = precoDaCompra;
        this.quantidadeComprada = quantidadeComprada;
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
}
