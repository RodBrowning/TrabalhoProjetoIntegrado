package com.example.tesouradouradaapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AgendaViewModel agendaViewModel;
    private EstabelecimentoViewModel estabelecimentoViewModel;
    private Estabelecimento estabelecimentoObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        estabelecimentoViewModel = ViewModelProviders.of(this).get(EstabelecimentoViewModel.class);
        estabelecimentoViewModel.getEstabelecimento().observe(this, new Observer<Estabelecimento>() {
            @Override
            public void onChanged(@Nullable Estabelecimento estabelecimento) {
                estabelecimentoObj = estabelecimento;
                setTitle(estabelecimentoObj.getNomeEstabelecimento());
            }
        });


        RecyclerView recyclerView = findViewById(R.id.recycler_view_agenda);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final AgendaAdapter adapter = new AgendaAdapter();
        recyclerView.setAdapter(adapter);


        agendaViewModel = ViewModelProviders.of(this).get(AgendaViewModel.class);
        agendaViewModel.getAgenda().observe(this, new Observer<List<Agendamento>>() {
            @Override
            public void onChanged(@Nullable List<Agendamento> agendamentos) {
                adapter.setAgenda(agendamentos);
            }
        });


    }

    private void irParaEditarDadosActivity(){
        Intent intent = new Intent(MainActivity.this, EditarEstabelecimentoActivity.class);
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
            case R.id.menu_edit_dados:
                irParaEditarDadosActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
