## Description for the VoiceConf android application 
### Table of contents
* Requirements
* Installation

We recommend Android Studio for development (v1.4+).
You can open the Project by selecting the VoiceConf directory or the build.gradle file within it.

### Requirements
You will have to install these tools from the android SDK manager
- Google Play Services, rev 29 
- Google Repository, rev 24
- Android Support Repository, rev 25
- Android Support Library, rev 23.1.1
- Android SDK Tools 25.0.1
- Android SDK 23.0.2

### Installation
To build and run the project simply press the green Run button, with the "app" selected as configuration. (You will need an android mobile device or an emulator with Google Play Services)

#### Building

Building from the command-line:

``` bash
./gradlew assemble
```

Note: Cleaning the project is recommended before building but not necessary.

``` bash
./gradlew clean
```
#### Testing

Run Unit tests from the command-line:

``` bash
./gradlew test
```

### Running
After the build a debug apk is generated which you can find it in the ```/VoiceConf/app/build/outputs/apk/app-debug.apk```.

This apk can be installed on any android device by opening it.
