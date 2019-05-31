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
    Long insertAgendamento(Agendamento agendamento);

    @Update
    void updateAgendamento(Agendamento agendamento);

    @Delete
    void deleteAgendamento(Agendamento agendamento);

    @Query("SELECT * FROM agenda where id_agendamento = :id_agendamento")
    Agendamento getAgendamento(int id_agendamento);

    @Query("SELECT * FROM agenda ORDER BY horarioInicio ASC")
    LiveData<List<Agendamento>> getAllAgendamentos();

    @Query("SELECT * FROM agenda WHERE " +
            "horarioInicio < :horarioDeAbertura " +
            "AND " +
            "horarioFim > :horarioDeAbertura " +
            "LIMIT 1")
    Agendamento verSeExisteAgendamentoEmAndamento(long horarioDeAbertura);

    @Query("SELECT * FROM agenda WHERE horarioInicio >= :horarioDeAbertura " +
            "AND " +
            "(SELECT SUM(tempo)  FROM servicos INNER JOIN " +
            "agenda_servicos_join ON id_servico_join = id_servico " +
            "WHERE id_agendamento = id_agendamento_join) + horarioInicio <= :horarioFechamento " +
            "ORDER BY horarioInicio ASC")
    LiveData<List<Agendamento>> getAgendamentosMarcadosParaDataLiveData(long horarioDeAbertura, long horarioFechamento);

    @Query("SELECT * FROM agenda WHERE horarioInicio >= :horarioDeAbertuta " +
            "AND " +
            "(SELECT SUM(tempo)  FROM servicos INNER JOIN " +
            "agenda_servicos_join ON id_servico_join = id_servico " +
            "WHERE id_agendamento = id_agendamento_join) + horarioInicio <= :horarioFechamento " +
            "ORDER BY horarioInicio ASC")
    List<Agendamento> getAgendamentosMarcadosParaData(long horarioDeAbertuta, long horarioFechamento);


    @Query("SELECT * FROM agenda WHERE horarioInicio >= :horarioDeAbertuta " +
            "AND (" +
            "SELECT SUM(tempo)  FROM servicos INNER JOIN " +
            "agenda_servicos_join ON id_servico_join = id_servico " +
            "WHERE id_agendamento = id_agendamento_join) + horarioInicio <= :horarioFechamento " +
            "AND id_agendamento != :id_agendamento_para_atualizar " +
            "ORDER BY horarioInicio ASC")
    List<Agendamento> getAgendamentosMarcadosParaDataSemAgendamentoParaEditar(long horarioDeAbertuta, long horarioFechamento, int id_agendamento_para_atualizar);

}
