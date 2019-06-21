package com.example.tesouradouradaapp.Repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.tesouradouradaapp.DataBase.CabelereiroDataBase;
import com.example.tesouradouradaapp.DataBase.Entities.Servico;
import com.example.tesouradouradaapp.DataBase.DAOs.ServicoDao;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ServicosRepository {
    private ServicoDao servicoDao;
    private LiveData<List<Servico>> servicos;
    private Servico servico;

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

    public Servico getServico(int id) throws ExecutionException, InterruptedException {
        servico = new GetServicoAsyncTask(servicoDao).execute(id).get();
        return servico;
    }

    //Async Tasks
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

    private static class GetServicoAsyncTask extends AsyncTask<Integer, Void, Servico> {
        private ServicoDao servicoDao;
        private Servico servico;

        public GetServicoAsyncTask(ServicoDao servicoDao) {
            this.servicoDao = servicoDao;
        }

        @Override
        protected Servico doInBackground(Integer... ints) {
            servico = servicoDao.getServico(ints[0]);
            return servico;
        }
    }
}
