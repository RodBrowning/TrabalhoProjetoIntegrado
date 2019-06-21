package com.example.tesouradouradaapp.DataBase.DAOs;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.tesouradouradaapp.DataBase.Entities.Servico;

import java.util.List;

@Dao
public interface ServicoDao {

    @Insert
    void insertServico(Servico servico);

    @Update
    void updateServico(Servico servico);

    @Delete
    void deleteServico(Servico servico);

    @Query("SELECT * FROM servicos ORDER BY nomeServico ASC")
    LiveData<List<Servico>> getAllServices();

    @Query("SELECT * FROM servicos WHERE id_servico = :id")
    Servico getServico(int id);
}
