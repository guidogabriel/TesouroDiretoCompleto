package br.com.guidogabriel.tesourodiretocompleto.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.guidogabriel.tesourodiretocompleto.MainActivity;
import br.com.guidogabriel.tesourodiretocompleto.R;
import br.com.guidogabriel.tesourodiretocompleto.TituloTesouro;
import br.com.guidogabriel.tesourodiretocompleto.adapter.TituloTesouroAdapter;
import br.com.guidogabriel.tesourodiretocompleto.interfaces.RecyclerViewOnClickListenerHack;

public class TituloTesouroFragment extends Fragment implements RecyclerViewOnClickListenerHack {
    private static String LOG = "Tesouro Direto"; //Vou usar para o logcat quando necessario
    private RecyclerView mRecyclerView;
    private List<TituloTesouro> mList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_titulo_tesouro, container, false);
        return view;//inflater.inflate(R.layout.fragment_titulo_tesouro, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);

        //-----------
        //Trabalhando a questao de carregar mais no scrol
        //-----------
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager llm = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                TituloTesouroAdapter adapter = (TituloTesouroAdapter) mRecyclerView.getAdapter();


                if (mList.size() == llm.findLastCompletelyVisibleItemPosition() + 1) {
                    List<TituloTesouro> listAux = ((MainActivity) getActivity()).gerarTitulosTesouro(30); //Se quiser carregar da internet
                    for (int i = 0; i < listAux.size(); i++) {
                        adapter.addListItem(listAux.get(i), mList.size()); //Adicionando na ultima posicao da lista um por um
                    }
                }
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        Log.i("Tesouro Direto", "LInear setado");

        //mList = ((MainActivity) getActivity()).gerarTitulosTesouro(30);
        //TituloTesouroAdapter adapter = new TituloTesouroAdapter(getActivity(), mList);
        mList = ((MainActivity) getActivity()).gerarTitulosTesouro(30);
        TituloTesouroAdapter adapter = new TituloTesouroAdapter(getActivity(), mList);
        adapter.setRecyclerViewOnClickListenerHack(this);
        mRecyclerView.setAdapter(adapter);
        Log.i("Tesouro Direto","Recycler view setado");
    }

    @Override
    public void onClickListener(View view, int position) {
        Toast.makeText(getActivity(), "Position" + position, Toast.LENGTH_SHORT).show(); //Nao basta implementar o metodo da interface pro android saber que ele representa um click
        //Voce tem que chamar esse metodo em algum lugar, vamos chamar no ViewHolder do adapter (la no ViewHolder que vamos implementar o onClick nativo do Andorid)
        TituloTesouroAdapter adapter = (TituloTesouroAdapter) mRecyclerView.getAdapter();
        adapter.removeListItem(position);
        Log.i(LOG, "Dentro do ClickListener");
        myClickHandler(view);

    }

    public void myClickHandler(View view) {
        String stringUrl = "http://www.guidogabriel.16mb.com/AtualizaCotacao.php";//urlText.getText().toString();
        ConnectivityManager connMgr = (ConnectivityManager) ((MainActivity) getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageTask().execute(stringUrl); //Chamando a subclasse do AssyncTask passando a URL
        } else {
            //textView.setText("No network connection available.");
            Log.d(LOG, "Nenhuma conexao disponivel");
        }
    }

    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //textView.setText(result);
            Log.d(LOG, result);
        }
    }

    // Given a URL, establishes an HttpUrlConnection and retrieves
// the web page content as a InputStream, which it returns as
// a string.
    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(LOG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);

        //Se eu quisesse fazer download de uma imagem, ficaria assim:
        /*
        InputStream is = null;
        ...
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        ImageView imageView = (ImageView) findViewById(R.id.image_view);
        imageView.setImageBitmap(bitmap);
        */
    }

}



