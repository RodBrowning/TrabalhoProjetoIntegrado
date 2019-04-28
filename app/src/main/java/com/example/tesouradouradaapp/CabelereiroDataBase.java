package com.example.tesouradouradaapp;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

@Database(entities = {Estabelecimento.class, Servico.class, Agendamento.class}, version = 1)
public abstract class CabelereiroDataBase extends RoomDatabase {
    private static CabelereiroDataBase instance;

    public abstract EstabelecimentoDao estabelecimentoDao();
    public abstract ServicoDao servicoDao();
    public abstract AgendaDao agendaDao();

    public static synchronized CabelereiroDataBase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    CabelereiroDataBase.class,
                    "cabelereiro_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void> {
        private EstabelecimentoDao estabelecimentoDao;
        private ServicoDao servicoDao;
        private AgendaDao agendaDao;

        public PopulateDbAsyncTask(CabelereiroDataBase db) {
            this.estabelecimentoDao = db.estabelecimentoDao();
            this.servicoDao = db.servicoDao();
            this.agendaDao = db.agendaDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Dados iniciais
            estabelecimentoDao.insertEstabelecimento(new Estabelecimento("Tesoura Dourada", "Marcos Mendonca", "07:00","18:00"));

            // Servicos
            servicoDao.insertServico(new Servico("Corte masculino", 25, 30));
            servicoDao.insertServico(new Servico("Corte infantil", 15, 30));
            servicoDao.insertServico(new Servico("Barba", 15, 20));
            servicoDao.insertServico(new Servico("Pezinho", 5, 15));
            servicoDao.insertServico(new Servico("Luzes", 50, 60));

            // Agendamentos
            ArrayList<Integer> listaServicos = new ArrayList<Integer>();
            listaServicos.add(1);
            listaServicos.add(2);
            listaServicos.add(3);
            agendaDao.insertAgendamento(new Agendamento("Claudio",
                    String.valueOf(new Timestamp(2019, 6, 12,14,30,00,0)),
                    String.valueOf(new Timestamp(2019, 6, 12,15,30,00,0)),
                    listaServicos));
            listaServicos.add(4);
            agendaDao.insertAgendamento(new Agendamento("Roberto",
                    String.valueOf(new Timestamp(2019, 6, 12,15,30,00,0)),
                    String.valueOf(new Timestamp(2019, 6, 12,16,00,00,0)),
                    listaServicos));
            listaServicos.remove(2);
            listaServicos.remove(0);
            agendaDao.insertAgendamento(new Agendamento("Fernando",
                    String.valueOf(new Timestamp(2019, 6, 12,16,30,00,0)),
                    String.valueOf(new Timestamp(2019, 6, 12,17,00,00,0)),
                    listaServicos));
            return null;
        }
    }
}

