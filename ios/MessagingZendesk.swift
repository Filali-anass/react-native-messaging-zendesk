import Foundation
import React
import ZendeskSDK
import ZendeskSDKMessaging

@objc(MessagingZendesk)
class MessagingZendesk: NSObject {
    
    
    @objc
    func initSdk(_ channelKey:String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
      Zendesk.initialize(withChannelKey: channelKey,
                        messagingFactory: DefaultMessagingFactory()) { result in
        if case let .failure(error) = result {
          reject("error","\(error)",nil)
          print("Messaging did not initialize.\nError: \(error.localizedDescription)")
        } else {
          resolve(true)
        }
      }
    }
    
    @objc
    func showMessaging() {
        DispatchQueue.main.async {
         guard let zendeskController = Zendesk.instance?.messaging?.messagingViewController() else {
           return }
         let viewController = RCTPresentedViewController();
         viewController?.present(zendeskController, animated: true) {
           print("Messaging have shown")
         }
       }
    }
    
    @objc
    func loginUser(_ jwtToken:String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        Zendesk.instance?.loginUser(with: jwtToken) { result in
            switch result {
            case .success:
                resolve(true)
            case .failure(let error):
                reject("error","\(error)",nil)
            }
        }
    }
    
    @objc
    func logoutUser(_ resolve: @escaping RCTPromiseResolveBlock,
                    rejecter reject: @escaping RCTPromiseRejectBlock) {
        Zendesk.instance?.logoutUser { result in
            switch result {
            case .success:
                resolve(true)
            case .failure(let error):
                reject("error","\(error)",nil)
            }
        }
    }
    
}
