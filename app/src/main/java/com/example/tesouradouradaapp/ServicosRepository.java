package com.example.tesouradouradaapp;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class ServicosRepository {
    private ServicoDao servicoDao;
    private LiveData<List<Servico>> servicos;

    public ServicosRepository(Application application) {
        CabelereiroDataBase cabelereiroDataBase = CabelereiroDataBase.getInstance(application);
        servicoDao = cabelereiroDataBase.servicoDao();
        servicos = servicoDao.getAllServices();
    }

    public void insert(Servico servico) {
        new InsertServicoAsyncTask(servicoDao).execute(servico);
    }

    public void update(Servico servico) {
        new UpdateServicoAsyncTask(servicoDao).execute(servico);
    }

    public void delete(Servico servico) {
        new DeleteServicoAsyncTask(servicoDao).execute(servico);
    }

    public LiveData<List<Servico>> getAllServicos() {
        return servicos;
    }

    private static class InsertServicoAsyncTask extends AsyncTask<Servico, Void, Void> {
        private ServicoDao servicoDao;

        private InsertServicoAsyncTask(ServicoDao servicoDao) {
            this.servicoDao = servicoDao;
        }

        @Override
        protected Void doInBackground(Servico... servicos) {
            servicoDao.insertServico(servicos[0]);
            return null;
        }
    }

    private static class UpdateServicoAsyncTask extends AsyncTask<Servico, Void, Void> {
        private ServicoDao servicoDao;

        private UpdateServicoAsyncTask(ServicoDao servicoDao) {
            this.servicoDao = servicoDao;
        }

        @Override
        protected Void doInBackground(Servico... servicos) {
            servicoDao.updateServico(servicos[0]);
            return null;
        }
    }

    private static class DeleteServicoAsyncTask extends AsyncTask<Servico, Void, Void> {
        private ServicoDao servicoDao;

        private DeleteServicoAsyncTask(ServicoDao servicoDao) {
            this.servicoDao = servicoDao;
        }

        @Override
        protected Void doInBackground(Servico... servicos) {
            servicoDao.deleteServico(servicos[0]);
            return null;
        }
    }
}
