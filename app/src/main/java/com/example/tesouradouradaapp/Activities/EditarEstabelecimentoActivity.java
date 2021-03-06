package com.example.tesouradouradaapp.Activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.tesouradouradaapp.DataBase.Entities.Estabelecimento;
import com.example.tesouradouradaapp.R;
import com.example.tesouradouradaapp.ViewModels.EstabelecimentoViewModel;

public class EditarEstabelecimentoActivity extends AppCompatActivity {
    private EstabelecimentoViewModel estabelecimentoViewModel;
    private Estabelecimento estabelecimentoObj;
    private EditText editTextNomeEstabelecimento;
    private EditText editTextNomeProprietario;
    private NumberPicker horaDeAbertura;
    private NumberPicker minutosDeAbertura;
    private NumberPicker horaDeFechamento;
    private NumberPicker minutosDeFechamento;
    private Button buttonEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_estabelecimento);
        if (!MainActivity.APP_TITLE.isEmpty()) {
            setTitle("Editar Dados");
        }

        editTextNomeEstabelecimento = findViewById(R.id.edit_text_nome_estabelecimento);
        editTextNomeProprietario = findViewById(R.id.edit_text_nome_prorietario);


        horaDeAbertura = findViewById(R.id.number_picker_horas_abertura);
        minutosDeAbertura = findViewById(R.id.number_picker_minutos_abertura);
        horaDeFechamento = findViewById(R.id.number_picker_horas_fechamento);
        minutosDeFechamento = findViewById(R.id.number_picker_minutos_fechamento);
        buttonEditar = findViewById(R.id.bottom_editar_estabelecimento);

        horaDeAbertura.setMinValue(0);
        horaDeAbertura.setMaxValue(23);
        horaDeFechamento.setMinValue(0);
        horaDeFechamento.setMaxValue(23);
        final String[] minutos = {"00", "30"};
        minutosDeAbertura.setMinValue(0);
        minutosDeAbertura.setMaxValue(minutos.length - 1);
        minutosDeFechamento.setMinValue(0);
        minutosDeFechamento.setMaxValue(minutos.length - 1);
        minutosDeAbertura.setDisplayedValues(minutos);
        minutosDeFechamento.setDisplayedValues(minutos);

        // Preenche os campos
        estabelecimentoViewModel = ViewModelProviders.of(this).get(EstabelecimentoViewModel.class);
        estabelecimentoViewModel.getEstabelecimento().observe(this, new Observer<Estabelecimento>() {
            @Override
            public void onChanged(@Nullable Estabelecimento estabelecimento) {
                estabelecimentoObj = estabelecimento;
                editTextNomeEstabelecimento.setText(estabelecimento.getNomeEstabelecimento());
                editTextNomeProprietario.setText(estabelecimento.getNomeProprietario());

                String[] horarioInicio = estabelecimentoObj.getHorarioAbertura().split(":");
                int horasAbertura = Integer.parseInt(horarioInicio[0]);
                horaDeAbertura.setValue(horasAbertura);
                int minutosAbertura = Integer.parseInt(horarioInicio[1]);
                minutosDeAbertura.setValue(minutosAbertura == 0 ? 0 : 1);

                String[] horarioFinal = estabelecimentoObj.getHorarioFechamento().split(":");
                int horasFechamento = Integer.parseInt(horarioFinal[0]);
                horaDeFechamento.setValue(horasFechamento);
                int minutosFechamento = Integer.parseInt(horarioFinal[1]);
                minutosDeFechamento.setValue(minutosFechamento == 0 ? 0 : 1);
            }
        });

        buttonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nomeEstabelecimento = editTextNomeEstabelecimento.getText().toString().trim();
                String nomeProprietario = editTextNomeProprietario.getText().toString().trim();
                String abertura, fechamento;

                // Validação dos campos
                int diferencaEntreAberturaFechamento = (horaDeFechamento.getValue() + (minutosDeFechamento.getValue()/10)) - (horaDeAbertura.getValue() + (minutosDeAbertura.getValue()/10));
                if (nomeEstabelecimento.isEmpty() || nomeProprietario.isEmpty()) {
                    Toast.makeText(EditarEstabelecimentoActivity.this, "O nome do estabelecimento e proprietário são obrigatórios", Toast.LENGTH_SHORT).show();
                    return;
                } else if (diferencaEntreAberturaFechamento <= 0) {
                    Toast.makeText(EditarEstabelecimentoActivity.this, "Horario de abertura deve ser inferior ao horario de fechamento", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Define string de horario de abertura e fechamento
                if (minutosDeAbertura.getValue() == 0) {
                    abertura = String.format("%s:00", String.valueOf(horaDeAbertura.getValue()));
                } else {
                    abertura = String.format("%s:30", String.valueOf(horaDeAbertura.getValue()));
                }


                if (minutosDeFechamento.getValue() == 0) {
                    fechamento = String.format("%s:00", String.valueOf(horaDeFechamento.getValue()));
                } else {
                    fechamento = String.format("%s:30", String.valueOf(horaDeFechamento.getValue()));
                }

                // Atualiza registro no banco de dados
                Estabelecimento estabelecimentoParaAtualizar = new Estabelecimento(nomeEstabelecimento,
                        nomeProprietario,
                        abertura,
                        fechamento);
                estabelecimentoParaAtualizar.setIdEstabelecimento(estabelecimentoObj.getIdEstabelecimento());
                estabelecimentoViewModel.update(estabelecimentoParaAtualizar);
                Intent intent = new Intent(EditarEstabelecimentoActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                Toast.makeText(EditarEstabelecimentoActivity.this, "Dados cadastrais atualizados", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
