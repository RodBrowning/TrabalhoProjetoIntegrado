package com.example.tesouradouradaapp;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "agenda")
public class Agendamento {

    @PrimaryKey(autoGenerate = true)
    private int id_agendamento;

    private String cliente;
    private long horarioInicio;
    private long horarioFim;
    private String servicos;
    private long criadoEm;

    public Agendamento(String cliente, long horarioInicio, long horarioFim, String servicos, long criadoEm) {
        this.cliente = cliente;
        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;
        this.servicos = servicos;
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

    public long getHorarioFim() {
        return horarioFim;
    }

    public String getServicos() {
        return servicos;
    }

    public long getCriadoEm() {
        return criadoEm;
    }


}

