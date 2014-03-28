Story Maker - Make your story great.
=====

## [StoryMaker.cc](http://storymaker.cc/)

Download [StoryMaker - Video, Audio & Photo](https://play.google.com/store/apps/details?id=info.guardianproject.mrapp) in the the Google Play Store. 

## Setting up Development Environment

**Prerequisites:**
. 
* [Android SDK](https://developer.android.com/sdk/installing/index.html)
* Working [Android NDK](https://developer.android.com/tools/sdk/ndk/index.html) toolchain
* Unix build toolchain.  Linux and OS X are well tested.

Follow these steps to setup your dev environment:

1. Clone the git repo, make sure you fork first if you intend to commit

1. Init and update git submodules

    git submodule update --init --recursive

1. Ensure `NDK_BASE` env variable is set to the location of your NDK, example:

    export NDK_BASE=/path/to/android-ndk

1. Build android-ffmpeg

    cd external/android-ffmpeg-java/external/android-ffmpeg/
    ./configure_make_everything.sh

1. run script to update all libraries android support library.  from command line run in the top level of the repo:

        $ scripts/copy-support-libs.sh

1. Import dependancies into Eclipse

    Using the File->Import->Android->"Existing Android Code Into Workspace" option, import these projects in this order:

        external/HoloEverywhere/contrib/ActionBarSherlock/library/
        external/HoloEverywhere/library/
        external/OnionKit/
        external/android-ffmpeg-java/
        external/WordpressJavaAndroid/
        external/cardsui-for-android/CardsUILib
        external/SlidingMenu/library
        external/Android-ViewPagerIndicator/library
        external/RangeSeekBar/library

1. Import the StoryMaker project from the app/ subfolder

1. (optional) Build from the command line

        $ cd app/
        $ ./setup-ant.sh
        $ ant clean debug


## Running tests with [Spoon](https://github.com/square/spoon)

Spoon automatically runs tests against all devices available to adb and generates pretty HTML reports in ./spoon-output/.

To invoke spoon:

	java -jar ./spoon/spoon-runner-1.1.1-jar-with-dependencies.jar \ 
	--class-name info.guardianproject.mrapp.test.TestCreateStory \  # fully qualified test class name. Ignore to run all tests
	--apk ./path/to/StoryMaker.apk \
	--test-apk ./bin/StoryMakerTest.apk
	Note: IPTest in the BouncyCastle library is detected by Spoon if you don't specify a test --class-name, and Spoon is unable to run that test.
	
With the storymaker tests combined into the main application, you can copy the below command verbatim to run the `TestCreateStory` test after building both .apks with `./gradlew assemble`

	java -jar ./spoon/spoon-runner-1.1.1-jar-with-dependencies.jar --class-name info.guardianproject.mrapp.test.TestCreateStory --apk ./app/build/apk/app-debug-unaligned.apk --test-apk ./app/build/apk/app-debug-test-unaligned.apk
