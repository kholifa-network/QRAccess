package com.kholifa.qraccess.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.kholifa.qraccess.R;
import com.kholifa.qraccess.RoomDatabaseStore.CreationHistory;

import java.util.List;

public class CreationAdapter extends RecyclerView.Adapter<CreationAdapter.MyViewHolder> {

    ScannerAdapter.interface_history_fun interface_history;
    Context context;
    List<CreationHistory> history_data;

    public CreationAdapter(Context context, ScannerAdapter.interface_history_fun interface_history) {
        this.context = context;
        this.interface_history = interface_history;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (history_data != null) {
            CreationHistory current = history_data.get(position);
            holder.txt_his_result.setText(current.getData());
            holder.txt_his_result_type.setText(current.getDatatype());

            if (holder.txt_his_result.getText().length() == 0) {
                holder.txt_his_result.setText("No Data");
            }
            if (current.getDatatype().equalsIgnoreCase("Facebook")) {
                holder.img_his_type.setBackground(context.getResources().getDrawable(R.drawable.circlered));
                holder.img_his_type.setImageResource(R.drawable.ic_facebook);
            } else if (current.getDatatype().equalsIgnoreCase("Youtube")) {
                holder.img_his_type.setBackground(context.getResources().getDrawable(R.drawable.circlered));
                holder.img_his_type.setImageResource(R.drawable.ic_youtube);
            } else if (current.getDatatype().equalsIgnoreCase("Instagram")) {
                holder.img_his_type.setBackground(context.getResources().getDrawable(R.drawable.circlered));
                holder.img_his_type.setImageResource(R.drawable.ic_instagram);
            } else if (current.getDatatype().equalsIgnoreCase("Whatsapp")) {
                holder.img_his_type.setBackground(context.getResources().getDrawable(R.drawable.circlered));
                holder.img_his_type.setImageResource(R.drawable.ic_whatsapp);
            } else if (current.getDatatype().equalsIgnoreCase("Paypal")) {
                holder.img_his_type.setBackground(context.getResources().getDrawable(R.drawable.circlered));
                holder.img_his_type.setImageResource(R.drawable.ic_paypal);
            } else if (current.getDatatype().equalsIgnoreCase("Twitter")) {
                holder.img_his_type.setBackground(context.getResources().getDrawable(R.drawable.circlered));
                holder.img_his_type.setImageResource(R.drawable.ic_twitter);
            } else if (current.getDatatype().equalsIgnoreCase("Viber")) {
                holder.img_his_type.setBackground(context.getResources().getDrawable(R.drawable.circlered));
                holder.img_his_type.setImageResource(R.drawable.ic_viber);
            } else if (current.getDatatype().equalsIgnoreCase("Wi-Fi")) {
                holder.img_his_type.setBackground(context.getResources().getDrawable(R.drawable.circlered));
                holder.img_his_type.setImageResource(R.drawable.ic_wifi);
            } else if (current.getDatatype().equalsIgnoreCase("URL")) {
                holder.img_his_type.setBackground(context.getResources().getDrawable(R.drawable.circlered));
                holder.img_his_type.setImageResource(R.drawable.ic_url);
            } else if (current.getDatatype().equalsIgnoreCase("Contact")) {
                holder.img_his_type.setBackground(context.getResources().getDrawable(R.drawable.circlered));
                holder.img_his_type.setImageResource(R.drawable.ic_contact);
            } else if (current.getDatatype().equalsIgnoreCase("Telephone")) {
                holder.img_his_type.setBackground(context.getResources().getDrawable(R.drawable.circlered));
                holder.img_his_type.setImageResource(R.drawable.ic_call);
            } else if (current.getDatatype().equalsIgnoreCase("Spotify")) {
                holder.img_his_type.setBackground(context.getResources().getDrawable(R.drawable.circlered));
                holder.img_his_type.setImageResource(R.drawable.ic_spotify);
            } else if (current.getDatatype().equalsIgnoreCase("Text")) {
                holder.img_his_type.setBackground(context.getResources().getDrawable(R.drawable.circlered));
                holder.img_his_type.setImageResource(R.drawable.ic_text);
            } else if (current.getDatatype().equalsIgnoreCase("Email")) {
                holder.img_his_type.setBackground(context.getResources().getDrawable(R.drawable.circlered));
                holder.img_his_type.setImageResource(R.drawable.ic_email);
            } else if (current.getDatatype().equalsIgnoreCase("SMS")) {
                holder.img_his_type.setBackground(context.getResources().getDrawable(R.drawable.circlered));
                holder.img_his_type.setImageResource(R.drawable.ic_sms);
            } else if (current.getDatatype().equalsIgnoreCase("Calendar")) {
                holder.img_his_type.setBackground(context.getResources().getDrawable(R.drawable.circlered));
                holder.img_his_type.setImageResource(R.drawable.ic_calendar);
            } else if (current.getDatatype().equalsIgnoreCase("My Card")) {
                holder.img_his_type.setBackground(context.getResources().getDrawable(R.drawable.circlered));
                holder.img_his_type.setImageResource(R.drawable.ic_my_card);
            } else if (current.getDatatype().equalsIgnoreCase("Clipboard")) {
                holder.img_his_type.setBackground(context.getResources().getDrawable(R.drawable.circlered));
                holder.img_his_type.setImageResource(R.drawable.ic_clipboard);
            } else {
                holder.img_his_type.setBackground(context.getResources().getDrawable(R.drawable.circlered));
                holder.img_his_type.setImageResource(R.drawable.ic_text);
            }
        } else {
            holder.txt_his_result.setText("No Data");
            holder.txt_his_result_type.setText("No DataType");
        }
    }

    @Override
    public int getItemCount() {

        if (history_data != null)
            return history_data.size();
        else return 0;
    }

    public void addAll(List<CreationHistory> data) {
        history_data = data;
        notifyDataSetChanged();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img_his_type, img_delete;
        TextView txt_his_result, txt_his_result_type;
        ConstraintLayout cl_histoy;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            img_his_type = itemView.findViewById(R.id.img_his_type);
            txt_his_result = itemView.findViewById(R.id.txt_his_result);
            txt_his_result_type = itemView.findViewById(R.id.txt_his_result_type);
            cl_histoy = itemView.findViewById(R.id.cl_histoy);
            img_delete = itemView.findViewById(R.id.img_delete);

            img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    interface_history.onDeleteById(history_data.get(getAdapterPosition()).getId(), view, getAdapterPosition());
                }
            });

            cl_histoy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    interface_history.onCreateItemClick(history_data.get(getAdapterPosition()).getData(), history_data.get(getAdapterPosition()).getDatatype(), history_data.get(getAdapterPosition()).getCodebitmap());
                }
            });

        }
    }
    public void onCreateItemClick(int position) {

        interface_history.onCreateItemClick(history_data.get(position).getData(),
                history_data.get(position).getDatatype(),
                history_data.get(position).getCodebitmap());
    }
}
