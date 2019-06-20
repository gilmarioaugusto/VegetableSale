package e.tux.ifruit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private Intent it;
    private TextView msg;
    private FirebaseAuth mAuthMain;
    private ImageButton btSair;
    private ImageButton btExibirProduto;
    private ImageButton btChamarCadastroProduto;
    private ImageButton btChamarCarrinho;
    private ImageButton btChamarCompras;
    private ImageButton btChamarVendas;
    private ImageButton btChamarRelatorios;
    private ImageButton btUserConfig;
    private Usuario usuarioMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuthMain = FirebaseAuth.getInstance();
        FirebaseUser userMain = mAuthMain.getCurrentUser();


        if (userMain != null) {
            msg =  findViewById(R.id.txtBemVindo);
            msg.setText(userMain.getEmail());
            obterPermissoes(userMain);
        } else {
            it = new Intent(this, LoginActivity.class);
            startActivity(it);
            finish();
        }

        usuarioMain = new Usuario();
        usuarioMain.setEmail("email@email.com");
        usuarioMain.setVendedor(false);
        usuarioMain.setAdministrador(false);



        btChamarCadastroProduto = findViewById(R.id.bt_cadastrar_produto);
        btChamarCadastroProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usuarioMain.isVendedor()) {
                    startActivity(new Intent(getApplicationContext(), CadastrarProduto.class));
                } else{
                    Toast.makeText(getApplicationContext(), "Você não é um vendedor, solicite a permissão a um administrador!",0).show();
                }
            }
        });

        btExibirProduto = findViewById(R.id.bt_listar_produto);
        btExibirProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ListarProdutos.class));
            }
        });

        btSair = findViewById(R.id.bt_logoff);
        btSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuthMain.signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

        btChamarCarrinho = findViewById(R.id.bt_carrinho);
        btChamarCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Carrinho.class));
            }
        });

        btChamarCompras = findViewById(R.id.bt_compras);
        btChamarCompras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Compras.class));
            }
        });

        btChamarVendas = findViewById(R.id.bt_vendas);
        btChamarVendas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Vendas.class));
            }
        });

        btChamarRelatorios = findViewById(R.id.bt_relatorio);
        btChamarRelatorios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TelaRelatorios.class));
            }
        });

        btUserConfig = findViewById(R.id.bt_UserConfig);
        btUserConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), UserConfig.class);
                //ADICIONAR EXTRA A INTENT
                i.putExtra("usuario",usuarioMain);
                startActivity(i);
            }
        });
    }

    private void obterPermissoes(FirebaseUser user) {
        FirebaseFirestore bancoMain = FirebaseFirestore.getInstance();
        bancoMain.collection("usuarios").document(user.getEmail().toString()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                usuarioMain = documentSnapshot.toObject(Usuario.class);
            }
        });
    }
}
