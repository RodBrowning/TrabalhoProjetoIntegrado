package com.example.tesouradouradaapp;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SelecionarDataHorarioActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private Button buttonSelecionarData;
    private TextView textViewHorarioSelecionado;
    private Calendar calendar;
    private EstabelecimentoViewModel estabelecimentoViewModel;
    private AgendaViewModel agendaViewModel;
    private List<Agendamento> agendaParaDia;
    private long horarioAbertura, horarioFechamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecionar_data_horario);
        setTitle("Data de agendamento");

        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        int dia = gregorianCalendar.get(Calendar.DAY_OF_MONTH);
        int mes = gregorianCalendar.get(Calendar.MONTH);
        int ano = gregorianCalendar.get(Calendar.YEAR);
        calendar = inicioExpediente(dia, mes, ano);

        Date inicioExpediente = calendar.getTime();
        editarTextoDoBotao(inicioExpediente);

        // Gerar horarios livres

        getHorariosAgendados(calendar);

        buttonSelecionarData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment(calendar);
                datePicker.show(getSupportFragmentManager(), "Date Picker");
            }
        });

    }

    private Calendar inicioExpediente(int dia, int mes, int ano) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.DAY_OF_MONTH, dia);
        calendar.set(calendar.MONTH, mes);
        calendar.set(calendar.YEAR, ano);

        calendar.set(calendar.HOUR_OF_DAY, 0);
        calendar.set(calendar.MINUTE, 0);
        calendar.set(calendar.SECOND, 0);
        calendar.set(calendar.MILLISECOND, 0);
        return calendar;
    }

    private long converterHorasMinutosParaMilisegundos(long horas, long minutos) {
        horas = ((horas * 60) * 60) * 1000;
        minutos = (minutos * 60) * 1000;
        return horas + minutos;
    }

    private List<Agendamento> getHorariosAgendados(final Calendar calendar) {
        agendaViewModel = ViewModelProviders.of(this).get(AgendaViewModel.class);
        estabelecimentoViewModel = ViewModelProviders.of(this).get(EstabelecimentoViewModel.class);
        estabelecimentoViewModel.getEstabelecimento().observe(this, new Observer<Estabelecimento>() {
            @Override
            public void onChanged(@Nullable Estabelecimento estabelecimento) {
                String[] horarioAberturaArray = estabelecimento.getHorarioAbertura().split(":");
                String[] horarioFechamentoArray = estabelecimento.getHorarioFechamento().split(":");

                int horasAbertura = Integer.parseInt(horarioAberturaArray[0]);
                int minutosAbertura = Integer.parseInt(horarioAberturaArray[1]);
                int horasFechamento = Integer.parseInt(horarioFechamentoArray[0]);
                int minutosFechamento = Integer.parseInt(horarioFechamentoArray[1]);

                horarioAbertura = calendar.getTime().getTime() + converterHorasMinutosParaMilisegundos(horasAbertura, minutosAbertura);
                horarioFechamento = calendar.getTime().getTime() + converterHorasMinutosParaMilisegundos(horasFechamento, minutosFechamento);

                try {
                    agendaParaDia = agendaViewModel.getAgendamentosMarcadosParaData(horarioAbertura, horarioFechamento);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        return agendaParaDia;
    }
    private void getHorariosLivres(List<Agendamento> agendamentos, long horarioAbertura, long horarioFechamento){

    }

    private void editarTextoDoBotao(Date dataSelecionada) {
        SimpleDateFormat simpleDateFormatDate = new SimpleDateFormat("EEEE - dd/MM/yyyy");
        SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat("HH:mm");

        buttonSelecionarData = findViewById(R.id.bottom_selecionar_data);
        buttonSelecionarData.setText(simpleDateFormatDate.format(dataSelecionada));
        textViewHorarioSelecionado = findViewById(R.id.text_view_horario_selecionado);
        textViewHorarioSelecionado.setText(simpleDateFormatTime.format(dataSelecionada));
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        calendar.set(Calendar.YEAR, i);
        calendar.set(Calendar.MONTH, i1);
        calendar.set(Calendar.DAY_OF_MONTH, i2);
        editarTextoDoBotao(calendar.getTime());
        getHorariosAgendados(calendar);
    }
}
