package id.ac.astra.polman.nim0320190026.criminalintent.architecture.repository;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.File;
import java.util.List;

import id.ac.astra.polman.nim0320190026.criminalintent.api.ApiUtils;
import id.ac.astra.polman.nim0320190026.criminalintent.api.CrimeService;
import id.ac.astra.polman.nim0320190026.criminalintent.architecture.dao.CrimeDao;
import id.ac.astra.polman.nim0320190026.criminalintent.model.Crime;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrimeRepository {
    private static final String TAG = "CrimeRepository";

    private static CrimeRepository INSTANCE;

    private CrimeService mCrimeService;
    private static CrimeDao mCrimeDao;
    private File mFilesDir;

    private CrimeRepository(Context context) {
        mCrimeService = ApiUtils.getCrimeService();
        mFilesDir = context.getApplicationContext().getFilesDir();
    }

    public static void initialize(Context context) {
        if (INSTANCE == null) {
            mCrimeDao = new CrimeDao();
            INSTANCE = new CrimeRepository(context);
        }
    }

    public static CrimeRepository get() {
        return INSTANCE;
    }

    public LiveData<List<Crime>> getCrimes() {
        Log.d(TAG, "getCrimes: Called");
        Call<List<Crime>> call = mCrimeService.getCrimes();
        call.enqueue(new Callback<List<Crime>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<Crime>> call, Response<List<Crime>> response) {
                if (response.isSuccessful()) {
                    mCrimeDao.loadCrimes(response.body());

                    Log.d(TAG, "onResponse: ");
                    Log.d(TAG, "getCrimes.onResponse: called");
                }
            }

            @Override
            public void onFailure(Call<List<Crime>> call, Throwable t) {
                Log.e(TAG, "onFailure API Call : " + t.getMessage());
            }
        });

        return mCrimeDao.getCrimes();
    }

    public MutableLiveData<Crime> getCrime(String crimeId) {
        Log.d(TAG, "getCrime: Called");
        MutableLiveData<Crime> crime = new MutableLiveData<>();

        Call<Crime> call = mCrimeService.getCrimeById(crimeId);
        call.enqueue(new Callback<Crime>() {
            @Override
            public void onResponse(Call<Crime> call, Response<Crime> response) {
                if (response.isSuccessful()) {
                    mCrimeDao.updateCrime(response.body());
                    crime.setValue(response.body());
                    Log.i(TAG, "getCrime.onResponse: called");
                }
            }

            @Override
            public void onFailure(Call<Crime> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

        return crime;
    }

    public void updateCrime(Crime crime) {
        Log.i(TAG, "updateCrime: called");
        Call<Crime> call = mCrimeService.updateCrime(crime);
        call.enqueue(new Callback<Crime>() {
            @Override
            public void onResponse(Call<Crime> call, Response<Crime> response) {
                if (response.isSuccessful()) {
                    mCrimeDao.updateCrime(crime);
                    Log.i(TAG, "Crime updated " + crime.getTitle());
                }
            }

            @Override
            public void onFailure(Call<Crime> call, Throwable t) {
                Log.e(TAG, "Error api call : " + t.getMessage());
            }
        });
    }

    public void addCrime(Crime crime) {
        Log.i(TAG, "addCrime: called" + crime);
        Call<Crime> call = mCrimeService.addCrime(crime);
        call.enqueue(new Callback<Crime>() {
            @Override
            public void onResponse(Call<Crime> call, Response<Crime> response) {
                if (response.isSuccessful()) {
                    mCrimeDao.updateCrime(response.body());
                    Log.i(TAG, "Crime added " + crime.getTitle());
                }
            }

            @Override
            public void onFailure(Call<Crime> call, Throwable t) {
                Log.e(TAG, "Error API call : " + t.getMessage());
            }
        });
    }

    public void deleteCrime(String crimeId) {
        Log.d(TAG, "deleteCrime: called");
        Call<Crime> call = mCrimeService.deleteCrimeById(crimeId);
        call.enqueue(new Callback<Crime>() {
            @Override
            public void onResponse(Call<Crime> call, Response<Crime> response) {
                if (response.isSuccessful()) {
                    mCrimeDao.deleteCrime(crimeId);
                    Log.d(TAG, "Crime deleted " + crimeId);
                }
            }

            @Override
            public void onFailure(Call<Crime> call, Throwable t) {
                Log.e(TAG, "Error API call : " + t.getMessage());
            }
        });
    }

    public File getPhotoFile(Crime crime) {
        return new File(mFilesDir, crime.getPhotoFilename());
    }
}
