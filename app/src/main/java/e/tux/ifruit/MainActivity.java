package e.tux.ifruit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import e.tux.ifruit.pagamento.PagamentoPayPal;

public class MainActivity extends AppCompatActivity {

    private Intent it;
    private TextView msg;
    private FirebaseAuth mAuth;
    private ImageButton btSair;
    private ImageButton btExibirProduto;
    private ImageButton btChamarCadastroProduto;
    private ImageButton btPagar;
    private DatabaseReference mBanco;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            //Toast.makeText(getApplicationContext(), "Bem-vindo de volta "+user.getEmail(), 1).show();
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

        btPagar = findViewById(R.id.bt_carrinho);
        btPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PagamentoPayPal.class));
            }
        });
    }
}
