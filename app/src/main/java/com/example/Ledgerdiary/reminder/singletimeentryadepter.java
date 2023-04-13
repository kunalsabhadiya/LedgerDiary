package com.example.Ledgerdiary.reminder;
        import android.content.Context;
        import android.content.Intent;
        import android.util.Log;
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
        import java.util.concurrent.TimeUnit;

public class singletimeentryadepter extends RecyclerView.Adapter<singletimeentryadepter.ViewHolder> {
    Context context;
    List<singletimeentrymodel> list;

    public singletimeentryadepter(Context context, List<singletimeentrymodel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public singletimeentryadepter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.occurancerow,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
         singletimeentrymodel model =list.get(position);
         String date=model.getDate();
        String[] parts = date.split(" ");
        String day = parts[0];
        String month= parts[1];

        holder.occrowmonth.setText(month);
        holder.occrowday.setText(day);
        holder.occrowdescription.setText(model.getDescription());
        holder.occrowtamount.setText(model.getAmount());
        holder.occrowcurrenttime.setText(model.getTime());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        LocalDate futureDate = LocalDate.parse(model.getDate(), formatter);
        LocalDate today = LocalDate.now();
        long due = ChronoUnit.DAYS.between(today, futureDate);


        if(due==0){
            holder.occrowdue.setText( "Due today");
            holder.occrowdue.setTextColor(ContextCompat.getColor(context,R.color.black));
        }else if(due>0){
            holder.occrowdue.setTextColor(ContextCompat.getColor(context,R.color.green));
            holder.occrowdue.setText( "Due in "+ due +" days");
        }else{
            holder.occrowdue.setTextColor(ContextCompat.getColor(context,R.color.gradcolor1));
            holder.occrowdue.setText( "Due was "+ model.getDate());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,updatesingletimeentry.class);
                intent.putExtra("occamount",model.getAmount());
                intent.putExtra("occdescription",model.getDescription());
                intent.putExtra("occtime",model.getTime());
                intent.putExtra("occdate",model.getDate());
                intent.putExtra("occdue",String.valueOf(due));
                intent.putExtra("timestamp",String.valueOf(model.getTimestamp()));
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

