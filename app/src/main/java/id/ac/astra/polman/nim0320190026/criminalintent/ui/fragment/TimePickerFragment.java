package id.ac.astra.polman.nim0320190026.criminalintent.ui.fragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;

public class TimePickerFragment extends DialogFragment {
    private static final String ARG_TIME = "time";

    private Callbacks callbacks;

    public interface Callbacks {
        void onTimeSelected(Date date);
    }

    public static TimePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, date);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        TimePickerDialog.OnTimeSetListener timeListener;

        timeListener = (view, hourOfDay, minute) -> {
            Calendar time = Calendar.getInstance();
            time.set(Calendar.HOUR_OF_DAY, hourOfDay);
            time.set(Calendar.MINUTE, minute);

            Date resultTime = time.getTime();

            callbacks = (Callbacks) getTargetFragment();
            callbacks.onTimeSelected(resultTime);
        };

        Date date = (Date) getArguments().getSerializable(ARG_TIME);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int initialHour = calendar.get(Calendar.HOUR);
        int initialMinute = calendar.get(Calendar.MINUTE);

        return new TimePickerDialog(
                requireContext(),
                timeListener,
                initialHour,
                initialMinute,
                true
        );

    }
}
