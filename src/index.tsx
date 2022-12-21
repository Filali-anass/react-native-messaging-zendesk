import { NativeEventEmitter, NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-messaging-zendesk' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const MessagingZendesk = NativeModules.MessagingZendesk
  ? NativeModules.MessagingZendesk
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

const eventEmitter = new NativeEventEmitter(MessagingZendesk);

export function subscribe(
  eventName: string,
  onEvent: (event: any) => void
): void {
  try {
    MessagingZendesk.subscribe(eventName);
    eventEmitter.addListener(eventName, (event) => {
      onEvent(event);
    });
  } catch (e) {}
}

export function unsubscribe(eventName: string): void {
  try {
    MessagingZendesk.unsubscribe(eventName);
    eventEmitter.removeAllListeners(eventName);
  } catch (e) {}
}

export function initSdk(channels: {
  android: string;
  ios: string;
}): Promise<boolean> {
  if (Platform.OS !== 'android') return new Promise((resolve) => resolve(true));
  return MessagingZendesk.initSdk(Platform.select(channels));
}

export function showMessaging(): void {
  if (Platform.OS !== 'android') return;
  return MessagingZendesk.showMessaging();
}

export function loginUser(jwt: string): Promise<boolean> {
  if (Platform.OS !== 'android') return new Promise((resolve) => resolve(true));
  return MessagingZendesk.loginUser(jwt);
}

export function logoutUser(): void {
  if (Platform.OS !== 'android') return;
  return MessagingZendesk.logoutUser();
}
