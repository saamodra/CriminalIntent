package id.ac.astra.polman.nim0320190026.criminalintent.architecture.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import id.ac.astra.polman.nim0320190026.criminalintent.model.Crime;
import id.ac.astra.polman.nim0320190026.criminalintent.architecture.repository.CrimeRepository;

public class CrimeListViewModel extends ViewModel {
    private CrimeRepository mCrimeRepository;

    public CrimeListViewModel() {
        mCrimeRepository = CrimeRepository.get();
    }

    public LiveData<List<Crime>> getCrimes() {
        return mCrimeRepository.getCrimes();
    }

    public void insert(Crime crime) {
        mCrimeRepository.addCrime(crime);
    }

}
