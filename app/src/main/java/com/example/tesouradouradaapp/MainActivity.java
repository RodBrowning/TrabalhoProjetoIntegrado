package com.example.tesouradouradaapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AgendaViewModel agendaViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        agendaViewModel = ViewModelProviders.of(this).get(AgendaViewModel.class);
        agendaViewModel.getAgenda().observe(this, new Observer<List<Agendamento>>() {
            @Override
            public void onChanged(@Nullable List<Agendamento> agendamentos) {
                Toast.makeText(MainActivity.this, "onChanged", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
