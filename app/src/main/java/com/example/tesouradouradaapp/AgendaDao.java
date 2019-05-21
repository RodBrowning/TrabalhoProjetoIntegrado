package com.example.tesouradouradaapp;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface AgendaDao {

    @Insert
    void insertAgendamento(Agendamento agendamento);

    @Update
    void updateAgendamento(Agendamento agendamento);

    @Delete
    void deleteAgendamento(Agendamento agendamento);

    @Query("SELECT * FROM agenda ORDER BY horarioInicio ASC")
    LiveData<List<Agendamento>> getAllAgendamentos();

    @Query("SELECT * FROM agenda WHERE horarioInicio >= :horarioDeAbertuta AND (" +
            "SELECT SUM(tempo) + horarioInicio FROM servicos INNER JOIN " +
            "agenda_servicos_join ON id_servico_join = id_servico " +
            "WHERE id_servico = id_servico_join) <= :horarioFechamento ORDER BY horarioInicio ASC")
    List<Agendamento> getAgendamentosMarcadosParaData(long horarioDeAbertuta, long horarioFechamento);

}
