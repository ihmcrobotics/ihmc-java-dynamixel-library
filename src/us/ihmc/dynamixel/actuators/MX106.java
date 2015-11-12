package us.ihmc.dynamixel.actuators;

import java.io.IOException;

import us.ihmc.dynamixel.DynamixelProtocol;
import us.ihmc.dynamixel.actuators.controlTables.MX106ControlTable;
import us.ihmc.dynamixel.exceptions.DynamixelDataCorruptedException;
import us.ihmc.dynamixel.exceptions.DynamixelTimeoutException;

/**
 * Convenience class to control the Dynamixel MX106 actuator.
 *
 */
public class MX106 extends Dynamixel
{
   private static final double POSITION_MULTIPLIER = 0.088 * ((2.0 * Math.PI)/360);
   private static final double LOAD_SCALE = 0.1;
   
   public MX106(int id, DynamixelProtocol protocol)
   {
      super(id, protocol);
   }

   /**
    * Reads the current position of the MX106
    * 
    * @return current drive position in radians, 0 - 2 PI
    * 
    * @throws DynamixelTimeoutException
    * @throws IOException
    * @throws DynamixelDataCorruptedException
    */
   public double readPosition() throws DynamixelTimeoutException, IOException, DynamixelDataCorruptedException
   {
      return readWord(MX106ControlTable.PresentPositionL) * POSITION_MULTIPLIER;
   }
   
   /**
    * 
    * Set the desired position
    * 
    * @param value Desired positon, 0 - 2 PI
    * 
    * @throws DynamixelTimeoutException
    * @throws IOException
    * @throws DynamixelDataCorruptedException
    * @throws RuntimeException if data is out of range 
    */
   public void setPosition(double value) throws DynamixelTimeoutException, IOException, DynamixelDataCorruptedException
   {
      int raw = (int) (value / POSITION_MULTIPLIER);
      if(raw < 0 || raw  > 4095)
      {
         throw new RuntimeException("Data out of range");
      }
      writeWord(MX106ControlTable.GoalPositionL, raw);
   }
   
   /**
    * 
    * @return Drive temperature in celsius
    * @throws DynamixelTimeoutException
    * @throws IOException
    * @throws DynamixelDataCorruptedException
    */
   public double readTemperature() throws DynamixelTimeoutException, IOException, DynamixelDataCorruptedException
   {
      return readByte(MX106ControlTable.PresentTemperature);
   }
   
   /**
    * 
    * @return Approximate load of the drive, in %
    * @throws DynamixelTimeoutException
    * @throws IOException
    * @throws DynamixelDataCorruptedException
    */
   public double readLoad() throws DynamixelTimeoutException, IOException, DynamixelDataCorruptedException 
   {
      int raw = readWord(MX106ControlTable.PresentLoadL);
      if(raw < 1024)
      {
         return -raw * LOAD_SCALE;
      }
      else
      {
         return (raw - 1024) * LOAD_SCALE;
      }
      
   }
}
