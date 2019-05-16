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
import java.util.List;
import java.util.Locale;


public class AdicionarEditarAgendamentoActivity extends AppCompatActivity {

    private List<Servico> servicosSelecionados;
    private TextView textViewDuracaoTotal, textViewValorTotal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_editar_agendamento);
        setTitle("Adicionar Agendamento");
        textViewDuracaoTotal = findViewById(R.id.text_view_duracao_total);
        textViewValorTotal = findViewById(R.id.text_view_valor_total);

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

    }

    private void salvarEditarAgendamento(){
        Intent intent = new Intent(AdicionarEditarAgendamentoActivity.this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Worked from insert update agendamento", Toast.LENGTH_SHORT).show();
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
    private String duracaoTotalParaApresentacao(List<Servico> servicos){
        int duracaoTotal = 0;
        for (Servico servico : servicos){
            duracaoTotal += servico.getTempo();
        }

        int duracaohoras = duracaoTotal / 60;
        int duracaoMinutos = duracaoTotal % 60;

        if (duracaohoras > 0){
            return String.format("%d hrs %02d min", duracaohoras, duracaoMinutos);
        } else {
            return String.format("%d min", duracaoMinutos);
        }

    }
    // Calcula valor total
    private String valorTotalParaApresentacao(List<Servico> servicos){
        Float valorTotal = new Float(0);
        Locale brasil = new Locale("pt", "BR");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(brasil);
        for (Servico servico : servicos) {
            valorTotal += servico.getValor();
        }
        return numberFormat.format(valorTotal);
    }
}
