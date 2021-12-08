package kg.geektech.noactivity.ui.fragments.second;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import kg.geektech.noactivity.Prefs;
import kg.geektech.noactivity.R;
import kg.geektech.noactivity.databinding.SecondFragmentBinding;

public class SecondFragment extends Fragment {
    private SecondFragmentBinding binding;
    private ActivityResultLauncher<Intent> launcher;
    private Prefs prefs;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SecondFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews();
        initListeners();
    }

    private void initViews() {
        prefs = new Prefs(requireContext());

        if (prefs.getUriImg() != null)
            Glide.with(requireContext()).load(prefs.getUriImg()).into(binding.ivSecond);

        if (prefs.getMyText() != null)
            binding.tvSecond.setText(prefs.getMyText());
    }

    private void initListeners() {
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getData() != null && result.getResultCode() == Activity.RESULT_OK) {
                        Uri uri = result.getData().getData();
                        prefs.saveImgUri(uri);
                        binding.ivSecond.setImageURI(uri);
                    }
                });

        binding.ivSecond.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            launcher.launch(intent);
        });

        binding.btnSignOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            openFragment();
        });
    }

    private void openFragment() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.mainContainer);
        navController.navigate(R.id.loginFragment);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!binding.etSecond.getText().toString().isEmpty()) {
            prefs.saveMyText(binding.etSecond.getText().toString().trim());
        }
    }
}
