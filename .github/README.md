# Private Server System [![Discord](https://img.shields.io/discord/499666347337449472.svg?color=7289DA&label=discord)](https://discord.gg/uskXdVZ) [![Build Status](https://travis-ci.com/derklaro/ps-system.svg?branch=master)](https://travis-ci.com/derklaro/ps-system)

This private server system is designed for very complex networks (maybe with their own cloud system).
Because of the **modular structure** the system is very flexible and has no need to load classes which
are not required for the current running environment.

## How to use
##### Lobby config
To use the system you have to upload the plugin core to your Lobby server's plugin folder. Then create
a new folder called `plugins/ps/modules` and upload the lobby as well as the cloud module to this folder.

The following cloud systems are supported: 
 - [ReformCloud V2.3+](https://github.com/derklaro/reformcloud2) with the module ps-reformcloud2.
 - [CloudNET V2.1.17](https://github.com/CloudNetService/CloudNet) with the module ps-cloudnet2-legacy.
 - [CloudNET V2.2+](https://github.com/CloudNetService/CloudNet) with the module ps-cloudnet2.
 - [CloudNET V3.3+](https://github.com/CloudNetService/CloudNet-v3) with the module ps-cloudnet3.
 
Then create a new 
 - ProcessGroup (if you are using reformcloud 2)
 - ServerGroup (if you are using cloudnet 2)
 - Task (if you are using cloudnet 3)

and restart the lobby server. Now there should be configuration file called `config.json` in the
`plugins/ps/modules/lobby` folder. Open this file and configure the options you see. Set `ps-group-name` to
the just created group name.

##### Private server group/task config

Upload the system, and the cloud module as mentioned above. Then put in the module directory the 
ps-runner module. 

**Now you are ready to go!**

## Support our work
If you like the pss and want to support our work you can **star** :star2: the project, leave a (positive)
review on [SpigotMC]() or join our [Discord](https://discord.gg/uskXdVZ).

## Developers
##### Clone & build the project
```
git clone https://github.com/derklaro/ps-system.git
cd ps-system/
mvn clean package
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
    <artifactId>ps-system</artifactId>
    <version>1.1.0</version>
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
compile group: 'com.github.derklaro', name: 'ps-system', version: '1.1.0'
```

##### Implement a new cloud system

If you need an example how to implement a new cloud system, just take a look [here](https://github.com/derklaro/ps-system/ps-cloudnet3/src/main/java/com/github/derklaro/privateservers/cloudnet/v3)

## Licence and copyright notice
The project is licenced under the [MIT Licence](https://github.com/derklaro/ps-system/blob/master/LICENSE).
All files are Copyright (c) 2020 Pasqual K. and all contributors.