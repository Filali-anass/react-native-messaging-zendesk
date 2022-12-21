package com.messagingzendesk;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;


import kotlin.Unit;
import zendesk.android.FailureCallback;
import zendesk.android.SuccessCallback;
import zendesk.android.Zendesk;
import zendesk.android.ZendeskUser;
import zendesk.messaging.android.DefaultMessagingFactory;

@ReactModule(name = MessagingZendeskModule.NAME)
public class MessagingZendeskModule extends ReactContextBaseJavaModule {
  public static final String NAME = "MessagingZendesk";

  public MessagingZendeskModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  @ReactMethod
    public void initSdk (String channelKey, Promise promise) {
        try {
            Zendesk.initialize(
                    getReactApplicationContext(),
                    channelKey,
                    zendesk -> {
                      Log.i("MessagingZendesk", "Initialization successful");
                      promise.resolve(true);
                    },
                    error -> {
                      Log.e("MessagingZendesk", "Messaging failed to initialize", error);
                      promise.reject(error);
                    },
                    new DefaultMessagingFactory());
        } catch (Exception err) {
          promise.reject(err);
        }
    }

    @ReactMethod
    public void loginUser (String jwtToken, Promise promise) {
        Zendesk.getInstance().loginUser(jwtToken, new SuccessCallback<ZendeskUser>() {
            @Override
            public void onSuccess(ZendeskUser value) {
              Log.i("MessagingZendesk", "LoginUser successful");
              promise.resolve(true);
          }
        }, new FailureCallback<Throwable>() {
            @Override
            public void onFailure(@NonNull Throwable error) {
              Log.e("MessagingZendesk", "LoginUser error", error);
              promise.reject(error);
            }
        });
    }

    @ReactMethod
    public void logoutUser (Promise promise) {
        Zendesk.getInstance().logoutUser(new SuccessCallback<Unit>() {
            @Override
            public void onSuccess(Unit value) {
              Log.i("MessagingZendesk", "logoutUser successful");
              promise.resolve(true);
            }
        }, new FailureCallback<Throwable>() {
            @Override
            public void onFailure(@NonNull Throwable error) {
              Log.e("MessagingZendesk", "logoutUser error", error);
              promise.reject(error);
            }
        });
    }

    @ReactMethod
    public void showMessaging (Promise promise) {
        try {
            Zendesk.getInstance().getMessaging().showMessaging(getCurrentActivity());
            promise.resolve(true);
        } catch (Exception err) {
            Log.e("MessagingZendesk", "Messaging failed to initialize", err);
            promise.reject(err);
        }
    }
}
