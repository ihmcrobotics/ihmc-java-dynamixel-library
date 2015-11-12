package us.ihmc.dynamixel.actuators;

import us.ihmc.dynamixel.Access;

/**
 * This class describes the MX106 Control Table as provided on 
 * http://support.robotis.com/en/product/dynamixel/mx_series/mx-106.htm
 * 
 * <p>Use the Low byte values to address a 2-byte value (names ending with L).</p>
 *
 */
public enum MX106ControlTable implements DynamixelControlTableElement
{

   ModelNumberL(0, "Lowest byte of model number", Access.R, 64),
   ModelNumberH(1, "Highest byte of model number ", Access.R, 1),
   VersionofFirmware(2, "Information on the version of firmware ", Access.R, -1),
   ID(3, "ID of Dynamixel", Access.RW, 1),
   BaudRate(4, "Baud Rate of Dynamixel ", Access.RW, 34),
   ReturnDelayTime(5, "Return Delay Time", Access.RW, 250),
   CWAngleLimitL(6, "Lowest byte of clockwise Angle Limit ", Access.RW, 0),
   CWAngleLimitH(7, "Highest byte of clockwise Angle Limit", Access.RW, 0),
   CCWAngleLimitL(8, "Lowest byte of counterclockwise Angle Limit", Access.RW, 255),
   CCWAngleLimitH(9, "Highest byte of counterclockwise Angle Limit ", Access.RW, 15),
   DriveMode(10, "Dual Mode Setting", Access.RW, 0),
   theHighestLimitTemperature(11, "Internal Limit Temperature ", Access.RW, 80),
   theLowestLimitVoltage(12, "Lowest Limit Voltage ", Access.RW, 60),
   theHighestLimitVoltage(13, "Highest Limit Voltage", Access.RW, 160),
   MaxTorqueL(14, "Lowest byte of Max. Torque ", Access.RW, 255),
   MaxTorqueH(15, "Highest byte of Max. Torque", Access.RW, 3),
   StatusReturnLevel(16, "Status Return Level", Access.RW, 2),
   AlarmLED(17, "LED for Alarm", Access.RW, 36),
   AlarmShutdown(18, "Shutdown for Alarm ", Access.RW, 36),
   MultiTurnOffsetL(20, "multi-turn offset least significant byte (LSB) ", Access.RW, 0),
   MultiTurnOffsetH(21, "multi-turn offset most significant byte (MSB)", Access.RW, 0),
   ResolutionDivider(22, "Resolution divider ", Access.RW, 1),
   TorqueEnable(24, "Torque On/Off", Access.RW, 0),
   LED(25, "LED On/Off ", Access.RW, 0),
   DGain(26, "Derivative Gain", Access.RW, 0),
   IGain(27, "Integral Gain", Access.RW, 0),
   PGain(28, "Proportional Gain", Access.RW, 32),
   GoalPositionL(30, "Lowest byte of Goal Position ", Access.RW, -1),
   GoalPositionH(31, "Highest byte of Goal Position", Access.RW, -1),
   MovingSpeedL(32, "Lowest byte of Moving Speed (Moving Velocity)", Access.RW, -1),
   MovingSpeedH(33, "Highest byte of Moving Speed (Moving Velocity) ", Access.RW, -1),
   TorqueLimitL(34, "Lowest byte of Torque Limit (Goal Torque)", Access.RW, 255),
   TorqueLimitH(35, "Highest byte of Torque Limit (Goal Torque) ", Access.RW, 3),
   PresentPositionL(36, "Lowest byte of Current Position (Present Velocity) ", Access.R, -1),
   PresentPositionH(37, "Highest byte of Current Position (Present Velocity)", Access.R, -1),
   PresentSpeedL(38, "Lowest byte of Current Speed ", Access.R, -1),
   PresentSpeedH(39, "Highest byte of Current Speed", Access.R, -1),
   PresentLoadL(40, "Lowest byte of Current Load", Access.R, -1),
   PresentLoadH(41, "Highest byte of Current Load ", Access.R, -1),
   PresentVoltage(42, "Current Voltage", Access.R, -1),
   PresentTemperature(43, "Current Temperature", Access.R, -1),
   Registered(44, "Means if Instruction is registered ", Access.R, 0),
   Moving(46, "Means if there is any movement ", Access.R, 0),
   Lock(47, "Locking EEPROM ", Access.RW, 0),
   PunchL(48, "Lowest byte of Punch ", Access.RW, 0),
   PunchH(49, "Highest byte of Punch", Access.RW, 0),
   CurrentL(68, "Lowest byte of Consuming Current ", Access.RW, 0),
   CurrentH(69, "Highest byte of Consuming Current", Access.RW, 0),
   TorqueControlModeEnable(70, "Torque control mode on/off ", Access.RW, 0),
   GoalTorqueL(71, "Lowest byte of goal torque value ", Access.RW, 0),
   GoalTorqueH(72, "Highest byte of goal torque value", Access.RW, 0),
   GoalAcceleration(73, "Goal Acceleration", Access.RW, 0);

   private final int address;
   private final String description;
   private final Access access;
   private final int initial;

   MX106ControlTable(int address, String description, Access access, int initial)
   {
      this.address = address;
      this.description = description;
      this.access = access;
      this.initial = initial;
   }

   public int getAddress()
   {
      return address;
   }

   public String getDescription()
   {
      return description;
   }

   public Access getAccess()
   {
      return access;
   }

   public int getInitial()
   {
      return initial;
   }

}
