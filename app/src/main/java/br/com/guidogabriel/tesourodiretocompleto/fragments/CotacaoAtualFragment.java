package br.com.guidogabriel.tesourodiretocompleto.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.guidogabriel.tesourodiretocompleto.HistoricoTituloActivity;
import br.com.guidogabriel.tesourodiretocompleto.R;
import br.com.guidogabriel.tesourodiretocompleto.TituloTesouro;
import br.com.guidogabriel.tesourodiretocompleto.TituloTesouroHttp;
import br.com.guidogabriel.tesourodiretocompleto.adapter.CotacaoAtualAdapter;
import br.com.guidogabriel.tesourodiretocompleto.interfaces.RecyclerViewOnClickListenerHack;

public class CotacaoAtualFragment extends Fragment implements RecyclerViewOnClickListenerHack {
    //private List<Livro> mList;
    private List<TituloTesouro> mList;
    private RecyclerView mRecyclerView;
    CotacaoAtualAdapter adapter;
    LivrosTask mTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_titulo_tesouro, null);
        //mTextMensagem = (TextView) layout.findViewById(android.R.id.empty);
        //mProgressBar = (ProgressBar) layout.findViewById(R.id.progressBar);
        //mListView = (ListView) layout.findViewById(R.id.list);
        //mListView.setEmptyView(mTextMensagem);
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
        //mList =;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mList == null) {
            mList = new ArrayList<TituloTesouro>();
        }
        //mAdapter = new CotacaoAtualAdapter(getActivity(), mList);
        adapter = new CotacaoAtualAdapter(getActivity(), mList);
        adapter.setRecyclerViewOnClickListenerHack(this);
        mRecyclerView.setAdapter(adapter);


        if (mTask == null) {
            if (TituloTesouroHttp.temConexao(getActivity())) {
                iniciarDownload();
            } else {
                Log.i("Tesouro Direto", "Sem conexao");
            }
        } else if (mTask.getStatus() == AsyncTask.Status.RUNNING) {
            Log.i("Tesouro Direto", "Progreess bar Ativar");
        }
    }
    public void iniciarDownload() {
        if (mTask == null || mTask.getStatus() != AsyncTask.Status.RUNNING) {
            mTask = new LivrosTask();
            mTask.execute();
        }
    }

    @Override
    public void onClickListener(View view, int position) {


        CotacaoAtualAdapter adapter = (CotacaoAtualAdapter) mRecyclerView.getAdapter();
        //adapter.removeListitem(position);
        Intent i = new Intent(getActivity(), HistoricoTituloActivity.class);
        TituloTesouro tituloTesouro = new TituloTesouro();
        //String nome = adapter.getItemNome(position);
        //i.putExtra("NomeTitulo", nome);
        //Toast.makeText(getActivity(), "Nome: "+ nome, Toast.LENGTH_SHORT).show();
        //startActivity(i);



    }

    class LivrosTask extends AsyncTask<Void, Void, List<TituloTesouro>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //exibirProgress(true);
        }

        @Override
        protected List<TituloTesouro> doInBackground(Void... strings) {

            return TituloTesouroHttp.carregarLivrosJson();
            //return LivroHttp.carregarLivrosXml();
        }

        @Override
        protected void onPostExecute(List<TituloTesouro> livros) {
            super.onPostExecute(livros);
            //exibirProgress(false);
            if (livros != null) {
                Log.i("Tesouro Direto", mList.toString());
                mList.clear();
                mList.addAll(livros);
                adapter.notifyDataSetChanged();
            } else {
                Log.i("Tesouro Direto", "Falha ao obter livros");        //mTextMensagem.setText("Falha ao obter livros");
            }
        }
    }
}