package e.tux.ifruit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Intent it;
    private EditText email;
    private EditText password;
    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";
    public int resultSolicitarPermissao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Toast.makeText(getApplicationContext(), "Bem-vindo de volta", 1).show();
        } else {
            it = new Intent(this, LoginActivity.class);
            startActivity(it);
            finish();
        }
    }

   /* @Override
    public void onStart(){
        super.onStart();
        //FirebaseApp.initializeApp(this);
        //FirebaseUser currentUser = mAuth.getCurrentUser();
    }*/

/*    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.botaoLogin){
            Toast.makeText(this,"Botão Login Acionado",1).show();
            //signIn(email.getText().toString(), password.getText().toString());

            //solicitarPermissao();
            //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET},);
        }
    } */



    public void telaCadastro(View view){
        it = new Intent(MainActivity.this, TelaCadastro.class);
        startActivity(it);
    }

    public void login(View view){
        email = findViewById(R.id.campoEmail);
        password = findViewById(R.id.campoPassword);
        String stremail = email.getText().toString();
        String strpassword = password.getText().toString();
    }

    private boolean validateForm() {
        boolean valid = true;

        String strEmail = email.getText().toString();
        if (TextUtils.isEmpty(strEmail)) {
            email.setError("Required.");
            valid = false;
        } else {
            email.setError(null);
        }

        String strPassword = password.getText().toString();
        if (TextUtils.isEmpty(strPassword)) {
            password.setError("Required.");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        //showProgressDialog();

        mAuth.signInWithEmailAndPassword(email, password);
    }



    /*public void solicitarPermissao(){
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, resultSolicitarPermissao);
        }
    }*/

    /*@Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case resultSolicitarPermissao: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                }
            }
        }
    }*/

}
