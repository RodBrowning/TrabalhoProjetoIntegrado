package com.example.tesouradouradaapp;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity(tableName = "servicos", indices = {@Index("id_servico")})
public class Servico implements Parcelable {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_servico")
    private int id_servico;

    private String nomeServico;
    private float valor;
    private long tempo;

    public Servico(String nomeServico, float valor, long tempo) {
        this.nomeServico = nomeServico;
        this.valor = valor;
        this.tempo = tempo;
    }

    protected Servico(Parcel in) {
        id_servico = in.readInt();
        nomeServico = in.readString();
        valor = in.readFloat();
        tempo = in.readLong();
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

    public void setId_servico(int id_servico) {
        this.id_servico = id_servico;
    }

    public int getId_servico() {
        return id_servico;
    }

    public String getNomeServico() {
        return nomeServico;
    }

    public float getValor() {
        return valor;
    }

    public long getTempo() {
        return tempo;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id_servico);
        parcel.writeString(nomeServico);
        parcel.writeFloat(valor);
        parcel.writeLong(tempo);
    }
}
