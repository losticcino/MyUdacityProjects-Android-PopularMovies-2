package com.rasjdd.ras.popularmoviesstage2.Utilities;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
public class AppExecutors {

    private static final Object LOCK = new Object();
    private static AppExecutors appInstance;
    private final Executor diskIO;
    private final Executor mainThread;

    private AppExecutors(Executor diskIO, Executor networkIO, Executor mainThread) {
        this.mainThread = mainThread;
        this.diskIO = diskIO;
    }

    public static AppExecutors getExecInstance() {

        if (null == appInstance) {

            synchronized (LOCK) {

                appInstance = new AppExecutors(Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(2),
                        new MainThreadExecutor());
            }
        }
        return appInstance;
    }

    private static class MainThreadExecutor implements Executor {


        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {

            mainThreadHandler.post(command);

        }
    }

    public Executor getMainThread() {

        return mainThread;

    }

    public Executor getDiskIO() {

        return diskIO;

    }
}
