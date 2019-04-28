package com.example.tesouradouradaapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.AgendamentoHolder> {
    private List<Agendamento> agenda = new ArrayList<>();
    @NonNull
    @Override
    public AgendamentoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.agendamento, viewGroup, false);
        return new AgendamentoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AgendamentoHolder agendamentoHolder, int i) {
        Agendamento agendamento = agenda.get(i);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        agendamentoHolder.textViewDuracao.setText(agendamento.getDuracaoEmMinutos() +" min");
        agendamentoHolder.textViewNomeCliente.setText(agendamento.getCliente());
        agendamentoHolder.textViewDataMarcada.setText(sdf.format(agendamento.getHorarioInicio()));
    }

    @Override
    public int getItemCount() {
        return agenda.size();
    }

    public void setAgenda(List<Agendamento> agenda){
        this.agenda = agenda;
        notifyDataSetChanged();

    }


    class AgendamentoHolder extends RecyclerView.ViewHolder{
        private TextView textViewNomeCliente;
        private TextView textViewDuracao;
        private TextView textViewDataMarcada;


        public AgendamentoHolder(@NonNull View itemView) {
            super(itemView);
            textViewDuracao = itemView.findViewById(R.id.duracao_atendimento);
            textViewNomeCliente = itemView.findViewById(R.id.nome_cliente);
            textViewDataMarcada = itemView.findViewById(R.id.data_marcada);
        }
    }
}
