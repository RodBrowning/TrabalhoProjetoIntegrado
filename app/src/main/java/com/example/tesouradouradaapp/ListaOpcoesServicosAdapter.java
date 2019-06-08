package com.example.tesouradouradaapp;

import android.app.Application;
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
import java.util.concurrent.ExecutionException;

public class ListaOpcoesServicosAdapter extends RecyclerView.Adapter<ListaOpcoesServicosAdapter.OpcoesServicosHolder> {
    private Context mContext;
    private Application application;
    private List<Servico> servicos = new ArrayList<>();
    public ArrayList<Servico> servicosSelecionados = new ArrayList<>();
    private int id_agendamento_atualizar = 0;
    private List<Servico> listaServicosParaAtualizar = new ArrayList<>();
    private AgendaServicosJoinViewModel agendaServicosJoinViewModel = new AgendaServicosJoinViewModel(application);

    public ListaOpcoesServicosAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public OpcoesServicosHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.lista_opcoes_servicos, viewGroup, false);
        return new OpcoesServicosHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final OpcoesServicosHolder opcoesServicosHolder, final int i) {
        final Servico servico = servicos.get(i);
        Locale brasil = new Locale("pt", "BR");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(brasil);

        if (id_agendamento_atualizar > 0 ){
            try {
                listaServicosParaAtualizar = agendaServicosJoinViewModel.getServicosParaAgendamentoJoinServicos(id_agendamento_atualizar);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (Servico servicoSelecionadoParaAtualizar : listaServicosParaAtualizar) {
                if(servicoSelecionadoParaAtualizar.getNomeServico().equals(servico.getNomeServico())){
                    opcoesServicosHolder.checkBoxNomeServico.setChecked(true);
                    servicosSelecionados.add(servico);
                }
            }
        }
        opcoesServicosHolder.checkBoxNomeServico.setText(servico.getNomeServico());
        opcoesServicosHolder.textViewDuracaoAtendimento.setText(duracaoTotalParaApresentacao(servico.getTempo()));
        opcoesServicosHolder.textViewValorAtendimento.setText(numberFormat.format(servico.getValor()));
        opcoesServicosHolder.checkBoxNomeServico.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (opcoesServicosHolder.checkBoxNomeServico.isChecked()) {
                    servicosSelecionados.add(servico);
                } else {
                    servicosSelecionados.remove(servico);
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

    public void setApplication(Application application) {
        this.application = application;
    }

    public void setId_agendamento_atualizar(int id_agendamento_atualizar) {
        this.id_agendamento_atualizar = id_agendamento_atualizar;
    }

    private String duracaoTotalParaApresentacao(long milisegundos) {
        long duracaoTotal = milisegundos;
        long duracaohoras = duracaoTotal / 1000 / 60 / 60;
        long duracaoMinutos = (duracaoTotal / 1000 / 60) % 60;
        String duracaoString;

        if (duracaohoras > 0) {
            if (duracaohoras == 1) {
                if (duracaoMinutos == 0) {
                    duracaoString = String.format("%d hr", duracaohoras);
                } else {
                    duracaoString = String.format("%d hr %02d mins", duracaohoras, duracaoMinutos);
                }
            } else {
                if (duracaoMinutos == 0) {
                    duracaoString = String.format("%d hrs", duracaohoras);
                } else {
                    duracaoString = String.format("%d hrs %02d mins", duracaohoras, duracaoMinutos);
                }
            }
        } else {
            duracaoString = String.format("%d mins", duracaoMinutos);
        }
        return duracaoString;
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
