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
      String port = "/dev/ttyUSB0";
      int baudRate = 1000000;
      if(args.length > 0)
      {
         port = args[0];
      }
      if(args.length > 1)
      {
         baudRate = Integer.parseInt(args[1]);
      }
      
      DynamixelErrorHolder dynamixelErrorHolder = new DynamixelErrorHolder();
      
      DynamixelProtocol dynamixelProtocol = new DynamixelProtocol(port, baudRate);
      dynamixelProtocol.open();
      
      
      
      for(int i = 0; i < (DynamixelProtocol.BROADCAST_ID & 0xFF); i++)
      {
         try
         {
            dynamixelProtocol.ping(i, dynamixelErrorHolder);
            
//            dynamixelProtocol.writeByte(i, MX106ControlTable.ReturnDelayTime, 0, dynamixelErrorHolder);
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
