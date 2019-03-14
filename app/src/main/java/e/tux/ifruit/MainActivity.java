package e.tux.ifruit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Intent it;
    private EditText email;
    private EditText senha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void telaCadastro(View view){
        it = new Intent(MainActivity.this, TelaCadastro.class);
        startActivity(it);
    }

    public void mensagemErro(String msg){
        Toast toast = Toast.makeText(getApplicationContext(), msg, 1);
        toast.show();
    }

    public void login(View view){
        email = findViewById(R.id.email);
        senha = findViewById(R.id.password);
        if (email.getText().toString().equals("gilmario@gmail.com") &&
                senha.getText().toString().equals("123")){
            it = new Intent(this, TelaAdmin.class);
            startActivity(it);
        }else {
            mensagemErro("Usuário ou Senha Inválidos");
        }
    }

}
