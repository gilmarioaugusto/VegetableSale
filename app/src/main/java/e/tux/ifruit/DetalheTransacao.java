package e.tux.ifruit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.common.internal.PendingResultUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class DetalheTransacao extends AppCompatActivity {

    private ProdutoComprado produtoComprado;
    private TextView nomeProduto;
    private TextView nomeComprador;
    private TextView nomeVendedor;
    private TextView qtComprada;
    private TextView precoIndividual;
    private TextView tvTotalCompra;
    private TextView tvPrecoKgUn;
    private TextView idTransacao;
    private double totalCompra;
    //private TextView status;
    private DecimalFormat decimalFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_transacao);

        nomeProduto = findViewById(R.id.nome_produto);
        nomeComprador = findViewById(R.id.nome_comprador);
        nomeVendedor = findViewById(R.id.nome_vendedor);
        qtComprada = findViewById(R.id.quantidade_comprada);
        precoIndividual = findViewById(R.id.preco_individual);
        tvTotalCompra = findViewById(R.id.total_compra);
        tvPrecoKgUn = findViewById(R.id.tv_preco_KG_UN);
        idTransacao = findViewById(R.id.IDTransacao);
        //status = findViewById(R.id.statusPagamento);
        decimalFormat = new DecimalFormat("0.00");

        //Pegando Detalhes da Transação
        Intent intent = getIntent();

        try {
            JSONObject json = new JSONObject(intent.getStringExtra("detalhesPagamento"));
            mostrarDetalhes(json.getJSONObject("response"), intent.getStringExtra("valorPagamento"));
        }catch (JSONException e){
            e.printStackTrace();
        }

        Intent i = getIntent();
        produtoComprado = (ProdutoComprado) i.getSerializableExtra("produto");
        if (produtoComprado != null){
            nomeProduto.setText(produtoComprado.getNome());
            nomeComprador.setText(produtoComprado.getComprador());
            nomeVendedor.setText(produtoComprado.getProprietario());
            if (produtoComprado.getPorUnidade()){
                tvPrecoKgUn.setText("Preço por Un.:");
                qtComprada.setText(String.valueOf(produtoComprado.getQuantidadeComprada())+" Un.");

            } else{
                tvPrecoKgUn.setText("Preço por Kg.:");
                qtComprada.setText(String.valueOf(produtoComprado.getQuantidadeComprada())+" Kg");
            }
            precoIndividual.setText("R$"+String.valueOf(produtoComprado.getPrecoIndividual()));
            totalCompra = produtoComprado.getPrecoIndividual()*produtoComprado.getQuantidadeComprada();
            tvTotalCompra.setText("R$ "+decimalFormat.format(totalCompra));
        }
    }

    private void mostrarDetalhes(JSONObject response, String valorPagamento) {
       try {
           idTransacao.setText(response.getString("id"));
           //totalCompra.setText("R$ " + valorPagamento);
           //status.setText(response.getString("state"));

       }catch (JSONException e){
            e.printStackTrace();
       }

    }


}
