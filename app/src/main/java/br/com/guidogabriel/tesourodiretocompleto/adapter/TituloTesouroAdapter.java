package br.com.guidogabriel.tesourodiretocompleto.adapter;

import android.app.Activity;
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
 * Created by guido_000 on 01/05/2015.
 */
public class TituloTesouroAdapter extends RecyclerView.Adapter<TituloTesouroAdapter.TituloTesouroViewHolder> {
    private List<TituloTesouro> mList;
    private LayoutInflater mLayoutInflater;


    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

    public TituloTesouroAdapter(Activity activity, List<TituloTesouro> mList) {
        this.mList = mList;
        this.mLayoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public TituloTesouroViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.item_titulo_tesouro, viewGroup, false);
        TituloTesouroViewHolder mvh = new TituloTesouroViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(TituloTesouroViewHolder tituloTesouroViewHolder, int position) {
        tituloTesouroViewHolder.nome.setText(mList.get(position).getNome());
        tituloTesouroViewHolder.preco_compra.setText(mList.get(position).getPrecoCompra());
        tituloTesouroViewHolder.taxa_compra.setText(mList.get(position).getTaxaCompra());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r) {
        this.mRecyclerViewOnClickListenerHack = r;
    }

    public void addListItem(TituloTesouro t, int position){
        mList.add(t);
        notifyItemInserted(position);
    }

    public void removeListItem(int position){
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public class TituloTesouroViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nome;
        public TextView preco_compra;
        public TextView taxa_compra;
        public TituloTesouroViewHolder (View itemView) {
            super(itemView);
            nome = (TextView) itemView.findViewById(R.id.tv_nome_titulo);
            preco_compra = (TextView) itemView.findViewById(R.id.tv_preco_compra);
            taxa_compra = (TextView) itemView.findViewById(R.id.tv_taxa_compra);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mRecyclerViewOnClickListenerHack != null){
                mRecyclerViewOnClickListenerHack.onClickListener(v,getPosition());
            }
        }
    }
}

