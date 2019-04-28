package com.example.tesouradouradaapp;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class AgendamentoRepository {
    private AgendaDao agendaDao;
    private LiveData<List<Agendamento>> agenda;

    public AgendamentoRepository(Application application){
        CabelereiroDataBase cabelereiroDataBase = CabelereiroDataBase.getInstance(application);
        agendaDao = cabelereiroDataBase.agendaDao();
        agenda = agendaDao.getAllAgendamentos();
    }

    public void inserAgendamento(Agendamento agendamento){
        new InsertAgendamentoAsyncTask(agendaDao).execute(agendamento);
    }
    public void updateAgendamento(Agendamento agendamento){
        new UpdateAgendamentoAsyncTask(agendaDao).execute(agendamento);
    }
    public void deleteAgendamento(Agendamento agendamento){
        new DeleteAgendamentoAsyncTask(agendaDao).execute(agendamento);
    }
    public LiveData<List<Agendamento>> getAgenda(){
        return agenda;
    }

    private static class InsertAgendamentoAsyncTask extends AsyncTask<Agendamento,Void,Void>{
        private AgendaDao agendaDao;

        private InsertAgendamentoAsyncTask(AgendaDao agendaDao) {
            this.agendaDao = agendaDao;
        }

        @Override
        protected Void doInBackground(Agendamento... agendamentos) {
            agendaDao.insertAgendamento(agendamentos[0]);
            return null;
        }
    }
    private static class UpdateAgendamentoAsyncTask extends AsyncTask<Agendamento,Void,Void>{
        private AgendaDao agendaDao;

        private UpdateAgendamentoAsyncTask(AgendaDao agendaDao) {
            this.agendaDao = agendaDao;
        }

        @Override
        protected Void doInBackground(Agendamento... agendamentos) {
            agendaDao.updateAgendamento(agendamentos[0]);
            return null;
        }
    }
    private static class DeleteAgendamentoAsyncTask extends AsyncTask<Agendamento,Void,Void>{
        private AgendaDao agendaDao;

        private DeleteAgendamentoAsyncTask(AgendaDao agendaDao) {
            this.agendaDao = agendaDao;
        }

        @Override
        protected Void doInBackground(Agendamento... agendamentos) {
            agendaDao.deleteAgendamento(agendamentos[0]);
            return null;
        }
    }
}
