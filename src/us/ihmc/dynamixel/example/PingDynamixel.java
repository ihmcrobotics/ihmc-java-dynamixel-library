package us.ihmc.dynamixel.example;

import java.io.IOException;

import gnu.io.NoSuchPortException;
import us.ihmc.dynamixel.DynamixelProtocol;
import us.ihmc.dynamixel.exceptions.DynamixelReceiveCorruptException;
import us.ihmc.dynamixel.exceptions.DynamixelReceiveTimeoutException;

public class PingDynamixel
{

   public static void main(String[] args) throws IOException, NoSuchPortException, InterruptedException
   {
      DynamixelProtocol dynamixelProtocol = new DynamixelProtocol("/dev/ttyUSB0", 1000000);
      dynamixelProtocol.open();
      
      while(true)
      {
         try
         {
            dynamixelProtocol.ping(2);
         }
         catch (DynamixelReceiveTimeoutException |  DynamixelReceiveCorruptException e)
         {
            e.printStackTrace();
            continue;
         }
         
         Thread.sleep(10);
      }
      
      
   }

}
