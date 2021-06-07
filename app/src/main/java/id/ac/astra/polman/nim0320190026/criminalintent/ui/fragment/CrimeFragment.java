package id.ac.astra.polman.nim0320190026.criminalintent.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.format.DateFormat;
import android.widget.Toast;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import id.ac.astra.polman.nim0320190026.criminalintent.helper.PictureUtils;
import id.ac.astra.polman.nim0320190026.criminalintent.R;
import id.ac.astra.polman.nim0320190026.criminalintent.architecture.viewmodel.CrimeDetailViewModel;
import id.ac.astra.polman.nim0320190026.criminalintent.model.Crime;

import static androidx.core.view.ViewCompat.jumpDrawablesToCurrentState;

public class CrimeFragment extends Fragment implements DatePickerFragment.Callbacks, TimePickerFragment.Callbacks {
    private static final String ARG_CRIME_ID = "crime_id";
    private static final String TAG = "CrimeFragment";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 0;
    private static final int REQUEST_CONTACT = 1;
    private static final int PERMISSION_CONTACT_CODE = 1;
    private static final int REQUEST_PHOTO = 2;

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mSolvedCheckBox;
    private CrimeDetailViewModel mCrimeDetailViewModel;
    private Button mReportButton;
    private Button mSuspectButton;
    private ImageButton mCallButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private File mPhotoFile;
    private Uri mPhotoUri;


    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    private CrimeDetailViewModel getCrimeDetailViewModel() {
        if (mCrimeDetailViewModel == null) {
            mCrimeDetailViewModel = new ViewModelProvider(this).get(CrimeDetailViewModel.class);
        }
        return mCrimeDetailViewModel;
    }

    public static CrimeFragment newInstance(String crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String crimeId = (String) getArguments().getSerializable(ARG_CRIME_ID);
        Log.d(TAG, "args bundle crime ID : " + crimeId);
        // eventually we will load crime from database here
        mCrime = new Crime();
        mCrimeDetailViewModel = getCrimeDetailViewModel();
        mCrimeDetailViewModel.loadCrime(crimeId);
    }


    private void updateUI() {
        String date = dateFormat.format(mCrime.getDate());
        String time = timeFormat.format(mCrime.getDate());
        mTitleField.setText(mCrime.getTitle());
        mDateButton.setText(date);
        mTimeButton.setText(time);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        if (mSolvedCheckBox.isChecked()) {
            jumpDrawablesToCurrentState(mSolvedCheckBox);
        }

        if (mCrime.getSuspect() != null) {
            mSuspectButton.setText(mCrime.getSuspect());
        }
        updatePhotoView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        String date = dateFormat.format(mCrime.getDate());
        String time = timeFormat.format(mCrime.getDate());

        mTitleField = v.findViewById(R.id.crime_title);
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateButton = v.findViewById(R.id.crime_date);
        mDateButton.setText(date);
        mDateButton.setOnClickListener(v12 -> {
            FragmentManager manager = getParentFragmentManager();
            DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
            dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
            dialog.show(manager, DIALOG_DATE);
        });

        mTimeButton = v.findViewById(R.id.crime_time);
        mTimeButton.setText(time);
        mTimeButton.setOnClickListener(v1 -> {
            FragmentManager manager = getParentFragmentManager();

            TimePickerFragment dialog = TimePickerFragment.newInstance(mCrime.getDate());
            dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
            dialog.show(manager, DIALOG_TIME);
        });

        mSolvedCheckBox = v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> mCrime.setSolved(isChecked));

