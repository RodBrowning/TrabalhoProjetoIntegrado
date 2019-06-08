package com.example.tesouradouradaapp;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ServicosActivity extends AppCompatActivity {

    private ServicoViewModel servicoViewModel;
    private FloatingActionButton floatingActionButtonAdicionarServico;
    private AgendaServicosJoinViewModel agendaServicosJoinViewModel = new AgendaServicosJoinViewModel(getApplication());
    private AgendaViewModel agendaViewModel = new AgendaViewModel(getApplication());


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
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        final RecyclerView recyclerView = findViewById(R.id.recycler_view_servicos);
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

        adapter.setOnItemClickListener(new ServicosAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final Servico servico, final View itemView) {
                PopupMenu popupMenu = new PopupMenu(getBaseContext(), itemView);
                popupMenu.inflate(R.menu.menu_list_item_servicos);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.menu_editar_servico:
                                Intent intent = new Intent(getApplicationContext(), AdicionarEditarServicoActivity.class);
                                intent.putExtra(ServicosAdapter.EXTRA_ID, servico.getId_servico());
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                                return true;
                            case R.id.menu_excluir_servico:
                                final StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("Excluir o servico pode desagendar algum agendamento.");
                                stringBuilder.append(System.getProperty("line.separator"));
                                stringBuilder.append(System.getProperty("line.separator"));
                                stringBuilder.append("Deseja continuar?");
                                new AlertDialog.Builder(itemView.getContext())
                                        .setTitle("Excluir")
                                        .setMessage(stringBuilder.toString())
                                        .setIcon(R.drawable.ic_alert_excluir)
                                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                // Descobrir quem esta agendado para o servico a ser excluido
                                                List<Integer> listaAgendadosParaServico = getListaAgendadosParaServico(agendaServicosJoinViewModel, servico);

                                                servicoViewModel.delete(servico);

                                                // Verificar se existe servico sem agendamento e deletar
                                                deletarAgendamentosSemServico(listaAgendadosParaServico, agendaServicosJoinViewModel, agendaViewModel);
                                                // Atualizar horario de fim de atendimento
                                                // atualizarHorarioDosAgendamentosComServicoExcluido(listaAgendadosParaServico, agendaViewModel, servico);

                                                Toast.makeText(getApplicationContext(), "Serviço " + servico.getNomeServico() + " excluido", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .setNegativeButton("Não", null).show();
                                return true;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }

        });

    }

    private List<Integer> getListaAgendadosParaServico(AgendaServicosJoinViewModel agendaServicosJoinViewModel, Servico servico) {
        List<AgendaServicosJoin> agendadosParaServicoJoin;
        List<Integer> listaAgendadosParaServico = new ArrayList<>();
        try {
            agendadosParaServicoJoin = agendaServicosJoinViewModel.getAgendamentosParaServico(servico.getId_servico());
            for (AgendaServicosJoin agendaServicosJoin : agendadosParaServicoJoin) {
                listaAgendadosParaServico.add(agendaServicosJoin.getIdAgendamentoJoin());
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return listaAgendadosParaServico;
    }

    private void deletarAgendamentosSemServico(List<Integer> listaAgendadosParaServico, AgendaServicosJoinViewModel agendaServicosJoinViewModel, AgendaViewModel agendaViewModel) {
        List<AgendaServicosJoin> listaServicosParaAgendamento;

        for (Integer integer : listaAgendadosParaServico) {
            try {
                listaServicosParaAgendamento = agendaServicosJoinViewModel.getServicosParaAgendamento(integer);
                if (listaServicosParaAgendamento.size() == 0) {
                    Agendamento agendamento = agendaViewModel.getAgendamento(integer);
                    agendaViewModel.delete(agendamento);
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
