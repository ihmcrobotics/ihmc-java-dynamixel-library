package us.ihmc.dynamixel.example;

import java.io.IOException;

import gnu.io.NoSuchPortException;
import us.ihmc.dynamixel.DynamixelErrorHolder;
import us.ihmc.dynamixel.DynamixelProtocol;
import us.ihmc.dynamixel.actuators.MX106ControlTable;
import us.ihmc.dynamixel.exceptions.DynamixelCorruptException;
import us.ihmc.dynamixel.exceptions.DynamixelReceiveTimeoutException;

public class EchoPosition
{

   public static void main(String[] args) throws IOException, NoSuchPortException, InterruptedException
   {
      DynamixelProtocol dynamixelProtocol = new DynamixelProtocol("/dev/ttyUSB0", 1000000);
      dynamixelProtocol.open();
      
      DynamixelErrorHolder dynamixelErrorHolder = new DynamixelErrorHolder();
      
      while(true)
      {
         try
         {
            System.out.println("Temperature: " + dynamixelProtocol.readByte(2, MX106ControlTable.PresentTemperature, dynamixelErrorHolder));
            if(dynamixelErrorHolder.hasError())
            {
               System.out.println(dynamixelErrorHolder);
            }
            System.out.println("Position: " + dynamixelProtocol.readWord(2, MX106ControlTable.PresentPositionL, dynamixelErrorHolder));
            if(dynamixelErrorHolder.hasError())
            {
               System.out.println(dynamixelErrorHolder);
            }
            
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
