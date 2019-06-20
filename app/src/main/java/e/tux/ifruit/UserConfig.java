package e.tux.ifruit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserConfig extends AppCompatActivity {

    private Usuario usuarioUserConfig;

    private EditText alterSenha;
    private EditText confirmAlterSenha;

    private AutoCompleteTextView autoCompleteTextViewUsuarioTipo;

    private Button btAlterarSenha;
    private Button btTornarVendedor;
    private Button btTornarAdministrador;

    private ArrayList<String> listaEmailsUserConfig;

    private FirebaseFirestore bancoConfigUser;
    private FirebaseUser firebaseUserConfigUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_config);

        usuarioUserConfig = (Usuario) getIntent().getSerializableExtra("usuario");
        listaEmailsUserConfig = new ArrayList<>();
        obterLista();

        bancoConfigUser = FirebaseFirestore.getInstance();
        firebaseUserConfigUser = FirebaseAuth.getInstance().getCurrentUser();

        alterSenha = findViewById(R.id.editTextAlterSenha);
        confirmAlterSenha = findViewById(R.id.editTextConfirmAlterSenha);
        autoCompleteTextViewUsuarioTipo = findViewById(R.id.autoTextViewUsuarioTipo);
        btAlterarSenha = findViewById(R.id.buttonAlterarSenha);
        btTornarVendedor = findViewById(R.id.buttonTornarVendedor);
        btTornarAdministrador = findViewById(R.id.buttonTornarAdministrador);

        btAlterarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alterarSenha();
            }
        });

        btTornarVendedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tornarVendedor();
            }
        });

        btTornarAdministrador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tornarAdministrador();
            }
        });

    }

    private void tornarAdministrador() {
        String usuarioDigitado = autoCompleteTextViewUsuarioTipo.getText().toString();
        if (usuarioUserConfig.isAdministrador()){
            if (listaEmailsUserConfig.contains(usuarioDigitado)){
                bancoConfigUser.collection("usuarios").document(usuarioDigitado).update("administrador", true);
                Toast.makeText(getApplicationContext(),"Permissão Concedida!",0).show();
                limparCampos();
            } else {
                Toast.makeText(getApplicationContext(),"Usuário não encontrado",0).show();
            }
        } else{
            Toast.makeText(getApplicationContext(),"Permissão negada, você não é um administrador!",0).show();
        }
    }

    private void tornarVendedor() {
        String usuarioDigitado = autoCompleteTextViewUsuarioTipo.getText().toString();
        if (usuarioUserConfig.isAdministrador()){
            if (listaEmailsUserConfig.contains(usuarioDigitado)){
                bancoConfigUser.collection("usuarios").document(usuarioDigitado).update("vendedor", true);
                Toast.makeText(getApplicationContext(),"Permissão Concedida!",0).show();
                limparCampos();
            } else {
                Toast.makeText(getApplicationContext(),"Usuário não encontrado",0).show();
            }
        } else{
            Toast.makeText(getApplicationContext(),"Permissão negada, você não é um administrador!",0).show();
        }
    }

    private void alterarSenha() {
        String senhaDigitada = alterSenha.getText().toString();
        String confirmarSenhaDigitada = confirmAlterSenha.getText().toString();
        if (TextUtils.isEmpty(alterSenha.getText().toString())){
            alterSenha.setError("Campo Obrigatório");
            alterSenha.requestFocus();
        } else if (TextUtils.isEmpty(confirmAlterSenha.getText().toString())){
            confirmAlterSenha.setError("Campo Obrigatório");
            confirmAlterSenha.requestFocus();
        } else if (!senhaDigitada.equals(confirmarSenhaDigitada)){
            confirmAlterSenha.setError("As senhas devem ser iguais");
            confirmAlterSenha.requestFocus();
        } else if (senhaDigitada.equals(confirmarSenhaDigitada)){
            firebaseUserConfigUser.updatePassword(senhaDigitada);
            Toast.makeText(getApplicationContext(), "Senha alterada com sucesso!", 0).show();
            limparCampos();
        }
    }

    private void limparCampos() {
        alterSenha.setText("");
        confirmAlterSenha.setText("");
        autoCompleteTextViewUsuarioTipo.setText("");
    }

    private void obterLista() {
        bancoConfigUser = FirebaseFirestore.getInstance();
        bancoConfigUser.collection("usuarios").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                    Usuario usuario = new Usuario();
                    usuario = document.toObject(Usuario.class);
                    listaEmailsUserConfig.add(usuario.getEmail());
                }
            }
        });
    }
}
