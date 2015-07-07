package br.com.guidogabriel.tesourodiretocompleto.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.guidogabriel.tesourodiretocompleto.R;
import br.com.guidogabriel.tesourodiretocompleto.TituloTesouro;
import br.com.guidogabriel.tesourodiretocompleto.interfaces.RecyclerViewOnClickListenerHack;

/**
 * Created by guido_000 on 15/05/2015.
 */
public class CotacaoAtualAdapter extends RecyclerView.Adapter<CotacaoAtualAdapter.CotacaoAtualViewHolder> {
    private List<TituloTesouro> mList;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r) {
        this.mRecyclerViewOnClickListenerHack = r;
    }


    public CotacaoAtualAdapter(Context context, List<TituloTesouro> mList) {
        this.mList = mList;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public CotacaoAtualViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.item_titulo_tesouro, parent,false);
        CotacaoAtualViewHolder  cotacaoAtualViewHolder = new CotacaoAtualViewHolder(v);
        return cotacaoAtualViewHolder;
    }

    @Override
    public void onBindViewHolder(CotacaoAtualViewHolder  holder, int position) {
        holder.id_remoto.setText(mList.get(position).getIdRemoto().toString());
        holder.nome.setText(mList.get(position).getNome());
        holder.taxa_compra.setText(mList.get(position).getTaxaCompra());
        holder.preco_compra.setText(mList.get(position).getPrecoCompra());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void removeListitem(int position){
        mList.remove(position);
        notifyItemRemoved(position);
    }
    public long getItemIdRemoto(int position){
        return mList.get(position).getIdRemoto();
    }



    public class CotacaoAtualViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener {
        public TextView id_remoto;
        public TextView nome;
        public TextView preco_compra;
        public TextView taxa_compra;


        public CotacaoAtualViewHolder (View itemView) {
            super(itemView);
            id_remoto = (TextView) itemView.findViewById(R.id.tv_id_remoto);
            nome = (TextView) itemView.findViewById(R.id.tv_nome_titulo);
            preco_compra = (TextView) itemView.findViewById(R.id.tv_preco_compra);
            taxa_compra = (TextView) itemView.findViewById(R.id.tv_taxa_compra);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mRecyclerViewOnClickListenerHack != null){
                mRecyclerViewOnClickListenerHack.onClickListener(v, getPosition());
            }

        }
    }
}
