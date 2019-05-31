package com.example.tesouradouradaapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class AgendaViewModel extends AndroidViewModel {
    private AgendaRepository agendaRepository;
    private LiveData<List<Agendamento>> agenda;

    public AgendaViewModel(@NonNull Application application) {
        super(application);
        agendaRepository = new AgendaRepository(application);
        agenda = agendaRepository.getAgenda();
    }

    public Long insert(Agendamento agendamento) throws ExecutionException, InterruptedException {
        return agendaRepository.insertAgendamento(agendamento);
    }

    public void update(Agendamento agendamento) {
        agendaRepository.updateAgendamento(agendamento);
    }

    public void delete(Agendamento agendamento) {
        agendaRepository.deleteAgendamento(agendamento);
    }

    public Agendamento getAgendamento(int id_agendamento) throws ExecutionException, InterruptedException {
        return agendaRepository.getAgendamento(id_agendamento);
    }

    public LiveData<List<Agendamento>> getAgenda() {
        return agenda;
    }

    public Agendamento verSeExisteAgendamentoEmAndamento(long horarioDeAbertura) throws ExecutionException, InterruptedException {
        return agendaRepository.verSeExisteAgendamentoEmAndamento(horarioDeAbertura);
    }

    public LiveData<List<Agendamento>> getAgendamentosMarcadosParaDataLiveData(long horarioDeAbertuta, long horarioFechamento){
        return agendaRepository.getAgendamentosMarcadosParaDataLiveData(horarioDeAbertuta, horarioFechamento);
    }

    public List<Agendamento> getAgendamentosMarcadosParaData(long horarioDeAbertuta, long horarioFechamento) throws ExecutionException, InterruptedException {
        return agendaRepository.getAgendamentosMarcadosParaData(horarioDeAbertuta, horarioFechamento);
    }

    public List<Agendamento> getAgendamentosMarcadosParaDataSemAgendamentoParaEditar(long horarioDeAbertuta, long horarioFechamento, int id_agendamento_para_atualizar) throws ExecutionException, InterruptedException {
        return agendaRepository.getAgendamentosMarcadosParaDataSemAgendamentoParaEditar(horarioDeAbertuta, horarioFechamento, id_agendamento_para_atualizar);
    }

}
