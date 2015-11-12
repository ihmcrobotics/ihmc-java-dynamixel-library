package us.ihmc.dynamixel.example;

import java.io.IOException;

import gnu.io.NoSuchPortException;
import us.ihmc.dynamixel.DynamixelErrorHolder;
import us.ihmc.dynamixel.DynamixelProtocol;
import us.ihmc.dynamixel.exceptions.DynamixelCorruptException;
import us.ihmc.dynamixel.exceptions.DynamixelReceiveTimeoutException;

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
         catch (DynamixelReceiveTimeoutException |  DynamixelCorruptException e)
         {
            e.printStackTrace();
            continue;
         }
         
         Thread.sleep(10);
      }
      
      
   }

}
