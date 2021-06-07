package id.ac.astra.polman.nim0320190026.criminalintent.architecture.dao;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import id.ac.astra.polman.nim0320190026.criminalintent.model.Crime;

public class CrimeDao {
    private MutableLiveData<List<Crime>> crimes = new MutableLiveData<>();

    public LiveData<List<Crime>> getCrimes() {
        return crimes;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Crime getCrimeById(String crimeId, List<Crime> crimeList) {
        return crimeList
                .stream()
                .filter(x -> x.getId().equals(crimeId))
                .findFirst()
                .orElse(null);
    }

    public void loadCrimes(List<Crime> crimesData) {
        crimes.setValue(crimesData);
    }

    public void deleteCrime(String crimeId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            List<Crime> crimeList = crimes.getValue();
            if(crimeList != null) {
                Crime selectedCrime = crimeList
                        .stream().filter(Crime -> Crime.getId().equals(crimeId))
                        .findAny().orElse(null);

                if(selectedCrime != null) {
                    crimeList.remove(selectedCrime);
                    crimes.setValue(crimeList);
                }
            }
        }
    }

    public void updateCrime(Crime crime) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            List<Crime> crimeList = crimes.getValue();
            if(crimeList != null) {
                Crime selectedCrime = getCrimeById(crime.getId(), crimeList);

                if(selectedCrime != null) {
                    int index = crimeList.indexOf(selectedCrime);
                    crimeList.set(index, crime);
                } else {
                    crimeList.add(crime);
                }
                crimes.setValue(crimeList);
            }
        }
    }
}
