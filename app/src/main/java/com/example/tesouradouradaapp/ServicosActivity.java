package com.example.tesouradouradaapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;

import java.util.List;

public class ServicosActivity extends AppCompatActivity {

    private ServicoViewModel servicoViewModel;
    private FloatingActionButton floatingActionButtonAdicionarServico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicos);
        setTitle("Serviços " + MainActivity.APP_TITLE);

        floatingActionButtonAdicionarServico = findViewById(R.id.button_add_servico);
        floatingActionButtonAdicionarServico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ServicosActivity.this, AdicionarEditarServicoActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view_servicos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final ServicosAdapter adapter = new ServicosAdapter(ServicosActivity.this);
        recyclerView.setAdapter(adapter);

        servicoViewModel = ViewModelProviders.of(this).get(ServicoViewModel.class);
        servicoViewModel.getAllServicos().observe(this, new Observer<List<Servico>>() {
            @Override
            public void onChanged(@Nullable List<Servico> servicos) {
                adapter.setServicos(servicos);
            }
        });

    }
}
