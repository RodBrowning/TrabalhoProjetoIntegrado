package com.example.tesouradouradaapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListaOpcoesServicoAdicionarEditarAgendamentoActivity extends AppCompatActivity {
    public static final String SERVICOS_ESCOLHIDOS = "package com.example.tesouradouradaapp.SERVICOS_ESCOLHIDOS";
    private ServicoViewModel servicoViewModel;
    private Button buttonSeguir;
    private Intent intentEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_opcoes_servico_adicionar_editar_agendamento);

        intentEditar = getIntent();
        if (intentEditar.hasExtra(VizualizarAgendamento.EDITAR)) {
            setTitle("Atualizar Serviços");
        } else {
            setTitle("Selecionar Serviços");
        }

        buttonSeguir = findViewById(R.id.bottom_seguir_para_selecionar_data_horario);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_opcoes_servico);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final ListaOpcoesServicosAdapter adapter = new ListaOpcoesServicosAdapter(this);
        recyclerView.setAdapter(adapter);

        adapter.setApplication(getApplication());

        servicoViewModel = ViewModelProviders.of(this).get(ServicoViewModel.class);
        servicoViewModel.getAllServicos().observe(this, new Observer<List<Servico>>() {
            @Override
            public void onChanged(@Nullable List<Servico> servicos) {
                adapter.setServicos(servicos);
            }
        });
        if (intentEditar.hasExtra(VizualizarAgendamento.EDITAR)) {
            adapter.setId_agendamento_atualizar(intentEditar.getIntExtra(VizualizarAgendamento.ID_AGEDAMENTO_EDITAR,0));
        }
        buttonSeguir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapter.servicosSelecionados.size() > 0) {
                    if (intentEditar.hasExtra(VizualizarAgendamento.EDITAR)) {
                        Intent intent = new Intent(ListaOpcoesServicoAdicionarEditarAgendamentoActivity.this, SelecionarDataHorarioActivity.class);
                        intent.putParcelableArrayListExtra(SERVICOS_ESCOLHIDOS, adapter.servicosSelecionados);
                        intent.putExtra(VizualizarAgendamento.ID_AGEDAMENTO_EDITAR, intentEditar.getIntExtra(VizualizarAgendamento.ID_AGEDAMENTO_EDITAR, 0));
                        intent.putExtra(VizualizarAgendamento.EDITAR, intentEditar.getBooleanExtra(VizualizarAgendamento.EDITAR, true));
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(ListaOpcoesServicoAdicionarEditarAgendamentoActivity.this, SelecionarDataHorarioActivity.class);
                        intent.putParcelableArrayListExtra(SERVICOS_ESCOLHIDOS, adapter.servicosSelecionados);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(ListaOpcoesServicoAdicionarEditarAgendamentoActivity.this, "Selecione algum serviço", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
