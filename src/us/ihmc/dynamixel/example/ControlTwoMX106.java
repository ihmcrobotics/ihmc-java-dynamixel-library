package us.ihmc.dynamixel.example;

import java.io.IOException;

import gnu.io.NoSuchPortException;
import us.ihmc.dynamixel.DynamixelProtocol;
import us.ihmc.dynamixel.actuators.MX106;
import us.ihmc.dynamixel.exceptions.DynamixelDataCorruptedException;
import us.ihmc.dynamixel.exceptions.DynamixelTimeoutException;

public class ControlTwoMX106
{

   public static void main(String[] args) throws IOException, NoSuchPortException, InterruptedException
   {
      DynamixelProtocol dynamixelProtocol = new DynamixelProtocol("/dev/ttyUSB0", 1000000);
      dynamixelProtocol.open();

      
//      MX106 a = new MX106(1, dynamixelProtocol);
      MX106 b = new MX106(2, dynamixelProtocol);
      
      while (true)
      {
         double desiredPositionA = Math.PI + (Math.PI * Math.sin(System.nanoTime()/1e9));
         double desiredPositionB = Math.PI + (Math.PI * Math.cos(1 * System.nanoTime()/1e9));
         
         try
         {
            b.setPosition(desiredPositionB);
            
            System.out.println("Error: " + (desiredPositionB - b.readPosition()) + ", temperature: " + b.readTemperature() + ", load: " + b.readLoad());
         }
         catch (DynamixelTimeoutException e)
         {
            e.printStackTrace();
         }
         catch (DynamixelDataCorruptedException e)
         {
            e.printStackTrace();
         }
         
         Thread.sleep(20);
      }
   }
}
