package adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import eventbox.EventDetailActivity;
import eventbox.R;
import model.Event;

public class List_event_adapter extends RecyclerView.Adapter<List_event_adapter.ViewHolder> {

    private Context context;
    private ArrayList<Event> list_events;

    public List_event_adapter(Context context, ArrayList<Event> list_events) {
        this.context = context;
        this.list_events = list_events;
    }

    @NonNull
    @Override
    public List_event_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_event,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final List_event_adapter.ViewHolder holder, final int position) {
        Picasso.get().load("http://sukien.vanlanguni.edu.vn/"+list_events.get(position).getAvatar()).placeholder(R.drawable.img_loading2).resize(1300,500).centerCrop().into(holder.image1);

        holder.image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EventDetailActivity.class);
                intent.putExtra("eventdetail",  list_events.get(position));
                context.startActivity(intent);
            }
        });
        holder.tvTitle.setText(list_events.get(position).getTitle());
        holder.tvDes.setText(list_events.get(position).getShortDesc());
        holder.tvStartDate.setText(list_events.get(position).getStartDate());
        holder.tvEndDate.setText(list_events.get(position).getEndDate());
        holder.tvTicket.setText(list_events.get(position).getTicketNumber());
        holder.lnlItemEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EventDetailActivity.class);
                intent.putExtra("eventdetail",  list_events.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_events.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image1;
        TextView tvTitle, tvDes, tvStartDate, tvEndDate, tvTicket;
        LinearLayout lnlItemEvent;
        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.TextViewTitle);
            tvDes = itemView.findViewById(R.id.TextViewDes);
            tvStartDate = itemView.findViewById(R.id.TextViewStartDate);
            tvEndDate = itemView.findViewById(R.id.TextViewEndDate);
            tvTicket = itemView.findViewById(R.id.TextViewTicket);
            image1 = itemView.findViewById(R.id.image1);
            lnlItemEvent = itemView.findViewById(R.id.LinearLayoutItemEvent);
        }
    }
}

