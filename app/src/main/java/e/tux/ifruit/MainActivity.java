package e.tux.ifruit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {

    private Intent it;
    private TextView msg;
    private FirebaseAuth mAuth;
    private ImageButton btSair;
    private ImageButton btExibirProduto;
    private ImageButton btChamarCadastroProduto;
    private ImageButton btChamarCarrinho;
    private ImageButton btChamarCompras;
    private ImageButton btChamarVendas;
    private ImageButton btChamarRelatorios;
    private ImageButton btUserConfig;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            msg =  findViewById(R.id.txtBemVindo);
            msg.setText(user.getEmail());
        } else {
            it = new Intent(this, LoginActivity.class);
            startActivity(it);
            finish();
        }

        btChamarCadastroProduto = findViewById(R.id.bt_cadastrar_produto);
        btChamarCadastroProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CadastrarProduto.class));
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
                mAuth.signOut();
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
                startActivity(i);
            }
        });
    }
}
