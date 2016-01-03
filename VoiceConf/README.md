## Description for the VoiceConf android application 
### Table of contents
* Requirements
* Installation

We recomend Android Studio for development (v1.4+).
You can open the Project by selecting the VoiceConf directory or the contained build.gradle file.

### Requirements
You will have to install these tools from the android SDK manager
- Google Play Services, rev 29 
- Google Repository, rev 24
- Android Support Repository, rev 25
- Android Support Library, rev 23.1.1
- Android SDK Tools 25.0.1
- Android SDK 23.0.2

### Installation
To build and run the project simply press the green Run button with the app selected as configuration. (You will need an android mobie device or an emulator with google play services)

#### Building

Building from the command line:

``` bash
./gradlew assemble
```

Note: Cleaning the project is recomended before building but not necesary.

``` bash
./gradlew clean
```
### Running
After the build a debug apk is generated yuo can find it in the ```/VoiceConf/app/build/outputs/apk/app-debug.apk```

This apk can be installed on any android device by opening it.
