package id.ac.astra.polman.nim0320190026.criminalintent.api;

public class ApiUtils {
    public static final String API_URL = "http://192.168.43.145:8080/";

    private ApiUtils() {

    }

    public static CrimeService getCrimeService() {
        return RetrofitClient.getClient(API_URL).create(CrimeService.class);
    }
}
