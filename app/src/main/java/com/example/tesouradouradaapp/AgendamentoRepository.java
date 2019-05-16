package com.example.tesouradouradaapp;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class AgendamentoRepository {
    private AgendaDao agendaDao;
    private LiveData<List<Agendamento>> agenda;
    private List<Agendamento> agendamentosParaDia;

    public AgendamentoRepository(Context context) {
        CabelereiroDataBase cabelereiroDataBase = CabelereiroDataBase.getInstance(context);
        agendaDao = cabelereiroDataBase.agendaDao();
        agenda = agendaDao.getAllAgendamentos();
    }

    public void inserAgendamento(Agendamento agendamento) {
        new InsertAgendamentoAsyncTask(agendaDao).execute(agendamento);
    }

    public void updateAgendamento(Agendamento agendamento) {
        new UpdateAgendamentoAsyncTask(agendaDao).execute(agendamento);
    }

    public void deleteAgendamento(Agendamento agendamento) {
        new DeleteAgendamentoAsyncTask(agendaDao).execute(agendamento);
    }

    public LiveData<List<Agendamento>> getAgenda() {
        return agenda;
    }

    public List<Agendamento> getAgendamentosMarcadosParaData(long horarioDeAbertuta, long horarioFechamento) throws ExecutionException, InterruptedException {
        agendamentosParaDia = new GetAgendamentosMarcadosParaDataAsyncTask(agendaDao).execute(horarioDeAbertuta, horarioFechamento).get();
        return agendamentosParaDia;
    }

    private static class InsertAgendamentoAsyncTask extends AsyncTask<Agendamento, Void, Void> {
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

    private static class UpdateAgendamentoAsyncTask extends AsyncTask<Agendamento, Void, Void> {
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

    private static class DeleteAgendamentoAsyncTask extends AsyncTask<Agendamento, Void, Void> {
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

    private static class GetAgendamentosMarcadosParaDataAsyncTask extends AsyncTask<Long, Void, List<Agendamento>> {
        private AgendaDao agendaDao;
        private List<Agendamento> agendamentosParaDia;

        public GetAgendamentosMarcadosParaDataAsyncTask(AgendaDao agendaDao) {
            this.agendaDao = agendaDao;
        }

        @Override
        protected List<Agendamento> doInBackground(Long... longs) {
            agendamentosParaDia = agendaDao.getAgendamentosMarcadosParaData(longs[0], longs[1]);
            return agendamentosParaDia;
        }
    }
}
