package id.ac.astra.polman.nim0320190026.criminalintent.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import id.ac.astra.polman.nim0320190026.criminalintent.Crime;

public class CrimeRepository {
    private static final String DATABASE_NAME = "crime-database";

    private static CrimeRepository INSTANCE;
    private CrimeDatabase mDatabase;
    private CrimeDao mCrimeDao;
    private Executor mExecutor;
    private File mFilesDir;

    public void insert(final Crime crime) {
        CrimeDatabase.databaseWriteExecutor.execute(() -> {
            mCrimeDao.insert(crime);
        });
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

//            CrimeDatabase.databaseWriteExecutor.execute(() -> {
//                CrimeDao dao = INSTANCE.mDatabase.crimeDao();
//
//                dao.deleteAll();
//
//                for (int i = 0; i < 100; i++) {
//                    Crime crime = new Crime();
//                    crime.setTitle("Crime #" + i);
//                    crime.setSolved(i % 2 == 0);
//                    dao.insert(crime);
//                }
//            });
        }
    };

    public CrimeRepository(Context context) {
        mDatabase = Room.databaseBuilder(context.getApplicationContext(), CrimeDatabase.class, DATABASE_NAME)
                .addCallback(sRoomDatabaseCallback)
                .addMigrations(CrimeDatabase.MIGRATION_2_3)
                .build();

        mCrimeDao = mDatabase.crimeDao();
        mExecutor = Executors.newSingleThreadExecutor();
        mFilesDir = context.getApplicationContext().getFilesDir();
    }

    public void updateCrime(Crime crime) {
        mExecutor.execute(() -> {
            mCrimeDao.updateCrime(crime);
        });
    }

    public File getPhotoFile(Crime crime) {
        return new File(mFilesDir, crime.getPhotoFilename());
    }

    public static void initialize(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new CrimeRepository(context);
        }
    }

    public static CrimeRepository get() {
        return INSTANCE;
    }

    public LiveData<List<Crime>> getCrimes() {
        return mCrimeDao.getCrimes();
    }

    public LiveData<Crime> getCrime(UUID id) {
        return mCrimeDao.getCrime(id);
    }
}

