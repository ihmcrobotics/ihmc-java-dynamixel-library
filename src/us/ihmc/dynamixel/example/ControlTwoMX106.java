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
      
      
      DynamixelProtocol dynamixelProtocol = new DynamixelProtocol(port, baudRate);
      dynamixelProtocol.open();

      
      MX106 a = new MX106(1, dynamixelProtocol);
      MX106 b = new MX106(2, dynamixelProtocol);
      
      while (true)
      {
         double desiredPositionA = Math.PI + (Math.PI * Math.sin(System.nanoTime()/1e9));
         double desiredPositionB = Math.PI + (Math.PI * Math.cos(1 * System.nanoTime()/1e9));
         
         try
         {
            long startTime = System.nanoTime();
            a.setPosition(desiredPositionA);
            long readTime = System.nanoTime() - startTime;
            
            System.out.println("Position error: " + (desiredPositionA - a.readPosition()) + ", temperature: " + a.readTemperature() + ", load: " + a.readLoad() + ", read time: " + readTime);

            startTime = System.nanoTime();
            b.setPosition(desiredPositionB);
            readTime = System.nanoTime() - startTime;
            
            System.out.println("Position error: " + (desiredPositionB - b.readPosition()) + ", temperature: " + b.readTemperature() + ", load: " + b.readLoad() + ", read time: " + readTime);
            
         }
         catch (DynamixelTimeoutException e)
         {
            e.printStackTrace();
         }
         catch (DynamixelDataCorruptedException e)
         {
            e.printStackTrace();
         }
         
         Thread.sleep(10);
      }
   }
}
