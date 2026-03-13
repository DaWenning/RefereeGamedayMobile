package de.recklessgreed.football.refereegamedaycompanion.ui.lastgame_results;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.recklessgreed.football.refereegamedaycompanion.R;
import de.recklessgreed.football.refereegamedaycompanion.actionrecords.ActionRecord;
import de.recklessgreed.football.refereegamedaycompanion.actionrecords.ActionRepository;
import de.recklessgreed.football.refereegamedaycompanion.databinding.FragmentGameactionsBinding;


public class LastGameResultsFragment extends Fragment {

    public static String TAG = "WearOSFragment";

    private FragmentGameactionsBinding binding;
    private NavController.OnDestinationChangedListener destinationChangedListener;

    private Map<String, List<ActionRecord>> recordsByGameId;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGameactionsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up the clear button
        binding.clearActionsButton.setOnClickListener(v -> {
            // Show confirmation dialog before clearing all actions
            new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.clear_actions_confirm_title))
                    .setMessage(getString(R.string.clear_actions_confirm_message))
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        ActionRepository repo = new ActionRepository(requireContext());
                        repo.clearAll();
                        loadActionRecords();
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();
        });
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

        ActionRepository repo = new ActionRepository(requireContext());
        List<ActionRecord> actions = repo.loadAll();

        recordsByGameId = new HashMap<>();

        Log.d("LastGameResultsFragment", "Loaded " + actions.size() + " action records.");

        // Defensive checks for binding
        if (binding == null) {
            Log.w(TAG, "Binding is null, cannot display actions");
            return;
        }

        LinearLayout container = binding.actionContainer;

        container.removeAllViews();

        if (actions.isEmpty()) {
            TextView empty = new TextView(requireContext());
            empty.setText(getString(R.string.no_actions));
            empty.setPadding(16, 8, 16, 8);
            container.addView(empty);
            return;
        }

        for (ActionRecord record : actions) {
            TextView tv = new TextView(requireContext());
            tv.setText(formatRecord(record));
            tv.setPadding(16, 12, 16, 12);
            tv.setTextSize(16);
            container.addView(tv);

            String gameId = record.getGameId() != null ? record.getGameId() : "n/a";
            if (recordsByGameId.containsKey(gameId)) {
                recordsByGameId.get(gameId).add(record);
            } else {
                recordsByGameId.put(gameId, new java.util.ArrayList<>(List.of(record)));
            }
        }

    }

    private String formatRecord(ActionRecord r) {
        if (r == null) return "(null)";
        // try to use common getters if available, otherwise fallback to toString
        try {
            String time = r.getReadableTimestamp(false);
            String gameId = r.getGameId() != null ? r.getGameId() : "n/a";
            String type = r.getActionType() != null ? r.getActionType() : "";

            Integer teamId = r.getTeamId() != null ? r.getTeamId().intValue() : null;
            String team = "";
            if (teamId != null && teamId == 0) { team = " (HEIM)"; }
            else if (teamId != null && teamId == 1) { team = " (GAST)"; }

            Integer period = r.getPeriod();
            Integer remaining = r.getRemainingTimeInPeriod();
            String remainingText = "n/a";
            if (remaining != null) {
                int minutes = remaining / 60;
                int seconds = remaining % 60;
                remainingText = String.format("%d:%02d", minutes, seconds);
            }



            return gameId + "\n" + time + " — " + type + team + "\n" + "Periode: " + (period != null ? period : "n/a") + ", Verbleibende Zeit: " + remainingText;
        } catch (Throwable t) {
            return r.toString();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
