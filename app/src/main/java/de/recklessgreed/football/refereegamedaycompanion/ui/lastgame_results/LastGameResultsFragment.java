package de.recklessgreed.football.refereegamedaycompanion.ui.lastgame_results;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.List;

import de.recklessgreed.football.refereegamedaycompanion.R;
import de.recklessgreed.football.refereegamedaycompanion.actionrecords.ActionRecord;
import de.recklessgreed.football.refereegamedaycompanion.actionrecords.ActionRepository;
import de.recklessgreed.football.refereegamedaycompanion.databinding.FragmentGameactionsBinding;


public class LastGameResultsFragment extends Fragment {

    public static String TAG = "WearOSFragment";

    private FragmentGameactionsBinding binding;
    private NavController.OnDestinationChangedListener destinationChangedListener;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGameactionsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        // It's crucial to remove the listener in onPause to avoid memory leaks
        // and prevent the listener from firing when the view is not visible.
        if (destinationChangedListener != null) {
            NavHostFragment.findNavController(this).removeOnDestinationChangedListener(destinationChangedListener);
        }
    }
    @Override
    public void onResume() {
        super.onResume();

        // The NavController is the source of truth for navigation events.
        // We find it from the NavHostFragment.
        NavController navController = NavHostFragment.findNavController(this);

        // Load data immediately for the first time the fragment is shown
        loadActionRecords();

        // Create a listener to react to future navigation changes
        destinationChangedListener = (controller, destination, arguments) -> {
            // Check if the destination we are navigating TO is this fragment
            if (destination.getId() == R.id.nav_last_game_results) {
                Log.d(TAG, "Navigated to LastGameResults. Reloading data.");
                loadActionRecords();
            }
        };
        // Add the listener to the NavController
        navController.addOnDestinationChangedListener(destinationChangedListener);
    }

    public void loadActionRecords() {

        // Placeholder for loading or refreshing data when this fragment becomes visible.
        // Intentionally left empty for now.

        ActionRepository repo = new ActionRepository(requireContext());
        List<ActionRecord> actions = repo.loadAll();

        Log.d("LastGameResultsFragment", "Loaded " + actions.size() + " action records.");


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}