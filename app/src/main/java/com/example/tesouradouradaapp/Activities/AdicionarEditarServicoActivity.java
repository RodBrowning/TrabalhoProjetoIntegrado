package com.example.tesouradouradaapp.Activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tesouradouradaapp.Adapters.ServicosAdapter;
import com.example.tesouradouradaapp.DataBase.Entities.Servico;
import com.example.tesouradouradaapp.R;
import com.example.tesouradouradaapp.ViewModels.ServicoViewModel;

import java.util.concurrent.ExecutionException;

public class AdicionarEditarServicoActivity extends AppCompatActivity {

    private EditText editTextNomeServico;
    private EditText editTextValorServico;
    private EditText editTextDuracaoServico;
    private ServicoViewModel servicoViewModel;
    private Servico servicoObj;
    private int idParaAtualizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_editar_servico);
        editTextNomeServico = findViewById(R.id.edit_text_nome_servico);
        editTextValorServico = findViewById(R.id.edit_text_valor_servico);
        editTextDuracaoServico = findViewById(R.id.edit_text_duracao_servico);
        servicoViewModel = ViewModelProviders.of(AdicionarEditarServicoActivity.this).get(ServicoViewModel.class);

        if (getIntent().hasExtra(ServicosAdapter.EXTRA_ID)) {
            idParaAtualizar = getIntent().getIntExtra(ServicosAdapter.EXTRA_ID, -1);
            try {
                servicoObj = servicoViewModel.getServico(idParaAtualizar);
                setTitle("Editar Serviço " + servicoObj.getNomeServico());
                editTextNomeServico.setText(servicoObj.getNomeServico());
                editTextValorServico.setText(String.valueOf(servicoObj.getValor()));
                editTextDuracaoServico.setText(String.valueOf(converterMilisegundosParaMinutos(servicoObj.getTempo())));
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            setTitle("Adicionar Serviço");
        }
    }

    private void sarvarAtualizarServico() {

        String nomeServico = editTextNomeServico.getText().toString().trim();
        String valorServico = editTextValorServico.getText().toString().trim();
        String duracaoServico = editTextDuracaoServico.getText().toString().trim();
        if (nomeServico.isEmpty() || valorServico.isEmpty() || duracaoServico.isEmpty()) {
            Toast.makeText(this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show();
            return;
        }

        Servico servico = new Servico(nomeServico, Float.parseFloat(valorServico), converterMinutosParaMilisegundos(Integer.parseInt(duracaoServico)));

        if (getIntent().hasExtra(ServicosAdapter.EXTRA_ID)) {
            servico.setId_servico(idParaAtualizar);
            servicoViewModel.update(servico);
            Toast.makeText(this, "Serviço editado", Toast.LENGTH_SHORT).show();
        } else {
            servicoViewModel.insert(servico);
            Toast.makeText(this, "Novo serviço criado", Toast.LENGTH_SHORT).show();
        }

        Intent intentParaServicos = new Intent(AdicionarEditarServicoActivity.this, ServicosActivity.class);
        startActivity(intentParaServicos);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private long converterMinutosParaMilisegundos(int minutos) {
        long mins = new Long((minutos * 60) * 1000);
        return mins;
    }

    private int converterMilisegundosParaMinutos(long minutos) {
        Long longMinutos = new Long(minutos);
        int mins = (longMinutos.intValue() / 1000) / 60;
        return mins;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_adicionar_servico_acivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_salvar_servico:
                sarvarAtualizarServico();
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
