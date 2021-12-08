package kg.geektech.noactivity.ui.fragments.board;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.tabs.TabLayoutMediator;

import kg.geektech.noactivity.Prefs;
import kg.geektech.noactivity.R;
import kg.geektech.noactivity.databinding.BoardFragmentBinding;
import kg.geektech.noactivity.interfaces.MyOnItemClickListeners;

public class BoardFragment extends Fragment {
    private BoardAdapter adapter;
    private BoardFragmentBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new BoardAdapter();
        adapter.setMyOnItemClickListeners(new MyOnItemClickListeners() {
            @Override
            public void onClick(int position) {
                closeFragment();
            }

            @Override
            public void onLongClick(int position) {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BoardFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews();
        initListeners();
    }

    private void initListeners() {
        binding.btnSkip.setOnClickListener(v -> {
            closeFragment();
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        requireActivity().finish();
                    }
                });
    }

    private void closeFragment() {
        Prefs prefs = new Prefs(requireContext());
        prefs.saveBoardState();
        NavController navController = Navigation.findNavController(requireActivity(), R.id.mainContainer);
        navController.navigateUp();
    }

    private void initViews() {
        binding.containerBoard.setAdapter(adapter);

        new TabLayoutMediator(binding.tabBoard, binding.containerBoard, (tab, position) -> {
        }).attach();
    }
}
