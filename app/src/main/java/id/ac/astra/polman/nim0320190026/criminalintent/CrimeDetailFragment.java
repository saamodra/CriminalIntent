package id.ac.astra.polman.nim0320190026.criminalintent;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


public class CrimeDetailFragment extends DialogFragment {
    public static final String DIALOG_PHOTO = "DialogPhoto";
    public static final String DIALOG_TITLE = "DialogTitle";

    private Button mCloseButton;
    private TextView mTitle;

    public static CrimeDetailFragment newInstance(String title, String photo) {
        Bundle args = new Bundle();
        args.putSerializable(DIALOG_TITLE, title);
        args.putSerializable(DIALOG_PHOTO, photo);
        CrimeDetailFragment fragment = new CrimeDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime_detail, container, false);
        ImageView imageView = v.findViewById(R.id.crime_image);
        String photoFileName = (String) getArguments().getSerializable(DIALOG_PHOTO);
        Bitmap photo = BitmapFactory.decodeFile(requireContext().getFilesDir().getPath() + "/" + photoFileName);

        imageView.setImageBitmap(photo);

        String title = (String) getArguments().getSerializable(DIALOG_TITLE);

        mTitle = v.findViewById(R.id.crime_detail_text);
        mTitle.setText(title);

        mCloseButton = v.findViewById(R.id.crime_detail_close);
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });

        return v;
    }
}
