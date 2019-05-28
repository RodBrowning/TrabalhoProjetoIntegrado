package com.example.tesouradouradaapp;

import android.app.TimePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

class SelecionarDataHorarioAdapter extends RecyclerView.Adapter<SelecionarDataHorarioAdapter.SelecionarDataHorarioHolder> {
    private List<Long> horariosLivresLong;
    List<List<Long>> listaDeParDeHorariosLivresLong;
    private List<String> horariosLivresString;
    private Context mContext;
    public int hourSelected, minutesSelected;
    public boolean timeSelected = false;

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
        selecionarDataHorarioHolder.textViewHorarioLivre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                Long horarioLivreInicio = new Long(listaDeParDeHorariosLivresLong.get(i).get(0));
                Long horarioLivreFim = new Long(listaDeParDeHorariosLivresLong.get(i).get(1));
                Date horarioLivreInicioDate = new Date(horarioLivreInicio);
                Date horarioLivreFimDate = new Date(horarioLivreFim);

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(horarioLivreInicioDate);
                int hora = calendar.get(calendar.HOUR_OF_DAY);
                int minutos = calendar.get(calendar.MINUTE);

                TimePickerFragment tpd = new TimePickerFragment(view.getRootView().getContext(), new TimePickerFragment.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        TextView textViewHorarioSelecionado = view.getRootView().findViewById(R.id.text_view_horario_selecionado);
                        hourSelected = selectedHour;
                        minutesSelected = selectedMinute;
                        timeSelected = true;
                        textViewHorarioSelecionado.setText(hourSelected+":"+minutesSelected);
                    }
                }, hora, minutos, DateFormat.is24HourFormat(view.getRootView().getContext()));

                tpd.setMin(hora, minutos);

                calendar.setTime(horarioLivreFimDate);
                hora = calendar.get(calendar.HOUR_OF_DAY);
                minutos = calendar.get(calendar.MINUTE);

                tpd.setMax(hora, minutos);
                tpd.setTitle(horarioLivreString);
                tpd.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return horariosLivresString.size();
    }

    public List<String> getStringDeHorarios(List<List<Long>> listaDeParDeHorariosLivresLong) {
        List<String> horariosLivresString = new ArrayList<>();
        SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat("HH:mm");
        for (int i = 0; i < listaDeParDeHorariosLivresLong.size(); i++) {

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Das ");
            stringBuilder.append(simpleDateFormatTime.format(listaDeParDeHorariosLivresLong.get(i).get(0)));
            stringBuilder.append(" às ");
            stringBuilder.append(simpleDateFormatTime.format(listaDeParDeHorariosLivresLong.get(i).get(1)));
            horariosLivresString.add(stringBuilder.toString());

        }
        return horariosLivresString;
    }

    class SelecionarDataHorarioHolder extends RecyclerView.ViewHolder {
        TextView textViewHorarioLivre;

        public SelecionarDataHorarioHolder(@NonNull View itemView) {
            super(itemView);
            textViewHorarioLivre = itemView.findViewById(R.id.text_view_horario_livre);
        }
    }

}
