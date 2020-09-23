# Get analytics for App Actions using Google Analytics for Firebase
## TL;DR
This sample demonstrates instrumenting the [To-do list App Actions Android App sample](https://github.com/actions-on-google/appactions-common-biis-kotlin) to log events using the Google Analytics for Firebase SDK. You can then use Firebase Console to visualize traffic patterns and obtain insights on app usage and user engagement.

## Why Google Analytics for Firebase
Google Analytics for Firebase is a free app measurement solution providing insights on user engagement for websites and Android apps (user actions, system events, errors and more).  Google Analytics for Firebase can provide insights into different user behaviors with App Actions, notably, the most popular built-in intents (BIIs) used in your app. Further, Google Analytics for Firebase data can be [exported to downstream systems like Big Query](https://firebase.google.com/docs/projects/bigquery-export) for further analysis.

## Requirements
Before using the sample, you must [add Firebase to your App](https://firebase.google.com/docs/android/setup):

* Add a Firebase configuration file
* Enable Firebase products in your root-level build.gradle file
* Apply the Google Service Gradle plugin
* Add the Google Analytics for Firebase SDK 

Once these steps are completed your app will be registered and ready to use Google Analytics for Firebase.

## How to use this sample

---
NOTE

This sample app has already been set up with Google Analytics for Firebase. To set up analytics for your own app, see the "Add Analytics to your app" section below.

---

Clone or download this project to your preferred location. Then, import and update it to suit your environment:

1. Switch to `get-analytics-with-firebase` branch.

    ```bash
    $ git checkout get-analytics-with-firebase
    ```

1. In Android Studio, select “Open an existing Android Studio project” from the initial screen or **File > Open**.

    Change `applicationId` in [app/build.gradle](https://github.com/actions-on-google/appactions-common-biis-kotlin/tree/get-analytics-with-firebase/app/build.gradle) to the applicationId of a draft or published app in Google Play Console.

    ```gradle
    android {
    defaultConfig {
        // This ID uniquely identifies your app on device and in Google Play
        applicationId "com.example.myapp"
    }
    ```
1. In Android Studio, find the root directory of the sample.

1. Select the build.gradle file.

1. Follow the instructions in the IDE.

Next, test the App Actions integration:

1. Build and run the sample on your physical test device (Run "app").

1. Open the App Actions test tool **Tools > App Actions > App Actions Test Tool**.

1. Define an invocation name for App Actions (like "my test app"). This name is only used for testing so it can be different from what is deployed to production.

1. Click **Create Preview**. Once your preview is created, the test tool window will update to reflect BIIs found in the actions.xml file.

    <img alt="Test tool after preview creation" width="40%" height="40%" src="media/app-actions-test-tool.png">

    After creating a preview you can use voice or written commands directly with Assistant on your test device.

    If you run into any issues, check out the [troubleshooting issues](https://developers.google.com/assistant/app/troubleshoot) in our developer documentation.

1. To view aggregated statistics about your events, navigate to the [Google Analytics for Firebase dashboards](https://console.firebase.google.com/) under **Analytics > Streamview**. These dashboards update periodically throughout the day. For immediate testing, use the logcat output as described in the previous section.

    You can access events data from the [Events](https://console.firebase.google.com/project/_/analytics/events) dashboard in the Firebase console. This dashboard shows the event reports that are automatically created for each distinct type of event logged by your app.

    <img alt="Firebase Streamview showing App Actions traffic" width="80%" height="80%" src="media/track-app-actions-fb-analytics.gif">

1. View events in the Android Studio debug log.

    You may enable verbose logging of events by the SDK to verify events are being logged properly. This includes both automatic and manually logged events.

    To enable verbose logging, enter this sequence of adb commands from your terminal:

    ```shell
    $ adb shell setprop log.tag.FA VERBOSE
    $ adb shell setprop log.tag.FA-SVC VERBOSE
    $ adb logcat -v time -s FA FA-SVC
    ```

    You should now be able to view events in the Android Studio logcat and verify events are being sent.

    ---
    NOTE

    You can also verify Firebase logs with minimal delay in the debug view report by enabling [debug mode](https://firebase.google.com/docs/analytics/debugview#enabling_debug_mode).

    ---

## Add Analytics to your app
This section describes how to enable Google Analytics for Firebase in the context of App Actions. Refer to the [Firebase Analytics guide](https://firebase.google.com/docs/analytics/get-started?platform=android) for more information.

1. Add the dependency for the Google Analytics Android library to your module (app-level) Gradle file (usually app/build.gradle):

    ```gradle
    implementation 'com.google.firebase:firebase-analytics-ktx:17.5.0'
    ```

1. Declare the `com.google.firebase.analytics.FirebaseAnalytics` object at the top of [TaskActivity.kt activity](https://github.com/actions-on-google/appactions-common-biis-kotlin/tree/get-analytics-with-firebase/app/src/main/java/com/example/android/architecture/blueprints/todoapp/tasks/TasksActivity.kt):

    ```gradle
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    ```

1. Initialize FirebaseAnalytics in the `onCreate()` method:

    ```kotlin
    // Obtain the FirebaseAnalytics instance.
    firebaseAnalytics = Firebase.analytics
    ```

1. Log events from TasksActivity class. Refer to [event logging events](https://firebase.google.com/docs/analytics/get-started?platform=android#start_logging_events) documentation for more information:

    ```kotlin
    override fun onCreate(savedInstanceState: Bundle?) {
      logEventToFirebase(intent.data);
    }


    /**
    * This method will record the event to Google Analytics for Firebase
    */
    private fun logEventToFirebase(data: Uri?) {
      val utmCampaign: String = data?.getQueryParameter("utm_campaign") ?: N_A
      val path: String = data?.path ?: N_A
      mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN) {
        param(FirebaseAnalytics.Param.CAMPAIGN, utmCampaign)
        param("app_action_path", path) // logging a custom parameter
      }
    }
    ```

    ---
    NOTE

    You may include custom params, for instance `app_action_path` for logging other types of data.

    ---

1. actions.xml needs additional information to indicate the source of the request. Add a url query parameter with the key: `utm_campaign` and value: `appactions`:

    ```xml
    <fulfillment urlTemplate="https://todo.androidappactions.com/all-tasks?utm_campaign=appactions"/>
    ```

## Contribution guidelines
If you want to contribute to this project, please review the [contribution guidelines](https://github.com/actions-on-google/appactions-common-biis-kotlin/tree/get-analytics-with-firebase/CONTRIBUTING.md).

We use [GitHub issues](https://github.com/actions-on-google/appactions-common-biis-kotlin/issues) for tracking requests and bugs. For technical questions, please use [Stack Overflow](https://stackoverflow.com/questions/tagged/app-actions).

Report [general issues with App Actions features](https://issuetracker.google.com/issues/new?component=617864&template=1257475) or [make suggestions for additional built-in intents](https://issuetracker.google.com/issues/new?component=617864&template=1261453) through our public issue tracker.

## References
* [App Actions Overview](https://developers.google.com/assistant/app/overview)
* [Built-in Intents reference](https://developers.google.com/assistant/app/reference/built-in-intents/bii-index)
* [App Actions test tool](https://developers.google.com/assistant/app/test-tool)
* [Codelab](https://developers.google.com/assistant/app/codelabs)
* [Firebase Log events](https://firebase.google.com/docs/analytics/events?platform=android)
* [Other samples](https://developers.google.com/assistant/app/samples)


