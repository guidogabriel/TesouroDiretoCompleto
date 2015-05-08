package br.com.guidogabriel.tesourodiretocompleto.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.guidogabriel.tesourodiretocompleto.MainActivity;
import br.com.guidogabriel.tesourodiretocompleto.R;
import br.com.guidogabriel.tesourodiretocompleto.TituloTesouro;
import br.com.guidogabriel.tesourodiretocompleto.adapter.TituloTesouroAdapter;

public class TituloTesouroFragment extends Fragment {
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

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        mList = ((MainActivity) getActivity()).gerarTitulosTesouro(30);
        TituloTesouroAdapter adapter = new TituloTesouroAdapter(getActivity(), mList);
        mRecyclerView.setAdapter(adapter);
    }
}
