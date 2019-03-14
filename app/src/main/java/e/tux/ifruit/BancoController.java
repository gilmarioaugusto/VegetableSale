package e.tux.ifruit;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BancoController {

    private SQLiteDatabase db;
    private CriarBanco banco;
    private Intent it;

    public BancoController(Context context){
        banco = new CriarBanco(context);
    }

    public String cadastrarNovoUsuario(
            String nome,
            String email,
            String senha,
            String cpf,
            String telefone){

        ContentValues valores;
        long resultado;

        db = banco.getReadableDatabase();
        valores = new ContentValues();
        valores.put(CriarBanco.NOME, nome);
        valores.put(CriarBanco.EMAIL, email);
        valores.put(CriarBanco.SENHA, senha);
        valores.put(CriarBanco.CPF, cpf);
        valores.put(CriarBanco.TELEFONE, telefone);

        //INSERE OS DADOS NO BANCO
        resultado = db.insert(CriarBanco.TABELA, null, valores);
        db.close();

        //TESTA SE HOUVE INSERÇÃO OU ERRO
        if (resultado == -1){
            return "Erro ao cadastrar novo usuário";
        } else {
            return "Usuário Cadastrado com sucesso";
        }

    }

    public Cursor carregarDados(){

       Cursor cursor;
       String[] campos = {banco.ID, banco.NOME};
       db = banco.getReadableDatabase();
       cursor = db.query(banco.TABELA, campos, null, null, null, null, null, null);

       if (cursor!=null){
           cursor.moveToFirst();
       }
       db.close();
       return cursor;

    }

}