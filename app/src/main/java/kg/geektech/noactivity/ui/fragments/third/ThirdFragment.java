package kg.geektech.noactivity.ui.fragments.third;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import kg.geektech.noactivity.R;
import kg.geektech.noactivity.databinding.ThirdFragmentBinding;
import kg.geektech.noactivity.interfaces.MyOnItemClickListeners;
import kg.geektech.noactivity.models.FirstModel;
import kg.geektech.noactivity.ui.fragments.first.FirstAdapter;

public class ThirdFragment extends Fragment {
    private ThirdFragmentBinding binding;
    private FirstAdapter adapter;
    private FirebaseFirestore db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                db.collection("users")
                        .document(firstModel.getUserId())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(requireContext(),  "Успешно удалено", Toast.LENGTH_SHORT).show();
                                adapter.removeItem(position);
                            }
                        });
            }
        });
    }

    private void openFragment(FirstModel firstModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("task", firstModel);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.mainContainer);
        navController.navigate(R.id.taskFragment, bundle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ThirdFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initList();
        getData();
    }

    private void getData() {
        db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot snapshots) {
                        List<FirstModel> list = new ArrayList<>();
                        for (DocumentSnapshot snapshot : snapshots) {
                            FirstModel firstModel = snapshot.toObject(FirstModel.class);
                            firstModel.setUserId(snapshot.getId());
                            list.add(firstModel);
                        }
                        adapter.addItems(list);
                    }
                });
    }

    private void initList() {
        binding.thirdRv.setAdapter(adapter);
    }
}
