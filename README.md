# Olelo

<p align="center">
  <img src="https://img.shields.io/badge/Android-Java-green?style=flat&logo=android" alt="Android" />
  <img src="https://img.shields.io/badge/Firebase-Integrated-orange?style=flat&logo=firebase" alt="Firebase" />
  <img src="https://img.shields.io/badge/MLKit-Translation-blue?style=flat&logo=google" alt="MLKit" />
  <img src="https://img.shields.io/badge/License-MIT-green.svg" alt="License" />
</p>

## Overview

**Olelo** is a Native Android Dating and Social Matching application developed in Java. It allows users to meet people nearby, match with them using a Tinder-like intuitive swipe card interface, and chat in real-time. 

A unique feature of Olelo is its integration with Google ML Kit, offering real-time language detection and on-device translation to bridge language barriers between matched users!

## ✨ Features

- **Intuitive Matching**: Swipe left or right to connect with other users.
- **Real-Time Chat**: Integrated with Firebase for instantaneous messaging.
- **Language Detection & Translation**: Powered by Google ML Kit to easily converse with people globally.
- **Smart Replies**: ML Kit smart-reply integration for quicker and easier chat flow.
- **User Authentication**: Secure user login with Firebase Auth. Support for Email/Password and Facebook Login.
- **Profile & Bio Management**: Complete user profile details and photo uploading.
- **Location-Based Connections**: Discover users around your actual vicinity.

## 🛠️ Technology Stack

- **Language**: Java / Android SDK
- **Backend & Database**: Firebase (Auth, Realtime Database, Firestore, Storage)
- **Machine Learning**: Google ML Kit (Language ID, Translate, Smart Reply)
- **UI & Navigation**: Material Components, ConstraintLayout, Jetpack Navigation
- **Image Processing/Loading**: Glide, CircleImageView
- **Social Login**: Facebook Android SDK

## 🚀 Getting Started

### Prerequisites
- [Android Studio](https://developer.android.com/studio) (Bumblebee or newer recommended)
- A Firebase Project (with Auth, Database, and Storage configured)
- Facebook Developer App (for Facebook Login configuration)

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/YOUR_USERNAME/olelo.git
   cd olelo
   ```

2. **Configure Firebase:**
   - Create a project on [Firebase Console](https://console.firebase.google.com/).
   - Add an Android App with package name `com.pro.st`.
   - Download the `google-services.json` file.
   - Place `google-services.json` inside the `app/` directory (you can use `app/google-services.example.json` as a reference).

3. **Configure Facebook Login:**
   - Open `app/src/main/res/values/strings.xml`.
   - Replace the `facebook_app_id` placeholder with your actual Facebook App ID:
     ```xml
     <string name="facebook_app_id">YOUR_FACEBOOK_APP_ID_HERE</string>
     ```

4. **Build and Run:**
   - Open the project in Android Studio.
   - Sync Gradle.
   - Run the app on an Android Emulator or a physical device.

## 🤝 Contributing

Contributions, issues, and feature requests are welcome! 
Feel free to check our [Contributing Guidelines](CONTRIBUTING.md) and open [Issues](https://github.com/YOUR_USERNAME/olelo/issues).

## 📄 License

This project is [MIT](LICENSE) licensed.
