package com.example.tesouradouradaapp;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "agenda", indices = {@Index("id_agendamento")})
public class Agendamento {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_agendamento")
    private int id_agendamento;

    private String cliente;
    private long horarioInicio;
    private long horarioFim;

    private long criadoEm;
    @Ignore
    private long duracao;

    public Agendamento(String cliente, long horarioInicio, long horarioFim, long criadoEm) {
        this.cliente = cliente;
        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;
        this.criadoEm = criadoEm;
        this.duracao = horarioFim - horarioInicio;
    }

    public void setId_agendamento(int id_agendamento) {
        this.id_agendamento = id_agendamento;
    }

    public int getId_agendamento() {
        return id_agendamento;
    }

    public String getCliente() {
        return cliente;
    }

    public long getHorarioInicio() {
        return horarioInicio;
    }

    public long getHorarioFim() {
        return horarioFim;
    }


    public long getCriadoEm() {
        return criadoEm;
    }


    public long getDuracaoEmMinutos() {
        return ((horarioFim - horarioInicio) / 1000)/60;
    }

}

