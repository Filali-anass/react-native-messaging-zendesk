#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(MessagingZendesk, NSObject)

RCT_EXTERN_METHOD(initSdk: (NSString *)channelKey resolver:(RCTPromiseResolveBlock *)resolve rejecter: (RCTPromiseRejectBlock *)reject)

RCT_EXTERN_METHOD(showMessaging)

RCT_EXTERN_METHOD(loginUser: (NSString *)jwtToken resolver:(RCTPromiseResolveBlock *)resolve rejecter: (RCTPromiseRejectBlock *)reject)

RCT_EXTERN_METHOD(logoutUser: (RCTPromiseResolveBlock *)resolve rejecter: (RCTPromiseRejectBlock *)reject)

RCT_EXTERN_METHOD(subscribe: (NSString *)eventName)

RCT_EXTERN_METHOD(unsubscribe: (NSString *)eventName)

+ (BOOL)requiresMainQueueSetup
{
  return NO;
}

@end
