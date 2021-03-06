package com.example.tesouradouradaapp.Activities;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tesouradouradaapp.Adapters.AdicionarEditarAgendamentoAdapter;
import com.example.tesouradouradaapp.Adapters.AgendaAdapter;
import com.example.tesouradouradaapp.DataBase.Entities.Agendamento;
import com.example.tesouradouradaapp.DataBase.Entities.Servico;
import com.example.tesouradouradaapp.R;
import com.example.tesouradouradaapp.ViewModels.AgendaServicosJoinViewModel;
import com.example.tesouradouradaapp.ViewModels.AgendaViewModel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class VizualizarAgendamentoActivity extends AppCompatActivity {
    public static final String ID_AGEDAMENTO_EDITAR = "com.example.tesouradouradaapp.ID_AGEDAMENTO_EDITAR";
    public static final String EDITAR = "com.example.tesouradouradaapp.EDITAR";
    TextView textViewNomeCliente, textViewDataSelecionada, textViewDuracaoTotal, textViewValorTotal;
    private Agendamento agendamento;
    private AgendaViewModel agendaViewModel;
    private AgendaServicosJoinViewModel agendaServicosJoinViewModel;
    private Context mContext;
    private int duracaoTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(MainActivity.APP_TITLE);
        setContentView(R.layout.activity_vizualizar_agendamento);
        textViewNomeCliente = findViewById(R.id.text_view_nome_cliente);
        textViewDuracaoTotal = findViewById(R.id.text_view_duracao_total);
        textViewValorTotal = findViewById(R.id.text_view_valor_total);
        textViewDataSelecionada = findViewById(R.id.text_view_data_selecionada);
        this.mContext = VizualizarAgendamentoActivity.this;
        this.agendaViewModel = new AgendaViewModel(getApplication());

        Intent intent = getIntent();
        int id_agendamento = intent.getIntExtra(AgendaAdapter.ID_AGENDAMENTO_EDITAR, 0);
        try {
            agendamento = agendaViewModel.getAgendamento(id_agendamento);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        textViewNomeCliente.setText(agendamento.getCliente());

        RecyclerView recyclerView = findViewById(R.id.recycler_view_servicos_selecionados);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        agendaServicosJoinViewModel = ViewModelProviders.of(VizualizarAgendamentoActivity.this).get(AgendaServicosJoinViewModel.class);
        List<Servico> servicosSelecionados = null;
        try {
            servicosSelecionados = agendaServicosJoinViewModel.getServicosParaAgendamentoJoinServicos(agendamento.getId_agendamento());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final AdicionarEditarAgendamentoAdapter adapter = new AdicionarEditarAgendamentoAdapter();
        adapter.setServicosSelecionados(servicosSelecionados);
        recyclerView.setAdapter(adapter);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd/MM/yyyy - HH:mm");
        SimpleDateFormat sdfHorarioFim = new SimpleDateFormat("HH:mm");

        Date dataSelecionada = new Date(agendamento.getHorarioInicio());
        duracaoTotal = duracaoTotal(servicosSelecionados);
        Date dataFinal = new Date(agendamento.getHorarioInicio() + duracaoTotal);

        textViewValorTotal.setText(valorTotalParaApresentacao(servicosSelecionados));

        textViewDuracaoTotal.setText(duracaoTotalParaApresentacao(servicosSelecionados));

        textViewDataSelecionada.setText(sdf.format(dataSelecionada) + " às " + sdfHorarioFim.format(dataFinal));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_agendamento, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_editar_agendamento:
                Date horarioPresente = new Date();
                if (horarioPresente.getTime() > agendamento.getHorarioInicio() && horarioPresente.getTime() < (agendamento.getHorarioInicio() + duracaoTotal)) {
                    Toast.makeText(mContext, "Erro: Agendamento em andamento.", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (horarioPresente.getTime() > (agendamento.getHorarioInicio() + duracaoTotal)) {
                    Toast.makeText(mContext, "Erro: Agendamento ja foi concluido.", Toast.LENGTH_SHORT).show();
                    return false;
                }
                Intent intent = getIntent();
                int id_agendamento = intent.getIntExtra(AgendaAdapter.ID_AGENDAMENTO_EDITAR, 0);
                Intent intentAtualizar = new Intent(VizualizarAgendamentoActivity.this, ListaOpcoesServicoAdicionarEditarAgendamentoActivity.class);
                intentAtualizar.putExtra(ID_AGEDAMENTO_EDITAR, id_agendamento);
                intentAtualizar.putExtra(EDITAR, true);
                startActivity(intentAtualizar);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            case R.id.menu_excluir_agendamento:
                AlertDialog dialog = new AlertDialog.Builder(mContext)
                        .setTitle("Excluir")
                        .setMessage("Excluir agendamento com " + agendamento.getCliente() + "?")
                        .setIcon(R.drawable.ic_alert_excluir)
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                agendaViewModel.delete(agendamento);
                                Toast.makeText(mContext, "Agendamento com " + agendamento.getCliente() + " excluido", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(mContext.getApplicationContext(), MainActivity.class);
                                mContext.startActivity(intent);
                                overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
                            }
                        })
                        .setNegativeButton("Não", null).show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Calcula duracao

    private int duracaoTotal(List<Servico> servicos) {
        int duracaoTotal = 0;
        for (Servico servico : servicos) {
            duracaoTotal += servico.getTempo();
        }
        return duracaoTotal;
    }

    private String duracaoTotalParaApresentacao(List<Servico> servicos) {
        int duracaoTotal = duracaoTotal(servicos);
        long duracaohoras = duracaoTotal / 1000 / 60 / 60;
        long duracaoMinutos = (duracaoTotal / 1000 / 60) % 60;
        String duracaoString;

        if (duracaohoras > 0) {
            if (duracaohoras == 1) {
                if (duracaoMinutos == 0) {
                    duracaoString = String.format("%d hr", duracaohoras);
                } else {
                    duracaoString = String.format("%d hr %02d mins", duracaohoras, duracaoMinutos);
                }
            } else {
                if (duracaoMinutos == 0) {
                    duracaoString = String.format("%d hrs", duracaohoras);
                } else {
                    duracaoString = String.format("%d hrs %02d mins", duracaohoras, duracaoMinutos);
                }
            }
        } else {
            duracaoString = String.format("%d mins", duracaoMinutos);
        }
        return duracaoString;
    }

    // Calcula valor total

    private String valorTotalParaApresentacao(List<Servico> servicos) {
        Float valorTotal = new Float(0);
        Locale brasil = new Locale("pt", "BR");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(brasil);
        for (Servico servico : servicos) {
            valorTotal += servico.getValor();
        }
        return numberFormat.format(valorTotal);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
