package com.example.tesouradouradaapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListaOpcoesServicosAdapter extends RecyclerView.Adapter<ListaOpcoesServicosAdapter.OpcoesServicosHolder> {
    private Context mContext;
    private List<Servico> servicos = new ArrayList<>();
    public ArrayList<Servico> servicosSelecionados = new ArrayList<>();

    public ListaOpcoesServicosAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public OpcoesServicosHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.listaopcoesservicos, viewGroup, false);
        return new OpcoesServicosHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final OpcoesServicosHolder opcoesServicosHolder, final int i) {
        final Servico servico = servicos.get(i);
        Locale brasil = new Locale("pt", "BR");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(brasil);

        opcoesServicosHolder.checkBoxNomeServico.setText(servico.getNomeServico());
        opcoesServicosHolder.textViewDuracaoAtendimento.setText(String.valueOf(servico.getTempo()) + " min");
        opcoesServicosHolder.textViewValorAtendimento.setText(numberFormat.format(servico.getValor()));
        opcoesServicosHolder.checkBoxNomeServico.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (opcoesServicosHolder.checkBoxNomeServico.isChecked()) {
                    servicosSelecionados.add(servico);
                    Toast.makeText(mContext, servico.getNomeServico() + " adicionado", Toast.LENGTH_SHORT).show();
                } else {
                    servicosSelecionados.remove(servico);
                    Toast.makeText(mContext, servico.getNomeServico() + " removido", Toast.LENGTH_SHORT).show();
                }
            }
        });
        opcoesServicosHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (opcoesServicosHolder.checkBoxNomeServico.isChecked()) {
                    opcoesServicosHolder.checkBoxNomeServico.setChecked(false);
                } else {
                    opcoesServicosHolder.checkBoxNomeServico.setChecked(true);
                }
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

    class OpcoesServicosHolder extends RecyclerView.ViewHolder {

        private CheckBox checkBoxNomeServico;
        private TextView textViewDuracaoAtendimento;
        private TextView textViewValorAtendimento;
        private RelativeLayout relativeLayout;

        public OpcoesServicosHolder(@NonNull View itemView) {
            super(itemView);
            checkBoxNomeServico = itemView.findViewById(R.id.checkboxOpcaoServico);
            textViewDuracaoAtendimento = itemView.findViewById(R.id.text_view_duracao_servico);
            textViewValorAtendimento = itemView.findViewById(R.id.text_view_valor);
            relativeLayout = itemView.findViewById(R.id.relative_layout_servico_item);
        }
    }
}
