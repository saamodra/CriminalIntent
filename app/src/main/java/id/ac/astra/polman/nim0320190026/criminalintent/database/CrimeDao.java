package id.ac.astra.polman.nim0320190026.criminalintent.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import java.util.UUID;

import id.ac.astra.polman.nim0320190026.criminalintent.Crime;

@Dao
public interface CrimeDao {

    @Query("SELECT * FROM crime")
//    public List<Crime> getCrimes();
    public LiveData<List<Crime>> getCrimes();

    @Query("SELECT * FROM crime WHERE mId = :id")
//    public Crime getCrime(UUID id);
    public LiveData<Crime> getCrime(UUID id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Crime crime);

    @Query("DELETE FROM crime")
    void deleteAll();

    @Update
    void updateCrime(Crime crime);
}
