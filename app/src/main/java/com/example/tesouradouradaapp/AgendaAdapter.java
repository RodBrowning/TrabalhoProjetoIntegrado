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
    public static final String ID_AGENDAMENTO = "com.example.tesouradouradaapp.ID_AGENDAMENTO";
    private List<Agendamento> agenda = new ArrayList<>();
    private Context mContext;
    private Application application;
    private AgendamentoRepository agendamentoRepository;
    private AgendaServicosJoinViewModel agendaServicosJoinViewModel = new AgendaServicosJoinViewModel(application);


    public AgendaAdapter(Context context) {
        this.mContext = context;
        this.agendamentoRepository = new AgendamentoRepository(mContext);
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
        long duracaoTotalDoAtendimento = (agendamento.getHorarioInicio()+ duracaoAgendamento) - agendamento.getHorarioInicio();
        int duracaoAgendamentoEmMinutos = converterMilisegundosParaMinutos(duracaoTotalDoAtendimento);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        agendamentoHolder.textViewDuracao.setText(duracaoAgendamentoEmMinutos + " min");
        agendamentoHolder.textViewNomeCliente.setText(agendamento.getCliente());
        agendamentoHolder.textViewDataMarcada.setText(sdf.format(agendamento.getHorarioInicio()));
        agendamentoHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext.getApplicationContext(), VizualizarAgendamento.class);
                intent.putExtra(ID_AGENDAMENTO, agendamento.getId_agendamento());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return agenda.size();
    }

    public void setAgenda(List<Agendamento> agenda) {
        this.agenda = agenda;
        notifyDataSetChanged();
    }

    public void notifyDataSetChangedServicos() {
        notifyDataSetChanged();
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    private int converterMilisegundosParaMinutos(long minutos) {
        Long longMinutos = new Long(minutos);
        int mins = (longMinutos.intValue() / 1000) / 60;
        return mins;
    }

    class AgendamentoHolder extends RecyclerView.ViewHolder {
        private TextView textViewNomeCliente;
        private TextView textViewDuracao;
        private TextView textViewDataMarcada;
        private RelativeLayout relativeLayout;


        public AgendamentoHolder(@NonNull View itemView) {
            super(itemView);
            textViewDuracao = itemView.findViewById(R.id.duracao_atendimento);
            textViewNomeCliente = itemView.findViewById(R.id.nome_cliente);
            textViewDataMarcada = itemView.findViewById(R.id.data_marcada);
            relativeLayout = itemView.findViewById(R.id.relative_layout_agenda_item);
        }
    }
}
