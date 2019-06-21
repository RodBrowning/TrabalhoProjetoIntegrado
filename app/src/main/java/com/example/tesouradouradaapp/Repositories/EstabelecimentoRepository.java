package com.example.tesouradouradaapp.Repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.tesouradouradaapp.DataBase.CabelereiroDataBase;
import com.example.tesouradouradaapp.DataBase.Entities.Estabelecimento;
import com.example.tesouradouradaapp.DataBase.DAOs.EstabelecimentoDao;

import java.util.concurrent.ExecutionException;


public class EstabelecimentoRepository {
    private EstabelecimentoDao estabelecimentoDao;
    private LiveData<Estabelecimento> estabelecimento;
    private Estabelecimento estab;

    public EstabelecimentoRepository(Application application) {
        CabelereiroDataBase cabelereiroDataBase = CabelereiroDataBase.getInstance(application);
        estabelecimentoDao = cabelereiroDataBase.estabelecimentoDao();
        estabelecimento = estabelecimentoDao.getEstabelecimento();
    }

    public void insert(Estabelecimento estabelecimento) {
        new InsertEstabelecimentoAsyncTask(estabelecimentoDao).execute(estabelecimento);
    }

    public void update(Estabelecimento estabelecimento) {
        new UpdateEstabelecimentoAsyncTask(estabelecimentoDao).execute(estabelecimento);
    }

    public LiveData<Estabelecimento> getEstabelecimento() {
        return estabelecimento;
    }

    public Estabelecimento getEstab() throws ExecutionException, InterruptedException {
        estab = new GetEstabAsyncTask(estabelecimentoDao).execute().get();
        return estab;
    }

    private static class InsertEstabelecimentoAsyncTask extends AsyncTask<Estabelecimento, Void, Void> {
        private EstabelecimentoDao estabelecimentoDao;

        private InsertEstabelecimentoAsyncTask(EstabelecimentoDao estabelecimentoDao) {
            this.estabelecimentoDao = estabelecimentoDao;
        }

        @Override
        protected Void doInBackground(Estabelecimento... estabelecimentos) {
            estabelecimentoDao.insertEstabelecimento(estabelecimentos[0]);
            return null;
        }
    }

    private static class UpdateEstabelecimentoAsyncTask extends AsyncTask<Estabelecimento, Void, Void> {
        private EstabelecimentoDao estabelecimentoDao;

        private UpdateEstabelecimentoAsyncTask(EstabelecimentoDao estabelecimentoDao) {
            this.estabelecimentoDao = estabelecimentoDao;
        }

        @Override
        protected Void doInBackground(Estabelecimento... estabelecimentos) {
            estabelecimentoDao.updateEstabelecimento(estabelecimentos[0]);
            return null;
        }
    }

    private static class GetEstabAsyncTask extends AsyncTask<Void, Void, Estabelecimento> {
        private EstabelecimentoDao estabelecimentoDao;
        private Estabelecimento estabelecimento;

        public GetEstabAsyncTask(EstabelecimentoDao estabelecimentoDao) {
            this.estabelecimentoDao = estabelecimentoDao;
        }

        @Override
        protected Estabelecimento doInBackground(Void... voids) {
            estabelecimento = estabelecimentoDao.getEstab();
            return estabelecimento;
        }
    }
}



