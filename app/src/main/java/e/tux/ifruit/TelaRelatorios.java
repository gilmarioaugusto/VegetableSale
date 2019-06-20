package e.tux.ifruit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TelaRelatorios extends AppCompatActivity {

    private EditText ddI;
    private EditText mmI;
    private EditText aaaaI;
    private EditText ddF;
    private EditText mmF;
    private EditText aaaaF;

    private TextView quantidadeComprada;
    private TextView valorTotalCompras;
    private TextView quantidadeVendida;
    private TextView valorTotalVendas;
    private TextView lucroTotal;

    private ImageButton btOk;

    private String dataInicial;
    private String dataFinal;
    private String user;

    private Double totalCompras;
    private Double totalVendas;
    private Double totalLucro;

    private CollectionReference comprasRef;
    private CollectionReference vendasRef;

    private ArrayList<ProdutoComprado> produtosComprados;
    private ArrayList<ProdutoVendido> produtosVendidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_relatorios);

        quantidadeComprada = findViewById(R.id.txtQuantidadeComprada);
        valorTotalCompras = findViewById(R.id.txtTotalDasCompras);
        quantidadeVendida = findViewById(R.id.txtQuantidadeVendida);
        valorTotalVendas = findViewById(R.id.txtTotalDasVendas);
        lucroTotal = findViewById(R.id.txtLucro);
        btOk = findViewById(R.id.bt_ok_relatorio);
        ddI = findViewById(R.id.ddI);
        mmI = findViewById(R.id.mmI);
        aaaaI = findViewById(R.id.aaaaI);
        ddF = findViewById(R.id.ddF);
        mmF = findViewById(R.id.mmF);
        aaaaF = findViewById(R.id.aaaaF);

        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chamarRelatorio();
            }
        });

    }

    public void chamarRelatorio(){

        dataInicial = aaaaI.getText().toString()+mmI.getText().toString()+ddI.getText().toString()+"000000";
        dataFinal = aaaaF.getText().toString()+mmF.getText().toString()+ddF.getText().toString()+"000000";
        totalCompras = 0.0;
        totalVendas = 0.0;
        totalLucro = 0.0;

        produtosComprados = new ArrayList<>();
        produtosVendidos = new ArrayList<>();

        user = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        comprasRef = FirebaseFirestore.getInstance().collection("transacoes").document(user).collection("compras");
        vendasRef = FirebaseFirestore.getInstance().collection("transacoes").document(user).collection("vendas");

        comprasRef.whereGreaterThanOrEqualTo("dataDaCompra", dataInicial).whereLessThanOrEqualTo("dataDaCompra", dataFinal)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    ProdutoComprado produtoComprado = new ProdutoComprado();
                    produtoComprado = document.toObject(ProdutoComprado.class);
                    produtosComprados.add(produtoComprado);
                    totalCompras = totalCompras + produtoComprado.getPrecoDaCompra();

                }
                quantidadeComprada.setText(String.valueOf(produtosComprados.size()));
                valorTotalCompras.setText("R$"+String.valueOf(totalCompras));
                totalLucro = totalVendas - totalCompras;
                lucroTotal.setText("R$"+String.valueOf(totalLucro));

                if (produtosComprados.isEmpty()){
                    quantidadeComprada.setText("Sem compras!");
                    valorTotalCompras.setText("Sem compras!");
                }
            }
        });

        vendasRef.whereGreaterThanOrEqualTo("dataDaCompra", dataInicial).whereLessThanOrEqualTo("dataDaCompra", dataFinal)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    ProdutoVendido produtoVendido = new ProdutoVendido();
                    produtoVendido = document.toObject(ProdutoVendido.class);
                    produtosVendidos.add(produtoVendido);
                    totalVendas = totalVendas + produtoVendido.getPrecoDaCompra();

                }
                quantidadeVendida.setText(String.valueOf(produtosVendidos.size()));
                valorTotalVendas.setText("R$"+String.valueOf(totalVendas));
                totalLucro = totalVendas - totalCompras;
                lucroTotal.setText("R$"+String.valueOf(totalLucro));

                if (produtosComprados.isEmpty()){
                    quantidadeVendida.setText("Sem compras!");
                    valorTotalVendas.setText("Sem compras!");
                }
            }
        });

        /*for (ProdutoComprado produtoComprado:produtosComprados){
            totalCompra =+ produtoComprado.getPrecoDaCompra();
        }*/



    }

}
