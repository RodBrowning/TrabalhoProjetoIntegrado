package com.example.tesouradouradaapp.DataBase;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;


import com.example.tesouradouradaapp.DataBase.DAOs.AgendaDao;
import com.example.tesouradouradaapp.DataBase.DAOs.AgendaServicoJoinDao;
import com.example.tesouradouradaapp.DataBase.DAOs.EstabelecimentoDao;
import com.example.tesouradouradaapp.DataBase.DAOs.ServicoDao;
import com.example.tesouradouradaapp.DataBase.Entities.AgendaServicosJoin;
import com.example.tesouradouradaapp.DataBase.Entities.Agendamento;
import com.example.tesouradouradaapp.DataBase.Entities.Estabelecimento;
import com.example.tesouradouradaapp.DataBase.Entities.Servico;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@Database(entities = {Estabelecimento.class, Servico.class, Agendamento.class, AgendaServicosJoin.class}, exportSchema = false,version = 1)
public abstract class CabelereiroDataBase extends RoomDatabase {
    private static CabelereiroDataBase instance;

    public abstract EstabelecimentoDao estabelecimentoDao();

    public abstract ServicoDao servicoDao();

    public abstract AgendaDao agendaDao();

    public abstract AgendaServicoJoinDao agendaServicosJoinDao();

    public static synchronized CabelereiroDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    CabelereiroDataBase.class,
                    "cabelereiro_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private EstabelecimentoDao estabelecimentoDao;
        private ServicoDao servicoDao;
        private AgendaDao agendaDao;
        private AgendaServicoJoinDao agendaServicosJoinDao;

        public PopulateDbAsyncTask(CabelereiroDataBase db) {
            this.estabelecimentoDao = db.estabelecimentoDao();
            this.servicoDao = db.servicoDao();
            this.agendaDao = db.agendaDao();
            this.agendaServicosJoinDao = db.agendaServicosJoinDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            // Dados iniciais
            estabelecimentoDao.insertEstabelecimento(new Estabelecimento("Tesoura Dourada", "Marcos Mendonca", "07:30", "18:00"));

            // Servicos
            servicoDao.insertServico(new Servico("Corte masculino", 25, (30 * 60) * 1000));
            servicoDao.insertServico(new Servico("Corte infantil", 15, (30 * 60) * 1000));
            servicoDao.insertServico(new Servico("Barba", 15, (20 * 60) * 1000));
            servicoDao.insertServico(new Servico("Pezinho", 5, (15 * 60) * 1000));
            servicoDao.insertServico(new Servico("Luzes", 50, (60 * 60) * 1000));

            // Agendamentos

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, 2019);
            calendar.set(Calendar.MONTH, 5);
            calendar.set(Calendar.DAY_OF_MONTH, 11);
            calendar.set(Calendar.HOUR_OF_DAY, 14);
            calendar.set(Calendar.MINUTE, 30);
            calendar.set(Calendar.SECOND, 00);
            calendar.set(Calendar.MILLISECOND, 00);
            Date horarioInicio = calendar.getTime();
            Agendamento agendamento1 = new Agendamento(
                    "Claudio Hugo",
                    horarioInicio.getTime(),
                    new Date().getTime()
            );
            agendaDao.insertAgendamento(agendamento1);

            agendaServicosJoinDao.insert(new AgendaServicosJoin(1, 1));
            agendaServicosJoinDao.insert(new AgendaServicosJoin(1, 2));
            agendaServicosJoinDao.insert(new AgendaServicosJoin(1, 3));

            Calendar calendar2 = Calendar.getInstance();
            calendar2.set(Calendar.YEAR, 2019);
            calendar2.set(Calendar.MONTH, 5);
            calendar2.set(Calendar.DAY_OF_MONTH, 11);
            calendar2.set(Calendar.HOUR_OF_DAY, 15);
            calendar2.set(Calendar.MINUTE, 00);
            calendar2.set(Calendar.SECOND, 00);
            calendar2.set(Calendar.MILLISECOND, 00);
            Date horarioInicio2 = calendar2.getTime();
            Agendamento agendamento2 = new Agendamento(
                    "Roberto",
                    horarioInicio2.getTime(),
                    new Date().getTime()
            );
            agendaDao.insertAgendamento(agendamento2);

            int agendamento12 = agendamento2.getId_agendamento();
            agendaServicosJoinDao.insert(new AgendaServicosJoin(2, 2));
            agendaServicosJoinDao.insert(new AgendaServicosJoin(2, 3));

            Calendar calendar3 = Calendar.getInstance();
            calendar3.set(Calendar.YEAR, 2019);
            calendar3.set(Calendar.MONTH, 5);
            calendar3.set(Calendar.DAY_OF_MONTH, 11);
            calendar3.set(Calendar.HOUR_OF_DAY, 16);
            calendar3.set(Calendar.MINUTE, 30);
            calendar3.set(Calendar.SECOND, 00);
            calendar3.set(Calendar.MILLISECOND, 00);
            Date horarioInicio3 = calendar3.getTime();
            Agendamento agendamento3 = new Agendamento("Fernando",
                    horarioInicio3.getTime(),
                    new Date().getTime()
            );
            agendaDao.insertAgendamento(agendamento3);


            agendaServicosJoinDao.insert(new AgendaServicosJoin(3, 3));
            agendaServicosJoinDao.insert(new AgendaServicosJoin(3, 4));

            return null;
        }
    }

}
