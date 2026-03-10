package de.recklessgreed.football.refereegamedaycompanion.ui.compensation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import de.recklessgreed.football.refereegamedaycompanion.databinding.FragmentCompensationBinding;

public class CompensationFragment extends Fragment {

    private FragmentCompensationBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CompensationViewModel galleryViewModel =
                new ViewModelProvider(this).get(CompensationViewModel.class);
        binding = FragmentCompensationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}