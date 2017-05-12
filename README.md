# Dynamixel library

A simple library written in Java to talk to the Robotis Dynamixel Drives. A low level command interface has been provided in us.ihmc.dynamixel.DynamixelProtocol.

A higher level object orientated interface for actuators has been provided in us.ihmc.dynamixel.actuators. Extends us.ihmc.dynamixel.actuators.Dynamixel to implement your own models. Currently the following actuators are implemented

- Dynamixel MX-106

JavaDoc is provided for us.ihmc.dynamixel.DynamixelProtocol and us.ihmc.dynamixel.actuarors.MX106.

## Usage
The library is available as a maven repository. To add to your gradle project, use the following snippet in your build.gradle.
```
repositories {
    maven {
        url  "http://dl.bintray.com/ihmcrobotics/maven-release"
    }
}
	
dependencies {
	   compile group: 'us.ihmc', name: 'IHMCJavaDynamixelLibrary', version: '0.1.1'
}
```

## Examples
See the examples in us.ihmc.dynamixel.example

- PingDynamixel	Send a ping to Dynamixel 0-254, this will find all Dynamixels with the correct baud rate on the bus
- LowLevelControlPosition	Control the position of a Dynamixel on ID 2 using the low level protocol library
- ControlTwoMX106	Control the position of two Dynamixel MX-106 on the bus at ID 1 and 2. 	 

## Decreasing delay
By default, the Dynamixel has a return delay of 0.5ms. Set Return Delay Time to 0 with

dynamixelProtocol.writeByte(i, MX106ControlTable.ReturnDelayTime, 0, dynamixelErrorHolder);
