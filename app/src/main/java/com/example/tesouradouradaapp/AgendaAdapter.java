package com.example.tesouradouradaapp;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AsyncPlayer;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.AgendamentoHolder> {
    private List<Agendamento> agenda = new ArrayList<>();
    private Context mContext;
    private AgendamentoRepository agendamentoRepository;


    public AgendaAdapter(Context context) {
        this.mContext = context;
        this.agendamentoRepository = new AgendamentoRepository(mContext);
    }


    @NonNull
    @Override
    public AgendamentoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.agendamento, viewGroup, false);
        return new AgendamentoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AgendamentoHolder agendamentoHolder, int i) {
        final Agendamento agendamento = agenda.get(i);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        agendamentoHolder.textViewDuracao.setText(agendamento.getDuracaoEmMinutos() + " min");
        agendamentoHolder.textViewNomeCliente.setText(agendamento.getCliente());
        agendamentoHolder.textViewDataMarcada.setText(sdf.format(agendamento.getHorarioInicio()));
        agendamentoHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(mContext, agendamentoHolder.relativeLayout);
                popupMenu.inflate(R.menu.menu_list_item_agenda);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.menu_editar_agendamento:
                                Toast.makeText(mContext, "Editar Agendamento com " + agendamento.getCliente(), Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.menu_excluir_agendamento:
                                new AlertDialog.Builder(mContext)
                                        .setTitle("Excluir")
                                        .setMessage("Excluir agendamento com " + agendamento.getCliente() + "?")
                                        .setIcon(R.drawable.ic_alert_excluir)
                                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                agendamentoRepository.deleteAgendamento(agendamento);
                                                Toast.makeText(mContext, "Agendamento com " + agendamento.getCliente() + " excluido", Toast.LENGTH_SHORT).show();
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
        return agenda.size();
    }

    public void setAgenda(List<Agendamento> agenda) {
        this.agenda = agenda;
        notifyDataSetChanged();

    }


    class AgendamentoHolder extends RecyclerView.ViewHolder {
        private TextView textViewNomeCliente;
        private TextView textViewDuracao;
        private TextView textViewDataMarcada;
        private RelativeLayout relativeLayout;


        public AgendamentoHolder(@NonNull View itemView) {
            super(itemView);
            textViewDuracao = itemView.findViewById(R.id.duracao_atendimento);
            textViewNomeCliente = itemView.findViewById(R.id.nome_cliente);
            textViewDataMarcada = itemView.findViewById(R.id.data_marcada);
            relativeLayout = itemView.findViewById(R.id.relative_layout_agenda_item);
        }
    }
}
