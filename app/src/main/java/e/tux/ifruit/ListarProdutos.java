package e.tux.ifruit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ListarProdutos extends AppCompatActivity {

    private ListView listView;
    private Produto rProduto;
    private String nome;
    private FirebaseFirestore db;
    private TextView txTeste;
    private TextView txTeste2;
    private FirebaseAuth mAuth;
    private String user;
    private ArrayAdapter listaAdaptador;
    private ArrayList<Produto> listaProdutos;
    private CollectionReference produtosRef;
    private ArrayList<Produto> listaCompras;
    private ImageButton abrirCarrinho;
    private boolean ativarCarrinho;
    private ReservaProduto reservaProduto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_produtos);

        listaProdutos = new ArrayList<>();
        ativarCarrinho = false;

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser().getEmail();
        listView =findViewById(R.id.listaItens);
        abrirCarrinho = findViewById(R.id.bt_floating_carrinho);

        Intent i = getIntent();
        ativarCarrinho = (boolean) i.getBooleanExtra("ativar",false);
        if (ativarCarrinho){
            abrirCarrinho.setVisibility(View.VISIBLE);
        } else {
            abrirCarrinho.setVisibility(View.INVISIBLE);
        }

        abrirCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Carrinho.class);
                startActivity(i);
            }
        });




        produtosRef = db.collection("produtos");
        produtosRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                        Produto produto = new Produto();
                        produto = document.toObject(Produto.class);
                        if (! produto.getProprietario().equals(user) && produto.getQuantidadeDisponivel()>0) {
                            listaProdutos.add(produto);
                        }
                    }

                setListViewProdutos(listaProdutos);
            }
        });

        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), AdicionarCarrinho.class);
                intent.putExtra("produto", listaProdutos.get(position));
                startActivity(intent);
                //return true;
            }
        });
    }

    public void setListViewProdutos(ArrayList lista){
        listaAdaptador = new ArrayAdapter<Produto>(this, android.R.layout.simple_expandable_list_item_2, android.R.id.text2, lista);
        listView.setAdapter(listaAdaptador);
    }
}
