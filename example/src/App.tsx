import * as React from 'react';

import { StyleSheet, View, Button } from 'react-native';
import {
  initSdk,
  showMessaging,
  loginUser,
  logoutUser,
  subscribe,
  unsubscribe,
} from 'react-native-messaging-zendesk';

export default function App() {
  const performInitSdk = () => {
    initSdk({
      android: '< ANDROID_CHAT_CHANNEL_ID >',
      ios: '< IOS_CHAT_CHANNEL_ID >',
    }).then(() => {
      subscribe('messagesCountChanged', (messagesCount) => {
        console.log({ messagesCount });
      });
    });
  };

  const performOpenChat = () => {
    showMessaging();
  };

  const performLoginUser = () => {
    loginUser('< BACKEND_GENERATED_JWT_TOKEN >');
  };

  const performLogoutUser = () => {
    logoutUser();
  };

  React.useEffect(() => {
    return () => {
      unsubscribe('messagesCountChanged');
    };
  }, []);

  return (
    <View style={styles.container}>
      <Button title="Init Sdk" onPress={() => performInitSdk()} />
      <Button title="Login User" onPress={() => performLoginUser()} />
      <Button title="Logout User" onPress={() => performLogoutUser()} />
      <Button title="Open Chat" onPress={() => performOpenChat()} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    gap: 2,
  },
});
