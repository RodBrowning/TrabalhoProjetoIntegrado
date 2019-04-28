package com.example.tesouradouradaapp;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "agenda")
public class Agendamento {

    @PrimaryKey(autoGenerate = true)
    private int id_agendamento;

    private String cliente;
    private String horarioInicio;
    private String horarioFim;
    private String servicos;
    private String criadoEm;

    public Agendamento(String cliente, String horarioInicio, String horarioFim, String servicos, String criadoEm) {
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

    public String getHorarioInicio() {
        return horarioInicio;
    }

    public String getHorarioFim() {
        return horarioFim;
    }

    public String getServicos() {
        return servicos;
    }

    public String getCriadoEm() {
        return criadoEm;
    }


}

