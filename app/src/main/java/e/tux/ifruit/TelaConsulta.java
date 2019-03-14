package e.tux.ifruit;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TelaConsulta extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_consulta);

        BancoController crud = new BancoController(getBaseContext());
        Cursor cursor = crud.carregarDados();



    }
}
