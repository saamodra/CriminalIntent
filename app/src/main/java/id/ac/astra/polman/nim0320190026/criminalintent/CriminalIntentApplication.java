package id.ac.astra.polman.nim0320190026.criminalintent;

import android.app.Application;

import id.ac.astra.polman.nim0320190026.criminalintent.architecture.repository.CrimeRepository;

public class CriminalIntentApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrimeRepository.initialize(this);
    }


}
