package e.tux.ifruit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CriarBanco extends SQLiteOpenHelper {

    protected static final String NOME_BANCO = "ifruitdb.db";
    protected static final String TABELA = "usuarios";
    protected static final String ID = "_id";
    protected static final String NOME = "nome";
    protected static final String EMAIL = "email";
    protected static final String SENHA = "senha";
    protected static final String CPF = "cpf";
    protected static final String TELEFONE = "telefone";
    protected static final int VERSAO = 1;

    public CriarBanco(Context context){
        super(context, NOME_BANCO,null, VERSAO);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE "+TABELA+"("
                +ID+" integer primary key autoincrement,"
                + NOME + " text,"
                + EMAIL + " text,"
                + SENHA + " text,"
                + CPF + " text,"
                + TELEFONE + " text)";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+TABELA);
        onCreate(db);

    }
}
