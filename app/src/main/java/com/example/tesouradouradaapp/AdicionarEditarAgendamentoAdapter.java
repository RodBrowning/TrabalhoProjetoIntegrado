package com.example.tesouradouradaapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdicionarEditarAgendamentoAdapter extends RecyclerView.Adapter<AdicionarEditarAgendamentoAdapter.AdicionarEditarAgendamentoHolder> {

    private List<Servico> servicosSelecionados;

    @NonNull
    @Override
    public AdicionarEditarAgendamentoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.servicos_selecionados, viewGroup, false);
        return new AdicionarEditarAgendamentoAdapter.AdicionarEditarAgendamentoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdicionarEditarAgendamentoHolder adicionarEditarAgendamentoHolder, int i) {
        Servico servico = servicosSelecionados.get(i);
        Locale brasil = new Locale("pt", "BR");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(brasil);
        adicionarEditarAgendamentoHolder.textViewNomeServico.setText(servico.getNomeServico());
        adicionarEditarAgendamentoHolder.textViewDuracaoAtendimento.setText(duracaoTotalParaApresentacao(servico.getTempo()));
        adicionarEditarAgendamentoHolder.textViewValorAtendimento.setText(numberFormat.format(servico.getValor()));
    }

    @Override
    public int getItemCount() {
        return servicosSelecionados.size();
    }

    public void setServicosSelecionados(List<Servico> servicosSelecionados) {
        this.servicosSelecionados = servicosSelecionados;
    }

    class AdicionarEditarAgendamentoHolder extends RecyclerView.ViewHolder {
        private TextView textViewNomeServico;
        private TextView textViewDuracaoAtendimento;
        private TextView textViewValorAtendimento;

        public AdicionarEditarAgendamentoHolder(@NonNull View itemView) {
            super(itemView);
            textViewNomeServico = itemView.findViewById(R.id.text_view_nome_servico_selecionado);
            textViewDuracaoAtendimento = itemView.findViewById(R.id.text_view_duracao_servico_selecionado);
            textViewValorAtendimento = itemView.findViewById(R.id.text_view_valor_servico_selecionado);
        }
    }
    public long converterMilisegundosParaMinutos(long minutos) {
        Long longMinutos = new Long(minutos);
        long mins = (longMinutos.intValue() / 1000) / 60;
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
}
