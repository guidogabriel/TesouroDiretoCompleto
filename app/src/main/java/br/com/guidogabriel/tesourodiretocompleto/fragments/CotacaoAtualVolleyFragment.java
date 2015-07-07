package br.com.guidogabriel.tesourodiretocompleto.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.guidogabriel.tesourodiretocompleto.HistoricoTituloActivity;
import br.com.guidogabriel.tesourodiretocompleto.R;
import br.com.guidogabriel.tesourodiretocompleto.TituloTesouro;
import br.com.guidogabriel.tesourodiretocompleto.TituloTesouroHttp;
import br.com.guidogabriel.tesourodiretocompleto.VolleySingleton;
import br.com.guidogabriel.tesourodiretocompleto.adapter.CotacaoAtualAdapter;
import br.com.guidogabriel.tesourodiretocompleto.interfaces.RecyclerViewOnClickListenerHack;

/**
 * Created by guido_000 on 23/05/2015.
 */
public class CotacaoAtualVolleyFragment extends Fragment implements Response.ErrorListener, Response.Listener<String>, RecyclerViewOnClickListenerHack {
    private List<TituloTesouro> mList;
    private RecyclerView mRecyclerView;
    CotacaoAtualAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_titulo_tesouro, null);
        return layout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mList == null) {
            mList = new ArrayList<TituloTesouro>();
        }

        adapter = new CotacaoAtualAdapter(getActivity(), mList);
        adapter.setRecyclerViewOnClickListenerHack(this);
        mRecyclerView.setAdapter(adapter);

        RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        String URL = "http://guidogabriel.16mb.com/CotacaoAtualJsonRefatorado.php";

        StringRequest request = new StringRequest(
                Request.Method.GET,
                URL, //LivroHttp.LIVROS_URL_JSON,  // url da requisocao
                this,
                this
        );
        queue.add(request);
    }

    @Override
    public void onResponse(String response) {

        Log.i("Tesouro Direto", "Resposta: " + response);
        //if (mList != null) {
        mList.clear();
        JSONObject json = null;
        try {
            json = new JSONObject(response);
        } catch (JSONException e) {
            Log.w("Tesouro Direto", e.getMessage() + " --> Erro 1");
            e.printStackTrace();
        }
        try {
            mList.addAll(TituloTesouroHttp.lerJsonTitulos(json));
                        adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            Log.w("Tesouro Direto", e.getMessage() + " --> Erro 2");
            e.printStackTrace();
        }

//        }
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        Log.w("Tesouro Direto", "Nao funfou esse volley");
    }

    @Override
    public void onClickListener(View view, int position) {
        CotacaoAtualAdapter adapter = (CotacaoAtualAdapter) mRecyclerView.getAdapter();
        Intent i = new Intent(getActivity(), HistoricoTituloActivity.class);
        TituloTesouro tituloTesouro = new TituloTesouro();
        long id = adapter.getItemIdRemoto(position);
        i.putExtra("id", id + "");
        Toast.makeText(getActivity(), "ID: " + id, Toast.LENGTH_SHORT).show();
        startActivity(i);
    }
/*
    @Override
    public Map<String, String> getParams() throws AuthFailureError {
        params = new HashMap<String, String>();
        params.put("email", etEmail.getText().toString());
        params.put("pasword", etPassword.getText().toString());
        params.put("method", "web-data-sr");

        return(params);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError{
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("apiKey", "Essa e minha API KEY: sdvkjbsdjvkbskdv");

        return(header);
    }

    @Override
    public Priority getPriority(){
        return(Priority.NORMAL);
    }
    */

}
