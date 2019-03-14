package e.tux.ifruit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TelaCadastro extends AppCompatActivity {

    private EditText nomeCadastro;
    private EditText emailCadastro;
    private EditText senhaCadastro;
    private EditText cpfCadastro;
    private EditText telefoneCadastro;
    String resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);
    }

    public void gravarDados(View view){

        BancoController dados = new BancoController(getBaseContext());
        nomeCadastro = findViewById(R.id.nomeCadastro);
        emailCadastro = findViewById(R.id.emailCadastro);
        senhaCadastro = findViewById(R.id.senhaCadastro);
        cpfCadastro = findViewById(R.id.cpfCadastro);
        telefoneCadastro = findViewById(R.id.telefoneCadastro);
        String nome = nomeCadastro.getText().toString();
        String email = emailCadastro.getText().toString();
        String senha = senhaCadastro.getText().toString();
        String cpf = cpfCadastro.getText().toString();
        String telefone = telefoneCadastro.getText().toString();

        resultado = dados.cadastrarNovoUsuario(nome, email, senha, cpf, telefone);
        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);
        Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_SHORT).show();

    }

}
