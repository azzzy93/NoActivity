package kg.geektech.noactivity.ui.fragments.first;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import kg.geektech.noactivity.R;
import kg.geektech.noactivity.databinding.ListFirstBinding;
import kg.geektech.noactivity.interfaces.MyOnItemClickListeners;
import kg.geektech.noactivity.models.FirstModel;

public class FirstAdapter extends RecyclerView.Adapter<FirstAdapter.ViewHolder> {
    private List<FirstModel> list;
    private ListFirstBinding binding;
    private MyOnItemClickListeners myOnItemClickListeners;

    public FirstAdapter() {
        list = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ListFirstBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(list.get(position));
        if (position % 2 == 0) {
            binding.tvFirst.setBackgroundResource(R.color.green);
        } else {
            binding.tvFirst.setBackgroundResource(R.color.yellow);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addItems(List<FirstModel> all) {
        list.clear();
        list.addAll(all);
        notifyDataSetChanged();
    }

    public void setMyOnItemClickListeners(MyOnItemClickListeners myOnItemClickListeners) {
        this.myOnItemClickListeners = myOnItemClickListeners;
    }

    public FirstModel getItem(int position) {
        return list.get(position);
    }

    public void removeItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ListFirstBinding binding;

        public ViewHolder(@NonNull ListFirstBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            itemView.setOnClickListener(v -> {
                myOnItemClickListeners.onClick(getAdapterPosition());
            });

            itemView.setOnLongClickListener(v -> {
                myOnItemClickListeners.onLongClick(getAdapterPosition());
                return true;
            });
        }

        public void onBind(FirstModel firstModel) {
            binding.tvFirst.setText(firstModel.getName());
            if (firstModel.getUserId() == null) {
                Glide.with(binding.ivFirst).load(firstModel.getImgStringUri()).into(binding.ivFirst);
            } else {
                Glide.with(binding.ivFirst).load(firstModel.getImgStringUrl()).into(binding.ivFirst);
            }
        }
    }
}
