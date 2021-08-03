package id.ac.astra.polman.nim0320190026.criminalintent.api;

import java.util.List;

import id.ac.astra.polman.nim0320190026.criminalintent.model.Crime;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface CrimeService {
    @GET("crime")
    Call<Crime> getCrimeById(@Query("id") String id);

    @GET("crimes")
    Call<List<Crime>> getCrimes();

    @POST("crime")
    Call<Crime> addCrime(@Body Crime crime);

    @PUT("crime")
    Call<Crime> updateCrime(@Body Crime crime);

    @DELETE("crime")
    Call<Crime> deleteCrimeById(@Query("id") String id);

}