        mReportButton = v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
                i = Intent.createChooser(i, getString(R.string.send_report));
                startActivity(i);
            }
        });

        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

        mSuspectButton = v.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkContactPermission()) {
                    pickContactIntent();
                } else {
                    requestContactPermission();
                }
            }
        });

        mCallButton = v.findViewById(R.id.crime_suspect_call);
        mCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri number = Uri.parse("tel:" + mCrime.getSuspectNumber());
                Intent call = new Intent(Intent.ACTION_DIAL, number);
                startActivity(call);
            }
        });

        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContact,
                PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectButton.setEnabled(false);
        }

        mPhotoButton = v.findViewById(R.id.crime_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (packageManager.resolveActivity(captureImage, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mPhotoButton.setEnabled(false);
        }
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
                List<ResolveInfo> cameraActivites = getActivity()
                        .getPackageManager().queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo activity : cameraActivites) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName, mPhotoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        mPhotoView = v.findViewById(R.id.crime_photo);
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getParentFragmentManager();

                CrimeDetailFragment fragment = CrimeDetailFragment.newInstance(mCrime.getTitle(), mCrime.getPhotoFilename());
                fragment.show(manager, CrimeDetailFragment.DIALOG_PHOTO);
            }
        });

        return v;
    }

    private boolean checkContactPermission() {
        return ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestContactPermission() {
        String[] permission = {Manifest.permission.READ_CONTACTS};

        super.requestPermissions(permission, PERMISSION_CONTACT_CODE);
    }

    private void pickContactIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, REQUEST_CONTACT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_CONTACT_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickContactIntent();
            } else {
                Toast.makeText(getContext(), "Permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getCrimeReport() {
        String solvedString = null;

        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE, MMM, dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();
        String suspect = mCrime.getSuspect();

        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect);
        }

        return getString(R.string.crime_report, mCrime.getTitle(), dateString, solvedString, suspect);
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), requireActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCrimeDetailViewModel.getCrimeLiveData().observe(
                getViewLifecycleOwner(),
                new Observer<Crime>() {
                    @Override
                    public void onChanged(Crime crime) {
                        mCrime = crime;
                        mPhotoFile = mCrimeDetailViewModel.getPhotoFile(crime);
                        mPhotoUri = FileProvider.getUriForFile(requireActivity(), "id.ac.astra.polman.nim0320190026.criminalintent.fileprovider", mPhotoFile);
                        updateUI();
                    }
                }
        );
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_PHOTO) {
            requireActivity().revokeUriPermission(mPhotoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updatePhotoView();
        }

        if (requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();

            Cursor c1, c2;

            c1 = getActivity().getContentResolver()
                    .query(contactUri, null, null, null, null);

            try {
                if (c1.getCount() == 0) {
                    return;
                }


                if(c1.moveToFirst()){
                    String contactId = c1.getString(c1.getColumnIndex(ContactsContract.Contacts._ID));
                    String contactName = c1.getString(c1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String idResults = c1.getString(c1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    int idResultHold = Integer.parseInt(idResults);

                    if (idResultHold == 1) {
                        c2 = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                                null,
                                null);
                        if(c2.moveToFirst()) {
                            String contactNumber = c2.getString(c2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            mCrime.setSuspectNumber(contactNumber);
                        }
                        c2.close();
                    }

                    mCrime.setSuspect(contactName);
                    mCrimeDetailViewModel.saveCrime(mCrime);
                    mSuspectButton.setText(contactName);
                }

            } finally {
                c1.close();
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: Called");
        requireActivity().revokeUriPermission(mPhotoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: Called");
        mCrimeDetailViewModel.saveCrime(mCrime);
    }

    public void setDateTime(String date, String time) {
        try {
            Date newDateTime = dateTimeFormat.parse(date + " " + time);
            mCrime.setDate(newDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDateSelected(Date date) {
        String newTime = timeFormat.format(mCrime.getDate());
        String newDate = dateFormat.format(date);
        setDateTime(newDate, newTime);

        updateUI();
    }

    @Override
    public void onTimeSelected(Date date) {
        String newTime = timeFormat.format(date);
        String newDate = dateFormat.format(mCrime.getDate());
        setDateTime(newDate, newTime);

        updateUI();
    }
}
