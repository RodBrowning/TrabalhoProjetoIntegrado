package com.example.tesouradouradaapp.Repositories;

import android.content.Context;
import android.os.AsyncTask;

import com.example.tesouradouradaapp.DataBase.DAOs.AgendaServicoJoinDao;
import com.example.tesouradouradaapp.DataBase.Entities.AgendaServicosJoin;
import com.example.tesouradouradaapp.DataBase.CabelereiroDataBase;
import com.example.tesouradouradaapp.DataBase.Entities.Servico;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class AgendaServicosJoinRepository {
    private AgendaServicoJoinDao agendaServicoJoinDao;

    public AgendaServicosJoinRepository(Context context) {
        CabelereiroDataBase cabelereiroDataBase = CabelereiroDataBase.getInstance(context);
        agendaServicoJoinDao = cabelereiroDataBase.agendaServicosJoinDao();
    }

    public void inserAgendaServicosJoin(AgendaServicosJoin agendaServicosJoin) {
        new InsertAgendaServicosJoinAsyncTask(agendaServicoJoinDao).execute(agendaServicosJoin);
    }

    public void deletarServicosDoAgendamentoParaEditar(long id_agendamnto){
        new DeletarAgendaServicosJoinAsyncTask(agendaServicoJoinDao).execute(id_agendamnto);
    }
    public List<Servico> getServicosParaAgendamentoJoinServicos(int id_agendamento) throws ExecutionException, InterruptedException {
        return new GetServicosParaAgendamentoJoinServicosAsyncTask(agendaServicoJoinDao).execute(id_agendamento).get();
    }

    public List<AgendaServicosJoin> getAgendamentosParaServico(int id_servico) throws ExecutionException, InterruptedException {
        return new GetAgendamentosParaServicoAsyncTask(agendaServicoJoinDao).execute(id_servico).get();
    }

    public List<AgendaServicosJoin> getServicosParaAgendamento(int id_agendamento) throws ExecutionException, InterruptedException {
        return new GetServicosParaAgendamentoAsyncTask(agendaServicoJoinDao).execute(id_agendamento).get();
    }
    private static class InsertAgendaServicosJoinAsyncTask extends AsyncTask<AgendaServicosJoin,Void,Void>{
        private AgendaServicoJoinDao agendaServicoJoinDao;

        public InsertAgendaServicosJoinAsyncTask(AgendaServicoJoinDao agendaServicoJoinDao) {
            this.agendaServicoJoinDao = agendaServicoJoinDao;
        }

        @Override
        protected Void doInBackground(AgendaServicosJoin... agendaServicosJoins) {
            agendaServicoJoinDao.insert(agendaServicosJoins[0]);
            return null;
        }
    }

    private static class DeletarAgendaServicosJoinAsyncTask extends AsyncTask<Long,Void,Void>{
        private AgendaServicoJoinDao agendaServicoJoinDao;

        public DeletarAgendaServicosJoinAsyncTask(AgendaServicoJoinDao agendaServicoJoinDao) {
            this.agendaServicoJoinDao = agendaServicoJoinDao;
        }

        @Override
        protected Void doInBackground(Long... longs) {
            agendaServicoJoinDao.deletarServicosDoAgendamentoParaEditar(longs[0]);
            return null;
        }
    }

    private static class GetServicosParaAgendamentoJoinServicosAsyncTask extends AsyncTask<Integer,Void,List<Servico>>{
        private AgendaServicoJoinDao agendaServicoJoinDao;

        public GetServicosParaAgendamentoJoinServicosAsyncTask(AgendaServicoJoinDao agendaServicoJoinDao) {
            this.agendaServicoJoinDao = agendaServicoJoinDao;
        }

        @Override
        protected List<Servico> doInBackground(Integer... integers) {
            return agendaServicoJoinDao.getServicosParaAgendamentoJoinServicos(integers[0]);
        }
    }

    private static class GetAgendamentosParaServicoAsyncTask extends AsyncTask<Integer,Void,List<AgendaServicosJoin>>{
        private AgendaServicoJoinDao agendaServicoJoinDao;

        public GetAgendamentosParaServicoAsyncTask(AgendaServicoJoinDao agendaServicoJoinDao) {
            this.agendaServicoJoinDao = agendaServicoJoinDao;
        }

        @Override
        protected List<AgendaServicosJoin> doInBackground(Integer... integers) {
            return agendaServicoJoinDao.getAgendamentosParaServico(integers[0]);
        }
    }

    private static class GetServicosParaAgendamentoAsyncTask extends AsyncTask<Integer, Void, List<AgendaServicosJoin>>{
        private AgendaServicoJoinDao agendaServicoJoinDao;

        public GetServicosParaAgendamentoAsyncTask(AgendaServicoJoinDao agendaServicoJoinDao) {
            this.agendaServicoJoinDao = agendaServicoJoinDao;
        }

        @Override
        protected List<AgendaServicosJoin> doInBackground(Integer... integers) {
            return agendaServicoJoinDao.getServicosParaAgendamento(integers[0]);
        }
    }
}
