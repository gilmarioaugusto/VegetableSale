package e.tux.ifruit.pagamento;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ConexaoPagamento {

    @FormUrlEncoded
    @POST("package/controle/controlePagamento.php")
    public Call<String> enviarPagamento(
       //id do produto //@Field("Product_id") String id,
        @Field("value") double valor,
        @Field("token") String token,
        @Field("parcels") int parcels
    );
}
