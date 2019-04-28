package com.example.tesouradouradaapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class AgendaViewModel extends AndroidViewModel {
    private AgendamentoRepository agendamentoRepository;
    private LiveData<List<Agendamento>> agenda;

    public AgendaViewModel(@NonNull Application application) {
        super(application);
        agendamentoRepository = new AgendamentoRepository(application);
        agenda = agendamentoRepository.getAgenda();
    }

    public void insert(Agendamento agendamento){
        agendamentoRepository.inserAgendamento(agendamento);
    }
    public void update(Agendamento agendamento){
        agendamentoRepository.updateAgendamento(agendamento);
    }
    public void delete(Agendamento agendamento){
        agendamentoRepository.deleteAgendamento(agendamento);
    }
    public LiveData<List<Agendamento>> getAgenda(){
        return agenda;
    }

}
