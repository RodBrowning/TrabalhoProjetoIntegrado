package com.example.tesouradouradaapp;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

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

        if (getIntent().hasExtra(ServicosAdapter.EXTRA_ID)){
            setTitle("Editar Serviço "+ servicoObj.getNomeServico());
            idParaAtualizar = getIntent().getIntExtra(ServicosAdapter.EXTRA_ID, -1);
            try {
                servicoObj = servicoViewModel.getServico(idParaAtualizar);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setTitle("Editar Serviço "+ servicoObj.getNomeServico());
            editTextNomeServico.setText(servicoObj.getNomeServico());
            editTextValorServico.setText(String.valueOf(servicoObj.getValor()));
            editTextDuracaoServico.setText(String.valueOf(servicoObj.getTempo()));
        } else {
            setTitle("Adicionar Serviço");
        }
    }

    private void sarvarAtualizarServico() {
        String nomeServico = editTextNomeServico.getText().toString().trim();
        float valorServico = Float.parseFloat(editTextValorServico.getText().toString().trim());
        int duracaoServico = Integer.parseInt(editTextDuracaoServico.getText().toString().trim());

        if (nomeServico.isEmpty() || valorServico < 0 || duracaoServico < 0) {
            Toast.makeText(this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show();
            return;
        }
        Servico servico = new Servico(nomeServico, valorServico, duracaoServico);

        if(getIntent().hasExtra(ServicosAdapter.EXTRA_ID)){
            servico.setIdServico(idParaAtualizar);
            servicoViewModel.update(servico);
            Toast.makeText(this, "Serviço editado", Toast.LENGTH_SHORT).show();
        } else {
            servicoViewModel.insert(servico);
            Toast.makeText(this, "Novo serviço criado", Toast.LENGTH_SHORT).show();
        }

        Intent intentParaServicos = new Intent(AdicionarEditarServicoActivity.this, ServicosActivity.class);
        startActivity(intentParaServicos);
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
}
