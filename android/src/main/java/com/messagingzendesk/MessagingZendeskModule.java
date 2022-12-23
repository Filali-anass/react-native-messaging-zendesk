package com.messagingzendesk;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.modules.core.DeviceEventManagerModule;


import kotlin.Unit;
import zendesk.android.FailureCallback;
import zendesk.android.SuccessCallback;
import zendesk.android.Zendesk;
import zendesk.android.ZendeskUser;
import zendesk.android.events.ZendeskEvent;
import zendesk.android.events.ZendeskEventListener;
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

  ZendeskEventListener zendeskEventListener =
    new ZendeskEventListener() {
      @Override
      public void onEvent(@NonNull ZendeskEvent zendeskEvent) {
        if (zendeskEvent instanceof ZendeskEvent.UnreadMessageCountChanged) {
          // Your custom action...
          Log.i("MessagingZendeskApp", "count changed " + ((ZendeskEvent.UnreadMessageCountChanged) zendeskEvent).getCurrentUnreadCount());

          sendMessagesCount(getReactApplicationContext(), ((ZendeskEvent.UnreadMessageCountChanged) zendeskEvent).getCurrentUnreadCount());
        } else if (zendeskEvent instanceof ZendeskEvent.AuthenticationFailed){
          // Your custom action...
        }
      }
    };

  private void sendMessagesCount(ReactContext reactContext, int messagesCount){
    reactContext
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
      .emit("unreadMessageCountChanged", messagesCount);
  }

  @ReactMethod
  public void addMessagesCountListener() {
    // Keep: Required for RN built in Event Emitter Calls.
    Zendesk.getInstance().addEventListener(zendeskEventListener);
  }

  @ReactMethod
  public void removeMessagesCountListener() {
    // Keep: Required for RN built in Event Emitter Calls.
    Zendesk.getInstance().removeEventListener(zendeskEventListener);
  }

  @ReactMethod
  public void subscribe(String eventName) {
    if(eventName.equals("unreadMessageCountChanged")){
      Zendesk.getInstance().addEventListener(zendeskEventListener);
    }
  }

  @ReactMethod
  public void unsubscribe(String eventName) {
    if(eventName.equals("unreadMessageCountChanged")) {
      Zendesk.getInstance().removeEventListener(zendeskEventListener);
    }
  }

  @ReactMethod
  public void addListener(String eventName) {}

  @ReactMethod
  public void removeListeners(int count) {}

  @ReactMethod
    public void initSdk (String channelKey, Promise promise) {
        try {
            Zendesk.initialize(
                    getReactApplicationContext(),
                    channelKey,
                    zendesk -> {
                      Log.i("MessagingZendeskApp", "Initialization successful");
                      // zendesk.addEventListener(zendeskEventListener);
                      promise.resolve(true);
                    },
                    error -> {
                      Log.e("MessagingZendeskApp", "Messaging failed to initialize", error);
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
              Log.i("MessagingZendeskApp", "LoginUser successful");
              promise.resolve(true);
          }
        }, new FailureCallback<Throwable>() {
            @Override
            public void onFailure(@NonNull Throwable error) {
              Log.e("MessagingZendeskApp", "LoginUser error", error);
              promise.reject(error);
            }
        });
    }

    @ReactMethod
    public void logoutUser (Promise promise) {
        Zendesk.getInstance().logoutUser(new SuccessCallback<Unit>() {
            @Override
            public void onSuccess(Unit value) {
              Log.i("MessagingZendeskApp", "logoutUser successful");
              promise.resolve(true);
            }
        }, new FailureCallback<Throwable>() {
            @Override
            public void onFailure(@NonNull Throwable error) {
              Log.e("MessagingZendeskApp", "logoutUser error", error);
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
            Log.e("MessagingZendeskApp", "Messaging failed to initialize", err);
            promise.reject(err);
        }
    }
}
