package com.example.tesouradouradaapp.Activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.tesouradouradaapp.DataBase.Entities.Estabelecimento;
import com.example.tesouradouradaapp.R;
import com.example.tesouradouradaapp.ViewModels.EstabelecimentoViewModel;

public class DadosEstabelecimentoActivity extends AppCompatActivity {
    private TextView textViewNomeEstamelecimento;
    private TextView textViewNomeProprietario;
    private TextView textViewHorarioFuncionamento;
    private EstabelecimentoViewModel estabelecimentoViewModel;
    private Estabelecimento estabelecimentoObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_estabelecimento);
        setTitle("Dados " + MainActivity.APP_TITLE);
        textViewNomeEstamelecimento = findViewById(R.id.text_view_nome_estabelecimento);
        textViewNomeProprietario = findViewById(R.id.text_view_nome_proprietario);
        textViewHorarioFuncionamento = findViewById(R.id.text_view_horario_funcionamento);

        estabelecimentoViewModel = ViewModelProviders.of(this).get(EstabelecimentoViewModel.class);
        estabelecimentoViewModel.getEstabelecimento().observe(this, new Observer<Estabelecimento>() {
            @Override
            public void onChanged(@Nullable Estabelecimento estabelecimento) {
                estabelecimentoObj = estabelecimento;
                textViewNomeEstamelecimento.setText(estabelecimentoObj.getNomeEstabelecimento());
                textViewNomeProprietario.setText(estabelecimentoObj.getNomeProprietario());
                StringBuilder horarioFuncionamento = new StringBuilder();
                horarioFuncionamento.append("Das ");
                horarioFuncionamento.append(estabelecimentoObj.getHorarioAbertura());
                horarioFuncionamento.append(" ás ");
                horarioFuncionamento.append(estabelecimentoObj.getHorarioFechamento());
                textViewHorarioFuncionamento.setText(horarioFuncionamento.toString());
            }
        });
    }

    private void irParaEditarEstabelecimentoActivity() {
        Intent intent = new Intent(DadosEstabelecimentoActivity.this, EditarEstabelecimentoActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_estabelecimento_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_editar_estabelecimento:
                irParaEditarEstabelecimentoActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
