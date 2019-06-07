package e.tux.ifruit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
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

import java.text.DecimalFormat;
import java.util.ArrayList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        listaReservaProdutos = new ArrayList<>();
        listViewCarrinho = findViewById(R.id.list_view_carrinho);

        finalizarCompra = findViewById(R.id.bt_finalizar_compra);
        finalizarCompra.setVisibility(View.INVISIBLE);
        txtVazio = findViewById(R.id.txt_vazio);
        txtVazio.setVisibility(View.INVISIBLE);

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
                }
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
        });
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
        }

    }

    public void setListViewProdutos(ArrayList lista) {
        carrinhoAdaptador = new ArrayAdapter<ReservaProduto>(this, android.R.layout.simple_expandable_list_item_2, android.R.id.text2, lista);
        listViewCarrinho.setAdapter(carrinhoAdaptador);
    }

}

