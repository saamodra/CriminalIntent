package id.ac.astra.polman.nim0320190026.criminalintent.database;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import id.ac.astra.polman.nim0320190026.criminalintent.Crime;

@Database(entities = {Crime.class}, version = 3)
@TypeConverters(CrimeTypeConverters.class)
abstract public class CrimeDatabase extends RoomDatabase {
    public static final int NUMBER_OF_THREADS = 4;

    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract CrimeDao crimeDao();

    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Crime " + "ADD COLUMN mSuspectNumber TEXT");
        }
    };

}
