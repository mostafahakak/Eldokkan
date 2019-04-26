package volleyappsetup.com.theapp.Remote;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ReftrofitClient {
    private static Retrofit retrofit =null;

    public static Retrofit getGoogleClient(String baseURL)
    {
        if(retrofit == null)
        {
            retrofit = new Retrofit.Builder().
                    baseUrl(baseURL).
                    addConverterFactory(ScalarsConverterFactory.create()).
                    build();
        }
        return retrofit;
    }
}
