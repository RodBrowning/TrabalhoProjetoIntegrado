package com.example.tesouradouradaapp;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "agenda", indices = {@Index("id_agendamento")})
public class Agendamento {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_agendamento")
    private int id_agendamento;

    private String cliente;
    private long horarioInicio;

    private long criadoEm;
    @Ignore
    private long duracao;

    public Agendamento(String cliente, long horarioInicio, long criadoEm) {
        this.cliente = cliente;
        this.horarioInicio = horarioInicio;
        this.criadoEm = criadoEm;
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

    public long getCriadoEm() {
        return criadoEm;
    }

}

