package com.example.tesouradouradaapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class EstabelecimentoViewModel extends AndroidViewModel {
    private EstabelecimentoRepository estabelecimentoRepository;
    private LiveData<Estabelecimento> estabelecimento;

    public EstabelecimentoViewModel(@NonNull Application application) {
        super(application);
        estabelecimentoRepository = new EstabelecimentoRepository(application);
        estabelecimento = estabelecimentoRepository.getEstabelecimento();
    }

    public void insert(Estabelecimento estabelecimento) {
        estabelecimentoRepository.insert(estabelecimento);
    }

    public void update(Estabelecimento estabelecimento) {
        estabelecimentoRepository.update(estabelecimento);
    }

    public LiveData<Estabelecimento> getEstabelecimento() {
        return estabelecimento;
    }

    public Estabelecimento getEstab() throws ExecutionException, InterruptedException, ExecutionException {
        return estabelecimentoRepository.getEstab();
    }
}
