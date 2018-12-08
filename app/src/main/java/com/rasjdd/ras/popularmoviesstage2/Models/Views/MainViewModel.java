package com.rasjdd.ras.popularmoviesstage2.Models.Views;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.rasjdd.ras.popularmoviesstage2.DatabaseFunctions.FavoriteUtilities;

public class MainViewModel extends AndroidViewModel {
    private FavoriteUtilities favoriteUtilities;

    public MainViewModel(@NonNull Application application) {
        super(application);
        favoriteUtilities = new FavoriteUtilities(application);
    }


}
