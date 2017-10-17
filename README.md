# Unofficial GPlayMusic API in JAVA

This library poses as a client for the [GooglePlay](https://play.google.com/music/) app.
It can search for songs/artists/albums, modify and create playlists and stations and even
download tracks. For most activities a valid subscription to _GooglePlay All Access_ is needed.

**This project is neither supported nor endorsed by Google.**

**This API should not be considered stable until version 1.0.0**


Installation
---------------

#### Maven:
To use this library, simply add the following lines to your pom.xml: **NOT YET SETUP**
```xml
<dependency>
    <groupId>com.github.felixgail</groupId>
    <artifactId>gplaymusic</artifactId>
    <version>0.1.0</version>
</dependency>
```

Otherwise download the latest version from the [releases page](https://github.com/FelixGail/gplaymusic/releases).

Getting Started
----------------
##### Collecting your credentials:
To use the api you will need to provide the following information:
- `USERNAME`: The email or username to your google account.
- `PASSWORD`: The password to your google account or, if you are using 2-factor-authorization,
an app password created [here](https://support.google.com/accounts/answer/185833).
- `ANDROID_ID`: The IMEI of an android device that had GooglePlayMusic installed.

Provided information will never be saved by this project.
##### Fetching an authorization token:
```java
AuthToken authToken = TokenProvider.provideToken(USERNAME,
                                      PASSWORD, ANDROID_ID);
```

##### Creating a new API instance:

```java
GPlayMusic api = new GPlayMusic.Builder()
                  .setAuthToken(authToken)
                  .build();
```

And you are ready to go. <br>
A full documentation should come sometime in the future.
For now use the Javadoc to help yourself. It can be found
[here](https://FelixGail.github.io/CircleCIArtifactProvider/index.html?vcs-type=github&user=FelixGail&project=gplaymusic&build=latest&token=ad2a969e7620106dc21efae732b4f3916744554e&branch=master&filter=successful&path=root/app/target/site/apidocs/index.html).

Feel free to create an [issue](https://github.com/FelixGail/gplaymusic/issues) if you have
questions or problems.
