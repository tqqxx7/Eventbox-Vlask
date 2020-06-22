package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import eventbox.R;
import model.Profile;

public class ListModAdapter extends RecyclerView.Adapter<ListModAdapter.ViewHolder>  {
    private Context context;
    private ArrayList<Profile> listProfile;

    public ListModAdapter(Context context, ArrayList<Profile> listProfile) {
        this.context = context;
        this.listProfile = listProfile;
    }


    @NonNull
    @Override
    public ListModAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_moderator,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListModAdapter.ViewHolder holder, int position) {
        holder.tvName.setText((position+1) + ". " + listProfile.get(position).getName());
        holder.tvEmail.setText(listProfile.get(position).getEmail());
//        Glide.with(context).load(R)
    }


    @Override
    public int getItemCount() {
        return listProfile.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgAvatar;
        TextView tvName, tvEmail;
        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name_mod);
            tvEmail = itemView.findViewById(R.id.tv_email_mod);
        }
    }
}
