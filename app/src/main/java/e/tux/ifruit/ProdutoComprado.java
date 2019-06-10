package e.tux.ifruit;

import java.io.Serializable;

public class ProdutoComprado extends ReservaProduto implements Serializable {

    private String comprador;

    public ProdutoComprado() {
    }

    @Override
    public String toString() {
        String msg = "Produto: "+this.getNome()+"    Quantidade: "+this.getQuantidadeComprada();
        return msg;
    }

    public ProdutoComprado(String nome, double precoIndividual, String proprietario, int quantidadeDisponivel, boolean porUnidade, double precoDaCompra, double quantidadeComprada, String comprador) {
        super(nome, precoIndividual, proprietario, quantidadeDisponivel, porUnidade, precoDaCompra, quantidadeComprada);
        this.comprador = comprador;
    }

    public String getComprador() {
        return comprador;
    }

    public void setComprador(String comprador) {
        this.comprador = comprador;
    }


}
