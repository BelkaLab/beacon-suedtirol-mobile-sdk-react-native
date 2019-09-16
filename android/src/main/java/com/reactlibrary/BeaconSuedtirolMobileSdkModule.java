package com.reactlibrary;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.Callback;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.WritableMap;

import it.bz.beacon.beaconsuedtirolsdk.NearbyBeaconManager;
import it.bz.beacon.beaconsuedtirolsdk.exception.MissingLocationPermissionException;
import it.bz.beacon.beaconsuedtirolsdk.exception.NoBluetoothException;
import it.bz.beacon.beaconsuedtirolsdk.listener.IBeaconListener;
import it.bz.beacon.beaconsuedtirolsdk.result.IBeacon;

public class BeaconSuedtirolMobileSdkModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    private final NearbyBeaconManager beaconManager;

    public BeaconSuedtirolMobileSdkModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "NearbyBeacons";
    }

    @ReactMethod
    public void sampleMethod(String stringArgument, int numberArgument, Callback callback) {
        // TODO: Implement some actually useful functionality
        callback.invoke("Received numberArgument: " + numberArgument + " stringArgument: " + stringArgument);
    }

    @ReactMethod
    public void startScanning(Callback callback) {
        try {
            NearbyBeaconManager.getInstance().startScanning();
            callback.invoke("Scan started");
        } catch (NoBluetoothException e) {
            e.printStackTrace();
        } catch (MissingLocationPermissionException e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void stopScanning(Callback callback) {
        try {
            NearbyBeaconManager.getInstance().stopScanning();
            callback.invoke("Scan stopped");
        } catch (NoBluetoothException e) {
            e.printStackTrace();
        } catch (MissingLocationPermissionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onIBeaconDiscovered(IBeacon iBeacon) {
        WritableMap beaconMap = Arguments.createMap();
        sendEvent(this.reactContext, "beaconDiscovered", beaconMap);
    }

    @Override
    public void onIBeaconLost(IBeacon iBeacon) {

    }

    private void sendEvent(ReactContext reactContext, String eventName, @Nullable WritableMap params) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }
}
