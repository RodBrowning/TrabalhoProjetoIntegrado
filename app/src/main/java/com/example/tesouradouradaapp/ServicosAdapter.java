package com.example.tesouradouradaapp;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class ServicosAdapter extends RecyclerView.Adapter<ServicosAdapter.ServicosHolder> {
    public static final String EXTRA_ID = "com.example.tesouradouradaapp.EXTRA_ID";
    private Context mContext;
    private List<Servico> servicos = new ArrayList<>();
    private Application application;
    private ServicoViewModel servicoViewModel = new ServicoViewModel(application);
    private AgendaServicosJoinViewModel agendaServicosJoinViewModel = new AgendaServicosJoinViewModel(application);
    private AgendaViewModel agendaViewModel = new AgendaViewModel(application);

    public ServicosAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public ServicosHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.servico, viewGroup, false);
        return new ServicosHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ServicosHolder servicosHolder, int i) {
        final Servico servico = servicos.get(i);
        Locale brasil = new Locale("pt", "BR");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(brasil);
        servicosHolder.textViewNomeServico.setText(servico.getNomeServico());
        servicosHolder.textViewDuracaoAtendimento.setText(String.valueOf(converterMilisegundosParaMinutos(servico.getTempo()) + "min"));
        servicosHolder.textViewValorAtendimento.setText(numberFormat.format(servico.getValor()));
        servicosHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(mContext, servicosHolder.relativeLayout);
                popupMenu.inflate(R.menu.menu_list_item_servicos);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.menu_editar_servico:
                                Intent intent = new Intent(mContext, AdicionarEditarServicoActivity.class);
                                intent.putExtra(ServicosAdapter.EXTRA_ID, servico.getId_servico());
                                mContext.startActivity(intent);
                                return true;
                            case R.id.menu_excluir_servico:
                                final StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("Excluir o servico pode desagendar algum agendamento.");
                                stringBuilder.append(System.getProperty("line.separator"));
                                stringBuilder.append(System.getProperty("line.separator"));
                                stringBuilder.append("Deseja continuar?");
                                new AlertDialog.Builder(mContext)
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

                                                Toast.makeText(mContext, "Serviço " + servico.getNomeServico() + " excluido", Toast.LENGTH_SHORT).show();
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

    @Override
    public int getItemCount() {
        return servicos.size();
    }

    public void setServicos(List<Servico> servicos) {
        this.servicos = servicos;
        notifyDataSetChanged();
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    private int converterMilisegundosParaMinutos(long minutos) {
        Long longMinutos = new Long(minutos);
        int mins = (longMinutos.intValue() / 1000) / 60;
        return mins;
    }

    private List<Integer> getListaAgendadosParaServico(AgendaServicosJoinViewModel agendaServicosJoinViewModel, Servico servico){
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
/*
    private void atualizarHorarioDosAgendamentosComServicoExcluido(List<Integer> listaAgendadosParaServico, AgendaViewModel agendaViewModel, Servico servico){
        Agendamento agendamento;

        for (Integer integer : listaAgendadosParaServico) {
            try {
                agendamento = agendaViewModel.getAgendamento(integer);
                if(agendamento != null){
                    Agendamento agendamentoParaAtualizar = new Agendamento(
                            agendamento.getCliente(),
                            agendamento.getHorarioInicio(),
                            agendamento.getHorarioFim() - servico.getTempo(),
                            agendamento.getCriadoEm());
                    agendamentoParaAtualizar.setId_agendamento(agendamento.getId_agendamento());
                    agendaViewModel.update(agendamentoParaAtualizar);
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

*/


    class ServicosHolder extends RecyclerView.ViewHolder {
        private TextView textViewNomeServico;
        private TextView textViewDuracaoAtendimento;
        private TextView textViewValorAtendimento;
        private RelativeLayout relativeLayout;

        public ServicosHolder(@NonNull View itemView) {
            super(itemView);
            textViewNomeServico = itemView.findViewById(R.id.text_view_servico);
            textViewDuracaoAtendimento = itemView.findViewById(R.id.text_view_duracao_servico);
            textViewValorAtendimento = itemView.findViewById(R.id.text_view_valor);
            relativeLayout = itemView.findViewById(R.id.relative_layout_servico_item);
        }
    }
}
