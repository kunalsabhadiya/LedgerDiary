package com.example.Ledgerdiary.reminder;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Ledgerdiary.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class occuranceadepter extends RecyclerView.Adapter<occuranceadepter.ViewHolder> {
  Context context;
  List<occurancemodel> list;

    public occuranceadepter(Context context, List<occurancemodel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public occuranceadepter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.occurancerow,parent,false);
        return new ViewHolder(view);
    }
    private String spinnerdate(String spinnerselecteditem,String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        Date userInputDate = sdf.parse(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(userInputDate);
        switch (spinnerselecteditem) {
            case "Daily":
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                break;
            case "Every 15 days":
                calendar.add(Calendar.DAY_OF_YEAR, 15);
                break;
            case "Weekly":
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
                break;
            case "Monthly":
                calendar.add(Calendar.MONTH, 1);
                break;
            case "Every 3 months":
                calendar.add(Calendar.MONTH, 3);
                break;
            case "Quarterly":
                calendar.add(Calendar.MONTH, 6);
                break;
            case "Yearly":
                calendar.add(Calendar.YEAR, 1);
                break;
        }

        return sdf.format(calendar.getTime());
    }

    @Override
    public void onBindViewHolder(@NonNull occuranceadepter.ViewHolder holder, int position) {
        occurancemodel model =list.get(position);
        String date= null;
        try {
            date = spinnerdate(model.getOcctime(), model.getDate());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        String[] parts = date.split(" ");
        String day = parts[0];
        String month= parts[1];

        holder.occrowmonth.setText(month);
        holder.occrowday.setText(day);
        holder.occrowdescription.setText(model.getDescription());
        holder.occrowtamount.setText(model.getAmount());
        holder.occrowcurrenttime.setText(model.getTime());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        LocalDate currentDate = LocalDate.parse(sdf.format(new Date()), formatter);
        LocalDate futureDate = null;
        try {
            futureDate = LocalDate.parse(spinnerdate(model.getOcctime(), model.getDate()),formatter);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        long daysBetween = ChronoUnit.DAYS.between(currentDate, futureDate);


        if(daysBetween==0){
            holder.occrowdue.setText( "Due today");
            holder.occrowdue.setTextColor(ContextCompat.getColor(context,R.color.black));
        }else if(daysBetween>0){
            holder.occrowdue.setTextColor(ContextCompat.getColor(context,R.color.green));
            holder.occrowdue.setText( "Due in "+ daysBetween +" days");
        }else{
            holder.occrowdue.setTextColor(ContextCompat.getColor(context,R.color.gradcolor1));
            holder.occrowdue.setText( "Due was "+ model.getDate());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,updateoccurance.class);
                intent.putExtra("occamount",model.getAmount());
                intent.putExtra("occdescription",model.getDescription());
                intent.putExtra("occtime",model.getTime());
                intent.putExtra("occdate",model.getDate());
                intent.putExtra("occdue",String.valueOf(daysBetween));
                intent.putExtra("timestamp",String.valueOf(model.getTimestamp()));
                intent.putExtra("reccurancetype",model.getOcctime());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView occrowmonth,occrowday,occrowdescription,occrowcurrenttime,occrowdue,occrowtamount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            occrowmonth=itemView.findViewById(R.id.occrowmonth);
            occrowday=itemView.findViewById(R.id.occrowday);
            occrowdescription=itemView.findViewById(R.id.occrowdescription);
            occrowcurrenttime=itemView.findViewById(R.id.occrowcurrenttime);
            occrowdue=itemView.findViewById(R.id.occrowdue);
            occrowtamount=itemView.findViewById(R.id.occrowtamount);
        }
    }
}
