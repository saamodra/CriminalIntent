package id.ac.astra.polman.nim0320190026.criminalintent.architecture.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.io.File;

import id.ac.astra.polman.nim0320190026.criminalintent.model.Crime;
import id.ac.astra.polman.nim0320190026.criminalintent.architecture.repository.CrimeRepository;

public class CrimeDetailViewModel extends ViewModel {
    private static final String TAG = "CrimeDetailViewModel";

    private MutableLiveData<String> mCrimeIdLiveData;
    private CrimeRepository mCrimeRepository;
    private LiveData<Crime> mCrimeLiveData;

    public CrimeDetailViewModel() {
        mCrimeRepository = CrimeRepository.get();
        mCrimeIdLiveData = new MutableLiveData<>();
        mCrimeLiveData = Transformations.switchMap(mCrimeIdLiveData, crimeId -> mCrimeRepository.getCrime(crimeId));
    }

    public void loadCrime(String crimeId) {
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
