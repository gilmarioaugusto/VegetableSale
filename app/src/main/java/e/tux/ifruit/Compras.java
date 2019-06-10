package e.tux.ifruit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Compras extends AppCompatActivity {

    private ListView listViewCompras;
    private FirebaseFirestore db;
    private FirebaseAuth user;
    private CollectionReference comprasRef;
    private ArrayList<ProdutoComprado> produtosComprados;
    private ArrayAdapter comprasAdaptador;
    private TextView msgComprasVazia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compras);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance();
        produtosComprados = new ArrayList<>();

        listViewCompras = findViewById(R.id.lista_compras);
        msgComprasVazia = findViewById(R.id.msg_compras_vazia);
        comprasRef = db.collection("transacoes").document(user.getCurrentUser().getEmail()).collection("compras");
        comprasRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    ProdutoComprado produtoComprado = new ProdutoComprado();
                    produtoComprado = document.toObject(ProdutoComprado.class);
                    produtosComprados.add(produtoComprado);
                }
                setListViewCompras(produtosComprados);
                if (produtosComprados.isEmpty()){
                    msgComprasVazia.setVisibility(View.VISIBLE);
                }
            }
        });

        listViewCompras.setClickable(true);
        listViewCompras.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DetalheTransacao.class);
                intent.putExtra("produto", produtosComprados.get(position));
                startActivity(intent);
            }
        });
    }

    public void setListViewCompras(ArrayList lista) {
        comprasAdaptador = new ArrayAdapter<ReservaProduto>(this, android.R.layout.simple_expandable_list_item_2, android.R.id.text2, lista);
        listViewCompras.setAdapter(comprasAdaptador);
    }
}
