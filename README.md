Mill Spinners
===================
The set of useful and beautiful spinners.

![enter image description here](https://lh3.googleusercontent.com/-bzGxaA_Oyk4/VR-miVWxGrI/AAAAAAAAAZE/tuTVk2dcRyQ/s0/Untitled-4.png "logo_title.png")

Currently the library contains following spinners:

 - [**MultiArcSpinner**](#multi_arc_spinner).
 - [**CirclesWaveSpinner**](#cws).
 - [**CubeSpinner**](#cs).
 - [**DyingLightSpinner**](#dls).
 - [**FigureSpinner**](#fs).
 
Wait for new.

U can check the sample app [here](https://github.com/GIGAMOLE/Mill-Spinners/tree/master/app).

Download
------------

You can download a .jar from GitHub's [releases page](https://github.com/GIGAMOLE/Mill-Spinners/releases).

Or use Gradle jCenter:

    dependencies {
        repositories {
            mavenCentral()
            maven {
                url  'http://dl.bintray.com/gigamole/maven/'
            }
        }
        compile 'com.github.gigamole.millspinners:library:+'
    }

Or Gradle Maven Central:

    compile 'com.github.gigamole.millspinners:library:1.0.1'

Or Maven:

    <dependency>
	    <groupId>com.github.gigamole.millspinners</groupId>
	    <artifactId>library</artifactId>
	    <version>1.0.1</version>
	    <type>aar</type>
    </dependency>

Android SDK Version
=========
Mill Spinners requires a minimum sdk version of 10.

<a name="multi_arc_spinner"></a>MultiArcSpinner
------------
![enter image description here](https://lh3.googleusercontent.com/-xmtjZYfrZ3g/VR-7WR2tQ1I/AAAAAAAAAZs/YzLSSYPukYE/s0/multi_arc_spinner.gif "multi_arc_spinner.gif")

You can set such parameters as:

 - speed
 - colors
 - reversibility 
 - color
 - autostart
 - roundness 
 - slowdown

Also given the opportunity to start and finish the animation by hand.
These settings can be made as programmatically and through XML.
It includes a set of three color schemes by default.

Check out an code example:

    <com.gigamole.millspinners.lib.MultiArcSpinner
        android:id="@+id/multiArcSpinner"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="#ff242624"
        arc_spinner:rounded="true"
        arc_spinner:autostart="false"
        arc_spinner:speed="5000"
        arc_spinner:slowdown="false"/>
        
<a name="cws"></a>CirclesWaveSpinner
------------
![enter image description here](https://lh5.googleusercontent.com/-PaQXf0zlY5E/VU00AZvffaI/AAAAAAAAAa4/VH36UhvzXl4/w153-h165-no/cws.gif)

You can set such parameters as:

 - speed
 - colors
 
<a name="cs"></a>CubeSpinner
------------
![enter image description here](https://lh4.googleusercontent.com/-ysG5WmZKG0w/VU00Ao5_MDI/AAAAAAAAAa0/bYf3v90XpcA/w144-h146-no/cs.gif)

You can set such parameters as:

- speed
- colors

<a name="dls"></a>DyingLightSpinner
------------
![enter image description here](https://lh5.googleusercontent.com/-X61M2jmXkTs/VU00AdyLk9I/AAAAAAAAAbI/4BKr42uJ4Vc/w170-h261-no/dls.gif)

You can set such parameters as:

- speed
- color
- type (CHAIN | BRACKET)

<a name="fs"></a>FigureSpinner
------------
![enter image description here](https://lh5.googleusercontent.com/-b8PfdN9PoLw/VU00BPNG2cI/AAAAAAAAAbE/AXDZuOMEb5k/w175-h262-no/fs.gif)

You can set such parameters as:

- speed
- colors
- sides
- roundness
- rotating

Getting Help
======

To report a specific problem or feature request, [open a new issue on Github](https://github.com/GIGAMOLE/Mill-Spinners/issues/new). 

License
======
Apache 2.0. See LICENSE file for details.


Author
=======
Basil Miller - @gigamole