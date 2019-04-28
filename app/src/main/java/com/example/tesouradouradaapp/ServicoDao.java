package com.example.tesouradouradaapp;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ServicoDao {

    @Insert
    void insertServico(Servico servico);

    @Update
    void updateServico(Servico servico);

    @Delete
    void deleteServico(Servico servico);

    @Query("SELECT * FROM servico ORDER BY nomeServico ASC")
    LiveData<List<Servico>> getAllServices();

}
