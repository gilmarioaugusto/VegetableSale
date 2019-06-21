package e.tux.ifruit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import cn.carbs.android.library.MDDialog;
import e.tux.ifruit.pagamento.CartaoDeCredito;
import e.tux.ifruit.pagamento.ConexaoPagamento;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Carrinho extends AppCompatActivity implements Observer {

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
    private TextView txtTotalCarrinho;
    private DecimalFormat decimalFormat;

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
        txtTotalCarrinho = findViewById(R.id.txt_total_carrinho);
        totalCarrinho = 0.0;

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
                    totalCarrinho = totalCarrinho + reservaProduto.getPrecoDaCompra()*reservaProduto.getQuantidadeComprada();
                }
                txtTotalCarrinho.setText("R$"+decimalFormat.format(totalCarrinho));
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



        /*
        finalizarCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCartao()) {
                    sincronizarBanco();
                    Toast.makeText(getApplicationContext(), "Compra efetuada com sucesso!", 1).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });*/
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

    //MÉTODO RESPONSÁVEL POR VALIDAR O RETORNO DA WEBVIEW DO CARTÃO
    private boolean validarCartao() {
        return true;
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


    public void comprar( View view ){
        new MDDialog.Builder(this)
                .setTitle("Dados do Pagamento")
                .setContentView(R.layout.dados_pagamento)
                .setNegativeButton("Cancelar", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(),"Cancelar!", Toast.LENGTH_LONG).show();
                    }
                })
                .setPositiveButton("Finalizar Compra", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View root = v.getRootView();


                        //botaoComprar(true);

                        CartaoDeCredito cartao = new CartaoDeCredito( Carrinho.this );
                        cartao.setNumCartao( pegarConteudoCampo( root, R.id.numcartao ) );
                        cartao.setNomeCartao( pegarConteudoCampo( root, R.id.nomeCartao ) );
                        cartao.setMes( pegarConteudoCampo( root, R.id.mesCartao ) );
                        cartao.setAno( pegarConteudoCampo( root, R.id.anoCartao ) );
                        cartao.setCvv( pegarConteudoCampo( root, R.id.cvv ) );
                        cartao.setParcels(Integer.parseInt( pegarConteudoCampo(root, R.id.parcelas) ));

                        pegarTokenPagamento( cartao );

                        Toast.makeText(Carrinho.this,
                                "Pagamento Aprovado. Em breve o Produto Estará em Suas Mãos.", Toast.LENGTH_LONG).show();
                        sincronizarBanco();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);

                    }
                })
                .create()
                .show();
    }

    private String pegarConteudoCampo( View root, int id ){
        EditText field = (EditText) root.findViewById(id);
        return field.getText().toString();
    }


    private void pegarTokenPagamento(CartaoDeCredito cartaoDeCredito){
        WebView webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled( true );
        webView.addJavascriptInterface(cartaoDeCredito, "Android");
        webView.loadUrl("file:///android_asset/index.html");
    }


    private void showMessage(final String messagem){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Carrinho.this, messagem, Toast.LENGTH_LONG).show();
            }
        });

    }


    @Override
    public void update(Observable o, Object arg) {
        CartaoDeCredito cartao = (CartaoDeCredito) o;

        if (cartao.getToken() ==  null){
            //botaoComprar(false);
            showMessage(cartao.getError());

            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vegetablepi.000webhostapp.com/?android-pagamento/")
                //.baseUrl("https://vegetablepi.000webhostapp.com/android-pagamento/")
                //.baseUrl("http://localhost/android-pagamento/")//esta no localhost. precisa passar pro servidor
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Produto produto = new Produto();

        ConexaoPagamento conexaoPagamento = retrofit.create(ConexaoPagamento.class);
        Call<String> requester = conexaoPagamento.enviarPagamento(
                produto.getPrecoIndividual(),//pega o preço total do carrinho
                cartao.getToken(),
                cartao.getParcels()
        );

        requester.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //botaoComprar(false);
                showMessage(response.body());

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //botaoComprar(false);
                Log.e("log_falha", "Error: " + t.getMessage());
            }
        });
    }

    /*
    private void botaoComprar(final Boolean estado){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String label;

                label = getResources().getString(R.string.btnComprar);
                if (estado){
                    label = getResources().getString(R.string.btnComprando);

                }else if(estado == false){
                    Toast.makeText(Carrinho.this,
                            "Pagamento Aprovado. Em breve o Produto Estará em Suas Mãos.", Toast.LENGTH_LONG).show();

                }

                ((Button) findViewById(R.id.btnComprar)).setText(label);

            }
        });
    }
    */

}

