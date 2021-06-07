package id.ac.astra.polman.nim0320190026.criminalintent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.io.File;
import java.util.UUID;

import id.ac.astra.polman.nim0320190026.criminalintent.database.CrimeRepository;

public class CrimeDetailViewModel extends ViewModel {
    private MutableLiveData<UUID> mCrimeIdLiveData;
    private CrimeRepository mCrimeRepository;
    private LiveData<Crime> mCrimeLiveData;

    public CrimeDetailViewModel() {
        mCrimeRepository = CrimeRepository.get();
        mCrimeIdLiveData = new MutableLiveData<>();
        mCrimeLiveData = Transformations.switchMap(mCrimeIdLiveData, crimeId -> mCrimeRepository.getCrime(crimeId));
    }

    public void loadCrime(UUID crimeId) {
        mCrimeIdLiveData.setValue(crimeId);
    }

    public LiveData<Crime> getCrimeLiveData() {
        return mCrimeLiveData;
    }

    public void saveCrime(Crime crime) {
        mCrimeRepository.updateCrime(crime);
    }

    public File getPhotoFile(Crime crime) {
        return mCrimeRepository.getPhotoFile(crime);
    }
}
