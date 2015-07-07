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

/**
 * Created by guido_000 on 19/05/2015.
 */
public class HistoricoTituloAdapter extends RecyclerView.Adapter<HistoricoTituloAdapter.HistoricoTituloViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private final List<TituloTesouro> mList;

    public HistoricoTituloAdapter(Context context, List<TituloTesouro> mList) {
        this.mList = mList;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public HistoricoTituloViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.item_historico, parent, false);
        HistoricoTituloViewHolder historicoTituloViewHolder = new HistoricoTituloViewHolder(v);
        return historicoTituloViewHolder;
    }

    @Override
    public void onBindViewHolder(HistoricoTituloViewHolder holder, int position) {
        holder.data.setText(mList.get(position).getDataUltimaAtualizacao());
        holder.preco_compra.setText(mList.get(position).getPrecoCompra());
        holder.preco_venda.setText(mList.get(position).getPrecoVenda());
        holder.taxa_compra.setText(mList.get(position).getTaxaCompra());
        holder.taxa_venda.setText(mList.get(position).getTaxaVenda());


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class HistoricoTituloViewHolder extends RecyclerView.ViewHolder {
        public TextView data, preco_compra, preco_venda, taxa_compra, taxa_venda;

        public HistoricoTituloViewHolder(View itemView) {
            super(itemView);

            data = (TextView) itemView.findViewById(R.id.tv_data);
            preco_compra = (TextView) itemView.findViewById(R.id.tv_preco_compra);
            preco_venda = (TextView) itemView.findViewById(R.id.tv_preco_venda);
            taxa_compra = (TextView) itemView.findViewById(R.id.tv_taxa_compra);
            taxa_venda = (TextView) itemView.findViewById(R.id.tv_taxa_venda);


        }
    }
}
