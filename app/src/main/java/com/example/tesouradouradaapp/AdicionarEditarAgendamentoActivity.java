package com.example.tesouradouradaapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AdicionarEditarAgendamentoActivity extends AppCompatActivity {

    private List<Servico> servicosSelecionados;
    private TextView textViewDuracaoTotal, textViewValorTotal, textViewDataSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_editar_agendamento);
        setTitle("Adicionar Agendamento");
        textViewDuracaoTotal = findViewById(R.id.text_view_duracao_total);
        textViewValorTotal = findViewById(R.id.text_view_valor_total);
        textViewDataSelecionada = findViewById(R.id.text_view_data_selecionada);

        Intent intent = getIntent();
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
        long dataSelecionada = intent.getLongExtra(SelecionarDataHorarioActivity.HORARIO_SELECIONADO,0);
        Date dataSelecionadaDate = new Date(dataSelecionada);
        textViewDataSelecionada.setText(sdf.format(dataSelecionadaDate));

    }

    private void salvarEditarAgendamento() {
        Intent intent = new Intent(AdicionarEditarAgendamentoActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_adicionar_editar_agendamento, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_salvar_agendamento:
                salvarEditarAgendamento();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    // Calcula duracao
    private String duracaoTotalParaApresentacao(List<Servico> servicos) {
        int duracaoTotal = 0;
        for (Servico servico : servicos) {
            duracaoTotal += servico.getTempo();
        }

        int duracaohoras = duracaoTotal / 1000 / 60 /60;
        int duracaoMinutos = (duracaoTotal / 1000 / 60)%60;

        if (duracaohoras > 0) {
            return String.format("%d hrs %02d min", duracaohoras, duracaoMinutos);
        } else {
            return String.format("%d min", duracaoMinutos);
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
