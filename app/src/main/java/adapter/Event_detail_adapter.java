package adapter;//package adapter;
//
//import android.content.Context;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bp.eventbox.R;
//
//import java.util.ArrayList;
//
//import model.Event_detail_model;
//
//
//public class Event_detail_adapter extends RecyclerView.Adapter<Event_detail_adapter.ViewHolder> {
//
//    Context context;
//    ArrayList<Event_detail_model>recipt_models;
//
//    public Event_detail_adapter(Context context, ArrayList<Event_detail_model> recipt_models) {
//        this.context = context;
//        this.recipt_models = recipt_models;
//    }
//
//    @NonNull
//    @Override
//    public Event_detail_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipt,parent,false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull Event_detail_adapter.ViewHolder holder, int position) {
//
//        holder.title.setText(recipt_models.get(position).getTitle());
//        holder.detaile.setText(recipt_models.get(position).getDetaile());
//        holder.km.setText(recipt_models.get(position).getKm());
//
//        holder.image1.setImageResource(recipt_models.get(position).getImage1());
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return recipt_models.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//
//        TextView title,detaile,km;
//
//        ImageView image1;
//        public ViewHolder(View itemView) {
//            super(itemView);
//
//            title = itemView.findViewById(R.id.title);
//            detaile = itemView.findViewById(R.id.detaile);
//            km = itemView.findViewById(R.id.km);
//
//            image1 = itemView.findViewById(R.id.image1);
//        }
//    }
//}
