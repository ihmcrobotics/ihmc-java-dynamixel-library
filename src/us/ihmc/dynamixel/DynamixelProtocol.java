package us.ihmc.dynamixel;

import java.io.IOException;

import gnu.io.NoSuchPortException;
import us.ihmc.dynamixel.exceptions.DynamixelReceiveCorruptException;
import us.ihmc.dynamixel.exceptions.DynamixelReceiveFailException;
import us.ihmc.dynamixel.exceptions.DynamixelReceiveTimeoutException;
import us.ihmc.dynamixel.exceptions.DynamixelTransmitFailException;

public class DynamixelProtocol
{
   protected final static int MAXNUM_TXPARAM = 150;
   protected final static int MAXNUM_RXPARAM = 60;
   
   /*
    * Available commands
    * 
    */
   private final static byte INST_PING       = 1;
   private final static byte INST_READ       = 2;
   private final static byte INST_WRITE      = 3;
   private final static byte INST_REG_WRITE  = 4;
   private final static byte INST_ACTION     = 5;
   private final static byte INST_RESET      = 6;
   private final static byte INST_SYNC_WRITE = (byte) 131;
   
   /*
    * Instruction offsets
    */
   private final static byte ID          = 2;
   private final static byte LENGTH      = 3;
   private final static byte INSTRUCTION = 4;
   private final static byte ERRBIT      = 4;
   private final static byte PARAMETER   = 5;

   
   /*
    * Status packet size
    */
   private final static int STATUS_HEADER_LENGTH = 6;
   
   private final DynamixelSerialPort serialPort;
   
   private final byte[] instructionPacket = new byte[MAXNUM_TXPARAM + 10];
   private final byte[] statusPacket = new byte[MAXNUM_RXPARAM + 10];
   
   
   
   
   public DynamixelProtocol(String port, int baudRate) throws NoSuchPortException
   {
      this.serialPort = new DynamixelSerialPort(port, baudRate);
      
      
   }
   
   private byte calculateChecksum(byte[] packet)
   {
      byte checksum = 0;
      for(int i = 0; i < packet[LENGTH] + 1; i++)
      {
         checksum += packet[i+2];
      }
      return (byte) ~checksum; 
   }
   
   private void sendPacket() throws DynamixelTransmitFailException
   {
      
      // Set instruction packet header bytes 0xFFFF
      instructionPacket[0] = (byte) 0xFF;
      instructionPacket[1] = (byte) 0xFF;
      
      instructionPacket[instructionPacket[LENGTH] + 3] = calculateChecksum(instructionPacket);
      
      int transmitLength = instructionPacket[LENGTH] + 4;
      try
      {
         serialPort.tx(instructionPacket, transmitLength);
      }
      catch (IOException e)
      {
         throw new DynamixelTransmitFailException(e);
      }
   }
   
   private void receivePacket(int id) throws DynamixelReceiveFailException, DynamixelReceiveTimeoutException, DynamixelReceiveCorruptException
   {      
      
         
         int bytesToRead = STATUS_HEADER_LENGTH;
         int offset = 0;
         while(true)
         {
            int read;
            try
            {
               read = serialPort.rx(statusPacket, offset, bytesToRead);
            }
            catch (IOException e)
            {
               throw new DynamixelReceiveFailException(e); 
            }
            
            if(read < bytesToRead)
            {
               throw new DynamixelReceiveTimeoutException(bytesToRead, read);
            }
            
            int headerOffset = -1;
            for (int i = 0; i < STATUS_HEADER_LENGTH - 1; i++)
            {
               if(statusPacket[i] == (byte) 0xFF && statusPacket[i+1] == (byte) 0xFF)
               {
                  headerOffset = i;
                  break;
               }
            }
            
            if(headerOffset == 0)
            {
               break;
            }
            
            bytesToRead = STATUS_HEADER_LENGTH - headerOffset - 1;
            offset = headerOffset + 1;
            
            System.out.println("READING ANOTHER " + bytesToRead);
            System.out.println("READ OFFSET " + offset);
            
            if(headerOffset != -1)
            {
               System.out.println("MOVING DATA");
               System.arraycopy(statusPacket, headerOffset, statusPacket, 0, STATUS_HEADER_LENGTH - headerOffset);
               
               System.out.println(statusPacket[0]);
               System.out.println(statusPacket[1]);
               System.out.println(statusPacket[2]);
               System.out.println(statusPacket[3]);
               System.out.println(statusPacket[4]);
               System.out.println(statusPacket[5]);
            }
            
         }
         
         if(statusPacket[ID] != id)
         {
            throw new DynamixelReceiveCorruptException();
         }
         
         int statusLength = statusPacket[LENGTH] + 4;
         if(STATUS_HEADER_LENGTH < statusLength)
         {
            System.out.println("RECEIVING REST OF PACKAGE");
            int read;
            try
            {
               read = serialPort.rx(statusPacket, STATUS_HEADER_LENGTH, statusLength - STATUS_HEADER_LENGTH);
            }
            catch (IOException e)
            {
               throw new DynamixelReceiveFailException(e); 
            }
            if(read < statusLength - STATUS_HEADER_LENGTH)
            {
               throw new DynamixelReceiveTimeoutException(bytesToRead, read);
            }
         }
         
         byte checksum = calculateChecksum(statusPacket);
         if(statusPacket[statusPacket[LENGTH] + 3] != checksum)
         {
            throw new DynamixelReceiveCorruptException();
         }
         
        
      
   }
   
   private void sendAndReceive() throws DynamixelReceiveFailException, DynamixelReceiveTimeoutException, DynamixelTransmitFailException, DynamixelReceiveCorruptException
   {
      sendPacket();
      receivePacket(instructionPacket[ID]);
   }
   
   public void open() throws IOException
   {
      serialPort.open();
   }
   
   public void close()
   {
      serialPort.close();
   }
   
   public void ping(int id) throws DynamixelReceiveFailException, DynamixelReceiveTimeoutException, DynamixelTransmitFailException, DynamixelReceiveCorruptException
   {
      instructionPacket[ID] = (byte) id;
      instructionPacket[INSTRUCTION] = INST_PING;
      instructionPacket[LENGTH] = 2;
      
      sendAndReceive();
   }
}
