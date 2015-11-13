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
      
      
      
      for(int i = 0; i < (DynamixelProtocol.BROADCAST_ID & 0xFF); i++)
      {
         try
         {
            dynamixelProtocol.ping(i, dynamixelErrorHolder);
         }
         catch (DynamixelDataCorruptedException e)
         {
            System.err.println("Data corruption trying to reach Dynamixel " + i + ", retrying");
            i--;
            continue;
         }
         catch (DynamixelTimeoutException e)
         {
            System.err.println("No dynamixel at address " + i);
            continue;
         }
         
         System.out.println("Found dynamixel at address " + i);
         
      }
      
      dynamixelProtocol.close();
      
   }

}
