# ServerSynchronizer 

[![Build Status](https://travis-ci.org/wwken/ServerSynchronizer.svg?branch=master)](https://travis-ci.org/wwken/ServerSynchronizer.svg)

### This software synchronizes all the files from a local server to all remote server(s) through ssh protocol

#### Prerequisites

To build the application you need [sbt](http://www.scala-sbt.org/). You can download it from [http://www.scala-sbt.org/download.html](http://www.scala-sbt.org/download.html) website, or you can just find sbt in your favourite package manager (`apt-get`, `yum` or other).

You still need to have Java Development Kit (JDK) installed. Application should compile on JDK version 1.7+, that can be found [here](http://www.oracle.com/technetwork/java/javase/downloads/index.html)

### Configuring

Create the config.json in the /etc/ServerSynchronizer. i.e. `/etc/ServerSynchronizer/config.json` must be created.  Here is the <a href="https://github.com/wwken/ServerSynchronizer/blob/master/src/main/resources/config.json" target="_blank">template</a> you can use.  For example, to connect to localhost with 'ken' as user name and the key file, an example can be <a href="https://github.com/wwken/ServerSynchronizer/blob/master/src/test/resources/test-config-localhost.json" target="_blank">found</a> here.  The parameters there all seem to be pretty much self-explanatory.

##### Runing

After the above step, you can now run the ServerSynchronizer by issuing the following command:
```bash
./run.sh
```
Let say, you have this <a href="https://github.com/wwken/ServerSynchronizer/blob/master/src/test/resources/test-config-iq-spark.json" target="_blank">config.json</a> set up in `/etc/ServerSynchronizer`, that means whenever you are editing the files in `/Users/ken/workspace/sparkProjectSrc` , only files with extensions "*.scala" or "*.java" or "*.py" or "*.txt" will get synchronized to the remote host at 'iq-spark' at the folder `/opt/sparkDeployFolder` for every 2 seconds

##### Testing

You can run all test cases with this command:
```bash
./test.sh
```

###### Related Articles 

https://wwken.wordpress.com/2015/10/23/serversynchronizer-the-software-that-synchronizes-all-the-files-from-a-local-server-to-all-remote-servers/



