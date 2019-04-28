package com.example.tesouradouradaapp;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, 2019);
            calendar.set(Calendar.MONTH, 5);
            calendar.set(Calendar.DAY_OF_MONTH, 10);
            calendar.set(Calendar.HOUR_OF_DAY, 14);
            calendar.set(Calendar.MINUTE, 30);
            Date horarioInicio = calendar.getTime();
            agendaDao.insertAgendamento(new Agendamento("Claudio",
                    horarioInicio.getTime(),
                    horarioInicio.getTime()+30*60*1000,
                    String.valueOf(listaServicos),
                    new Date().getTime()
            ));

            listaServicos.add(4);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.set(Calendar.YEAR, 2019);
            calendar2.set(Calendar.MONTH, 5);
            calendar2.set(Calendar.DAY_OF_MONTH, 10);
            calendar2.set(Calendar.HOUR_OF_DAY, 15);
            calendar2.set(Calendar.MINUTE, 00);
            Date horarioInicio2 = calendar2.getTime();
            agendaDao.insertAgendamento(new Agendamento("Roberto",
                    horarioInicio2.getTime(),
                    horarioInicio2.getTime()+70*60*1000,
                    String.valueOf(listaServicos),
                    new Date().getTime()
            ));

            listaServicos.remove(2);
            listaServicos.remove(0);
            Calendar calendar3 = Calendar.getInstance();
            calendar3.set(Calendar.YEAR, 2019);
            calendar3.set(Calendar.MONTH, 5);
            calendar3.set(Calendar.DAY_OF_MONTH, 10);
            calendar3.set(Calendar.HOUR_OF_DAY, 16);
            calendar3.set(Calendar.MINUTE, 30);
            Date horarioInicio3 = calendar3.getTime();
            agendaDao.insertAgendamento(new Agendamento("Fernando",
                    horarioInicio3.getTime(),
                    horarioInicio3.getTime()+20*60*1000,
                    String.valueOf(listaServicos),
                    new Date().getTime()
            ));
            return null;
        }
    }
}

/*agendaDao.insertAgendamento(new Agendamento("Claudio",
                    String.valueOf(new Timestamp(2019, 6, 12,14,30,00,0)),
                    String.valueOf(new Timestamp(2019, 6, 12,15,30,00,0)),
                    String.valueOf(listaServicos),
                    String.valueOf(new Timestamp(new Date().getTime()))
                    ));
            listaServicos.add(4);
            agendaDao.insertAgendamento(new Agendamento("Roberto",
                    String.valueOf(new Timestamp(2019, 6, 12,15,30,00,0)),
                    String.valueOf(new Timestamp(2019, 6, 12,16,00,00,0)),
                    String.valueOf(listaServicos),
                    String.valueOf(new Timestamp(new Date().getTime()))
                    ));
            listaServicos.remove(2);
            listaServicos.remove(0);
            agendaDao.insertAgendamento(new Agendamento("Fernando",
                    String.valueOf(new Timestamp(2019, 6, 12,16,30,00,0)),
                    String.valueOf(new Timestamp(2019, 6, 12,17,00,00,0)),
                    String.valueOf(listaServicos),
                    String.valueOf(new Timestamp(new Date().getTime()))
                    ));*/
