package com.example.tesouradouradaapp.Adapters;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tesouradouradaapp.DataBase.Entities.Servico;
import com.example.tesouradouradaapp.R;
import com.example.tesouradouradaapp.ViewModels.AgendaServicosJoinViewModel;
import com.example.tesouradouradaapp.ViewModels.AgendaViewModel;
import com.example.tesouradouradaapp.ViewModels.ServicoViewModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ServicosAdapter extends RecyclerView.Adapter<ServicosAdapter.ServicosHolder> {
    public static final String EXTRA_ID = "com.example.tesouradouradaapp.EXTRA_ID";
    private Context mContext;
    private List<Servico> servicos = new ArrayList<>();
    private Application application;
    private ServicoViewModel servicoViewModel = new ServicoViewModel(application);
    private AgendaServicosJoinViewModel agendaServicosJoinViewModel = new AgendaServicosJoinViewModel(application);
    private AgendaViewModel agendaViewModel = new AgendaViewModel(application);
    private OnItemClickListener listener;

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
        servicosHolder.textViewDuracaoAtendimento.setText(duracaoTotalParaApresentacao(servico.getTempo()));
        servicosHolder.textViewValorAtendimento.setText(numberFormat.format(servico.getValor()));

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


    private String duracaoTotalParaApresentacao(long milisegundos) {
        long duracaoTotal = milisegundos;
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


    class ServicosHolder extends RecyclerView.ViewHolder {
        private TextView textViewNomeServico;
        private TextView textViewDuracaoAtendimento;
        private TextView textViewValorAtendimento;

        public ServicosHolder(@NonNull final View itemView) {
            super(itemView);
            textViewNomeServico = itemView.findViewById(R.id.text_view_servico);
            textViewDuracaoAtendimento = itemView.findViewById(R.id.text_view_duracao_servico);
            textViewValorAtendimento = itemView.findViewById(R.id.text_view_valor);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION){}
                    listener.onItemClick(servicos.get(position), itemView);
                }
            });
        }
    }


    public interface OnItemClickListener {
        void onItemClick(Servico servico, View itemView);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
