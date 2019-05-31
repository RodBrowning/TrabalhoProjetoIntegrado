package com.example.tesouradouradaapp;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class AgendaRepository {
    private AgendaDao agendaDao;
    private LiveData<List<Agendamento>> agenda;
    private List<Agendamento> agendamentosParaDia;
    private LiveData<List<Agendamento>> agendamentosParaDiaLiveData;

    public AgendaRepository(Context context) {
        CabelereiroDataBase cabelereiroDataBase = CabelereiroDataBase.getInstance(context);
        agendaDao = cabelereiroDataBase.agendaDao();
        agenda = agendaDao.getAllAgendamentos();
    }

    public Long insertAgendamento(Agendamento agendamento) throws ExecutionException, InterruptedException {
        return new InsertAgendamentoAsyncTask(agendaDao).execute(agendamento).get();
    }

    public void updateAgendamento(Agendamento agendamento) {
        new UpdateAgendamentoAsyncTask(agendaDao).execute(agendamento);
    }

    public void deleteAgendamento(Agendamento agendamento) {
        new DeleteAgendamentoAsyncTask(agendaDao).execute(agendamento);
    }

    public Agendamento getAgendamento(int id_agendamento) throws ExecutionException, InterruptedException {
        return new GetAgendamentoAsyncTask(agendaDao).execute(id_agendamento).get();
    }

    public LiveData<List<Agendamento>> getAgenda() {
        return agenda;
    }

    public Agendamento verSeExisteAgendamentoEmAndamento(long horarioDeAbertura) throws ExecutionException, InterruptedException {
        return new VerSeExisteAgendamentoEmAndamentoAsyncTask(agendaDao).execute(horarioDeAbertura).get();
    }

    public LiveData<List<Agendamento>> getAgendamentosMarcadosParaDataLiveData(long horarioDeAbertuta, long horarioFechamento){
        return agendaDao.getAgendamentosMarcadosParaDataLiveData(horarioDeAbertuta, horarioFechamento);
    }
    public List<Agendamento> getAgendamentosMarcadosParaData(long horarioDeAbertuta, long horarioFechamento) throws ExecutionException, InterruptedException {
        agendamentosParaDia = new GetAgendamentosMarcadosParaDataAsyncTask(agendaDao).execute(horarioDeAbertuta, horarioFechamento).get();
        return agendamentosParaDia;
    }

    public List<Agendamento> getAgendamentosMarcadosParaDataSemAgendamentoParaEditar(long horarioDeAbertuta, long horarioFechamento, int id_agendamento_para_atualizar) throws ExecutionException, InterruptedException {
        agendamentosParaDia = new GetAgendamentosMarcadosParaDataSemAgendamentoParaEditarAsyncTask(agendaDao, horarioDeAbertuta, horarioFechamento, id_agendamento_para_atualizar).execute().get();
        return agendamentosParaDia;
    }

    private static class InsertAgendamentoAsyncTask extends AsyncTask<Agendamento, Void, Long> {
        private AgendaDao agendaDao;

        private InsertAgendamentoAsyncTask(AgendaDao agendaDao) {
            this.agendaDao = agendaDao;
        }

        @Override
        protected Long doInBackground(Agendamento... agendamentos) {

            return agendaDao.insertAgendamento(agendamentos[0]);
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

    private static class GetAgendamentoAsyncTask extends AsyncTask<Integer, Void, Agendamento>{
        private AgendaDao agendaDao;

        public GetAgendamentoAsyncTask(AgendaDao agendaDao) {
            this.agendaDao = agendaDao;
        }

        @Override
        protected Agendamento doInBackground(Integer... integers) {
            return agendaDao.getAgendamento(integers[0]);
        }
    }

    private static class VerSeExisteAgendamentoEmAndamentoAsyncTask extends AsyncTask<Long, Void, Agendamento>{
        private AgendaDao agendaDao;

        public VerSeExisteAgendamentoEmAndamentoAsyncTask(AgendaDao agendaDao) {
            this.agendaDao = agendaDao;
        }

        @Override
        protected Agendamento doInBackground(Long... longs) {
            return agendaDao.verSeExisteAgendamentoEmAndamento(longs[0]);
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

    private static class GetAgendamentosMarcadosParaDataSemAgendamentoParaEditarAsyncTask extends AsyncTask<Void, Void, List<Agendamento>> {
        private AgendaDao agendaDao;
        private List<Agendamento> agendamentosParaDia;
        long horarioDeAbertuta, horarioFechamento;
        int id_agendamento_para_atualizar;

        public GetAgendamentosMarcadosParaDataSemAgendamentoParaEditarAsyncTask(AgendaDao agendaDao, long horarioDeAbertuta, long horarioFechamento, int id_agendamento_para_atualizar) {
            this.agendaDao = agendaDao;
            this.horarioDeAbertuta = horarioDeAbertuta;
            this.horarioFechamento = horarioFechamento;
            this.id_agendamento_para_atualizar = id_agendamento_para_atualizar;
        }

        @Override
        protected List<Agendamento> doInBackground(Void... voids) {
            agendamentosParaDia = agendaDao.getAgendamentosMarcadosParaDataSemAgendamentoParaEditar(horarioDeAbertuta, horarioFechamento, id_agendamento_para_atualizar);
            return agendamentosParaDia;
        }
    }
}
