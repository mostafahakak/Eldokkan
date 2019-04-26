package volleyappsetup.com.theapp.commen;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import volleyappsetup.com.theapp.Model.User;
import volleyappsetup.com.theapp.Remote.IGoogleService;

public class Common {

    public static class ReftrofitClient {
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
    public static User currentuser;


    private static final String GOOGLE_API_URL = "https://maps.googleapis.com/";

    public static IGoogleService getGoogleMaps()
    {
        return ReftrofitClient.getGoogleClient(GOOGLE_API_URL).create(IGoogleService.class);
    }

    public static boolean isConnectingToNet(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null)
            {
                for (int i=0;i<info.length;i++)
                {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }

    public static final String DELETE = "Delete";
    public static final String USER_KEY = "user";
    public static final String PWD_KEY = "password";


}
