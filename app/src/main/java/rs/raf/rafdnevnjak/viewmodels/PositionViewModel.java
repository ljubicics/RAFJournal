package rs.raf.rafdnevnjak.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PositionViewModel extends ViewModel {
    private final MutableLiveData<Integer> position = new MutableLiveData<>();

    public void setPosition(int position) {
        this.position.setValue(position);
    }

    public LiveData<Integer> getPosition() {
        return this.position;
    }
}
