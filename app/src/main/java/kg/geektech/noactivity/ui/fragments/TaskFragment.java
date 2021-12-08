package kg.geektech.noactivity.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;

import kg.geektech.noactivity.App;
import kg.geektech.noactivity.R;
import kg.geektech.noactivity.databinding.TaskFragmentBinding;
import kg.geektech.noactivity.models.FirstModel;

public class TaskFragment extends Fragment {
    private TaskFragmentBinding binding;
    private FirstModel firstModel;
    private ActivityResultLauncher<Intent> launcher;
    private String uriStringImg;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private String fireStoreImgUrl;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = TaskFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBundle();
        initListeners();
    }

    private void getBundle() {
        db = FirebaseFirestore.getInstance();
        firstModel = (FirstModel) requireArguments().getSerializable("task");
        if (firstModel != null) {
            binding.etTask.setText(firstModel.getName());
            uriStringImg = firstModel.getImgStringUri();

            if (firstModel.getUserId() == null) {
                Glide.with(requireContext()).load(firstModel.getImgStringUri()).into(binding.ivForSelect);
            } else {
                Glide.with(requireContext()).load(firstModel.getImgStringUrl()).into(binding.ivForSelect);
            }
        }
    }

    private void initListeners() {
        binding.btnTaskSave.setOnClickListener(v -> {
            save();
        });

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        uriStringImg = uri.toString();
                        upload(uri);
                        binding.progressBar.setVisibility(View.VISIBLE);
                    }
                });

        binding.ivForSelect.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            launcher.launch(intent);
        });
    }

    private void upload(Uri uri) {
        String time = new SimpleDateFormat("yyyyMMdd_HHmmSS").format(new Date());
        String userId = FirebaseAuth.getInstance().getUid();
        storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference().child("IMG_" + userId + "_" + time + ".jpg");

        reference.putFile(uri)
                .continueWithTask(task -> reference.getDownloadUrl())
                .addOnCompleteListener(task -> {
                    fireStoreImgUrl = task.getResult().toString();
                    Glide.with(requireContext()).load(uri).into(binding.ivForSelect);
                    binding.progressBar.setVisibility(View.GONE);
                });
    }

    private void save() {
        String text = binding.etTask.getText().toString().trim();

        if (!text.isEmpty()) {
            if (firstModel == null) {
                if (uriStringImg != null && fireStoreImgUrl != null) {
                    firstModel = new FirstModel();
                    firstModel.setName(text);
                    firstModel.setImgStringUri(uriStringImg);
                    firstModel.setImgStringUrl(fireStoreImgUrl);
                    App.getInstance().getDatabase().firstModelDao().insert(firstModel);
                    saveFirestore(firstModel);
                } else {
                    Toast.makeText(requireContext(), "Выберите фото", Toast.LENGTH_SHORT).show();
                }
            } else {
                firstModel.setName(text);

                if (uriStringImg != null) {
                    firstModel.setImgStringUri(uriStringImg);
                }
                if (fireStoreImgUrl != null) {
                    firstModel.setImgStringUrl(fireStoreImgUrl);
                }

                if (firstModel.getUserId() != null) {
                    update(firstModel);
                } else {
                    App.getInstance().getDatabase().firstModelDao().update(firstModel);
                    close();
                }
            }
        } else {
            Toast.makeText(requireContext(), "Введите текст", Toast.LENGTH_SHORT).show();
        }
    }

    private void update(FirstModel firstModel) {
        db.collection("users")
                .document(firstModel.getUserId())
                .update("name", firstModel.getName(), "imgStringUrl", firstModel.getImgStringUrl())
                .addOnSuccessListener(unused -> close());
    }

    private void saveFirestore(FirstModel firstModel) {
        db.collection("users")
                .add(firstModel)
                .addOnSuccessListener(documentReference -> close());
    }

    private void close() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.mainContainer);
        navController.navigateUp();
    }

}
