package volleyappsetup.com.theapp.Helper;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Locale;

public class LocalHelper {
    private static String Selected_Lang = "Locale.Helper.Selected.Language";

    public static String onAttach (Context context){
        String lang = String.valueOf(getPersistedData(context,Locale.getDefault().getLanguage()));
        return setLocale(context,lang);
    }

    public static String onAttach (Context context, String defLang){
        String lang = String.valueOf(getPersistedData(context,defLang));
        return setLocale(context,lang);
    }

    private static Context getPersistedData(Context context, String language) {

        persist(context,language);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            return updateRes(context,language);
        return updateResLeg(context,language);
    }
        @TargetApi(Build.VERSION_CODES.N)
    private static Context updateRes(Context context, String language) {

        Locale locale = new Locale(language);
        Locale.setDefault(locale);

            Configuration conf = context.getResources().getConfiguration();
            conf.setLocale(locale);
            conf.setLayoutDirection(locale);

            return context.createConfigurationContext(conf);
    }
    @SuppressWarnings("depraction")
    private static Context updateResLeg(Context context, String language) {

        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration config = resources.getConfiguration();
        config.locale = locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            config.setLayoutDirection(locale);
        resources.updateConfiguration(config,resources.getDisplayMetrics());
        return context;
    }

    private static void persist(Context context,String lang)
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(Selected_Lang,lang);
        editor.apply();
    }

    private static String setLocale(Context context, String lang) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(Selected_Lang,lang);
    }
}
