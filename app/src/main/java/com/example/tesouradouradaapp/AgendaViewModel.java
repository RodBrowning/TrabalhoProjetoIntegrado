package com.example.tesouradouradaapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class AgendaViewModel extends AndroidViewModel {
    private AgendamentoRepository agendamentoRepository;
    private LiveData<List<Agendamento>> agenda;

    public AgendaViewModel(@NonNull Application application) {
        super(application);
        agendamentoRepository = new AgendamentoRepository(application);
        agenda = agendamentoRepository.getAgenda();
    }

    public Long insert(Agendamento agendamento) throws ExecutionException, InterruptedException {
        return agendamentoRepository.insertAgendamento(agendamento);
    }

    public void update(Agendamento agendamento) {
        agendamentoRepository.updateAgendamento(agendamento);
    }

    public void delete(Agendamento agendamento) {
        agendamentoRepository.deleteAgendamento(agendamento);
    }

    public Agendamento getAgendamento(int id_agendamento) throws ExecutionException, InterruptedException {
        return agendamentoRepository.getAgendamento(id_agendamento);
    }

    public LiveData<List<Agendamento>> getAgenda() {
        return agenda;
    }

    public List<Agendamento> getAgendamentosMarcadosParaData(long horarioDeAbertuta, long horarioFechamento) throws ExecutionException, InterruptedException {
        return agendamentoRepository.getAgendamentosMarcadosParaData(horarioDeAbertuta, horarioFechamento);
    }

}
