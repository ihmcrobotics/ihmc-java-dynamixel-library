# Dynamixel library

## Using Dynamixels
See the examples in us.ihmc.dynamixel.example

## Decreasing delay
By default, the Dynamixel has a return delay of 0.5ms. Set Return Delay Time to 0 with

dynamixelProtocol.writeByte(i, MX106ControlTable.ReturnDelayTime, 0, dynamixelErrorHolder);
