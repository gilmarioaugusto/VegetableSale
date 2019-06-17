package e.tux.ifruit.pagamento;

import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

public class CartaoDeCredito extends Observable {
    private String nomeCartao;
    private String numCartao;
    private String mes;
    private String ano;
    private String cvv;
    private int parcels;
    private String error;
    private String token;

    public CartaoDeCredito (Observer observer){
            addObserver(observer);
    }

    @JavascriptInterface
    public String getNomeCartao() {
        return nomeCartao;
    }

    public void setNomeCartao(String nomeCartao) {
        this.nomeCartao = nomeCartao;
    }

    @JavascriptInterface
    public String getNumCartao() {
        return numCartao;
    }

    public void setNumCartao(String numCartao) {
        this.numCartao = numCartao;
    }

    @JavascriptInterface
    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    @JavascriptInterface
    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    @JavascriptInterface
    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public int getParcels() {
        return parcels;
    }

    public void setParcels(int parcels) {
        this.parcels = parcels;
    }

    public String getError() {
        return error;
    }

    @JavascriptInterface
    public void setError(String... errors) {
        for (String erro : errors){
            if (erro.equalsIgnoreCase("card_number")){
                error += "Numero do Cartão Inválido.";

            }else if (erro.equalsIgnoreCase("card_holder_name")) {
                error += "Nome do portador Inválido.";

            }else if (erro.equalsIgnoreCase("card_expiration_month")) {
                error += "Mês de expiração inválido.";

            }else if (erro.equalsIgnoreCase("card_expiration_year")) {
                error += "Ano de expiração inválido.";

            }else if (erro.equalsIgnoreCase("card_cvv")) {
                error += "Código de Segurança Inválido.";

            }
        }
        Log.i("log_error", "error: " + error);

        setChanged();
        notifyObservers();

    }

    public String getToken() {
        return token;
    }

    @JavascriptInterface
    public void setToken(String token) {
        this.token = token;
        Log.i("log_token", "token: " + token);

        setChanged();
        notifyObservers();

    }
}
