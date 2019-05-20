package e.tux.ifruit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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
    private Button btSair;
    private Button btExibirProduto;
    private Button btChamarCadastroProduto;
    private Button btPagar;
    private DatabaseReference mBanco;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            Toast.makeText(getApplicationContext(), "Bem-vindo de volta "+user.getEmail(), 1).show();
            msg =  findViewById(R.id.txtBemVindo);
            msg.setText("Bem-Vindo "+user.getEmail());
        } else {
            it = new Intent(this, LoginActivity.class);
            startActivity(it);
            finish();
        }

        btChamarCadastroProduto = findViewById(R.id.btChamarCadastroProduto);
        btChamarCadastroProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CadastrarProduto.class));
            }
        });

        btExibirProduto = findViewById(R.id.btExibirProdutos);
        btExibirProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ListarProdutos.class));
            }
        });

        btSair = findViewById(R.id.btSair);
        btSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

        btPagar = (Button) findViewById(R.id.pagar);
        btPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PagamentoPayPal.class));
            }
        });
    }
}
