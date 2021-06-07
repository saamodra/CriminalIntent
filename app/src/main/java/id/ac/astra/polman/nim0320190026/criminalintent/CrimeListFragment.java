package id.ac.astra.polman.nim0320190026.criminalintent;

import android.content.Context;
import android.os.Bundle;
import android.telecom.Call;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class CrimeListFragment extends Fragment {
    private static final String TAG = "CrimeListFragment";

    private CrimeListViewModel mCrimeListViewModel;
    private RecyclerView mCrimeRecyclerView;
    private View mNoCrimes;
    private Button mAddNewCrime;
    private CrimeAdapter mAdapter = new CrimeAdapter(Collections.emptyList());
    private SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");


    interface Callbacks {
        public void onCrimeSelected(UUID crimeId);
    }

    private Callbacks mCallbacks = null;

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_crime:
                newItem();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    private void updateUI(List<Crime> crimes) {
        mAdapter = new CrimeAdapter(crimes);
        mCrimeRecyclerView.setAdapter(mAdapter);

        mCrimeRecyclerView.setVisibility(crimes.size() > 0 ? View.VISIBLE : View.GONE);
        mNoCrimes.setVisibility(crimes.size() > 0 ? View.GONE : View.VISIBLE);
    }

    public void newItem() {
        Crime crime = new Crime();
        mCrimeListViewModel.insert(crime);
        mCallbacks.onCrimeSelected(crime.getId());
    }

    public static CrimeListFragment newInstance() {
        return new CrimeListFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mCrimeListViewModel = new ViewModelProvider(this).get(CrimeListViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = v.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCrimeRecyclerView.setAdapter(mAdapter);

        mNoCrimes = v.findViewById(R.id.no_crimes);
        mAddNewCrime = v.findViewById(R.id.add_new_crime);
        mAddNewCrime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newItem();
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCrimeListViewModel.getCrimes().observe(
                getViewLifecycleOwner(),
                new Observer<List<Crime>>() {
                    @Override
                    public void onChanged(List<Crime> crimes) {
                        updateUI(crimes);
                        Log.i(TAG, "Got crimes = " + crimes.size());
                    }
                }
        );
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mSolvedImageView;

        private Crime mCrime;

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = itemView.findViewById(R.id.crime_title);
            mDateTextView = itemView.findViewById(R.id.crime_date);
            mSolvedImageView = itemView.findViewById(R.id.crime_solved);
        }

        public void bind(Crime crime) {
            java.text.DateFormat dateFormat = DateFormat.getLongDateFormat(getContext());

            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());

            String day = dayFormat.format(mCrime.getDate());
            String date = day + ", " + dateFormat.format(mCrime.getDate());

            mDateTextView.setText(date);
            mSolvedImageView.setVisibility(mCrime.isSolved() ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View v) {
            mCallbacks.onCrimeSelected(mCrime.getId());
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public int getItemViewType(int position) {
            return mCrimes.get(position).isRequiresPolice() ? 1 : 0;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new CrimeHolder(layoutInflater, parent);
        }


        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            ((CrimeHolder) holder).bind(crime);


        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }
}
