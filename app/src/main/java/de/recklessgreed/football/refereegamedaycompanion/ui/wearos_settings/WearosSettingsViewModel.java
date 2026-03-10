package de.recklessgreed.football.refereegamedaycompanion.ui.wearos_settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WearosSettingsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public WearosSettingsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}