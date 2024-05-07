package medicare.application.network;


// ApiService.java
import medicare.application.model.APIResponse;
import medicare.application.model.Loginrequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIService {

    // Endpoint for patient login
    @POST("patient/login")
    Call<APIResponse> patientLogin(@Body Loginrequest request);

    // Endpoint for doctor login
    @POST("doctor/login")
    Call<APIResponse> doctorLogin(@Body Loginrequest request);

    // Add more endpoints as needed
}
