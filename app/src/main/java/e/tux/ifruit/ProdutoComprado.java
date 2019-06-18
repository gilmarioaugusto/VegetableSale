package e.tux.ifruit;

import com.google.type.Date;

import java.io.Serializable;
import java.time.LocalDate;

public class ProdutoComprado extends ReservaProduto implements Serializable {

    private String comprador;
    private String dataDaCompra;

    public ProdutoComprado() {
    }

    @Override
    public String toString() {
        String msg = "Produto: "+this.getNome()+"    Quantidade: "+this.getQuantidadeComprada();
        return msg;
    }

    public ProdutoComprado(String nome, double precoIndividual, String proprietario, double quantidadeDisponivel, boolean porUnidade, double precoDaCompra, double quantidadeComprada, String comprador, String comprador1, String dataDaCompra) {
        super(nome, precoIndividual, proprietario, quantidadeDisponivel, porUnidade, precoDaCompra, quantidadeComprada, comprador);
        this.comprador = comprador1;
        this.dataDaCompra = dataDaCompra;
    }

    @Override
    public String getComprador() {
        return comprador;
    }

    @Override
    public void setComprador(String comprador) {
        this.comprador = comprador;
    }

    public String getDataDaCompra() {
        return dataDaCompra;
    }

    public void setDataDaCompra(String dataDaCompra) {
        this.dataDaCompra = dataDaCompra;
    }
}
