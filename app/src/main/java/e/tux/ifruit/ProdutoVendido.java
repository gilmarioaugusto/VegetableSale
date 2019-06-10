package e.tux.ifruit;

import java.io.Serializable;

public class ProdutoVendido extends ProdutoComprado implements Serializable {

    @Override
    public String toString() {
        String msg = "Produto: "+this.getNome()+"   Para: "+this.getComprador();
        return msg;
    }

    public ProdutoVendido(){}

    public ProdutoVendido(String nome, double precoIndividual, String proprietario, int quantidadeDisponivel, boolean porUnidade, double precoDaCompra, double quantidadeComprada, String comprador) {
        super(nome, precoIndividual, proprietario, quantidadeDisponivel, porUnidade, precoDaCompra, quantidadeComprada, comprador);
    }
}
