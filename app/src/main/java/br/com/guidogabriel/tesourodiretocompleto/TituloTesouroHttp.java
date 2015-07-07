package br.com.guidogabriel.tesourodiretocompleto;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guido_000 on 15/05/2015.
 */
public class TituloTesouroHttp {

    public static final String URL_JSON_LOCAL = "http://localhost:63343/TesouroDireto20150514/CotacaoAtualJson.php";
    public static final String URL_JSON = "http://guidogabriel.16mb.com/CotacaoAtualJson.php";
    public static final String LIVROS_URL_JSON = "https://raw.githubusercontent.com/nglauber/" + "dominando_android/master/livros_novatec.json";
    public static Boolean carregarmais;

    public static boolean temConexao(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    public static List<TituloTesouro> carregarLivrosJson() {
        try {
            HttpURLConnection conexao = connectar(URL_JSON);
            int resposta = conexao.getResponseCode();
            if (resposta == HttpURLConnection.HTTP_OK) {
                InputStream is = conexao.getInputStream();

                JSONObject json = new JSONObject(bytesParaString(is));
                return lerJsonTitulos(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static HttpURLConnection connectar(String urlArquivo) throws IOException {
        final int SEGUNDOS = 1000;
        URL url = new URL(urlArquivo);
        HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
        conexao.setReadTimeout(10 * SEGUNDOS);
        conexao.setConnectTimeout(15 * SEGUNDOS);
        conexao.setRequestMethod("GET");
        conexao.setDoInput(true);
        conexao.setDoOutput(false);

        conexao.connect();
        return conexao;
    }

    //TODO o nome correto deveria ser algo como lerJsonCotacaoAtual
    public static List<TituloTesouro> lerJsonTitulos(JSONObject json) throws JSONException {
        List<TituloTesouro> listaDeTitulos = new ArrayList<TituloTesouro>();

        JSONArray jsonTitulos = json.getJSONArray("cotacaoatual");
        for (int j = 0; j < jsonTitulos.length(); j++) {
            JSONObject jsonTitulo = jsonTitulos.getJSONObject(j);
            TituloTesouro livro = new TituloTesouro();
            //      jsonTitulo.getInt("idtitulotesouro"),
            //    jsonTitulo.getString("nometitulo"),
            //  jsonTitulo.getString("taxacompra"),
            //jsonTitulo.getString("precocompra")
            //);
            livro.setIdRemoto(jsonTitulo.getLong("idtitulotesouro"));
            livro.setNome(jsonTitulo.getString("nometitulo"));
            livro.setPrecoCompra(jsonTitulo.getString("precocompra"));
            livro.setPrecoVenda(jsonTitulo.getString("precovenda"));
            livro.setTaxaCompra(jsonTitulo.getString("taxacompra"));
            livro.setTaxaVenda(jsonTitulo.getString("taxavenda"));
            livro.setDataUltimaAtualizacao(jsonTitulo.getString("ultimaatualizacao").substring(0, 19));

            listaDeTitulos.add(livro);
        }
        return listaDeTitulos;
    }

    public static String jsonParaString(String json, String string) throws JSONException {
        JSONObject jo = new JSONObject(json);
        String valor = jo.getString(string);
        return valor;
    }

    public static List<TituloTesouro> lerJsonTitulosTesouro(JSONObject json) throws JSONException {
        List<TituloTesouro> listaDeTitulos = new ArrayList<TituloTesouro>();

        JSONArray jsonTitulos = json.getJSONArray("titulostesouro");
        for (int j = 0; j < jsonTitulos.length(); j++) {
            JSONObject jsonTitulo = jsonTitulos.getJSONObject(j);
            TituloTesouro tituloTesouro = new TituloTesouro();

            tituloTesouro.setIdRemoto(jsonTitulo.getLong("idtitulotesouro"));
            tituloTesouro.setNome(jsonTitulo.getString("nometitulo"));
            //TODO tituloTesouro.setNomeAbreviado
            //TODO tituloTousouro.setVencimento
            listaDeTitulos.add(tituloTesouro);
        }
        return listaDeTitulos;
    }
    public static List<Monitor> lerJsonMonitor(JSONObject json) throws JSONException {
        List<Monitor> listaDeMonitor = new ArrayList<Monitor>();

        JSONArray jsonMonitores = json.getJSONArray("monitor");
        for (int j = 0; j < jsonMonitores.length(); j++) {
            JSONObject jsonMonitor = jsonMonitores.getJSONObject(j);
            Monitor monitor = new Monitor();
            monitor.setIdMonitorRemoto(jsonMonitor.getLong("idmonitor"));
            monitor.setIdUsuario(jsonMonitor.getLong("idusuario"));
            monitor.setIdTituloRemoto(jsonMonitor.getLong("idtitulotesouro"));
            //monitor.setNomeTitulo(jsonTitulo.getString("nometitulo"));
            monitor.setTaxaCompraMenorQue(jsonMonitor.getString("taxacompra_menorque").equals("null") ? "" : jsonMonitor.getString("taxacompra_menorque"));
            monitor.setTaxaCompraMaiorQue(jsonMonitor.getString("taxacompra_maiorque").equals("null") ? "" : jsonMonitor.getString("taxacompra_maiorque"));
            listaDeMonitor.add(monitor);
        }
        return listaDeMonitor;
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
