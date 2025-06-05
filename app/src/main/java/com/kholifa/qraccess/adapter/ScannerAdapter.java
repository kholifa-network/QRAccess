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
import com.kholifa.qraccess.RoomDatabaseStore.HistoryList;

import java.util.List;

import static com.kholifa.qraccess.R.*;


public class ScannerAdapter extends RecyclerView.Adapter<ScannerAdapter.MyViewHolder> {
    interface_history_fun interface_history;
    Context context;
    List<HistoryList> history_data;


    public ScannerAdapter(Context context, interface_history_fun interface_history) {
        this.context = context;
        this.interface_history = interface_history;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(layout.item_history_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (history_data != null) {
            HistoryList current = history_data.get(position);
            holder.txt_his_result_type.setText(current.getDatatype());

            if (current.getDatatype().equalsIgnoreCase("Facebook")) {
                holder.txt_his_result.setText(current.getData1());
                holder.img_his_type.setBackground(context.getResources().getDrawable(drawable.circlered));
                holder.img_his_type.setImageResource(drawable.ic_facebook);
            } else if (current.getDatatype().equalsIgnoreCase("Youtube")) {
                holder.img_his_type.setBackground(context.getResources().getDrawable(drawable.circlered));
                holder.txt_his_result.setText(current.getData1());
                holder.img_his_type.setImageResource(drawable.ic_youtube);
            } else if (current.getDatatype().equalsIgnoreCase("Instagram")) {
                holder.img_his_type.setBackground(context.getResources().getDrawable(R.drawable.circlered));
                holder.txt_his_result.setText(current.getData1());
                holder.img_his_type.setImageResource(drawable.ic_instagram);
            } else if (current.getDatatype().equalsIgnoreCase("Whatsapp")) {
                holder.img_his_type.setBackground(context.getResources().getDrawable(drawable.circlered));
                holder.txt_his_result.setText(current.getData1());
                holder.img_his_type.setImageResource(drawable.ic_whatsapp);
            } else if (current.getDatatype().equalsIgnoreCase("Paypal")) {
                holder.img_his_type.setBackground(context.getResources().getDrawable(drawable.circlered));
                holder.txt_his_result.setText(current.getData1());
                holder.img_his_type.setImageResource(drawable.ic_paypal);
            } else if (current.getDatatype().equalsIgnoreCase("Twitter")) {
                holder.img_his_type.setBackground(context.getResources().getDrawable(drawable.circlered));
                holder.txt_his_result.setText(current.getData1());
                holder.img_his_type.setImageResource(drawable.ic_twitter);
            } else if (current.getDatatype().equalsIgnoreCase("Viber")) {
                holder.img_his_type.setBackground(context.getResources().getDrawable(drawable.circlered));
                holder.txt_his_result.setText(current.getData1());
                holder.img_his_type.setImageResource(drawable.ic_viber);
            } else {
                holder.txt_his_result.setText(current.getData());
                if (current.getDatatype().equalsIgnoreCase("Wi-Fi")) {

                    holder.img_his_type.setBackground(context.getResources().getDrawable(R.drawable.circlered));
                    holder.img_his_type.setImageResource(drawable.ic_wifi);
                } else if (current.getDatatype().equalsIgnoreCase("URL")) {
                    holder.img_his_type.setBackground(context.getResources().getDrawable(drawable.circlered));
                    holder.img_his_type.setImageResource(drawable.ic_url);
                } else if (current.getDatatype().equalsIgnoreCase("Contact")) {
                    holder.img_his_type.setBackground(context.getResources().getDrawable(drawable.circlered));
                    holder.img_his_type.setImageResource(drawable.ic_contact);
                } else if (current.getDatatype().equalsIgnoreCase("Telephone")) {
                    holder.img_his_type.setBackground(context.getResources().getDrawable(drawable.circlered));
                    holder.img_his_type.setImageResource(drawable.ic_call);
                } else if (current.getDatatype().equalsIgnoreCase("Product")) {
                    holder.img_his_type.setBackground(context.getResources().getDrawable(drawable.circlered));
                    holder.img_his_type.setImageResource(drawable.ic_product);
                } else if (current.getDatatype().equalsIgnoreCase("Spotify")) {
                    holder.img_his_type.setBackground(context.getResources().getDrawable(drawable.circlered));
                    holder.img_his_type.setImageResource(drawable.ic_spotify);
                } else if (current.getDatatype().equalsIgnoreCase("Text")) {
                    holder.img_his_type.setBackground(context.getResources().getDrawable(drawable.circlered));
                    holder.img_his_type.setImageResource(drawable.ic_text);
                } else if (current.getDatatype().equalsIgnoreCase("Email")) {
                    holder.img_his_type.setBackground(context.getResources().getDrawable(drawable.circlered));
                    holder.img_his_type.setImageResource(drawable.ic_email);
                } else if (current.getDatatype().equalsIgnoreCase("SMS")) {
                    holder.img_his_type.setBackground(context.getResources().getDrawable(drawable.circlered));
                    holder.img_his_type.setImageResource(drawable.ic_sms);
                } else if (current.getDatatype().equalsIgnoreCase("Calendar")) {
                    holder.img_his_type.setBackground(context.getResources().getDrawable(drawable.circlered));
                    holder.img_his_type.setImageResource(drawable.ic_calendar);
                } else if (current.getDatatype().equalsIgnoreCase("Book")) {
                    holder.img_his_type.setBackground(context.getResources().getDrawable(drawable.circlered));
                    holder.img_his_type.setImageResource(drawable.ic_book);
                } else if (current.getDatatype().equalsIgnoreCase("Geo")) {
                    holder.img_his_type.setBackground(context.getResources().getDrawable(drawable.circlered));
                    holder.img_his_type.setImageResource(drawable.ic_geo);
                } else if (current.getDatatype().equalsIgnoreCase("Driver License")) {
                    holder.img_his_type.setBackground(context.getResources().getDrawable(drawable.circlered));
                    holder.img_his_type.setImageResource(drawable.ic_my_card);
                } else {

                    holder.img_his_type.setBackground(context.getResources().getDrawable(drawable.circlered));
                    holder.img_his_type.setImageResource(drawable.ic_text);
                }
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

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img_his_type, img_delete;
        TextView txt_his_result, txt_his_result_type;
        ConstraintLayout cl_histoy;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            img_his_type = itemView.findViewById(id.img_his_type);
            txt_his_result = itemView.findViewById(id.txt_his_result);
            txt_his_result_type = itemView.findViewById(id.txt_his_result_type);
            cl_histoy = itemView.findViewById(id.cl_histoy);
            img_delete = itemView.findViewById(id.img_delete);

            img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    interface_history.onDeleteById(history_data.get(getAdapterPosition()).getId(), view, getAdapterPosition());
                }
            });

            cl_histoy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    interface_history.onItemClick(history_data.get(getAdapterPosition()).getData(), history_data.get(getAdapterPosition()).getData1(), history_data.get(getAdapterPosition()).getData2(), history_data.get(getAdapterPosition()).getData3(), history_data.get(getAdapterPosition()).getData4(), history_data.get(getAdapterPosition()).getData5(), history_data.get(getAdapterPosition()).getData6(), history_data.get(getAdapterPosition()).getData7(), history_data.get(getAdapterPosition()).getData8(), history_data.get(getAdapterPosition()).getData9(), history_data.get(getAdapterPosition()).getData10(), history_data.get(getAdapterPosition()).getData11(), history_data.get(getAdapterPosition()).getData12(), history_data.get(getAdapterPosition()).getDatatype());
                }
            });

        }
    }

    public void onItemClick(int position) {

        interface_history.onItemClick(history_data.get(position).getData(), history_data.get(position).getData1(),
                history_data.get(position).getData2(), history_data.get(position).getData3(), history_data.get(position).getData4(),
                history_data.get(position).getData5(), history_data.get(position).getData6(), history_data.get(position).getData7(),
                history_data.get(position).getData8(), history_data.get(position).getData9(), history_data.get(position).getData10(),
                history_data.get(position).getData11(), history_data.get(position).getData12(), history_data.get(position).getDatatype());

    }

    public void addAll(List<HistoryList> data) {
        history_data = data;
        notifyDataSetChanged();
    }

    public interface interface_history_fun {
        void onDeleteById(int id, View view, int position);

        void onItemClick(String data, String data1, String data2, String data3, String data4, String data5, String data6, String data7, String data8, String data9, String data10, String data11, String data12, String dataType);

        void onCreateItemClick(String data, String dataType, String bitmap);

    }

}
