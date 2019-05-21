package com.example.tesouradouradaapp;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "agenda_servicos_join",
        indices = {
                @Index(value = {"id_agendamento_join", "id_servico_join"})},
        foreignKeys = {
                @ForeignKey(entity = Servico.class, parentColumns = "id_servico", childColumns = "id_servico_join"),
                @ForeignKey(entity = Agendamento.class, parentColumns = "id_agendamento", childColumns = "id_agendamento_join")

        })
public class AgendaServicosJoin {

    @PrimaryKey(autoGenerate = true)
    private int idAgendaServicosJoin;

    @ColumnInfo(name = "id_agendamento_join")
    private int idAgendamentoJoin;

    @ColumnInfo(name = "id_servico_join")
    private int idServicoJoin;

    public AgendaServicosJoin(int idAgendamentoJoin, int idServicoJoin) {
        this.idAgendamentoJoin = idAgendamentoJoin;
        this.idServicoJoin = idServicoJoin;
    }

    public void setIdAgendaServicosJoin(int idAgendaServicosJoin) {
        this.idAgendaServicosJoin = idAgendaServicosJoin;
    }

    public int getIdAgendaServicosJoin() {
        return idAgendaServicosJoin;
    }

    public int getIdAgendamentoJoin() {
        return idAgendamentoJoin;
    }

    public int getIdServicoJoin() {
        return idServicoJoin;
    }
}
