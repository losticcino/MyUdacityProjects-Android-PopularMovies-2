package com.rasjdd.ras.popularmoviesstage2.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtilities {

    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public String readSharedPreferenceString(String key, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.preferencesIdentity, 0);
        return sharedPreferences.getString(key,defaultValue);
    }

    public void setSharedPreferenceString(String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.preferencesIdentity, 0).edit();

        if (validatePreference(key, value)) {
            editor.putString(key,value);
        }
        editor.apply();
        editor.commit();
    }

    public boolean validatePreference(String key, String value) {
        switch (key) {
            case Constants.prefSortTypeKey:
                return value.equals(Constants.sortByFavorites) || value.equals(Constants.TMDBAPIQueryKeyGetPopular)
                        || value.equals(Constants.TMDBAPIQueryKeyGetRating) || value.equals(Constants.TMDBAPIQueryKeyGetNowPlaying);
            case Constants.prefSortOrderKey:
                return value.equals(Constants.sortDescending) || value.equals(Constants.sortAscending);
            default:
                return false;
        }
     }
}
