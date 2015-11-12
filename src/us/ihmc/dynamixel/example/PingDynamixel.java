package us.ihmc.dynamixel.example;

import java.io.IOException;

import gnu.io.NoSuchPortException;
import us.ihmc.dynamixel.DynamixelErrorHolder;
import us.ihmc.dynamixel.DynamixelProtocol;
import us.ihmc.dynamixel.exceptions.DynamixelDataCorruptedException;
import us.ihmc.dynamixel.exceptions.DynamixelTimeoutException;

public class PingDynamixel
{

   public static void main(String[] args) throws IOException, NoSuchPortException, InterruptedException
   {
      DynamixelErrorHolder dynamixelErrorHolder = new DynamixelErrorHolder();
      
      DynamixelProtocol dynamixelProtocol = new DynamixelProtocol("/dev/ttyUSB0", 1000000);
      dynamixelProtocol.open();
      
      
      
      while(true)
      {
         try
         {
            dynamixelProtocol.ping(2, dynamixelErrorHolder);
         }
         catch (DynamixelTimeoutException |  DynamixelDataCorruptedException e)
         {
            e.printStackTrace();
            continue;
         }
         
         Thread.sleep(10);
      }
      
      
   }

}
