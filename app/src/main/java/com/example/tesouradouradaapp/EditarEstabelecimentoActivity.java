package com.example.tesouradouradaapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

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
        final String[] minutos = {"0", "30"};
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
                String nomeEstabelecimento = String.valueOf(editTextNomeEstabelecimento.getText());
                String nomeProprietario = String.valueOf(editTextNomeProprietario.getText());
                String abertura, fechamento;;

                // Validação dos campos
                int diferencaEntreAberturaFechamento =(horaDeFechamento.getValue() +  minutosDeFechamento.getValue()) - (horaDeAbertura.getValue() +  minutosDeAbertura.getValue());
                if(diferencaEntreAberturaFechamento <= 0){
                    Toast.makeText(EditarEstabelecimentoActivity.this, "Horario de abertura deve ser inferior ao horario de fechamento", Toast.LENGTH_SHORT).show();
                    return;
                } else if (nomeEstabelecimento.isEmpty() || nomeProprietario.isEmpty()){
                    Toast.makeText(EditarEstabelecimentoActivity.this, "Os Campos nome do estabelecimento e proprietario são obrigatórios", Toast.LENGTH_SHORT).show();
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

                Toast.makeText(EditarEstabelecimentoActivity.this, "Dados cadastrais atualizados", Toast.LENGTH_SHORT).show();
            }
        });


    }

}
