package com.example.tesouradouradaapp.DataBase.DAOs;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.tesouradouradaapp.DataBase.Entities.Estabelecimento;

@Dao
public interface EstabelecimentoDao {

    @Insert
    void insertEstabelecimento(Estabelecimento estabelecimento);

    @Update
    void updateEstabelecimento(Estabelecimento estabelecimento);

    @Query("SELECT * FROM estabelecimento ORDER BY idEstabelecimento LIMIT 1")
    LiveData<Estabelecimento> getEstabelecimento();

    @Query("SELECT * FROM estabelecimento ORDER BY idEstabelecimento LIMIT 1")
    Estabelecimento getEstab();

}
