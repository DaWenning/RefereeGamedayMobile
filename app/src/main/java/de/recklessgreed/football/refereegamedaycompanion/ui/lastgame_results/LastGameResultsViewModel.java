package de.recklessgreed.football.refereegamedaycompanion.ui.lastgame_results;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LastGameResultsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public LastGameResultsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}