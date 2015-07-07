package br.com.guidogabriel.tesourodiretocompleto.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.guidogabriel.tesourodiretocompleto.Monitor;
import br.com.guidogabriel.tesourodiretocompleto.R;
import br.com.guidogabriel.tesourodiretocompleto.interfaces.RecyclerViewOnClickListenerHack;
import br.com.guidogabriel.tesourodiretocompleto.interfaces.RecyclerViewOnFocusChangeListenerGuido;

/**
 * Created by guido_000 on 17/06/2015.
 */
public class MonitorAdapter extends RecyclerView.Adapter<MonitorAdapter.MonitorViewHolder> {
    private static final String TAG = "Tesouro Direto";
    private List<Monitor> mList;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;
    private RecyclerViewOnFocusChangeListenerGuido mRecyclerViewOnFocusChangeListenerGuido;

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r) {
        this.mRecyclerViewOnClickListenerHack = r;
    }

    public void setRecyclerViewOnFocusChangeListenerGuido(RecyclerViewOnFocusChangeListenerGuido r) {
        this.mRecyclerViewOnFocusChangeListenerGuido = r;
    }

    public MonitorAdapter(Activity activity, List<Monitor> mList) {
        this.mList = mList;
        this.mLayoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public MonitorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.item_monitoramento, parent, false);
        MonitorViewHolder mvh = new MonitorViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MonitorViewHolder holder, int position) {
        holder.idMonitorRemoto.setText(mList.get(position).getIdMonitorRemoto() == null ? "" : mList.get(position).getIdMonitorRemoto().toString());
        holder.idTituloRemoto.setText(mList.get(position).getIdTituloRemoto().toString());
        holder.nome.setText(mList.get(position).getNomeTitulo());
        holder.menor_que.setText(mList.get(position).getTaxaCompraMenorQue() == null ? "" : mList.get(position).getTaxaCompraMenorQue());
        holder.maior_que.setText(mList.get(position).getTaxaCompraMaiorQue() == null ? "" : mList.get(position).getTaxaCompraMaiorQue());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public List<Monitor> getAll() {
        int tamanho = getItemCount();
        List<Monitor> listaMonitor = new ArrayList<Monitor>();
        for (int i = 0; i < tamanho; i++) {
            listaMonitor.add(mList.get(i));
        }

        return listaMonitor;
    }

    public class MonitorViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener, View.OnFocusChangeListener {
        public TextView idTituloRemoto;
        public TextView idMonitorRemoto;
        public TextView nome;
        public EditText menor_que;
        public EditText maior_que;


        public MonitorViewHolder(View itemView) {
            super(itemView);
            idTituloRemoto = (TextView) itemView.findViewById(R.id.tv_id_titulo_remoto);
            idMonitorRemoto = (TextView) itemView.findViewById(R.id.tv_id_monitor_remoto);
            nome = (TextView) itemView.findViewById(R.id.tv_nome_titulo);
            menor_que = (EditText) itemView.findViewById(R.id.ed_taxa_menor_que);
            maior_que = (EditText) itemView.findViewById(R.id.ed_taxa_maior_que);

            itemView.setOnClickListener(this);

            menor_que.setOnFocusChangeListener(this);
            maior_que.setOnFocusChangeListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mRecyclerViewOnClickListenerHack != null) {
                mRecyclerViewOnClickListenerHack.onClickListener(v, getPosition());
            }
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) { //Quando perder o foco

                final int position = getPosition();
                switch (v.getTag().toString()) {
                    case "menor_que": {
                        final EditText menor_que = (EditText) v;
                        mList.get(position).setTaxaCompraMenorQue(menor_que.getText().toString());
                    }
                    break;
                    case "maior_que": {
                        final EditText maior_que = (EditText) v;
                        mList.get(position).setTaxaCompraMaiorQue(maior_que.getText().toString());
                        //Log.i(TAG, "O valor eh: "+maior_que.getText().toString());
                    }
                    break;
                }
            }
        }

    }


}
