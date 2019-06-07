package e.tux.ifruit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class CadastrarProduto extends AppCompatActivity {

    private AutoCompleteTextView tfNome;
    private AutoCompleteTextView tfPreco;
    private AutoCompleteTextView tfQuantidade;
    private Produto produtoUp;
    private ImageButton btCadastrar;
    private FirebaseFirestore mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private RadioButton porUnidade;
    private RadioButton porQuilo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_produto);

        mDatabase = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        porUnidade = findViewById(R.id.rbPorUn);
        porQuilo = findViewById(R.id.rbPorQuilo);

        btCadastrar = findViewById(R.id.btCadastrarProduto);
        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executarCadastro();
            }
        });

    }

    public void executarCadastro() {
        boolean cancel = false;
        View erro = null;
        tfNome = findViewById(R.id.tfNome);
        tfPreco = findViewById(R.id.tfPreco);
        tfQuantidade = findViewById(R.id.tfQuantidade);
        String strNome = tfNome.getText().toString();
        String strPreco = tfPreco.getText().toString();
        String strQuantidade = tfQuantidade.getText().toString();
        if (TextUtils.isEmpty(strNome)) {
            tfNome.setError("Campo obrigatório");
            erro = tfNome;
            cancel = true;
        } else if (TextUtils.isEmpty(strPreco)) {
            tfPreco.setError("Campo obrigatório");
            erro = tfPreco;
            cancel = true;
        } else if (TextUtils.isEmpty(strQuantidade)) {
            tfQuantidade.setError("Campo obrigatório");
            erro = tfQuantidade;
            cancel = true;
        }
        if (cancel) {
            erro.requestFocus();
        } else {
            double doublePreco = Double.parseDouble(strPreco);
            double doubleQuantidade = Integer.parseInt(strQuantidade);
            String proprietario = user.getEmail().substring(0, user.getEmail().indexOf("@"));
            produtoUp = new Produto();
            if (porQuilo.isChecked()){
                produtoUp.setPorUnidade(false);
            } else if(porUnidade.isChecked()) {
                produtoUp.setPorUnidade(true);
            }
            produtoUp.setNome(strNome);
            produtoUp.setPrecoIndividual(doublePreco);
            produtoUp.setQuantidadeDisponivel(doubleQuantidade);
            produtoUp.setProprietario(user.getEmail());
            mDatabase.collection("produtos").document(strNome).set(produtoUp);
            Toast.makeText(getApplicationContext(), "Produto Cadastrado", 1).show();
            limparCampos();
        }
    }

    public void limparCampos() {
        tfNome.setText("");
        tfPreco.setText("");
        tfQuantidade.setText("");
    }

}
