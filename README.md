## Overview

Climber is an experiment in libgdx development. The focus is on a simple controller-based desktop game with procedural platforming.

[![Video](http://img.youtube.com/vi/b8bzys1QbOM/0.jpg)](https://www.youtube.com/watch?v=b8bzys1QbOM)

## Setup and running
* [Setup your development environment](https://github.com/libgdx/libgdx/wiki/Gradle-and-Intellij-IDEA)
* Clone the repository or download and extract the ZIP file
* Import the project into your preferred development environment and run

## Tools used

* IDE: [IntelliJ](https://www.jetbrains.com/idea/)
* Graphics: [PyxelEdit](http://pyxeledit.com/)
* Sound effect generation: [bfxr](http://www.bfxr.net/)
* Sound effect editing: [Audacity](http://sourceforge.net/projects/audacity/)
* Music: [Reaper](http://www.reaper.fm/)

## References

Here are some links that I found useful. Most features are implemented from scratch for the purpose of learning vs. using higher level libraries.

#### Physics

* [Time step](http://gafferongames.com/game-physics/fix-your-timestep/)
* [More time stepping](http://lolengine.net/blog/2015/05/03/damping-with-delta-time)

#### Collision handling

* [Collision fundamentals](http://noonat.github.io/intersect/)
* [Minkowski Difference](http://hamaluik.com/posts/simple-aabb-collision-using-the-minkowski-difference/)
* [More Minkowski](http://stackoverflow.com/questions/13503811/how-do-i-calculate-the-minkowski-difference-between-two-aabbs-with-no-vector-ma)
* [Swept collision detection](http://hamaluik.com/posts/swept-aabb-collision-detection-using-the-minkowski-difference/)
* [Ghost collisions with Box2d](http://gamedev.stackexchange.com/questions/45808/what-could-cause-a-sudden-stop-in-box2d)
* [Ghost vertices with Box2d](http://www.iforce2d.net/b2dtut/ghost-vertices)