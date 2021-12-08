package kg.geektech.noactivity.ui.fragments.first;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.List;

import kg.geektech.noactivity.App;
import kg.geektech.noactivity.R;
import kg.geektech.noactivity.databinding.FirstFragmentBinding;
import kg.geektech.noactivity.interfaces.MyOnItemClickListeners;
import kg.geektech.noactivity.models.FirstModel;


public class FirstFragment extends Fragment {
    private FirstFragmentBinding binding;
    private FirstAdapter adapter;
    private boolean b;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        adapter = new FirstAdapter();
        adapter.setMyOnItemClickListeners(new MyOnItemClickListeners() {
            @Override
            public void onClick(int position) {
                FirstModel firstModel = adapter.getItem(position);
                openFragment(firstModel);
            }

            @Override
            public void onLongClick(int position) {
                FirstModel firstModel = adapter.getItem(position);
                App.getInstance().getDatabase().firstModelDao().delete(firstModel);
                adapter.removeItem(position);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FirstFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initList();
        initListeners();
    }

    private void initListeners() {
        binding.fabTask.setOnClickListener(v -> {
            openFragment(null);
        });
    }

    private void openFragment(FirstModel firstModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("task", firstModel);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.mainContainer);
        navController.navigate(R.id.taskFragment, bundle);
    }

    private void initList() {
        binding.firstRv.setAdapter(adapter);
        adapter.addItems(App.getInstance().getDatabase().firstModelDao().getAll());
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.sort){
            if (!b) {
                adapter.addItems(App.getInstance().getDatabase().firstModelDao().getAllSortedByTitle());
                b = true;
            } else {
                adapter.addItems(App.getInstance().getDatabase().firstModelDao().getAll());
                b = false;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
