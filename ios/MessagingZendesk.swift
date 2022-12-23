import Foundation
import React
import ZendeskSDK
import ZendeskSDKMessaging

@objc(MessagingZendesk)
class MessagingZendesk: RCTEventEmitter {
    private var hasListener = false

    override private init() {
      super.init()
    }

    override func startObserving() {
      hasListener = true
    }

    override func stopObserving() {
      hasListener = false
    }

    @objc(supportedEvents)
    override func supportedEvents() -> [String] {
      return ["unreadMessageCountChanged", "authenticationFailed"]
    }
    
    @objc
    func subscribe(_ eventName: String){
      Zendesk.instance?.addEventObserver(self) { event in
        switch event {
        case .unreadMessageCountChanged(let unreadCount):
          self.sendEvent(withName: "unreadMessageCountChanged", body: unreadCount)
        case .authenticationFailed(let error as NSError):
            print("Authentication error received: \(error)")
            print("Domain: \(error.domain)")
            print("Error code: \(error.code)")
            print("Localized Description: \(error.localizedDescription)")
            // ...
        @unknown default:
            break
        }
      }
    }

    @objc
    func unsubscribe(_ eventName: String){
      Zendesk.instance?.removeEventObserver(self)
    }

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
