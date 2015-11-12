package us.ihmc.dynamixel;

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

   public void setError(byte error)
   {
      this.error = error;
   }
   
   public boolean hasError()
   {
      return error != 0;
   }
   
   public boolean isOverVoltage()
   {
      return (error & ERRBIT_VOLTAGE) == ERRBIT_VOLTAGE;
   }
   
   public boolean isAngleLimit()
   {
      return (error & ERRBIT_ANGLE) == ERRBIT_ANGLE;
   }
   
   public boolean isOverheating()
   {
      return (error & ERRBIT_OVERHEAT) == ERRBIT_OVERHEAT;
   }
   
   public boolean isOutOfRange()
   {
      return (error & ERRBIT_RANGE) == ERRBIT_RANGE;
   }
   
   public boolean isChecksumError()
   {
      return (error & ERRBIT_CHECKSUM) == ERRBIT_CHECKSUM;
   }
   
   public boolean isOverload()
   {
      return (error & ERRBIT_OVERLOAD) == ERRBIT_OVERLOAD;
   }
   
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
