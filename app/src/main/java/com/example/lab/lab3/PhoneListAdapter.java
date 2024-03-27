package com.example.lab.lab3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab.R;
import com.example.lab.lab1.RecyclerAdapter;

import java.util.List;

public class PhoneListAdapter extends RecyclerView.Adapter<PhoneListAdapter.PhoneViewHolder>{
    private List<Phone> phoneList;
    private PhoneListAdapterInterface phoneListAdapterInterface;
    public PhoneListAdapter(List<Phone> phoneList,
                            PhoneListAdapterInterface phoneListAdapterInterface) {
        this.phoneList = phoneList;
        this.phoneListAdapterInterface = phoneListAdapterInterface;
    }
    public void setPhoneList(List<Phone> phoneList) {
        this.phoneList = phoneList;
        notifyDataSetChanged();
    }
    public static class PhoneViewHolder extends RecyclerView.ViewHolder {
        private TextView phoneManufacturer, phoneModel;
        public PhoneViewHolder(@NonNull View itemView,
                               PhoneListAdapterInterface phoneListAdapterInterface) {
            super(itemView);
            phoneManufacturer = itemView.findViewById(R.id.phoneManufacturer);
            phoneModel = itemView.findViewById(R.id.phoneModel);

            itemView.setOnClickListener(v -> {
                if(phoneListAdapterInterface != null) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        phoneListAdapterInterface.onItemClick(pos);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public PhoneListAdapter.PhoneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.phone_list_item, parent, false);
        return new PhoneViewHolder(itemView, this.phoneListAdapterInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneListAdapter.PhoneViewHolder holder, int position) {
        Phone phone = this.phoneList.get(position);
        holder.phoneManufacturer.setText(phone.getManufacturer());
        holder.phoneModel.setText(phone.getModel());
    }

    @Override
    public int getItemCount() {
        return this.phoneList.size();
    }
}
