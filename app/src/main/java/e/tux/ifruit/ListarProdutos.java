package e.tux.ifruit;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListarProdutos extends AppCompatActivity {

    private ListView listView;
    private Produto rProduto;
    private String nome;
    private DatabaseReference mBanco;
    private TextView txTeste;
    private TextView txTeste2;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ArrayAdapter listaAdaptador;
    private ArrayList<Produto> listaProdutos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_produtos);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        txTeste = findViewById(R.id.txTeste);

        listaProdutos = new ArrayList<>();

       // mBanco.
    }

    public void setListViewProdutos(ArrayList lista){
        listaAdaptador = new ArrayAdapter<Produto>(this, android.R.layout.simple_expandable_list_item_2, android.R.id.text2, lista);
        listView.setAdapter(listaAdaptador);
    }
}
