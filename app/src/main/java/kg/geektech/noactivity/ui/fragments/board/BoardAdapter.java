package kg.geektech.noactivity.ui.fragments.board;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import kg.geektech.noactivity.R;
import kg.geektech.noactivity.databinding.ListBoardBinding;
import kg.geektech.noactivity.interfaces.MyOnItemClickListeners;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder> {
    private ListBoardBinding binding;
    private String[] title, text;
    private Integer[] lottieImg;
    private MyOnItemClickListeners myOnItemClickListeners;

    public BoardAdapter() {
        title = new String[]{"Салам", "Привет", "Hello"};
        text = new String[]{"Кош келиниз!", "Добро пожаловать!", "Welcome"};
        lottieImg = new Integer[]{R.raw.city, R.raw.city2, R.raw.city3};
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ListBoardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return title.length;
    }

    public void setMyOnItemClickListeners(MyOnItemClickListeners myOnItemClickListeners) {
        this.myOnItemClickListeners = myOnItemClickListeners;
    }









    public class ViewHolder extends RecyclerView.ViewHolder {
        private ListBoardBinding binding;

        public ViewHolder(@NonNull ListBoardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.btnStart.setOnClickListener(v -> {
                myOnItemClickListeners.onClick(getAdapterPosition());
            });
        }

        public void onBind(int position) {
            binding.tvText.setText(text[position]);
            binding.tvTitle.setText(title[position]);
            binding.lottie.setAnimation(lottieImg[position]);

            if (position == title.length - 1) {
                binding.btnStart.setVisibility(View.VISIBLE);
            } else {
                binding.btnStart.setVisibility(View.INVISIBLE);
            }
        }
    }
}
