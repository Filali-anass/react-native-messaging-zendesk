import { NativeModules, Platform } from 'react-native';

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

export function initSdk(channels: {
  android: string;
  ios: string;
}): Promise<boolean> {
  if (Platform.OS !== 'android') return new Promise((resolve) => resolve(true));
  return MessagingZendesk.initSdk(Platform.select(channels));
}

export function openChat(): Promise<void> {
  if (Platform.OS !== 'android') return new Promise((resolve) => resolve());
  return MessagingZendesk.showMessaging();
}

export function loginUser(jwt: string): Promise<boolean> {
  if (Platform.OS !== 'android') return new Promise((resolve) => resolve(true));
  return MessagingZendesk.loginUser(jwt);
}

export function logoutUser(): Promise<void> {
  if (Platform.OS !== 'android') return new Promise((resolve) => resolve());
  return MessagingZendesk.logoutUser();
}
