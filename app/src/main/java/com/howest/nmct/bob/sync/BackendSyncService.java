package com.howest.nmct.bob.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * illyism
 * 29/12/15
 */
public class BackendSyncService extends Service {

    private static BackendSyncAdapter sSyncAdapter = null;
    private static final Object sLock = new Object();

    @Override
    public void onCreate() {
        super.onCreate();
        synchronized (sLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new BackendSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
