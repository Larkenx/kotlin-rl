# Creating a Roguelike in Kotlin, KTerminal, and KDX (LibGDX bindings for Kotlin)

## Part 1

### Intro
This tutorial series will be about how to create a roguelike using the Kotlin programming language.
For those who aren't familiar with Kotlin, Kotlin a language that compiles down
to Java bytecode. It can import existing Java libraries like LibGDX and Squidlib.
Some of the reasons you might want to use Kotlin over Java is that it has inferred
types and functional programming features that allow for rapid development.

### Setting up your project
For this tutorial, I will be using the IntelliJ Community Edition IDE.
To get started, you'll want to download the (LibGDX project starter tool)[http://libgdx.com]
and create a new Kotlin project with the following options selected:

<!-- Show screenshot of libgdx set up options -->

Once you set up your project, you'll want to import the project into Intellij by opening Intellij,
and selecting import project as a Gradle project. Gradle is used to manage the dependencies for your project.
You can add new libraries from git repositories by opening your `build.gradle` 