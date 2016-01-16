### Description of the server of the VoiceConf application 
### Table of contents
* Version
* Requirements
* Installation
* WARNING!

### Requirements
 * python 2.7
 * py2exe 0.6.9
 * Theano lib
 * pyaudio

### Installation
#### Building

``` bash
python setup.py py2exe
```
#### Running

The resulting executable is for command-line use and it does not require any command-line arguments.

``` bash
cd dist
server
```

### WARNING!

Be sure to rewrite all IP adresses to local machine's IP adress
