# Documentation
A brief documentation of the project such as the minimum sdk, etc.

## Prerequisites

Android version: 12 (S) <br />
Android Studio version: [latest](https://developer.android.com/studio) <br />
Kotlin version: [latest](https://developer.android.com/kotlin) <br />
Firebase SDK version: [latest](https://firebase.google.com/docs/android/setup) <br />
Minimum API SDK: 33 <br />

**Note: some of the button may not function correctly.**

## Contributors

1. [Andrew Virya Victorio](https://github.com/AlphaByte-RedTeam)
2. [Feri Andika](https://github.com/FeriAndika-Hub)
3. [Janice Claresta Lingga](https://github.com/janeclrst)
4. [Eric Wiyanto](https://github.com/wiyantoeric)

## Prototype
![prototype](https://i.imgur.com/EZmnQTG.png)

## Getting Started

To use the apps, please follow the instruction below:

1. Open terminal and do the following command

   ```bash
   git clone https://github.com/AlphaByte-RedTeam/bangun-ruang
   cd bangun-ruang
   ```

2. Open the project in Android Studio

3. Run the app using your emulator device

## Sreenshots

### Splash Screen
![splash screen](https://i.imgur.com/Hx8gWfs.png)

### Onboarding Screen
![onboarding screen](https://i.imgur.com/okV2rRl.png)

### Register Activity
`RegisterActivity` will have a several components, such as:
1. EditText
   - Full Name
   - Email
   - Password
2. Button
   - Register (password based)
   - Register with Google
3. TextView
The footer TextView can be click on the `Login!` text. Just click it, and you'll be directed to `LoginActivity` </br>
![register](https://i.imgur.com/4B9g3A6.png)

### Login Activity
`LoginActivity` will have a several components, such as:
1. EditText
   - Email
   - Password
2. Button
   - Login (password based)
   - Login with Google
3. TextView
The footer TextView can be click on the `Register!` text. Just click it, and you'll be directed to `RegisterActivity` </br>
![login](https://i.imgur.com/ZfsEnoi.png)

### Invalid State
Invalid state will prompt a Snackbar that shows the input is not valid </br>

![invalid register](https://i.imgur.com/zyLoGLW.png)

![invalid login](https://i.imgur.com/r7Gc5ya.png)

### Fill State
Fill state is a state where the `EditText` column have a real value </br>
![fill register](https://i.imgur.com/ngAdcBi.png)

![fill login](https://i.imgur.com/ngAdcBi.png)

## Home Activity
![home](https://i.imgur.com/VhNzwyC.png)

## Google One-Tap Sign In
![google](https://i.imgur.com/9U4Jp0w.png)

## Firebase Auth Database
A proof of concept that we did implement the firebase auth to register and login </br>
![fb db](https://i.imgur.com/hytr4qP.png)
