# react-native-messaging-zendesk

react-native module for Zendes Messaging

based on the native SDKs guide for Zendesk messaging

See the [Android integration guide](https://developer.zendesk.com/documentation/zendesk-web-widget-sdks/sdks/web/getting_started/)

See the [IOS integration guide](https://developer.zendesk.com/documentation/zendesk-web-widget-sdks/sdks/ios/getting_started/)

## Installation

```sh
npm install react-native-messaging-zendesk
```

## Usage

- initialize SDK

```js
import { initSdk } from 'react-native-messaging-zendesk';

// ...

initSdk({
  android: '< ANDROID_CHAT_CHANNEL_ID >',
  ios: '< IOS_CHAT_CHANNEL_ID >',
});
```

- open the chat Screen

```js
import { openChat } from 'react-native-messaging-zendesk';

// ...

openChat();
```

- authenticated Access

See the [Enabling authenticated visitors for messaging with Zendesk SDKs](https://developer.zendesk.com/documentation/zendesk-web-widget-sdks/sdks/web/enabling_auth_visitors/)

```js
import { openChat } from 'react-native-messaging-zendesk';

// ...

// login User
loginUser('< BACKEND_GENERATED_JWT_TOKEN >');
// logout User
logoutUser();
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
