package com.example.tesouradouradaapp;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private TextView textViewAgendaLivre;
    private Button buttonSelecionarData;
    private RelativeLayout relativeLayoutButtonSelecionarData;
    private Calendar calendar;
    private AgendaViewModel agendaViewModel;
    private EstabelecimentoViewModel estabelecimentoViewModel;
    private ServicoViewModel servicoViewModel;
    private FloatingActionButton floatingActionButtonAdicionarAgendamento;
    private long horarioAbertura, horarioFechamento;
    private List<Long> horariosAgendadosParaDia = new ArrayList<>();
    private Estabelecimento estabelecimento;
    public static String APP_TITLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewAgendaLivre = findViewById(R.id.text_view_agenda_livre);
        calendar = setDataParaIniciarCalendario(Calendar.getInstance());
        buttonSelecionarData = findViewById(R.id.bottom_selecionar_data);

        relativeLayoutButtonSelecionarData = findViewById(R.id.relative_layout_bottom_selecionar_data_selecionar_data);
        relativeLayoutButtonSelecionarData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment(calendar);
                datePicker.show(getSupportFragmentManager(), "Date Picker main activity");
            }
        });

        floatingActionButtonAdicionarAgendamento = findViewById(R.id.floating_action_button_adicionar_agendamento);
        floatingActionButtonAdicionarAgendamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListaOpcoesServicoAdicionarEditarAgendamentoActivity.class);
                startActivity(intent);
            }
        });

        estabelecimentoViewModel = ViewModelProviders.of(this).get(EstabelecimentoViewModel.class);
        try {
            estabelecimento = estabelecimentoViewModel.getEstab();
            setTituloParaActivity(estabelecimento);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        carregarCodigosIguaisParaOnCreateEOnDateSet();


    }

    private void carregarCodigosIguaisParaOnCreateEOnDateSet() {
        Date inicioExpediente = calendar.getTime();
        editarTextoDoBotaoCalendario(inicioExpediente);

        try {
            horarioAbertura = getHorarioAbertura(calendar);
            horarioFechamento = getHorarioFechamento(calendar);
            horariosAgendadosParaDia = getHorariosAgendadosPadaDia(horarioAbertura, horarioFechamento);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Date horarioPresente = new Date();
        if (horarioAbertura < horarioPresente.getTime()) {
            horarioAbertura = horarioPresente.getTime();
        }


        RecyclerView recyclerView = findViewById(R.id.recycler_view_agenda);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final AgendaAdapter adapter = new AgendaAdapter(MainActivity.this);
        adapter.setApplication(getApplication());
        recyclerView.setAdapter(adapter);


        agendaViewModel = ViewModelProviders.of(this).get(AgendaViewModel.class);
        agendaViewModel.getAgendamentosMarcadosParaDataLiveData(horarioAbertura, horarioFechamento).observe(this, new Observer<List<Agendamento>>() {
            @Override
            public void onChanged(@Nullable List<Agendamento> agendamentos) {

                try {
                    Agendamento agendamentoEmAndamento;
                    agendamentoEmAndamento = agendaViewModel.verSeExisteAgendamentoEmAndamento(horarioAbertura);
                    if (agendamentoEmAndamento != null) {
                        agendamentos.add(0, agendamentoEmAndamento);
                        adapter.setExisteAgendamentoEmAndamento(true);
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        List<Agendamento> agendamentos = new ArrayList<>();
        try {
            agendamentos = agendaViewModel.getAgendamentosMarcadosParaData(horarioAbertura,horarioFechamento);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (agendamentos.size() == 0) {
            textViewAgendaLivre.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            adapter.setAgenda(agendamentos);
        } else {
            textViewAgendaLivre.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.setAgenda(agendamentos);
        }

        servicoViewModel = ViewModelProviders.of(this).get(ServicoViewModel.class);
        servicoViewModel.getAllServicos().observe(this, new Observer<List<Servico>>() {
            @Override
            public void onChanged(@Nullable List<Servico> servicos) {
                adapter.notifyDataSetChangedServicos();
            }
        });


    }

    private void setTituloParaActivity(Estabelecimento estabelecimento) {
        if (estabelecimento != null) {
            APP_TITLE = estabelecimento.getNomeEstabelecimento();
            setTitle("Agenda " + APP_TITLE);
        } else {
            APP_TITLE = "Bem vindo";
        }
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
                horariosAgendadosParaDia.add(agendaParaDia.get(i).getHorarioInicio() + duracaoAgendamento);
            }

        }
        return horariosAgendadosParaDia;
    }

    private Calendar setDataParaIniciarCalendario(Calendar calendar) {

        int dia;
        int mes;
        int ano;

        dia = calendar.get(Calendar.DAY_OF_MONTH);
        mes = calendar.get(Calendar.MONTH);
        ano = calendar.get(Calendar.YEAR);
        calendar = inicioExpediente(ano, mes, dia);

        return calendar;
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

    private long converterHorasMinutosParaMilisegundos(long horas, long minutos) {
        horas = ((horas * 60) * 60) * 1000;
        minutos = (minutos * 60) * 1000;
        return horas + minutos;
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

    private void editarTextoDoBotaoCalendario(Date dataSelecionada) {
        SimpleDateFormat simpleDateFormatDate = new SimpleDateFormat("EEEE - dd/MM/yyyy");

        buttonSelecionarData = findViewById(R.id.bottom_selecionar_data);

        buttonSelecionarData.setText(simpleDateFormatDate.format(dataSelecionada));
    }

    private void irParaDadosEstabelecimentoActivity() {
        Intent intent = new Intent(MainActivity.this, DadosEstabelecimentoActivity.class);
        startActivity(intent);
    }

    private void irParaServicosActivity() {
        Intent intent = new Intent(MainActivity.this, ServicosActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_estabelecimento:
                irParaDadosEstabelecimentoActivity();
                return true;
            case R.id.menu_servicos:
                irParaServicosActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar = inicioExpediente(year, month, dayOfMonth);
        carregarCodigosIguaisParaOnCreateEOnDateSet();
    }
}
