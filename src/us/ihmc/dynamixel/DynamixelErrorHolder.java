package us.ihmc.dynamixel;

/**
 * Holder for the value of the status error byte coming from the Dynamixel.
 * 
 * <p>Use this return type to check for correct operation of the actuator. 
 * A convenient toString method has been provided to print the status of the Dynamixel.</p>
 *
 */
public class DynamixelErrorHolder
{
   
   private static final int ERRBIT_VOLTAGE     =1;
   private static final int ERRBIT_ANGLE       =2;
   private static final int ERRBIT_OVERHEAT    =4;
   private static final int ERRBIT_RANGE       =8;
   private static final int ERRBIT_CHECKSUM    =16;
   private static final int ERRBIT_OVERLOAD    =32;
   private static final int ERRBIT_INSTRUCTION =64;

   private int error;

   void setError(byte error)
   {
      this.error = error;
   }
   
   /**
    * @return true if an error status is active on the Dynamixel
    */
   public boolean hasError()
   {
      return error != 0;
   }
   
   /**
    * 
    * @return true  when the applied voltage is out of the range of operating voltage set in the Control table.
    */
   public boolean isOverVoltage()
   {
      return (error & ERRBIT_VOLTAGE) == ERRBIT_VOLTAGE;
   }
   
   /**
    * 
    * @return true  when Goal Position is written out of the range from CW Angle Limit to CCW Angle Limit.
    */
   public boolean isAngleLimit()
   {
      return (error & ERRBIT_ANGLE) == ERRBIT_ANGLE;
   }
   
   /**
    * 
    * @return true when internal temperature of Dynamixel is out of the range of operating temperature set in the Control table.
    */
   public boolean isOverheating()
   {
      return (error & ERRBIT_OVERHEAT) == ERRBIT_OVERHEAT;
   }

   /**
    * 
    * @return true when a command is out of the range for use. 
    */
   public boolean isOutOfRange()
   {
      return (error & ERRBIT_RANGE) == ERRBIT_RANGE;
   }
   
   /**
    * 
    * @return true when the Checksum of the transmitted Instruction Packet is incorrect
    */
   public boolean isChecksumError()
   {
      return (error & ERRBIT_CHECKSUM) == ERRBIT_CHECKSUM;
   }
   
   /**
    * 
    * @return true when the current load cannot be controlled by the set Torque
    */
   public boolean isOverload()
   {
      return (error & ERRBIT_OVERLOAD) == ERRBIT_OVERLOAD;
   }
   
   /**
    * 
    * @return true in case of sending an undefined instruction or delivering the action command without the reg_write command
    */
   public boolean isInstructionError()
   {
      return (error & ERRBIT_INSTRUCTION) == ERRBIT_INSTRUCTION;
   }

   @Override
   public String toString()
   {
      return "DynamixelErrorHolder [hasError()=" + hasError() + ", isOverVoltage()=" + isOverVoltage() + ", isAngleLimit()=" + isAngleLimit()
            + ", isOverheating()=" + isOverheating() + ", isOutOfRange()=" + isOutOfRange() + ", isChecksumError()=" + isChecksumError() + ", isOverload()="
            + isOverload() + ", isInstructionError()=" + isInstructionError() + "]";
   }
   
   
}
