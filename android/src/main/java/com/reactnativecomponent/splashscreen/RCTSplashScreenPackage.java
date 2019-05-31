package com.reactnativecomponent.splashscreen;

import android.util.Log;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class RCTSplashScreenPackage implements ReactPackage {
    protected static String LandescapeMode = "";

    public RCTSplashScreenPackage(String landescapeMode) {
        Log.e("RCTSplashScreenPackage","Setting LandescapeMode to : " + landescapeMode.toString());
        LandescapeMode = landescapeMode;
    }

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        return Arrays.<NativeModule>asList(
                new RCTSplashScreenModule(reactContext)
        );
    }

    @Override
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Arrays.<ViewManager>asList();
    }
}
