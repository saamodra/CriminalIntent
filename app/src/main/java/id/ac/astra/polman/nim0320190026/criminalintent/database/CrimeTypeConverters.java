package id.ac.astra.polman.nim0320190026.criminalintent.database;

import androidx.room.TypeConverter;

import java.util.Date;
import java.util.UUID;

public class CrimeTypeConverters {

    @TypeConverter
    public Long fromDate(Date date) {
        if (date == null) {
            return null;
        } else {
            return date.getTime();
        }
    }

    @TypeConverter
    public Date toDate(Long millisSinceEpoch) {
        if (millisSinceEpoch == null) {
            return new Date();
        } else {
            return new Date(millisSinceEpoch);
        }
    }

    @TypeConverter
    public UUID toUUID(String uuid) {
        if (uuid == null) {
            return UUID.randomUUID();
        } else {
            return UUID.fromString(uuid);
        }
    }

    @TypeConverter
    public String fromUUID(UUID uuid) {
        if (uuid == null) {
            return null;
        } else {
            return uuid.toString();
        }
    }
}
