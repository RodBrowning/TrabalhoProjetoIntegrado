package com.example.tesouradouradaapp;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

@Entity(tableName = "agenda_servicos_join",
    primaryKeys = {
        "id_agendamento_join","id_agendamento_join"
    },
    foreignKeys = {
     @ForeignKey(entity = Agendamento.class,
        parentColumns = "id_agendamento",
        childColumns = "id_agendamento_join"),
     @ForeignKey(entity = Servico.class,
     parentColumns = "id_sevico",
     childColumns = "id_servico_join")
    })
public class AgendaServicosJoin {
    private int id_agendamento_join;
    private int id_servico_join;

    public AgendaServicosJoin(int id_agendamento_join, int id_servico_join) {
        this.id_agendamento_join = id_agendamento_join;
        this.id_servico_join = id_servico_join;
    }
}
