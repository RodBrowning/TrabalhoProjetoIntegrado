package com.example.tesouradouradaapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ServicoViewModel extends AndroidViewModel {
    private ServicosRepository servicosRepository;
    private LiveData<List<Servico>> servicos;

    public ServicoViewModel(@NonNull Application application) {
        super(application);
        servicosRepository = new ServicosRepository(application);
        servicos = servicosRepository.getAllServicos();
    }

    public void insert(Servico servico) {
        servicosRepository.insert(servico);
    }

    public void update(Servico servico) {
        servicosRepository.update(servico);
    }

    public void delete(Servico servico) {
        servicosRepository.delete(servico);
    }

    public LiveData<List<Servico>> getAllServicos() {
        return servicos;
    }

    public Servico getServico(int id) throws ExecutionException, InterruptedException {
        return servicosRepository.getServico(id);
    }
}
