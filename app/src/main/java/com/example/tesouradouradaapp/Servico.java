package com.example.tesouradouradaapp;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "servico")
public class Servico implements Parcelable {

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

    protected Servico(Parcel in) {
        idServico = in.readInt();
        nomeServico = in.readString();
        valor = in.readFloat();
        tempo = in.readInt();
    }

    public static final Creator<Servico> CREATOR = new Creator<Servico>() {
        @Override
        public Servico createFromParcel(Parcel in) {
            return new Servico(in);
        }

        @Override
        public Servico[] newArray(int size) {
            return new Servico[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(idServico);
        parcel.writeString(nomeServico);
        parcel.writeFloat(valor);
        parcel.writeInt(tempo);
    }
}
