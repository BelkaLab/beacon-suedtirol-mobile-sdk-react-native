package com.reactlibrary;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.List;

import androidx.annotation.Nullable;
import it.bz.beacon.beaconsuedtirolsdk.NearbyBeaconManager;
import it.bz.beacon.beaconsuedtirolsdk.configuration.BluetoothMode;
import it.bz.beacon.beaconsuedtirolsdk.configuration.RangeDistance;
import it.bz.beacon.beaconsuedtirolsdk.exception.MissingLocationPermissionException;
import it.bz.beacon.beaconsuedtirolsdk.exception.NoBluetoothException;
import it.bz.beacon.beaconsuedtirolsdk.listener.IBeaconListener;
import it.bz.beacon.beaconsuedtirolsdk.result.IBeacon;
import it.bz.beacon.beaconsuedtirolsdk.result.IBeaconExtended;

public class BeaconSuedtirolMobileSdkModule extends ReactContextBaseJavaModule implements IBeaconListener {

    private final ReactApplicationContext reactContext;
    private final NearbyBeaconManager beaconManager;

    public BeaconSuedtirolMobileSdkModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        this.beaconManager = NearbyBeaconManager.getInstance();
        this.beaconManager.setIBeaconListener(this);
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
            beaconManager.startScanning();
            callback.invoke("Scan started");
        } catch (NoBluetoothException e) {
            e.printStackTrace();
        } catch (MissingLocationPermissionException e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public boolean isScanning() {
        return beaconManager.isScanning();
    }

    @ReactMethod
    public void stopScanning(Callback callback) {
        beaconManager.stopScanning();
        callback.invoke("Scan stopped");
    }

    @ReactMethod
    public void configureScanMode(int bluetoohMode) {
        beaconManager.configureScanMode(BTMode.getBTMode(bluetoohMode));
    }

    @ReactMethod
    public void setDeviceUpdateCallbackInterval(int seconds) {
        beaconManager.setDeviceupdateCallbackInterval(seconds);
    }

    private enum BTMode {
        low_power(0),
        balanced(1),
        low_latency(2);

        private final int value;

        BTMode(int value) {
            this.value = value;
        }

        public static BTMode valueOf(int value) {
            for (BTMode bt : BTMode.values()) {
                if (bt.value == value) return bt;
            }
            throw new IllegalArgumentException("BTMode not supported");
        }

        public static BluetoothMode getBTMode(int btMode) {
            switch (valueOf(btMode)) {
                case low_power:
                    return BluetoothMode.low_power;
                case balanced:
                default:
                    return BluetoothMode.balanced;
                case low_latency:
                    return BluetoothMode.low_latency;
            }
        }
    };

    @Override
    public void onIBeaconDiscovered(IBeacon iBeacon) {
        WritableMap beaconMap = Arguments.createMap();
        if (iBeacon.getInfo() == null) {
            return;
        }
        beaconMap.putString("id", iBeacon.getInfo().getId());
        beaconMap.putDouble("latitude", iBeacon.getInfo().getLatitude());
        beaconMap.putDouble("longitude", iBeacon.getInfo().getLongitude());
        sendEvent(this.reactContext, "beaconDiscovered", beaconMap);
    }

    @Override
    public void onIBeaconDiscovered(IBeaconExtended iBeacon) {
        WritableMap beaconMap = Arguments.createMap();
        if (iBeacon.getInfo() == null) {
            return;
        }
        beaconMap.putString("id", iBeacon.getInfo().getId());
        beaconMap.putDouble("latitude", iBeacon.getInfo().getLatitude());
        beaconMap.putDouble("longitude", iBeacon.getInfo().getLongitude());
        beaconMap.putDouble("distance", iBeacon.getDistance());
        beaconMap.putString("range", valueOf(iBeacon.getRangeDistance()));
        sendEvent(this.reactContext, "beaconDiscovered", beaconMap);
    }

    @Override
    public void onIBeaconUpdated(List<IBeaconExtended> iBeacons) {
        WritableArray array = Arguments.createArray();
        for (IBeaconExtended b : iBeacons) {
            WritableMap beaconMap = Arguments.createMap();
            if (b.getInfo() == null) {
                return;
            }
            beaconMap.putString("id", b.getInfo().getId());
            beaconMap.putDouble("latitude", b.getInfo().getLatitude());
            beaconMap.putDouble("longitude", b.getInfo().getLongitude());
            beaconMap.putDouble("distance", b.getDistance());
            beaconMap.putString("range", valueOf(b.getRangeDistance()));
            array.pushMap(beaconMap);
        }
        WritableMap beaconsMap = Arguments.createMap();
        beaconsMap.putArray("beacons", array);
        sendEvent(this.reactContext, "beaconsUpdated", beaconsMap);
    }

    private String valueOf(RangeDistance rangeDistance) {
        switch (rangeDistance) {
            case immediate:
                return "immediate";
            case near:
                return "near";
            case far:
                return "far";
            case unknown:
            default:
                return "unknown";
        }
    }

    @Override
    public void onIBeaconLost(IBeacon iBeacon) {
        WritableMap beaconMap = Arguments.createMap();
        beaconMap.putString("id", iBeacon.getInfo().getId());
        sendEvent(this.reactContext, "beaconLost", beaconMap);
    }

    private void sendEvent(ReactContext reactContext, String eventName, @Nullable WritableMap params) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }
}