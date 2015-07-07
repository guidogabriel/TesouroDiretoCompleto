package br.com.guidogabriel.tesourodiretocompleto.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.guidogabriel.tesourodiretocompleto.R;
import br.com.guidogabriel.tesourodiretocompleto.TituloTesouro;
import br.com.guidogabriel.tesourodiretocompleto.TituloTesouroHttp;
import br.com.guidogabriel.tesourodiretocompleto.VolleySingleton;
import br.com.guidogabriel.tesourodiretocompleto.adapter.HistoricoTituloAdapter;

/**
 * Created by guido_000 on 18/05/2015.
 */
public class HistoricoTituloFragment extends Fragment implements Response.ErrorListener, Response.Listener<String> {
    private List<TituloTesouro> mList;
    private LinearLayoutManager llm;
    private RecyclerView mRecyclerView;
    private HistoricoTituloAdapter adapter;
    private Map<String, String> params;
    private String idTitulo;
    private Boolean carregarMais = false;
    private int tamanho = 20;
    private int inicio = 0;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;


    //HistoricoTituloAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_historico_titulo, null);
        Log.i("Tesouro Direto", "onCreateView : ok");
        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = llm.getChildCount();
                totalItemCount = llm.getItemCount();
                pastVisiblesItems = llm.findFirstVisibleItemPosition();

                if (loading) {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        loading = false;
                        Log.v("Tesouro Direto", "Last Item Wow !");

                        if (carregarMais) {
                            String url = "http://guidogabriel.16mb.com/HitoricoTituloRefatoradoJson.php";
                            url += "?idtitulo=" + idTitulo + "&inicio=" + inicio + "&tamanho=" + tamanho;
                            Log.i("Tesouro Direto", "Entrei no if(carregaMais), Nova URL: " + url);
                            chamarRequest(url);
                            //Log.i("Tesouro Direto", url);
                        } else
                            Log.i("Tesouro Direto", "Chegamos ao fimmm tem dias que voce nao vem, saudade de mim");
                    }
                }
            }
        });

        Log.i("Tesouro Direto", "onViewCreated : ok");

        if (mList == null) {//se nao existir
            mList = new ArrayList<TituloTesouro>();
        }
        adapter = new HistoricoTituloAdapter(getActivity(), mList);
        mRecyclerView.setAdapter(adapter);


        idTitulo = getActivity().getIntent().getExtras().getString("id");
        inicio = 0;
        //tamanho = "10";


        RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        String URL = "http://guidogabriel.16mb.com/HitoricoTituloRefatoradoJson.php";
        URL += "?idtitulo=" + idTitulo + "&inicio=" + inicio + "&tamanho=" + tamanho;
        Log.i("Tesouro Direto", URL);


        //Long teste = getActivity().getIntent().getExtras().getLong("id");
        //Log.i("Tesouro Direto", "Extra fora do String Request -----------------------------------" + teste);

        StringRequest request = new StringRequest(Request.Method.GET, URL, this, this) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                String idTitulo = getActivity().getIntent().getExtras().getString("id");

                Log.i("Tesouro Direto", "idTitulo antes de ser enviado por GEt: " + idTitulo);
                params = new HashMap<String, String>();
                params.put("idtitulo", idTitulo);
                params.put("inicio", "0");
                params.put("tamanho", "10");

                return (params);
            }
        };

        queue.add(request);
        //Log.i("Tesouro Direto", mList.toString());

        //mList.clear();
        //mList.addAll();
        adapter.notifyDataSetChanged();


        //HistoricoTituloHttp.carregarLivrosJson(nomeTitulo);

    }

    public void chamarRequest(String url) {
        RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        StringRequest request = new StringRequest(Request.Method.GET, url, this, this);
        queue.add(request);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onResponse(String s) {
        Log.i("Tesouro Direto", "Caiu no onResponse do HistoricoTituloFragment");
        Log.i("Tesouro Direto", "-------------------------------------------");
        // Log.i("Tesouro Direto", "resposta: " + s);

        //mList.clear();
        JSONObject json = null;
        try {
            json = new JSONObject(s);
        } catch (JSONException e) {
            Log.w("Tesouro Direto", e.getMessage() + " --> Erro 1");
            e.printStackTrace();
        }
        try {

            //carregarMais = TituloTesouroHttp.jsonParaBoolean(json, "carregarmais");
            //Log.i("Tesouro Direto", "Valor do carregaMais dentro do onResponse do Volley: " + carregarMais);

            List<TituloTesouro> lista = TituloTesouroHttp.lerJsonTitulos(json);
            Log.i("Tesouro Direto", "tamanho da lista: " + lista.size());
            Log.i("Tesouro Direto", "tamanho da variavel tamanho: " + tamanho);
            if (lista.size() >= tamanho) {

                mList.addAll(lista);
                adapter.notifyDataSetChanged();
                carregarMais = true;
                inicio = inicio + tamanho - 1;
                loading = true;

            } else {
                mList.addAll(lista);
                adapter.notifyDataSetChanged();
                carregarMais = false;
                loading = false;
                Log.i("Tesouro Direto", "entrei no else");

                            }

        } catch (JSONException e) {
            Log.w("Tesouro Direto", e.getMessage() + " --> Erro 2");
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        Log.i("Tesouro Direto", "Caiu no onErrorResponse do HistoricoTituloFragment");
    }


}

