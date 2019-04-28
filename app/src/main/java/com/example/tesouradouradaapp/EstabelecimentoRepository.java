package com.example.tesouradouradaapp;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;


public class EstabelecimentoRepository {
    private EstabelecimentoDao estabelecimentoDao;
    private LiveData<Estabelecimento> estabelecimento;

    public EstabelecimentoRepository(Application application){
        CabelereiroDataBase cabelereiroDataBase = CabelereiroDataBase.getInstance(application);
        estabelecimentoDao = cabelereiroDataBase.estabelecimentoDao();
        estabelecimento = estabelecimentoDao.getEstabelecimento();
    }

    public void insert(Estabelecimento estabelecimento){
        new InsertEstabelecimentoAsyncTask(estabelecimentoDao).execute(estabelecimento);
    }
    public  void update(Estabelecimento estabelecimento){
        new UpdateEstabelecimentoAsyncTask(estabelecimentoDao).execute(estabelecimento);
    }

    public LiveData<Estabelecimento> getEstabelecimento(){
        return estabelecimento;
    }

    private static class InsertEstabelecimentoAsyncTask extends AsyncTask<Estabelecimento, Void, Void>{
        private EstabelecimentoDao estabelecimentoDao;

        private InsertEstabelecimentoAsyncTask(EstabelecimentoDao estabelecimentoDao){
            this.estabelecimentoDao = estabelecimentoDao;
        }

        @Override
        protected Void doInBackground(Estabelecimento... estabelecimentos) {
            estabelecimentoDao.insertEstabelecimento(estabelecimentos[0]);
            return null;
        }
    }

    private static class UpdateEstabelecimentoAsyncTask extends AsyncTask<Estabelecimento,Void,Void>{
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
}
