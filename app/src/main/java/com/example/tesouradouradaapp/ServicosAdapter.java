package com.example.tesouradouradaapp;

import android.content.Context;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ServicosAdapter extends RecyclerView.Adapter<ServicosAdapter.ServicosHolder> {
    private List<Servico> servicos = new ArrayList<>();
    private Context mContext;

    public ServicosAdapter(Context context){
        this.mContext = context;
    }
    @NonNull
    @Override
    public ServicosHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.servico, viewGroup, false);
        return new ServicosHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ServicosHolder servicosHolder, int i) {
        final Servico servico = servicos.get(i);
        Locale brasil = new Locale("pt", "BR");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(brasil);
        servicosHolder.textViewNomeServico.setText(servico.getNomeServico());
        servicosHolder.textViewDuracaoAtendimento.setText(String.valueOf(servico.getTempo()+" min"));
        servicosHolder.textViewValorAtendimento.setText(numberFormat.format(servico.getValor()));
        servicosHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(mContext, servicosHolder.relativeLayout);
                popupMenu.inflate(R.menu.menu_list_item_servicos);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.menu_editar_servico:
                                Toast.makeText(mContext, "Editar Serviço " + servico.getNomeServico(), Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.menu_excluir_servico:
                                Toast.makeText(mContext, "Excluir Serviço "+ servico.getNomeServico(), Toast.LENGTH_SHORT).show();
                                return true;
                                default:
                                    break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return servicos.size();
    }

    public void setServicos(List<Servico> servicos) {
        this.servicos = servicos;
        notifyDataSetChanged();
    }

    class ServicosHolder extends RecyclerView.ViewHolder{
        private TextView textViewNomeServico;
        private TextView textViewDuracaoAtendimento;
        private TextView textViewValorAtendimento;
        private RelativeLayout relativeLayout;
        public ServicosHolder(@NonNull View itemView) {
            super(itemView);
            textViewNomeServico = itemView.findViewById(R.id.text_view_servico);
            textViewDuracaoAtendimento = itemView.findViewById(R.id.text_view_duracao_servico);
            textViewValorAtendimento = itemView.findViewById(R.id.text_view_valor);
            relativeLayout = itemView.findViewById(R.id.relative_layout_servico_item);
        }
    }
}
