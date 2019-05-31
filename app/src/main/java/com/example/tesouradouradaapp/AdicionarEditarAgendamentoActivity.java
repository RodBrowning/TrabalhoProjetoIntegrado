package com.example.tesouradouradaapp;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;


public class AdicionarEditarAgendamentoActivity extends AppCompatActivity {

    private List<Servico> servicosSelecionados;
    private EditText editTextNomeCliente;
    private TextView textViewDuracaoTotal, textViewValorTotal, textViewDataSelecionada;
    private Button buttonAgendar;
    Agendamento agendamentoParaEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_editar_agendamento);
        editTextNomeCliente = findViewById(R.id.edit_text_nome_cliente);
        textViewDuracaoTotal = findViewById(R.id.text_view_duracao_total);
        textViewValorTotal = findViewById(R.id.text_view_valor_total);
        textViewDataSelecionada = findViewById(R.id.text_view_data_selecionada);
        buttonAgendar = findViewById(R.id.button_agendar);

        final Intent intent = getIntent();

        setTituloParaActivity(intent, VizualizarAgendamento.EDITAR);

        if (intent.hasExtra(VizualizarAgendamento.EDITAR)) {
            AgendaViewModel agendaViewModel = new AgendaViewModel(getApplication());
            try {
                agendamentoParaEditar = agendaViewModel.getAgendamento(intent.getIntExtra(VizualizarAgendamento.ID_AGEDAMENTO_EDITAR, 0));
                String nomeCliente = agendamentoParaEditar.getCliente();
                editTextNomeCliente.setText(nomeCliente);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        servicosSelecionados = intent.getParcelableArrayListExtra(ListaOpcoesServicoAdicionarEditarAgendamentoActivity.SERVICOS_ESCOLHIDOS);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_servicos_selecionados);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final AdicionarEditarAgendamentoAdapter adapter = new AdicionarEditarAgendamentoAdapter();
        adapter.setServicosSelecionados(servicosSelecionados);
        recyclerView.setAdapter(adapter);

        textViewDuracaoTotal.setText(duracaoTotalParaApresentacao(servicosSelecionados));
        textViewValorTotal.setText(valorTotalParaApresentacao(servicosSelecionados));

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd/MM/yyyy - HH:mm");
        SimpleDateFormat sdfHorarioFim = new SimpleDateFormat("HH:mm");

        long dataSelecionada = intent.getLongExtra(SelecionarDataHorarioActivity.HORARIO_SELECIONADO, 0);
        Date dataSelecionadaDate = new Date(dataSelecionada);
        int duracaoTotal = duracaoTotal(servicosSelecionados);
        Date dataFinal = new Date(dataSelecionadaDate.getTime() + duracaoTotal);

        textViewDataSelecionada.setText(sdf.format(dataSelecionadaDate) +" às "+ sdfHorarioFim.format(dataFinal));

        buttonAgendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomeCliente = editTextNomeCliente.getText().toString().trim();
                if (nomeCliente.isEmpty()) {
                    Toast.makeText(AdicionarEditarAgendamentoActivity.this, "Digite o nome do cliente", Toast.LENGTH_SHORT).show();
                    return;
                }

                inserirEditarAgendamento(intent, nomeCliente, agendamentoParaEditar);

                Intent intent = new Intent(AdicionarEditarAgendamentoActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

    }

    private void setTituloParaActivity(Intent intent, String modoDeCadastro) {
        if (intent.hasExtra(modoDeCadastro)) {
            setTitle("Editar Agendamento");
        } else {
            setTitle("Adicionar Agendamento");
        }
    }

    private void inserirEditarAgendamento(Intent intent, String nomeCliente, Agendamento agendamentoParaEditar) {

        List<Servico> servicosSelecionados = intent.<Servico>getParcelableArrayListExtra(ListaOpcoesServicoAdicionarEditarAgendamentoActivity.SERVICOS_ESCOLHIDOS);
        long duracaoTotal = new Long(duracaoTotal(servicosSelecionados));
        Date criadoEm = new Date();
        Agendamento agendamento = new Agendamento(nomeCliente, intent.getLongExtra(SelecionarDataHorarioActivity.HORARIO_SELECIONADO, 0), intent.getLongExtra(SelecionarDataHorarioActivity.HORARIO_SELECIONADO, 0) + duracaoTotal, criadoEm.getTime());
        AgendaViewModel agendaViewModel = ViewModelProviders.of(AdicionarEditarAgendamentoActivity.this).get(AgendaViewModel.class);
        Long agendamentoId;
        AgendaServicosJoinViewModel agendaServicosJoinViewModel = ViewModelProviders.of(AdicionarEditarAgendamentoActivity.this).get(AgendaServicosJoinViewModel.class);

        try {
            if (intent.hasExtra(VizualizarAgendamento.EDITAR)) {
                agendamento.setId_agendamento(agendamentoParaEditar.getId_agendamento());
                agendaViewModel.update(agendamento);
                agendamentoId = new Long(agendamentoParaEditar.getId_agendamento());
                agendaServicosJoinViewModel.deletarServicosDoAgendamentoParaEditar(agendamentoId);
            } else {
                agendamentoId = agendaViewModel.insert(agendamento);
            }

            for (Servico servicosSelecionado : servicosSelecionados) {
                AgendaServicosJoin agendaServicosJoin = new AgendaServicosJoin(agendamentoId.intValue(), servicosSelecionado.getId_servico());
                agendaServicosJoinViewModel.insert(agendaServicosJoin);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Calcula duracao

    private int duracaoTotal(List<Servico> servicos) {
        int duracaoTotal = 0;
        for (Servico servico : servicos) {
            duracaoTotal += servico.getTempo();
        }
        return duracaoTotal;
    }

    private String duracaoTotalParaApresentacao(List<Servico> servicos) {
        int duracaoTotal = duracaoTotal(servicos);
        int duracaohoras = duracaoTotal / 1000 / 60 / 60;
        int duracaoMinutos = (duracaoTotal / 1000 / 60) % 60;

        if (duracaohoras > 0) {
            if(duracaohoras == 1){
                return String.format("%d hr %02d mins", duracaohoras, duracaoMinutos);
            } else {
                return String.format("%d hrs %02d mins", duracaohoras, duracaoMinutos);
            }
        } else {
            return String.format("%d mins", duracaoMinutos);
        }

    }

    // Calcula valor total
    private String valorTotalParaApresentacao(List<Servico> servicos) {
        Float valorTotal = new Float(0);
        Locale brasil = new Locale("pt", "BR");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(brasil);
        for (Servico servico : servicos) {
            valorTotal += servico.getValor();
        }
        return numberFormat.format(valorTotal);
    }

    public int converterMilisegundosParaMinutos(long minutos) {
        Long longMinutos = new Long(minutos);
        int mins = (longMinutos.intValue() / 1000) / 60;
        return mins;
    }
}
