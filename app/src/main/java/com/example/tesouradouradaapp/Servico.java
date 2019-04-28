package com.example.tesouradouradaapp;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "servico")
public class Servico {

    @PrimaryKey(autoGenerate = true)
    private int idServico;

    private String nomeServico;
    private float valor;
    private int tempo;

    public Servico(String nomeServico, float valor, int tempo) {
        this.nomeServico = nomeServico;
        this.valor = valor;
        this.tempo = tempo;
    }

    public void setIdServico(int idServico) {
        this.idServico = idServico;
    }

    public int getIdServico() {
        return idServico;
    }

    public String getNomeServico() {
        return nomeServico;
    }

    public float getValor() {
        return valor;
    }

    public int getTempo() {
        return tempo;
    }
}
