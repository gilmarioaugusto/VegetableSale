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

public class Vendas extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth user;
    private ArrayList<ProdutoVendido> produtosVendidos;
    private ArrayAdapter vendasAdaptador;
    private ListView listViewVendas;
    private TextView msgVendasVazias;
    private CollectionReference vendasRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendas);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance();
        produtosVendidos = new ArrayList<>();

        listViewVendas = findViewById(R.id.lista_vendas);
        msgVendasVazias = findViewById(R.id.msg_vendas_vazia);
        vendasRef = db.collection("transacoes").document(user.getCurrentUser().getEmail()).collection("vendas");
        vendasRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    ProdutoVendido produtoVendido = new ProdutoVendido();
                    produtoVendido = document.toObject(ProdutoVendido.class);
                    produtosVendidos.add(produtoVendido);
                }
                setListViewVendas(produtosVendidos);
                if (produtosVendidos.isEmpty()){
                    msgVendasVazias.setVisibility(View.VISIBLE);
                }
            }
        });

        listViewVendas.setClickable(true);
        listViewVendas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DetalheTransacao.class);
                intent.putExtra("produto", produtosVendidos.get(position));
                startActivity(intent);
            }
        });
    }

    public void setListViewVendas(ArrayList lista) {
        vendasAdaptador = new ArrayAdapter<ReservaProduto>(this, android.R.layout.simple_expandable_list_item_2, android.R.id.text2, lista);
        listViewVendas.setAdapter(vendasAdaptador);
    }
}
