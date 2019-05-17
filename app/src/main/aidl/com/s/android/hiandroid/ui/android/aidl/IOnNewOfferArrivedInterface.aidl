// IOnNewOfferArrivedInterface.aidl
package com.s.android.hiandroid.ui.android.aidl;

// Declare any non-default types here with import statements
import com.s.android.hiandroid.ui.android.aidl.Offer;

interface IOnNewOfferArrivedInterface {

    void onNewOfferArrived(in Offer offer);
}
