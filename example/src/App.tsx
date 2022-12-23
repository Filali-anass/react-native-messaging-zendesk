import * as React from 'react';

import { StyleSheet, View, Button, Text } from 'react-native';
import {
  initSdk,
  showMessaging,
  loginUser,
  logoutUser,
  subscribe,
  unsubscribe,
} from 'react-native-messaging-zendesk';

export default function App() {
  const [unreadMessagesCount, setUnreadMessagesCount] =
    React.useState<number>(0);

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
    initSdk({
      android: '< ANDROID_CHAT_CHANNEL_ID >',
      ios: '< IOS_CHAT_CHANNEL_ID >',
    }).then(() => {
      subscribe('unreadMessageCountChanged', (messagesCount) => {
        setUnreadMessagesCount(messagesCount);
      });
    });

    return () => {
      unsubscribe('unreadMessageCountChanged');
    };
  }, []);

  return (
    <View style={styles.container}>
      <Text>Unread Messages: {unreadMessagesCount}</Text>
      <View style={styles.buttonContainer}>
        <Button title="Login User" onPress={() => performLoginUser()} />
      </View>
      <View style={styles.buttonContainer}>
        <Button title="Logout User" onPress={() => performLogoutUser()} />
      </View>
      <View style={styles.buttonContainer}>
        <Button title="Open Chat" onPress={() => performOpenChat()} />
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  buttonContainer: {
    marginVertical: 10,
    width: 250,
  },
});
