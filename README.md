# Android-app
# Assignment2

This is a simple Android application developed in Kotlin that demonstrates the use of explicit and implicit intents to navigate between activities. The main activity displays your full name and student ID, along with two buttons that launch a second activity. The second activity lists five mobile software engineering challenges and includes a button to return to the main screen.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)

## Features

- **Two Activities:**  
  - **MainActivity:** Displays a hard-coded full name and student ID along with two buttons:
    - **Start Activity Explicitly:** Launches the second activity using an explicit intent.
    - **Start Activity Implicitly:** Launches the second activity using an implicit intent with a custom action.
  - **SecondActivity:** Displays a scrollable list of five mobile software engineering challenges and includes a button labeled "Main Activity" to return to the main screen.

- **Intent Handling:**  
  - Demonstrates the use of both explicit and implicit intents.
  - Provides proper handling of the `android:exported` attribute to comply with Android 12 (API level 31) requirements.

## Technologies Used

- **Language:** Kotlin
- **IDE:** Android Studio
- **Build System:** Gradle
- **Minimum SDK:** Android 5.0 (Lollipop) (adjustable as needed)
- **Target SDK:** Android 12 and higher

