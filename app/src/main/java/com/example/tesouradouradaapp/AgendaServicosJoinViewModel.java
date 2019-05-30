package com.example.tesouradouradaapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class AgendaServicosJoinViewModel extends AndroidViewModel {
    private AgendaServicosJoinRepository agendaServicosJoinRepository;
    public AgendaServicosJoinViewModel(@NonNull Application application) {
        super(application);
        agendaServicosJoinRepository = new AgendaServicosJoinRepository(application);
    }

    public void insert(AgendaServicosJoin agendaServicosJoin){
        agendaServicosJoinRepository.inserAgendaServicosJoin(agendaServicosJoin);
    }

    public  void deletarServicosDoAgendamentoParaEditar(long id_agendamnto){
        agendaServicosJoinRepository.deletarServicosDoAgendamentoParaEditar(id_agendamnto);
    }

    public List<Servico> getServicosParaAgendamentoJoinServicos(int id_agendamento) throws ExecutionException, InterruptedException {
        return agendaServicosJoinRepository.getServicosParaAgendamentoJoinServicos(id_agendamento);
    }

    public List<AgendaServicosJoin> getAgendamentosParaServico(int id_servico) throws ExecutionException, InterruptedException {
        return agendaServicosJoinRepository.getAgendamentosParaServico(id_servico);
    }

    public  List<AgendaServicosJoin> getServicosParaAgendamento(int id_agendamento) throws ExecutionException, InterruptedException {
        return agendaServicosJoinRepository.getServicosParaAgendamento(id_agendamento);
    }
}
