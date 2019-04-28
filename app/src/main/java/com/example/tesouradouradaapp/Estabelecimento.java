package com.example.tesouradouradaapp;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "estabelecimento")
public class Estabelecimento {

    @PrimaryKey(autoGenerate = true)
    private int idEstabelecimento;

    @ColumnInfo(name = "estabelecimento")
    private String nomeEstabelecimento;
    @ColumnInfo(name = "proprietario")
    private String nomeProprietario;
    @ColumnInfo(name = "hr_abertura")
    private String horarioAbertura;
    @ColumnInfo(name = "hr_fechamento")
    private String horarioFechamento;

    public Estabelecimento(String nomeEstabelecimento, String nomeProprietario, String horarioAbertura, String horarioFechamento) {
        this.nomeEstabelecimento = nomeEstabelecimento;
        this.nomeProprietario = nomeProprietario;
        this.horarioAbertura = horarioAbertura;
        this.horarioFechamento = horarioFechamento;
    }

    public void setIdEstabelecimento(int idEstabelecimento) {
        this.idEstabelecimento = idEstabelecimento;
    }

    public int getIdEstabelecimento() {
        return idEstabelecimento;
    }

    public String getNomeEstabelecimento() {
        return nomeEstabelecimento;
    }

    public String getNomeProprietario() {
        return nomeProprietario;
    }

    public String getHorarioAbertura() {
        return horarioAbertura;
    }

    public String getHorarioFechamento() {
        return horarioFechamento;
    }
}
