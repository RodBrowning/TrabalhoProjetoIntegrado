package com.example.tesouradouradaapp;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "agenda")
public class Agendamento {

    @PrimaryKey(autoGenerate = true)
    private int id_agendamento;

    private String cliente;
    private String horarioInicio;
    private String getHorarioFim;
    private ArrayList<Integer> servicos;

    public Agendamento(String cliente, String horarioInicio, String getHorarioFim, ArrayList<Integer> servicos) {
        this.cliente = cliente;
        this.horarioInicio = horarioInicio;
        this.getHorarioFim = getHorarioFim;
        this.servicos = servicos;
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

    public String getGetHorarioFim() {
        return getHorarioFim;
    }

    public ArrayList<Integer> getServicos() {
        return servicos;
    }
}

