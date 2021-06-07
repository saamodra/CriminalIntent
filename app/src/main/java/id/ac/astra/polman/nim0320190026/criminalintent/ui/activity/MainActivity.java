package id.ac.astra.polman.nim0320190026.criminalintent.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import id.ac.astra.polman.nim0320190026.criminalintent.R;
import id.ac.astra.polman.nim0320190026.criminalintent.ui.fragment.CrimeFragment;
import id.ac.astra.polman.nim0320190026.criminalintent.ui.fragment.CrimeListFragment;

public class MainActivity extends AppCompatActivity implements CrimeListFragment.Callbacks {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = CrimeListFragment.newInstance();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }

    }

    @Override
    public void onCrimeSelected(String crimeId) {
        Fragment fragment = CrimeFragment.newInstance(crimeId);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();

    }
}