// JobsInterface.aidl
package com.s.android.hiandroid.ui.android.aidl;

// Declare any non-default types here with import statements
import com.s.android.hiandroid.ui.android.aidl.Offer;
import com.s.android.hiandroid.ui.android.aidl.IOnNewOfferArrivedInterface;

interface JobsInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    List<Offer> queryOffers();

    void addOffer(in Offer offer);

    void registerListener(IOnNewOfferArrivedInterface listener);

    void unregisterListener(IOnNewOfferArrivedInterface listener);
}
