# android-remote-notifications
An Google GCM/Amazon SNS alternative
## Main features
- Independent user notifications (no Google GCM or Amazon SNS), just put a JSON file in the cloud
- Framework will update available notifications automatically in defined time intervals (`now`, `daily`, `weekly`, `monthly`)
- Flexible parameters:
  - start date for notification (distribute early, show later)
  - amount of times the notification should be shown
  - interval between showing notifications (`always`, `every day`, `every week`, `every month`)
  - specify on which app version (`versionCode`) the notification should be shown
- Show notifications as `AlertDialog` or `Toast` message (defined by the remote JSON file)
  - `AlertDialog`: Specify button captions and actions: `Open Store`-Action, `Open URL`-Action, `Exit App`-Action, all defined by a JSON file on your server
- perfect to inform your users about discounts on one of your other apps or premium version
- pro tip: as some users don't always update to the latest app versions you should integrate this framework in an early phase if you plan to use it at a later time.

## How to integrate
### The easy way
- Call `RemoteNotifications.start(context, url, UpdatePolicy.NOW);` in your `onCreate()` or `onResume()` method

this will
- start an update of the notifications (use `UpdatePolicy.WEEKLY` in production environment to update once a week)
- show due notifications to the user
 
### If you want more control
```java
RemoteNotifications rn = new RemoteNotifications(context);
rn.updateNotifications(UpdatePolicy.NOW);
rn.showNotifications(); // if update still runs, event will be queued and carried out later

// -or, register a listener on update completion-
rn.updateNotifications(UpdatePolicy.NOW, finishListener);
```

## Download
- [Demo App](https://github.com/kaiwinter/android-remote-notifications/releases/download/v1.0.0/remotenotifications-example-1.0.0.apk)
- [android-remote-notifications_1.0.0.aar](https://github.com/kaiwinter/android-remote-notifications/releases/download/v1.0.0/android-remote-notifications_1.0.0.aar)
- [android-remote-notifications_1.0.0.jar](https://github.com/kaiwinter/android-remote-notifications/releases/download/v1.0.0/android-remote-notifications_1.0.0.jar)

## Screenshots
![Preview](http://i.imgur.com/Hp1aowm.png)
