package de.recklessgreed.football.refereegamedaycompanion.ui.compensation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CompensationViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CompensationViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}