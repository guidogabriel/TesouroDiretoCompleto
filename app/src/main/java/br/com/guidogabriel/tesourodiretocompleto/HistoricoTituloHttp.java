package br.com.guidogabriel.tesourodiretocompleto;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guido_000 on 19/05/2015.
 */
public class HistoricoTituloHttp {
    public static final String URL_JSON = "http://guidogabriel.16mb.com/HistoricoTituloJson.php";

    public static boolean temConexao(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    public static List<TituloTesouro> carregarLivrosJson(String nomeTitulo) {
        try {
            HttpURLConnection conexao = connectar(URL_JSON, nomeTitulo);
            int resposta = conexao.getResponseCode();
            if (resposta == HttpURLConnection.HTTP_OK) {
                InputStream is = conexao.getInputStream();
                //Log.i("Tesouro Direto",bytesParaString(is));
                JSONObject json = new JSONObject(bytesParaString(is));

                return lerJsonTitulos(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static HttpURLConnection connectar(String urlArquivo, String nomeTitulo) throws IOException {
        final int SEGUNDOS = 10000;
        URL url = new URL(urlArquivo);
        HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
        conexao.setReadTimeout(100 * SEGUNDOS);
        conexao.setConnectTimeout(150 * SEGUNDOS);
        //conexao.setRequestMethod("GET");
        conexao.setDoInput(true);
        conexao.setDoOutput(true);
        conexao.setRequestMethod("POST");
        OutputStreamWriter writer = new OutputStreamWriter(conexao.getOutputStream());
        writer.write("nomeTitulo="+nomeTitulo);
        Log.i("Tesouro Direto", nomeTitulo);
        writer.close();
        conexao.connect();
        return conexao;
    }

    public static List<TituloTesouro> lerJsonTitulos(JSONObject json) throws JSONException {
        List<TituloTesouro> listaDeTitulos = new ArrayList<TituloTesouro>();

        JSONArray jsonTitulos = json.getJSONArray("titulotesouro");
        for (int j = 0; j < jsonTitulos.length(); j++) {
            JSONObject jsonTitulo = jsonTitulos.getJSONObject(j);
            TituloTesouro livro = new TituloTesouro(
                    jsonTitulo.getInt("idtitulotesouro"),
                    jsonTitulo.getString("NomeTitulo"),
                    jsonTitulo.getString("TaxaCompra"),
                    jsonTitulo.getString("PrecoCompra")
            );
            listaDeTitulos.add(livro);
            //Log.i("Tesouro Direto",livro.toString()+" ");
        }
        return listaDeTitulos;
    }

    private static String bytesParaString(InputStream is) throws IOException {
        byte[] buffer = new byte[1024];
        // O bufferzao vai armazenar todos os bytes lidos
        ByteArrayOutputStream bufferzao = new ByteArrayOutputStream();
        // precisamos saber quantos bytes foram lidos
        int bytesLidos;
        // Vamos lendo de 1KB por vez...
        while ((bytesLidos = is.read(buffer)) != -1) {
            // copiando a quantidade de bytes lidos do buffer para o bufferzao
            bufferzao.write(buffer, 0, bytesLidos);
        }
        return new String(bufferzao.toByteArray(), "UTF-8");
    }
}
