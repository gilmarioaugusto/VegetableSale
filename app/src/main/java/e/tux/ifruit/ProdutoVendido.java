package e.tux.ifruit;

import com.google.type.Date;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProdutoVendido extends ProdutoComprado implements Serializable {

    @Override
    public String toString() {
        String msg = "Produto: "+this.getNome()+"   Para: "+this.getComprador();
        return msg;
    }

    public ProdutoVendido(){}

    public ProdutoVendido(String nome, double precoIndividual, String proprietario, double quantidadeDisponivel, boolean porUnidade, double precoDaCompra, double quantidadeComprada, String comprador, String comprador1, String dataDaCompra) {
        super(nome, precoIndividual, proprietario, quantidadeDisponivel, porUnidade, precoDaCompra, quantidadeComprada, comprador, comprador1, dataDaCompra);
    }
}
