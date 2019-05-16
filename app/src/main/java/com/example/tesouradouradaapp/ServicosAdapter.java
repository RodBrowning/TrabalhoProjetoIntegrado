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

public class ServicosAdapter extends RecyclerView.Adapter<ServicosAdapter.ServicosHolder> {
    public static final String EXTRA_ID = "com.example.tesouradouradaapp.EXTRA_ID";
    private Context mContext;
    private List<Servico> servicos = new ArrayList<>();
    private Application application;
    private ServicosRepository servicosRepository = new ServicosRepository(application);

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
        servicosHolder.textViewDuracaoAtendimento.setText(String.valueOf(servico.getTempo() + " min"));
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
                                intent.putExtra(ServicosAdapter.EXTRA_ID, servico.getIdServico());
                                mContext.startActivity(intent);
                                return true;
                            case R.id.menu_excluir_servico:
                                new AlertDialog.Builder(mContext)
                                        .setTitle("Excluir")
                                        .setMessage("Excluir " + servico.getNomeServico() + "?")
                                        .setIcon(R.drawable.ic_alert_excluir)
                                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                servicosRepository.delete(servico);
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
