# StoryMaker Gradle Conversion

The following describes the process required to modify StoryMaker to build with gradle.

1. Add `<application />` tag to HoloEverywhere and SlidingMenu Manifests (Required by the Android gradle plugin)
2. Fix ABS / SlidingMenu minSdkVersion mismatch. The Android Gradle Plugin doesn't allow an android library project
   to have a greater minSdkVersion than it's host project / library project.

    Gradle reports:

        /PATH/TO/build/exploded-bundles/StorymakerExternalHoloEverywhereContribActionBarSherlockLibraryUnspecified.aar/AndroidManifest.xml:3] Main manifest has <uses-sdk android:minSdkVersion='5'> but library uses minSdkVersion='7'

3. Remove duplicate menu_strings entry in the arabic strings.xml:

    Gradle reports:

	    /PATH/TO/storymaker/app/res/values-ar/strings.xml: Error: Found item String/menu_settings more than one time

5. Exclude duplicate files to avoid packaging conflicts. Multiple jars define these files, so they
   must be excluded from the final storymaker .apk packaging process.

    Add to app/build.gradle:

        packagingOptions {
            exclude 'META-INF/LICENSE.txt'
            exclude 'META-INF/NOTICE.txt'
            exclude 'META-INF/LICENSE'
            exclude 'META-INF/NOTICE'
        }
        
	The Android gradle plugin generates html lint reports when building. You can check them out after building in `/path/to/project/build/lint-results.html`

7. Excuse linter errors for Storymaker, HoloEverywhere:lib, CardsUiLib, ActionBarSherlock

	Add to app/build.gradle:

        // Temporarily ignore linter errors
        lintOptions {
            abortOnError false
        }

## A sermon on Gradle

And thus I will try to convince you of the divine and simple beauty of this new build system.

### Gradle is Simple

By adding one `settings.gradle` files to our top level directory, and a `build.gradle`
to each Android project directory we define the entire project relationship.
Forget importing projects in Eclipse and manually adding them as dependencies!
<sup><sup>Hell, Forget Eclipse</sup></sup>

#### settings.gradle

This file describes the location of every project to build when gradle is invoked
from this directory. We typically only need one of these files at the root of our project.

Note gradle uses a colon instead of a slash as a project path separators.

#### build.gradle

This file completely describes the build for a given project. The `build.gradle` in
the root directory of our project is optional, but allows us to inject common items
that will be inherited by all `build.gradle` files identified in our `settings.gradle`.


### Gradle is Loving

#### The Gradle Wrapper

It is common practice for gradle-built projects to include a `gradlew` binary (`gradlew.bat` for Windows)
and `gradle/` subdirectory in their root. This allows the project maintainer to specify in
`gradle/wrapper/gradle-wrapper.properties` the precise version of gradle used to build the project.

Thus contributors don't need to have gradle installed to get to work! By invoking all gradle commands
with `./gradlew`, a user's machine will automatically acquire the prescribed version of gradle before
running the specified command.

The gradle wrapper is automatically created and setup when creating a project with Android Studio
(or by exporting a project's gradle files in Eclipse).

Can I get an Amen?

#### Dependency Management

Gradle makes it divinely simple to declare project dependencies.

##### Maven Repositories

Gradle allows us to import dependencies from the Maven Central Repsitory in one `build.gradle` line.
Forget downloading jars and bloating your git repository. [gradleplease](http://gradleplease.appspot.com/)
is a great convenience for searching dependencies on Maven Central.

For example, to add gson to our project as a dependency:

    // build.gradle
    dependencies {
        compile 'com.google.code.gson:gson:2.2.4'
    }

Note that anyone can host a maven compatible repository. People do it on Github, or their own servers.
Fetching from a third party repository requires adding a `repository` entry to our `build.gradle` just
as is done by Android's project tools by default for mavenCentral.

    # build.gradle
    repositories {
        // Protokit's custom HoloEverywhere repo
        maven { url "http://192.241.191.41/repo" }

        // A shortcut for the Maven Central Repository
        mavenCentral()
    }

##### Local Android Library Projects

If your dependency needs to be a local Android library project, you craft a `build.gradle` for it,
add it's project path to your root `settings.gradle`, and declare the project as a dependency in
the `build.gradle` of the depending project.


    // depending project's build.gradle
    dependencies {
        // Indicate that gradle will find a build.gradle file
        // at ./PROJECT-ROOT/external/android-ffmpeg-java/build.gradle
        compile project(':external:android-ffmpeg-java')
    }

##### Local Jars.

    // build.gradle
    dependencies {
        // Pull in every .jar from the libs folder
        compile fileTree(dir: 'libs', include: '*.jar')

        // Import a single .jar
        compile files('libs/apache-mime4j-0.6.jar')
    }
    
    
## Building with gradle

Make sure you have the following installed from the Android SDK Manager

+ Android Support Repository
+ Android SDK Platforms 14, 18, 19
+ Android SDK Build-tools 19.0.3
+ Android SDK Tools 22.6.2

First clone the repository and checkout submodules

	$ git clone -b gradle2 --recursive https://github.com/OnlyInAmerica/storymaker.git
	
Then build a debug-signed .apk

	$ cd /path/to/storymaker/
	$ ./gradlew assembleDebug
	
The apk will reside at `./app/build/apk/`.

## Running Tests with gradle

Install the Android SDK modules specified in **Building with gradle**.

To run the included tests on all devices available to adb:

	$ ./gradlew connectedAndroidTest
	