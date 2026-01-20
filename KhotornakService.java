package com.khotornak.guard;

import android.net.VpnService;
import android.os.ParcelFileDescriptor;
import android.content.Intent;
import android.util.Log;

public class KhotornakService extends VpnService {
    private ParcelFileDescriptor vpnInterface = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Builder builder = new Builder();
        
        try {
            builder.setSession("KhotornakShield")
                   .addAddress("10.0.0.2", 32)
                   .addDnsServer("94.140.14.14") // AdGuard DNS (Primary)
                   .addDnsServer("94.140.15.15") // AdGuard DNS (Secondary)
                   .addRoute("0.0.0.0", 0)
                   .setBlocking(true);

            // যে অ্যাপকে ব্লক করতে চাস (যেমন গুগল প্লে স্টোর)
            builder.addDisallowedApplication("com.android.vending");

            vpnInterface = builder.establish();
            Log.d("Khotornak", "Firewall Started!");
        } catch (Exception e) {
            Log.e("Khotornak", "Error: " + e.getMessage());
        }
        
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        try {
            if (vpnInterface != null) {
                vpnInterface.close();
                vpnInterface = null;
            }
        } catch (Exception e) { e.printStackTrace(); }
        super.onDestroy();
    }
}
