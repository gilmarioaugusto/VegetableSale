package e.tux.ifruit;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AdicionarCarrinho extends AppCompatActivity {

    private TextView txtNomeProduto;
    private TextView txtKgUn;
    private TextView txtValorProduto;
    private TextView txtQuantidade;
    private TextView txtTotal;
    private double quantidadeKg;
    private double quantidadeUn;
    private double quantidadeComprada;
    private ImageButton somarProduto;
    private ImageButton subtrairProduto;
    private ImageButton addCarrinho;
    private DecimalFormat decimalFormat;
    private ReservaProduto reservaProduto;
    private double totalProduto;
    private FirebaseFirestore mDatabase;
    private FirebaseAuth mAuth;
    private Produto produtoCarregado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_carrinho);

        txtNomeProduto = findViewById(R.id.txt_nome_produto);
        txtKgUn = findViewById(R.id.txt_Kg_ou_Un);
        txtValorProduto = findViewById(R.id.txt_valor_produto);
        txtQuantidade = findViewById(R.id.txt_Quantidade);
        txtTotal = findViewById(R.id.txt_Total);
        somarProduto = findViewById(R.id.bt_adicionar);
        subtrairProduto = findViewById(R.id.bt_remover);
        addCarrinho = findViewById(R.id.bt_adicionar_ao_carrinho);
        decimalFormat = new DecimalFormat("0.00");
        mDatabase = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        produtoCarregado = new Produto();

        Intent i = getIntent();
        Produto produto = (Produto) i.getSerializableExtra("produto");
        if (produto != null){
            produtoCarregado = produto;
            txtQuantidade.setText(String.valueOf(1));
            totalProduto = produto.getPrecoIndividual()*Double.parseDouble(String.valueOf(txtQuantidade.getText()));
            txtTotal.setText("Total: R$"+decimalFormat.format(totalProduto));
            quantidadeKg = 1;
            quantidadeUn = 1;
        }

        Intent ii = getIntent();
        ReservaProduto produtoReservado = (ReservaProduto) ii.getSerializableExtra("produtoReservado");
        if (produtoReservado != null){
            produtoCarregado = new ReservaProduto();
            produtoCarregado = produtoReservado;
            txtQuantidade.setText(String.valueOf(((ReservaProduto) produtoCarregado).getQuantidadeComprada()));
            totalProduto = produtoCarregado.getPrecoIndividual()*Double.parseDouble(String.valueOf(txtQuantidade.getText()));
            txtTotal.setText("Total: R$"+decimalFormat.format(totalProduto));
            quantidadeUn = ((ReservaProduto) produtoCarregado).getQuantidadeComprada();
            quantidadeKg = ((ReservaProduto) produtoCarregado).getQuantidadeComprada();
        }

        escreverDados();



        somarProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                somarProduto();
            }
        });

        subtrairProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subtrairProduto();
            }
        });

        addCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionarCarrinho();
            }
        });

    }



    private void adicionarCarrinho() {
        reservaProduto = new ReservaProduto();
        reservaProduto.setNome(produtoCarregado.getNome());
        reservaProduto.setPorUnidade(produtoCarregado.getPorUnidade());
        reservaProduto.setPrecoIndividual(produtoCarregado.getPrecoIndividual());
        reservaProduto.setQuantidadeDisponivel(produtoCarregado.getQuantidadeDisponivel());
        reservaProduto.setProprietario(produtoCarregado.getProprietario());
        reservaProduto.setComprador(mAuth.getCurrentUser().getEmail());
        quantidadeComprada = Double.parseDouble(String.valueOf(txtQuantidade.getText()));
        totalProduto = produtoCarregado.getPrecoIndividual()*quantidadeComprada;
        reservaProduto.setPrecoDaCompra(totalProduto);
        reservaProduto.setQuantidadeComprada(quantidadeComprada);
        mDatabase.collection("carrinhos").document(reservaProduto.getComprador())
                .collection("produtos").document(reservaProduto.getNome()).set(reservaProduto);
        Toast.makeText(getApplicationContext(), "Produto adicionado ao carrinho!", 1).show();
        Intent intent = new Intent(getApplicationContext(), ListarProdutos.class);
        intent.putExtra("ativar", true);
        startActivity(intent);
    }

    private void escreverDados() {
        txtNomeProduto.setText(produtoCarregado.getNome());
        if (produtoCarregado.getPorUnidade()){
            txtKgUn.setText("Preço por Unidade:");
        } else {
            txtKgUn.setText("Preço por Quilo:");
        }
        txtValorProduto.setText("R$"+decimalFormat.format(produtoCarregado.getPrecoIndividual()));
    }

    private void somarProduto(){
        if (produtoCarregado.getPorUnidade()){
            quantidadeUn = quantidadeUn +1;
            txtQuantidade.setText(String.valueOf(quantidadeUn));
        } else if (!produtoCarregado.getPorUnidade()){
            quantidadeKg = quantidadeKg + 0.1;
            txtQuantidade.setText(String.valueOf(decimalFormat.format(quantidadeKg)));
        }
        totalProduto = produtoCarregado.getPrecoIndividual()*Double.parseDouble(String.valueOf(txtQuantidade.getText()));
        txtTotal.setText("Total: R$"+decimalFormat.format(totalProduto));
    }

    private void subtrairProduto(){
        if (produtoCarregado.getPorUnidade() && quantidadeUn>0){
            quantidadeUn = quantidadeUn - 1;
            txtQuantidade.setText(String.valueOf(quantidadeUn));
        } else if (!produtoCarregado.getPorUnidade() && quantidadeKg>0.1){
            quantidadeKg = quantidadeKg - 0.1;
            txtQuantidade.setText(String.valueOf(decimalFormat.format(quantidadeKg)));
        } else {
            Toast.makeText(getApplicationContext(), "Quantidade não pode ser negativa!!",0).show();
        }
        totalProduto = produtoCarregado.getPrecoIndividual()*Double.parseDouble(String.valueOf(txtQuantidade.getText()));
        txtTotal.setText("Total: R$"+decimalFormat.format(totalProduto));
    }
}
