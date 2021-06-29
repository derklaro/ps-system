# Private Server System ![Build Status](https://github.com/derklaro/ps-system/actions/workflows/build.yml/badge.svg)

This private server system is designed for very complex networks (maybe with their own cloud system). Because of the
**modular structure** the system is very flexible and has no need to load classes which are not required for the current
running environment.

## How to use

##### Lobby config

To use the system you have to upload the plugin core to your Lobby server's plugin folder. Then create a new folder
called `plugins/ps/modules` and upload the lobby as well as the cloud module to this folder.

The following cloud systems are supported:

- [CloudNET V2.1.17](https://github.com/CloudNetService/CloudNet) using the module ps-cloudnet2-legacy.
- [CloudNET V2.2+](https://github.com/CloudNetService/CloudNet/tree/development) using the module ps-cloudnet2.
- [CloudNET V3.4+](https://github.com/CloudNetService/CloudNet-v3) using the module ps-cloudnet3.

Then create a new

- Task (if you are using cloudnet 3)
- ServerGroup (if you are using cloudnet 2)

and restart the lobby server. Now there should be configuration file called `config.json` in the
`plugins/ps/modules/lobby` folder. Open this file and configure the options you see. Set `ps-group-name` to the just
created group name.

##### Private server group/task config

Upload the system and cloud module as mentioned above. Then put the runner into the modules folder as well.

**Now you are ready to go!**

## Help & Bug-Reports

If you need help or are experiencing any issues using the system, feel free to use the GitHub issue tracker to submit
[a new ticket](https://github.com/derklaro/ps-system/issues/new).

## Support our work

If you like the system and want to support our work you can **star** :star2: the project.

## Developers

##### Clone & build the project

```
git clone https://github.com/derklaro/ps-system.git
cd ps-system/
gradlew
```

##### Get the api

Maven repository:

```xml

<repository>
  <id>jitpack.io</id>
  <url>https://jitpack.io</url>
</repository>
```

Maven dependency:

```xml

<dependency>
  <groupId>com.github.derklaro</groupId>
  <artifactId>ps-api</artifactId>
  <version>1.1.0</version>
  <scope>provided</scope>
</dependency>
```

Gradle repository:

```groovy
maven {
  name 'jitpack.io'
  url 'https://jitpack.io'
}
```

Gradle dependency:

```groovy
compileOnly group: 'com.github.derklaro', name: 'ps-api', version: '1.1.0'
```

##### Implement a new cloud system

If you need an example how to implement a new cloud system, just take a
look [here](https://github.com/derklaro/ps-system/ps-cloudnet3/src/main/java/com/github/derklaro/privateservers/cloudnet/v3)

## Licence and copyright notice

The project is licenced under the terms of the
[MIT Licence](https://github.com/derklaro/ps-system/blob/master/license.txt). All files are Copyright (c) 2020 Pasqual
K. and all contributors.
