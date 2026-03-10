package de.recklessgreed.football.refereegamedaycompanion.ui.wearos_settings;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import de.recklessgreed.football.refereegamedaycompanion.R;
import de.recklessgreed.football.refereegamedaycompanion.databinding.FragmentWearosSettingsBinding;
import de.recklessgreed.football.refereegamedaycompanion.settings.MatchSettings;
import de.recklessgreed.football.refereegamedaycompanion.settings.SettingsStorage;
import de.recklessgreed.football.refereegamedaycompanion.settings.SettingsSyncClient;


public class WearosSettingsFragment extends Fragment {

    public static String TAG = "WearOSFragment";

    private FragmentWearosSettingsBinding binding;
    private NavController.OnDestinationChangedListener destinationChangedListener;

    private EditText hometeamNameInp, awayteamNameInp,
            lengthPerPeriodInp, numberOfPeriodsInp, timeoutsPerHalfInp;
    private Button saveBtn;

    private Spinner hometeamColorSpinner, awayteamColorSpinner;

    private SettingsStorage storage;
    private SettingsSyncClient sync;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWearosSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        storage = new SettingsStorage(requireContext());
        sync = new SettingsSyncClient(requireContext());

        // Initialize UI components once the view is created
        hometeamNameInp = binding.hometeamNameInp;
        hometeamColorSpinner = binding.hometeamColorInp;
        awayteamNameInp = binding.awayteamNameInp;
        awayteamColorSpinner = binding.awayteamColorInp;
        lengthPerPeriodInp = binding.lengthPerPeriodInp;
        numberOfPeriodsInp = binding.numberOfPeriodsInp;
        timeoutsPerHalfInp = binding.timeoutsPerHalfInp;
        saveBtn = binding.saveButton;

        // Set listeners here
        saveBtn.setOnClickListener(l -> onSave());
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
        loadSettings();

        // Create a listener to react to future navigation changes
        destinationChangedListener = (controller, destination, arguments) -> {
            // Check if the destination we are navigating TO is this fragment
            if (destination.getId() == R.id.nav_wearos_settings) {
                Log.d(TAG, "Navigated to WearOSSettingsFragment. Reloading data.");
                loadSettings();
            }
        };
        // Add the listener to the NavController
        navController.addOnDestinationChangedListener(destinationChangedListener);
    }


    public void loadSettings() {
        sync.fetchLatestSettings(settings -> {
            if (settings == null) return;
            MatchSettings local = storage.load();
            if (MatchSettings.isIncomingNewer(local, settings)) {
                storage.save(settings);
            }
        });
        MatchSettings current = storage.load();

        String homeTeamName = current.homeName;
        int homeTeamColor = current.homeColorArgb;
        String awayTeamName = current.guestName;
        int awayTeamColor = current.guestColorArgb;
        String lengthPerPeriod = String.valueOf(current.periodLengthSec);
        String numberOfPeriods = String.valueOf(current.periodCount);
        String timeoutsPerHalf = String.valueOf(current.timeoutsPerHalf);

        hometeamNameInp.setText(homeTeamName);
        awayteamNameInp.setText(awayTeamName);

        // Set spinner selection based on color value
        hometeamColorSpinner.setSelection(homeTeamColor);
        awayteamColorSpinner.setSelection(awayTeamColor);

        lengthPerPeriodInp.setText(lengthPerPeriod);
        numberOfPeriodsInp.setText(numberOfPeriods);
        timeoutsPerHalfInp.setText(timeoutsPerHalf);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void onSave() {
        String homeTeamName = hometeamNameInp.getText().toString();
        int homeTeamColor = hometeamColorSpinner.getSelectedItemPosition();
        String awayTeamName = awayteamNameInp.getText().toString();
        int awayTeamColor = awayteamColorSpinner.getSelectedItemPosition();
        int lengthPerPeriod = Integer.parseInt(lengthPerPeriodInp.getText().toString());
        int numberOfPeriods = Integer.parseInt(numberOfPeriodsInp.getText().toString());
        int timeoutsPerHalf = Integer.parseInt(timeoutsPerHalfInp.getText().toString());

        MatchSettings newSettings = new MatchSettings(
                homeTeamName, homeTeamColor, awayTeamName, awayTeamColor,
                lengthPerPeriod, numberOfPeriods, timeoutsPerHalf, System.currentTimeMillis(), "PHONE"
                );

        storage.save(newSettings);
        sync.pushSettings(newSettings, true);

        Toast.makeText(getContext(), "Settings saved and pushed successfully!", Toast. LENGTH_LONG).show();
    }
}