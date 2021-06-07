package id.ac.astra.polman.nim0320190026.criminalintent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import id.ac.astra.polman.nim0320190026.criminalintent.database.CrimeRepository;

public class CrimeListViewModel extends ViewModel {

    private LiveData<List<Crime>> mCrimesListLiveData;
    private CrimeRepository mCrimeRepository;

    public CrimeListViewModel() {
        mCrimeRepository = CrimeRepository.get();
        mCrimesListLiveData = mCrimeRepository.getCrimes();
    }

    public LiveData<List<Crime>> getCrimes() {
        return mCrimesListLiveData;
    }

    public void insert(Crime crime) {
        mCrimeRepository.insert(crime);
    }

}
