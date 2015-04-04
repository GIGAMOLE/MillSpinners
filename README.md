Mill Spinners
===================
The set of useful and beautiful spinners.

![enter image description here](https://lh3.googleusercontent.com/-bzGxaA_Oyk4/VR-miVWxGrI/AAAAAAAAAZE/tuTVk2dcRyQ/s0/Untitled-4.png "logo_title.png")

Currently the library is only one spinner - [**MultiArcSpinner**](#multi_arc_spinner). Later will appear new. So suggest you view it.

Download
------------

You can download a jar from GitHub's [releases page](https://github.com/GIGAMOLE/Mill-Spinners/releases).

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

    compile 'com.github.gigamole.millspinners:library:1.0.0'

Or Maven:

    <dependency>
	    <groupId>com.github.gigamole.millspinners</groupId>
	    <artifactId>library</artifactId>
	    <version>1.0.0</version>
	    <type>aar</type>
    </dependency>

Android SDK Version
=========
Mill Spinners requires a minimum sdk version of 10.

<a name="multi_arc_spinner"></a>MultiArcSpinner
------------
#### Sample
![enter image description here](https://lh3.googleusercontent.com/-xmtjZYfrZ3g/VR-7WR2tQ1I/AAAAAAAAAZs/YzLSSYPukYE/s0/multi_arc_spinner.gif "multi_arc_spinner.gif")

U can check the sample app [here](https://github.com/GIGAMOLE/Mill-Spinners/tree/master/app).

####Using
You can set such parameters as:

 - speed
 -  colors 
 - reversibility 
 - color
 -  autostart 
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

Getting Help
======

To report a specific problem or feature request, [open a new issue on Github](https://github.com/GIGAMOLE/Mill-Spinners/issues/new). 

License
======
Apache 2.0. See LICENSE file for details.


Author
=======
Basil Miller - @gigamole