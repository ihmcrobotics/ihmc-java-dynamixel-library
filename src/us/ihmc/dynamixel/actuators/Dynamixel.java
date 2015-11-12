package us.ihmc.dynamixel.actuators;

import java.io.IOException;

import us.ihmc.dynamixel.DynamixelErrorHolder;
import us.ihmc.dynamixel.DynamixelProtocol;
import us.ihmc.dynamixel.actuators.controlTables.DynamixelControlTableElement;
import us.ihmc.dynamixel.exceptions.DynamixelDataCorruptedException;
import us.ihmc.dynamixel.exceptions.DynamixelTimeoutException;

/**
 * 
 * Convenience class to abstract Dynamixel actuators as objects.
 *
 */
public class Dynamixel
{
   private final int id;
   private final DynamixelProtocol protocol;
   private final DynamixelErrorHolder errorHolder = new DynamixelErrorHolder();

   public Dynamixel(int id, DynamixelProtocol protocol)
   {
      this.id = id;
      this.protocol = protocol;
   }

   public int readByte(DynamixelControlTableElement address) throws DynamixelTimeoutException, IOException, DynamixelDataCorruptedException
   {
      return protocol.readByte(id, address, errorHolder);
   }

   public int readWord(DynamixelControlTableElement address) throws DynamixelTimeoutException, IOException, DynamixelDataCorruptedException
   {
      return protocol.readWord(id, address, errorHolder);
   }

   public void writeByte(DynamixelControlTableElement address, int value) throws DynamixelTimeoutException, IOException, DynamixelDataCorruptedException
   {
      protocol.writeByte(id, address, value, errorHolder);
   }

   public void writeWord(DynamixelControlTableElement address, int value) throws DynamixelTimeoutException, IOException, DynamixelDataCorruptedException
   {
      protocol.writeWord(id, address, value, errorHolder);
   }

   public boolean hasError()
   {
      return errorHolder.hasError();
   }

   public boolean isOverVoltage()
   {
      return errorHolder.isOverVoltage();
   }

   public boolean isAngleLimit()
   {
      return errorHolder.isAngleLimit();
   }

   public boolean isOverheating()
   {
      return errorHolder.isOverheating();
   }

   public boolean isOutOfRange()
   {
      return errorHolder.isOutOfRange();
   }

   public boolean isChecksumError()
   {
      return errorHolder.isChecksumError();
   }

   public boolean isOverload()
   {
      return errorHolder.isOverload();
   }

   public boolean isInstructionError()
   {
      return errorHolder.isInstructionError();
   }

   public String getErrorString()
   {
      return errorHolder.toString();
   }

}
