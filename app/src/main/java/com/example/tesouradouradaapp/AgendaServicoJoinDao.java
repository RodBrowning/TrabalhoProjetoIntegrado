package com.example.tesouradouradaapp;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface AgendaServicoJoinDao {

    @Insert
    void insert(AgendaServicosJoin agendaServicosJoin);

    @Query("SELECT * FROM servicos " +
            "INNER JOIN " +
            "agenda_servicos_join ON " +
            "id_servico = id_servico_join " +
            "WHERE " +
            "id_agendamento_join = :id_agendamento")
    List<Servico> getServicosParaAgendamentoJoinServicos(int id_agendamento);

    @Query("SELECT * FROM agenda_servicos_join WHERE id_agendamento_join = :id_agendamento")
    List<AgendaServicosJoin> getServicosParaAgendamento(int id_agendamento);

    @Query("SELECT * FROM agenda_servicos_join WHERE id_servico_join = :id_servico")
    List<AgendaServicosJoin> getAgendamentosParaServico(int id_servico);

    @Query("DELETE FROM agenda_servicos_join WHERE id_agendamento_join = :id_agendamento")
    void deletarServicosDoAgendamentoParaEditar(long id_agendamento);


}
