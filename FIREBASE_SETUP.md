# Firebase setup

The app is designed to work offline first. Firebase sync is optional until you connect your own Firebase project.

1. Create a Firebase project.
2. Add Android app with package:
   com.rsdurvasacooling.service
3. Download `google-services.json`.
4. Put it at:
   app/google-services.json
5. Enable Cloud Firestore.
6. For a production app, configure Firebase Authentication and Firestore security rules.

The Android project intentionally does NOT include a `google-services.json` because that file is unique to your Firebase project.
