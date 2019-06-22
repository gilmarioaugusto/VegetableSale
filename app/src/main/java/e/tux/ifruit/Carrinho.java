package e.tux.ifruit;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Observer;

import e.tux.ifruit.pagamento.Config;

public class Carrinho extends AppCompatActivity {

    private ListView listViewCarrinho;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private CollectionReference carrinhoRef;
    private ArrayList<ReservaProduto> listaReservaProdutos;
    private ArrayAdapter carrinhoAdaptador;
    private ImageButton finalizarCompra;
    private Produto produtoUp;
    private TextView txtVazio;
    private Double totalCarrinho;
    private TextView valorTotalCarrinho;
    private DecimalFormat decimalFormat;


    public static final int PAYPAL_REQUEST_CODE = 7171;

    public static PayPalConfiguration configuracao = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);

    String valorPagamento;

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        listaReservaProdutos = new ArrayList<>();
        listViewCarrinho = findViewById(R.id.list_view_carrinho);

        decimalFormat = new DecimalFormat("0.00");

        finalizarCompra = findViewById(R.id.bt_finalizar_compra);
        finalizarCompra.setVisibility(View.INVISIBLE);
        txtVazio = findViewById(R.id.txt_vazio);
        txtVazio.setVisibility(View.INVISIBLE);
        valorTotalCarrinho = findViewById(R.id.txt_total_carrinho);
        String valorCarregado = String.valueOf(valorTotalCarrinho.getText().toString());
        valorCarregado = valorCarregado.replaceAll(",",".");
        totalCarrinho = 0.00;

        produtoUp = new Produto();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        carrinhoRef = db.collection("carrinhos").document(user.getEmail()).collection("produtos");

        carrinhoRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    ReservaProduto reservaProduto = new ReservaProduto();
                    reservaProduto = document.toObject(ReservaProduto.class);
                    listaReservaProdutos.add(reservaProduto);
                    totalCarrinho = totalCarrinho + reservaProduto.getPrecoDaCompra();
                }
                valorTotalCarrinho.setText(decimalFormat.format(totalCarrinho));
                setListViewProdutos(listaReservaProdutos);
                if (!listaReservaProdutos.isEmpty()){
                    finalizarCompra.setVisibility(View.VISIBLE);
                } else{
                    txtVazio.setVisibility(View.VISIBLE);
                }
            }
        });

        listViewCarrinho.setClickable(true);
        listViewCarrinho.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), AdicionarCarrinho.class);
                intent.putExtra("produtoReservado", listaReservaProdutos.get(position));
                startActivity(intent);
            }
        });


        //Ligando Serviço Paypal
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuracao);
        startService(intent);

        finalizarCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processarPagamento(v);

            }

        });
    }

    public void processarPagamento(View view) {
        valorPagamento = totalCarrinho.toString();

        PayPalPayment pagamentoPayPal =
                new PayPalPayment(new BigDecimal(String.valueOf(valorPagamento)), "BRL",
                        "Pagando para vegetable Sale", PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuracao);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, pagamentoPayPal);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PAYPAL_REQUEST_CODE){
                if (resultCode == RESULT_OK){
                    PaymentConfirmation confirmPagamento =
                            data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                    if (confirmPagamento != null){
                            try {
                                String detalhesPagamento = confirmPagamento.toJSONObject().toString(4);

                                startActivity(new Intent(this, DetalheTransacao.class)
                                    .putExtra("detalhesPagamento", detalhesPagamento)
                                        .putExtra("valorPagamento", valorPagamento)
                                    );

                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                    }
                }else if (requestCode == Activity.RESULT_CANCELED){
                    Toast.makeText(this, "Compra Cancelada!", Toast.LENGTH_SHORT).show();
                }
        }else if (resultCode ==  PaymentActivity.RESULT_EXTRAS_INVALID){
            Toast.makeText(this, "Compra Inválida!", Toast.LENGTH_SHORT).show();
        }
    }

    private void adicionarCompra(ReservaProduto produto) {
        ProdutoComprado produtoComprado = new ProdutoComprado();
        produtoComprado.setComprador(user.getEmail());
        produtoComprado.setPrecoDaCompra(produto.getPrecoDaCompra());
        produtoComprado.setQuantidadeComprada(produto.getQuantidadeComprada());
        produtoComprado.setNome(produto.getNome());
        produtoComprado.setPorUnidade(produto.getPorUnidade());
        produtoComprado.setPrecoIndividual(produto.getPrecoIndividual());
        produtoComprado.setProprietario(produto.getProprietario());
        String dataCompra = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        produtoComprado.setDataDaCompra(dataCompra);
        db.collection("transacoes").document(produtoComprado.getComprador()).collection("compras").document(dataCompra).set(produtoComprado);
        db.collection("transacoes").document(produtoComprado.getProprietario()).collection("vendas").add(produtoComprado);
    }

    private void sincronizarBanco() {

        for (ReservaProduto produto : listaReservaProdutos) {
            double novaQuantidade = produto.getQuantidadeDisponivel() - produto.getQuantidadeComprada();
            db.collection("produtos").document(String.valueOf(produto.getNome())).update("quantidadeDisponivel", novaQuantidade);
            carrinhoRef.document(String.valueOf(produto.getNome())).delete();
            adicionarCompra(produto);
        }

    }

    public void setListViewProdutos(ArrayList lista) {
        carrinhoAdaptador = new ArrayAdapter<ReservaProduto>(this, android.R.layout.simple_expandable_list_item_2, android.R.id.text2, lista);
        listViewCarrinho.setAdapter(carrinhoAdaptador);
    }


}

