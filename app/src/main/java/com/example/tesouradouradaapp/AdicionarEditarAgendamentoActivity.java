package com.example.tesouradouradaapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class AdicionarEditarAgendamentoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_editar_agendamento);
        setTitle("Adicionar Agendamento");
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
}
