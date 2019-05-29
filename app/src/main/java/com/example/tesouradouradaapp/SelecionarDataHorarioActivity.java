package com.example.tesouradouradaapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SelecionarDataHorarioActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public static final String HORARIO_SELECIONADO = "com.example.tesouradouradaapp.HORARIO_SELECIONADO";
    private Button buttonSelecionarData;
    private Button buttonConfirmarAgendamento;
    private TextView textViewHorarioSelecionado;
    private Calendar calendar;
    private long horarioAbertura = new Long(0);
    private long horarioFechamento = new Long(0);
    private List<Long> horariosAgendadosParaDia = new ArrayList<>();
    private List<Long> horariosLivresParaDiaLong = new ArrayList<>();
    private List<List<Long>> horariosLivresParaDiaParaServicosSelecionados = new ArrayList<>();
    private List<List<Long>> listaDeParDeHorariosLivresParaDiaLong;
    private ArrayList<Servico> servicosSelecionados = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecionar_data_horario);
        setTitle("Data de agendamento");

        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        int dia = gregorianCalendar.get(Calendar.DAY_OF_MONTH);
        int mes = gregorianCalendar.get(Calendar.MONTH);
        int ano = gregorianCalendar.get(Calendar.YEAR);
        calendar = inicioExpediente(ano, mes, dia);

        Date inicioExpediente = calendar.getTime();

        buttonSelecionarData = findViewById(R.id.bottom_selecionar_data);
        buttonConfirmarAgendamento = findViewById(R.id.bottom_seguir_para_adicionar_editar_agendamento);
        editarTextoDoBotaoCalendario(inicioExpediente);

        // Gerar horarios livres

        try {
            horarioAbertura = getHorarioAbertura(calendar);
            horarioFechamento = getHorarioFechamento(calendar);
            horariosAgendadosParaDia = getHorariosAgendadosPadaDia(horarioAbertura, horarioFechamento);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        horariosLivresParaDiaLong = getHorariosLivresParaDiaLong(horariosAgendadosParaDia, horarioAbertura, horarioFechamento);
        listaDeParDeHorariosLivresParaDiaLong = getParDeHorariosLivresLong(horariosLivresParaDiaLong);

        //Para passar para o proximo activity
        Intent intent = getIntent();
        servicosSelecionados = intent.getParcelableArrayListExtra(ListaOpcoesServicoAdicionarEditarAgendamentoActivity.SERVICOS_ESCOLHIDOS);
        horariosLivresParaDiaParaServicosSelecionados = getHorariosLivresParaDiaParaServicosSelecionados(listaDeParDeHorariosLivresParaDiaLong, servicosSelecionados);

        // Popular horarios livres


        RecyclerView recyclerView = findViewById(R.id.recycler_view_horarios_disponiveis);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final SelecionarDataHorarioAdapter adapter = new SelecionarDataHorarioAdapter(horariosLivresParaDiaParaServicosSelecionados);
        recyclerView.setAdapter(adapter);


        // Mostrar datepicker
        buttonSelecionarData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment(calendar);
                datePicker.show(getSupportFragmentManager(), "Date Picker");
            }
        });

        buttonConfirmarAgendamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!adapter.timeSelected) {
                    Toast.makeText(SelecionarDataHorarioActivity.this, "Selecione um horario", Toast.LENGTH_SHORT).show();
                    return;
                }
                addHorarioSelecionado(calendar, adapter.hourSelected, adapter.minutesSelected);
                textViewHorarioSelecionado.setText(adapter.hourSelected + ":" + adapter.minutesSelected);
                Intent intent = getIntent();
                intent.setClass(SelecionarDataHorarioActivity.this, AdicionarEditarAgendamentoActivity.class);
                intent.putExtra(HORARIO_SELECIONADO, calendar.getTimeInMillis());
                startActivity(intent);
            }
        });

    }

    private Calendar inicioExpediente(int ano, int mes, int dia) {
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

    private Calendar addHorarioSelecionado(Calendar calendar, int hourOfDay, int minute) {
        calendar.set(calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(calendar.MINUTE, minute);
        return calendar;
    }

    private long converterHorasMinutosParaMilisegundos(long horas, long minutos) {
        horas = ((horas * 60) * 60) * 1000;
        minutos = (minutos * 60) * 1000;
        return horas + minutos;
    }

    private long getHorarioAbertura(Calendar calendar) throws ExecutionException, InterruptedException {
        EstabelecimentoViewModel estabelecimentoViewModel = ViewModelProviders.of(this).get(EstabelecimentoViewModel.class);
        Estabelecimento estabelecimento = estabelecimentoViewModel.getEstab();

        String[] horarioAberturaArray = estabelecimento.getHorarioAbertura().split(":");

        int horasAbertura = Integer.parseInt(horarioAberturaArray[0]);
        int minutosAbertura = Integer.parseInt(horarioAberturaArray[1]);

        long horarioAbertura = calendar.getTime().getTime() + converterHorasMinutosParaMilisegundos(horasAbertura, minutosAbertura);
        return horarioAbertura;
    }

    private long getHorarioFechamento(Calendar calendar) throws ExecutionException, InterruptedException {
        EstabelecimentoViewModel estabelecimentoViewModel = ViewModelProviders.of(this).get(EstabelecimentoViewModel.class);
        Estabelecimento estabelecimento;
        estabelecimento = estabelecimentoViewModel.getEstab();

        String[] horarioFechamentoArray = estabelecimento.getHorarioFechamento().split(":");

        int horasFechamento = Integer.parseInt(horarioFechamentoArray[0]);
        int minutosFechamento = Integer.parseInt(horarioFechamentoArray[1]);

        long horarioFechamento = calendar.getTime().getTime() + converterHorasMinutosParaMilisegundos(horasFechamento, minutosFechamento);
        return horarioFechamento;
    }

    private List<Long> getHorariosAgendadosPadaDia(long horarioAbertura, long horarioFechamento) {
        AgendaViewModel agendaViewModel = ViewModelProviders.of(this).get(AgendaViewModel.class);

        List<Agendamento> agendaParaDia = new ArrayList<>();
        List<Long> horariosAgendadosParaDia = new ArrayList<>();
        try {
            agendaParaDia = agendaViewModel.getAgendamentosMarcadosParaData(horarioAbertura, horarioFechamento);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            AgendaServicosJoinViewModel agendaServicosJoinViewModel = new AgendaServicosJoinViewModel(getApplication());
            for (int i = 0; i < agendaParaDia.size(); i++) {
                long duracaoAgendamento = 0;
                try {
                    List<Servico> listServicos = agendaServicosJoinViewModel.getServicosParaAgendamentoJoinServicos(agendaParaDia.get(i).getId_agendamento());
                    for (Servico listServico : listServicos) {
                        duracaoAgendamento += new Long(listServico.getTempo());
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                horariosAgendadosParaDia.add(agendaParaDia.get(i).getHorarioInicio());
                horariosAgendadosParaDia.add(agendaParaDia.get(i).getHorarioInicio()+ duracaoAgendamento);
            }

        }
        return horariosAgendadosParaDia;
    }

    private List<Long> getHorariosLivresParaDiaLong(List<Long> horariosAgendados, long horarioAbertura, long horarioFechamento) {
        List<Long> horariosLivresLong = new ArrayList<>();

        horariosLivresLong.add(horarioAbertura);
        for (Long horariosAgendado : horariosAgendados) {
            horariosLivresLong.add(horariosAgendado);
        }
        horariosLivresLong.add(horarioFechamento);
        return horariosLivresLong;
    }

    private List<List<Long>> getParDeHorariosLivresLong(List<Long> horariosLivresParaDiaLong) {
        List<List<Long>> listaDeParDeHorariosLivresLong = new ArrayList<>();
        for (int i = 0; i < horariosLivresParaDiaLong.size(); i++) {
            if (horariosLivresParaDiaLong.get(i).equals(horariosLivresParaDiaLong.get(i + 1))) {
                i++;
                continue;
            } else {
                List<Long> parDeHorariosLivresLong = new ArrayList<>();
                parDeHorariosLivresLong.add(horariosLivresParaDiaLong.get(i));
                i++;
                parDeHorariosLivresLong.add(horariosLivresParaDiaLong.get(i));
                listaDeParDeHorariosLivresLong.add(parDeHorariosLivresLong);
            }
        }

        return listaDeParDeHorariosLivresLong;
    }

    private List<List<Long>> getHorariosLivresParaDiaParaServicosSelecionados(List<List<Long>> listaDeParDeHorariosLivresLong, ArrayList<Servico> servicosSelecionados) {
        List<Long> horariosLivresParaDiaParaServicosSelecionados = new ArrayList<>();
        long sumDuracaoServidosSelecionados = new Long(0);
        for (int i = 0; i < servicosSelecionados.size(); i++) {
            sumDuracaoServidosSelecionados += servicosSelecionados.get(i).getTempo();
        }
        for (int i = 0; i < listaDeParDeHorariosLivresLong.size(); i++) {
            listaDeParDeHorariosLivresLong.get(i)
                    .set(1, listaDeParDeHorariosLivresLong.get(i).get(1) - sumDuracaoServidosSelecionados);
        }
        for (int i = 0; i < listaDeParDeHorariosLivresLong.size(); i++) {
            if (listaDeParDeHorariosLivresLong.get(i).get(1).longValue() < listaDeParDeHorariosLivresLong.get(i).get(0).longValue()) {
                listaDeParDeHorariosLivresLong.remove(i);
                i--;
            }
        }
        return listaDeParDeHorariosLivresLong;
    }

    private void editarTextoDoBotaoCalendario(Date dataSelecionada) {
        SimpleDateFormat simpleDateFormatDate = new SimpleDateFormat("EEEE - dd/MM/yyyy");
        SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat("HH:mm");

        buttonSelecionarData.setText(simpleDateFormatDate.format(dataSelecionada));
        textViewHorarioSelecionado = findViewById(R.id.text_view_horario_selecionado);
        textViewHorarioSelecionado.setText(simpleDateFormatTime.format(dataSelecionada));
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        calendar = inicioExpediente(i, i1, i2);
        editarTextoDoBotaoCalendario(calendar.getTime());
        // Gerar horarios livres
        try {
            horarioAbertura = getHorarioAbertura(calendar);
            horarioFechamento = getHorarioFechamento(calendar);
            horariosAgendadosParaDia = getHorariosAgendadosPadaDia(horarioAbertura, horarioFechamento);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        horariosLivresParaDiaLong = getHorariosLivresParaDiaLong(horariosAgendadosParaDia, horarioAbertura, horarioFechamento);
        listaDeParDeHorariosLivresParaDiaLong = getParDeHorariosLivresLong(horariosLivresParaDiaLong);

        //Para passar para o proximo activity
        Intent intent = getIntent();
        servicosSelecionados = intent.getParcelableArrayListExtra(ListaOpcoesServicoAdicionarEditarAgendamentoActivity.SERVICOS_ESCOLHIDOS);
        horariosLivresParaDiaParaServicosSelecionados = getHorariosLivresParaDiaParaServicosSelecionados(listaDeParDeHorariosLivresParaDiaLong, servicosSelecionados);

        // Popular horarios livres

        RecyclerView recyclerView = findViewById(R.id.recycler_view_horarios_disponiveis);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final SelecionarDataHorarioAdapter adapter = new SelecionarDataHorarioAdapter(horariosLivresParaDiaParaServicosSelecionados);
        recyclerView.setAdapter(adapter);
    }

}
