package com.example.tesouradouradaapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tesouradouradaapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SelecionarDataHorarioAdapter extends RecyclerView.Adapter<SelecionarDataHorarioAdapter.SelecionarDataHorarioHolder> {
    private List<Long> horariosLivresLong;
    List<List<Long>> listaDeParDeHorariosLivresLong;
    private List<String> horariosLivresString;
    private Context mContext;
    private OnItemClickListener listener;

    public SelecionarDataHorarioAdapter(List<List<Long>> listaDeParDeHorariosLivresLong) {
        this.listaDeParDeHorariosLivresLong = listaDeParDeHorariosLivresLong;
        this.horariosLivresString = getStringDeHorarios(this.listaDeParDeHorariosLivresLong);
    }

    @NonNull
    @Override
    public SelecionarDataHorarioHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.horario_livre, viewGroup, false);
        return new SelecionarDataHorarioHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SelecionarDataHorarioHolder selecionarDataHorarioHolder, final int i) {
        final String horarioLivreString = horariosLivresString.get(i);
        selecionarDataHorarioHolder.textViewHorarioLivre.setText(horarioLivreString);
    }

    @Override
    public int getItemCount() {
        return horariosLivresString.size();
    }

    public List<String> getStringDeHorarios(List<List<Long>> listaDeParDeHorariosLivresLong) {
        List<String> horariosLivresString = new ArrayList<>();
        SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat("HH:mm");
        for (int i = 0; i < listaDeParDeHorariosLivresLong.size(); i++) {
            long horarioLivreInicio = listaDeParDeHorariosLivresLong.get(i).get(0);
            long horarioLivreFim = listaDeParDeHorariosLivresLong.get(i).get(1);
            if (horarioLivreInicio == horarioLivreFim) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Ás ");
                stringBuilder.append(simpleDateFormatTime.format(horarioLivreInicio));
                horariosLivresString.add(stringBuilder.toString());

            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Das ");
                stringBuilder.append(simpleDateFormatTime.format(horarioLivreInicio));
                stringBuilder.append(" às ");
                stringBuilder.append(simpleDateFormatTime.format(horarioLivreFim));
                horariosLivresString.add(stringBuilder.toString());

            }

        }
        return horariosLivresString;
    }


    class SelecionarDataHorarioHolder extends RecyclerView.ViewHolder {
        TextView textViewHorarioLivre;

        public SelecionarDataHorarioHolder(@NonNull final View itemView) {
            super(itemView);
            textViewHorarioLivre = itemView.findViewById(R.id.text_view_horario_livre);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                    }
                    listener.onItemClick(itemView, listaDeParDeHorariosLivresLong.get(position), horariosLivresString.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, List<Long> parDeHorariosLivre, String horariosLivresString);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
