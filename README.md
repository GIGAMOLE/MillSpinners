Mill Spinners
=============

<b>\[DEPRECATED\]</b> The set of useful and beautiful spinners.

[![DevLight](https://lh4.googleusercontent.com/-9btnRFp_eVo/V5cfwZsBpMI/AAAAAAAAC4E/s4NGoezKhpAVdVofAoez1QWpzK5Na8_cQCL0B/w147-h20-no/devlight-badge.png)](http://devlight.com.ua)

[![Android](https://img.shields.io/badge/platform-android-brightgreen.svg?style=flat&label=Platform)](https://github.com/DevLight-Mobile-Agency)
[![Download](https://api.bintray.com/packages/gigamole/maven/millspinners/images/download.svg) ](https://bintray.com/gigamole/maven/millspinners/_latestVersion)
[![Crates.io](https://img.shields.io/crates/l/rustc-serialize.svg?maxAge=2592000&label=License)](https://github.com/DevLight-Mobile-Agency/MillSpinners/blob/master/LICENSE.txt)

![](https://lh3.googleusercontent.com/-bzGxaA_Oyk4/VR-miVWxGrI/AAAAAAAAAZE/tuTVk2dcRyQ/s0/Untitled-4.png "logo_title.png")

The library contains following spinners:

 - [**MultiArcSpinner**](#multi_arc_spinner).
 - [**CirclesWaveSpinner**](#cws).
 - [**CubeSpinner**](#cs).
 - [**DyingLightSpinner**](#dls).
 - [**FigureSpinner**](#fs).

You can check the sample app [here](https://github.com/DevLight-Mobile-Agency/MillSpinners/tree/master/app).

Download
------------

You can download a `.jar` from GitHub's [releases page](https://github.com/DevLight-Mobile-Agency/MillSpinners/releases).

Or use Gradle jCenter:

```groovy
dependencies {
    repositories {
        mavenCentral()
        maven {
            url  'http://dl.bintray.com/gigamole/maven/'
        }
    }
    compile 'com.github.gigamole.millspinners:library:+'
}
```

Or Gradle Maven Central:

```groovy
compile 'com.github.gigamole.millspinners:library:1.0.1'
```

Or Maven:

```groovy
<dependency>
    <groupId>com.github.gigamole.millspinners</groupId>
    <artifactId>library</artifactId>
    <version>1.0.1</version>
    <type>aar</type>
</dependency>
```

Android SDK Version
=========
`MillSpinners` requires a minimum SDK version of 10.

<a name="multi_arc_spinner"></a>MultiArcSpinner
------------
![](https://lh3.googleusercontent.com/-xmtjZYfrZ3g/VR-7WR2tQ1I/AAAAAAAAAZs/YzLSSYPukYE/s0/multi_arc_spinner.gif "multi_arc_spinner.gif")

You can set such parameters as:

 - speed
 - colors
 - reversibility
 - color
 - autostart
 - roundness
 - slowdown

Also given the opportunity to start and finish the animation by hand.
These settings can be made as programmatically and through `XML`.

It includes a set of three color schemes by default.

Check out an code example:

```xml
<com.gigamole.millspinners.lib.MultiArcSpinner
    android:id="@+id/multiArcSpinner"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#ff242624"
    arc_spinner:rounded="true"
    arc_spinner:autostart="false"
    arc_spinner:speed="5000"
    arc_spinner:slowdown="false"/>
```

<a name="cws"></a>CirclesWaveSpinner
------------
![](https://lh5.googleusercontent.com/-PaQXf0zlY5E/VU00AZvffaI/AAAAAAAAAa4/VH36UhvzXl4/w153-h165-no/cws.gif)

You can set such parameters as:

 - speed
 - colors

<a name="cs"></a>CubeSpinner
------------
![](https://lh4.googleusercontent.com/-ysG5WmZKG0w/VU00Ao5_MDI/AAAAAAAAAa0/bYf3v90XpcA/w144-h146-no/cs.gif)

You can set such parameters as:

- speed
- colors

<a name="dls"></a>DyingLightSpinner
------------
![](https://lh5.googleusercontent.com/-X61M2jmXkTs/VU00AdyLk9I/AAAAAAAAAbI/4BKr42uJ4Vc/w170-h261-no/dls.gif)

You can set such parameters as:

- speed
- color
- type (`CHAIN` | `BRACKET`)

<a name="fs"></a>FigureSpinner
------------
![](https://lh5.googleusercontent.com/-b8PfdN9PoLw/VU00BPNG2cI/AAAAAAAAAbE/AXDZuOMEb5k/w175-h262-no/fs.gif)

You can set such parameters as:

- speed
- colors
- sides
- roundness
- rotating

License
======
Apache 2.0 and MIT. See [LICENSE](https://github.com/DevLight-Mobile-Agency/MillSpinners/blob/master/LICENSE.txt) file for details.

Author
=======

Made in [DevLight Mobile Agency](https://github.com/DevLight-Mobile-Agency)

Created by [Basil Miller](https://github.com/GIGAMOLE) - [@gigamole](mailto:gigamole53@gmail.com)