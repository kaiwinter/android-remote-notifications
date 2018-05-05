[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-android--remote--notifications-green.svg?style=flat)](https://android-arsenal.com/details/1/2058)
# android-remote-notifications
A Google GCM/Amazon SNS alternative using pull instead of push.
## Main features
- Independent user notifications (no Google GCM or Amazon SNS), just put a JSON file in the cloud
- Framework will update available notifications automatically in defined time intervals (`now`, `daily`, `weekly`, `bi-weekly`, `monthly`)
- Flexible parameters:
  - start date for notification (distribute early, show later)
  - amount of times the notification should be shown
  - interval between showing notifications (`always`, `every day`, `every week`, `every month`)
  - specify on which app version (`versionCode`) the notification should be shown
- Show notifications as `AlertDialog` or `Toast` message (defined by the remote JSON file)
  - `AlertDialog`: Specify button captions and actions: `Open Store`-Action, `Open URL`-Action, `Exit App`-Action, all defined by a JSON file on your server
- perfect to inform your users about discounts on one of your other apps or premium version
- pro tip: as some users don't always update to the latest app versions you should integrate this framework in an early phase if you plan to use it at a later time.

## Download
Gradle:
```groovy
implementation ('com.github.kaiwinter:android-remote-notifications:1.1.3@aar') {
    transitive = true
}
```

## Demo:
- [Demo App](https://github.com/kaiwinter/android-remote-notifications/releases/download/v1.1.0/remotenotifications-example-1.1.0.apk)
- Live Demo: [Appetize.io](https://appetize.io/app/efkur5jaxuztahp6wtc5n65x74)

## How to integrate
You can find the source code of the example app here: [MainActivity](https://github.com/kaiwinter/android-remote-notifications/blob/master/example/src/main/java/com/github/kaiwinter/androidremotenotifications/example/MainActivity.java)
### The easy way
- Call `RemoteNotifications.start(context, url, UpdatePolicy.NOW);` in your `onCreate()` or `onResume()` method

this will
- start an update of the notifications (use `UpdatePolicy.WEEKLY` in production environment to update once a week)
- show due notifications to the user
 
### For more control
```java
RemoteNotifications rn = new RemoteNotifications(context, url);
rn.updateNotifications(UpdatePolicy.NOW);
rn.showNotifications(); // if update still runs, event will be queued and carried out later

// -or, register a listener on update completion-
rn.updateNotifications(UpdatePolicy.NOW, finishListener);
```

## How to build a JSON notification file
First: You don´t have to write the JSON file by hand. Just use the API to initialize a Notification object and then create JSON from it: [NotificationCreatorUtil ](https://github.com/kaiwinter/android-remote-notifications/blob/master/notification-creator-util/src/main/java/com/github/kaiwinter/androidremotenotifications/util/NotificationCreatorUtil.java)
### Example of a Toast Notification
```
[
  {
    "type": "ToastNotification",
    "notificationConfiguration": {
      "startShowingDate": null,
      "executionPolicy": "ALWAYS",
      "numberOfTotalViews": null,
      "versionCodePolicy": null
    },
    "message": "This is a Toast Notification",
    "duration": 1
  }
]
```
### Example of an AlertDialog Notification
```
[
  {
    "type": "AlertDialogNotification",
    "notificationConfiguration": {
      "startShowingDate": null,
      "executionPolicy": "ALWAYS",
      "numberOfTotalViews": null,
      "versionCodePolicy": null
    },
    "title": "Title",
    "message": "This is an AlertDialog notification",
    "negativeButtonText": "Exit App",
    "neutralButtonText": "Open web page",
    "positiveButtonText": "Open Play Store",
    "positiveButtonAction": {
      "type": "OpenStoreButtonAction",
      "packageName": "de.vorlesungsfrei.taekwondo.ads"
    },
    "negativeButtonAction": {
      "type": "ExitAppButtonAction"
    },
    "neutralButtonAction": {
      "type": "OpenUrlButtonAction",
      "link": "https://github.com/kaiwinter/android-remote-notifications"
    },
    "modal": false
  }
]
```

## Screenshots
![Preview](http://i.imgur.com/Hp1aowm.png)

## License
     Copyright 2018 Kai Winter
     
     Licensed under the Apache License, Version 2.0 (the "License");
     you may not use this file except in compliance with the License.
     You may obtain a copy of the License at
     
         http://www.apache.org/licenses/LICENSE-2.0
     
     Unless required by applicable law or agreed to in writing, software
     distributed under the License is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     See the License for the specific language governing permissions and
     limitations under the License.
