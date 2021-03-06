package us.ihmc.dynamixel.example;

import java.io.IOException;

import gnu.io.NoSuchPortException;
import us.ihmc.dynamixel.DynamixelErrorHolder;
import us.ihmc.dynamixel.DynamixelProtocol;
import us.ihmc.dynamixel.actuators.controlTables.MX106ControlTable;
import us.ihmc.dynamixel.exceptions.DynamixelDataCorruptedException;
import us.ihmc.dynamixel.exceptions.DynamixelTimeoutException;

public class LowLevelControlPosition
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
      
      DynamixelErrorHolder dynamixelErrorHolder = new DynamixelErrorHolder();
      
      while(true)
      {
         try
         {
            int desiredPosition = 2000 + (int) (2000 * Math.sin(System.nanoTime()/1e9));
            
            
            /*
             * Use DynamixelProtocol.registerWriterWord followed by DynamixelProtocol.action to synchronize motions
             */
            
//            dynamixelProtocol.registerWriteWord(2, MX106ControlTable.GoalPositionL, desiredPosition, dynamixelErrorHolder);
//            if(dynamixelErrorHolder.hasError())
//            {
//               System.out.println(dynamixelErrorHolder);
//            }
//            
//            dynamixelProtocol.action(DynamixelProtocol.BROADCAST_ID, dynamixelErrorHolder);

            dynamixelProtocol.writeWord(2, MX106ControlTable.GoalPositionL, desiredPosition, dynamixelErrorHolder);
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
         catch (DynamixelTimeoutException |  DynamixelDataCorruptedException e)
         {
            e.printStackTrace();
            continue;
         }
         
         Thread.sleep(10);
      }
      
      
   }

}
