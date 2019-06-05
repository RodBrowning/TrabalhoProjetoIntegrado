package com.example.tesouradouradaapp;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.AgendamentoHolder> {
    public static final String ID_AGENDAMENTO_EDITAR = "com.example.tesouradouradaapp.ID_AGENDAMENTO_EDITAR";
    private List<Agendamento> agenda = new ArrayList<>();
    private Context mContext;
    private Application application;
    private AgendaRepository agendaRepository;
    private AgendaServicosJoinViewModel agendaServicosJoinViewModel = new AgendaServicosJoinViewModel(application);
    private Boolean existeAgendamentoEmAndamento = false;
    private OnItemClickListener listener;


    public AgendaAdapter(Context context) {
        this.mContext = context;
        this.agendaRepository = new AgendaRepository(mContext);
    }


    @NonNull
    @Override
    public AgendamentoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.agendamento, viewGroup, false);
        return new AgendamentoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AgendamentoHolder agendamentoHolder, int i) {
        final Agendamento agendamento = agenda.get(i);
        long duracaoAgendamento = 0;
        try {
            List<Servico> listaServicos = agendaServicosJoinViewModel.getServicosParaAgendamentoJoinServicos(agendamento.getId_agendamento());
            for (Servico listServico : listaServicos) {
                duracaoAgendamento += new Long(listServico.getTempo());
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (existeAgendamentoEmAndamento && i == 0) {
            agendamentoHolder.textViewAgendamentoEmAndamento.setVisibility(View.VISIBLE);
        }
        long duracaoTotalDoAtendimento = (agendamento.getHorarioInicio() + duracaoAgendamento) - agendamento.getHorarioInicio();
        String duracaoAgendamentoEmMinutos = duracaoTotalParaApresentacao(duracaoTotalDoAtendimento);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        agendamentoHolder.textViewDuracao.setText(duracaoAgendamentoEmMinutos);
        agendamentoHolder.textViewNomeCliente.setText(agendamento.getCliente());
        agendamentoHolder.textViewDataMarcada.setText(sdf.format(agendamento.getHorarioInicio()));
    }

    @Override
    public int getItemCount() {
        return agenda.size();
    }

    public void setAgenda(List<Agendamento> agenda) {
        this.agenda = agenda;
        notifyDataSetChanged();
    }

    public void setExisteAgendamentoEmAndamento(Boolean existeAgendamentoEmAndamento) {
        this.existeAgendamentoEmAndamento = existeAgendamentoEmAndamento;
    }


    public void setApplication(Application application) {
        this.application = application;
    }

    private int converterMilisegundosParaMinutos(long milisegundos) {
        Long longMilisegundos = milisegundos;
        int mins = (longMilisegundos.intValue() / 1000) / 60;
        return mins;
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

    class AgendamentoHolder extends RecyclerView.ViewHolder {
        private TextView textViewNomeCliente;
        private TextView textViewDuracao;
        private TextView textViewDataMarcada;
        private TextView textViewAgendamentoEmAndamento;


        public AgendamentoHolder(@NonNull View itemView) {
            super(itemView);
            textViewDuracao = itemView.findViewById(R.id.duracao_atendimento);
            textViewNomeCliente = itemView.findViewById(R.id.nome_cliente);
            textViewDataMarcada = itemView.findViewById(R.id.data_marcada);
            textViewAgendamentoEmAndamento = itemView.findViewById(R.id.agendamento_em_andamento);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(agenda.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Agendamento agendamento);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
