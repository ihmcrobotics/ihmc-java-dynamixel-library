package us.ihmc.dynamixel;

import java.io.IOException;

import gnu.io.NoSuchPortException;
import us.ihmc.dynamixel.actuators.controlTables.DynamixelControlTableElement;
import us.ihmc.dynamixel.exceptions.DynamixelDataCorruptedException;
import us.ihmc.dynamixel.exceptions.DynamixelTimeoutException;

/**
 * Implementation of the Dynamixel Communications 1.0 serial protocol.
 * 
 * <p>This class implements the serial wire protocol as described on
 * http://support.robotis.com/en/techsupport_eng.htm#product/dynamixel/dxl_communication.htm<p>
 * 
 * <p>Functions for all command except INST_SYNC_WRITE have been provided.</p>
 * 
 * <p>Communication errors are reported trough exceptions. DynamixelTimeoutException 
 * and DynamixelDataCorruptedException can be recovered from. IOExceptions require reinitialization.
 * 
 * <p>This code is based on the C source code provided by Robotis. 
 *  http://support.robotis.com/en/techsupport_eng.htm#software/dynamixelsdk.htm
 * Copyright (c) 2014, ROBOTIS Inc. All rights reserved.</p>

 *
 */
public class DynamixelProtocol
{
   protected final static int MAXNUM_TXPARAM = 150;
   protected final static int MAXNUM_RXPARAM = 60;

   /**
    * The ID used to broadcast to all Dynamixels. No return message will be provided.
    */
   public final static byte BROADCAST_ID = (byte) 254; 
   
   /*
    * Available commands
    * 
    */
   private final static byte INST_PING = 1;
   private final static byte INST_READ = 2;
   private final static byte INST_WRITE = 3;
   private final static byte INST_REG_WRITE = 4;
   private final static byte INST_ACTION = 5;
   private final static byte INST_RESET = 6;
   private final static byte INST_SYNC_WRITE = (byte) 131;

   /*
    * Instruction offsets
    */
   private final static byte ID = 2;
   private final static byte LENGTH = 3;
   private final static byte INSTRUCTION = 4;
   private final static byte ERRBIT = 4;
   private final static byte PARAMETER = 5;

   /*
    * Status packet size
    */
   private final static int STATUS_HEADER_LENGTH = 6;

   private final DynamixelSerialPort serialPort;

   private final byte[] instructionPacket = new byte[MAXNUM_TXPARAM + 10];
   private final byte[] statusPacket = new byte[MAXNUM_RXPARAM + 10];

   /**
    * Create a new DynamixelProtocol.
    * 
    * Call open() to start sending and receiving data
    * 
    * @param port Serial port to connect to
    * @param baudRate Has to match the baudrate of the dynamixels on the bus
    * 
    * @throws NoSuchPortException If the serial port cannot be found
    */
   public DynamixelProtocol(String port, int baudRate) throws NoSuchPortException
   {
      this.serialPort = new DynamixelSerialPort(port, baudRate);

   }

   private byte calculateChecksum(byte[] packet)
   {
      byte checksum = 0;
      for (int i = 0; i < packet[LENGTH] + 1; i++)
      {
         checksum += packet[i + 2];
      }
      return (byte) ~checksum;
   }

   private void sendPacket() throws IOException
   {

      // Set instruction packet header bytes 0xFFFF
      instructionPacket[0] = (byte) 0xFF;
      instructionPacket[1] = (byte) 0xFF;

      instructionPacket[instructionPacket[LENGTH] + 3] = calculateChecksum(instructionPacket);

      int transmitLength = instructionPacket[LENGTH] + 4;
      serialPort.tx(instructionPacket, transmitLength);
   }

   private void receivePacket(int id, DynamixelErrorHolder errorHolder)
         throws DynamixelDataCorruptedException, IOException, DynamixelTimeoutException
   {

      int bytesToRead = STATUS_HEADER_LENGTH;
      int offset = 0;
      while (true)
      {
         int read = serialPort.rx(statusPacket, offset, bytesToRead);
         
         if (read < bytesToRead)
         {
            throw new DynamixelTimeoutException(bytesToRead, read);
         }

         int headerOffset = -1;
         for (int i = 0; i < STATUS_HEADER_LENGTH - 1; i++)
         {
            if (statusPacket[i] == (byte) 0xFF && statusPacket[i + 1] == (byte) 0xFF)
            {
               headerOffset = i;
               break;
            }
         }

         if (headerOffset == 0)
         {
            break;
         }

         bytesToRead = STATUS_HEADER_LENGTH - headerOffset - 1;
         offset = headerOffset + 1;

         System.out.println("READING ANOTHER " + bytesToRead);
         System.out.println("READ OFFSET " + offset);

         if (headerOffset != -1)
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

      if (statusPacket[ID] != id)
      {
         throw new DynamixelDataCorruptedException("Invalid ID on return packet");
      }

      int statusLength = statusPacket[LENGTH] + 4;
      if (STATUS_HEADER_LENGTH < statusLength)
      {
         int read = serialPort.rx(statusPacket, STATUS_HEADER_LENGTH, statusLength - STATUS_HEADER_LENGTH);
         if (read < statusLength - STATUS_HEADER_LENGTH)
         {
            throw new DynamixelTimeoutException(bytesToRead, read);
         }
      }

      byte checksum = calculateChecksum(statusPacket);
      if (statusPacket[statusPacket[LENGTH] + 3] != checksum)
      {
         throw new DynamixelDataCorruptedException("Invalid checksum on status packet");
      }

      errorHolder.setError(statusPacket[ERRBIT]);

      if (errorHolder.isChecksumError())
      {
         throw new DynamixelDataCorruptedException("Invalid checksum on instruction packet");
      }

      if (errorHolder.isInstructionError())
      {
         throw new DynamixelDataCorruptedException("Invalid instruction received by actuator");
      }

      if (errorHolder.isOutOfRange())
      {
         throw new DynamixelDataCorruptedException("Invalid range for instruction");
      }

   }

   private void sendAndReceive(DynamixelErrorHolder errorHolder)
         throws DynamixelTimeoutException, DynamixelDataCorruptedException, IOException
   {
      sendPacket();
      if(instructionPacket[ID] != BROADCAST_ID)
      {
         receivePacket(instructionPacket[ID], errorHolder);
      }
   }

   /**
    * Open the serial port and prepare for communication
    * 
    * @throws IOException when the serial port cannot be opened
    */
   public void open() throws IOException
   {
      serialPort.open();
   }

   /**
    * Close the connection. The connection can be reopened after calling
    * close()
    */
   public void close()
   {
      serialPort.close();
   }

   
   /**
    * Ping a Dynamixel. This function will return cleanly when the Dynamixel is online.
    * 
    * @param id 0-253 
    * @param errorHolder Holder to provide the return status from the Dynamixel, not null
    * 
    * @throws DynamixelTimeoutException if no response has been received from the Dynamixel
    * @throws DynamixelDataCorruptedException if unexpected data has been received from the Dynamixel. Check for no conflicting IDs 
    * @throws IOException if the connection to the serial port died
    */
   public void ping(int id, DynamixelErrorHolder errorHolder)
         throws  DynamixelTimeoutException, DynamixelDataCorruptedException, IOException
   {
      instructionPacket[ID] = (byte) id;
      instructionPacket[INSTRUCTION] = INST_PING;
      instructionPacket[LENGTH] = 2;

      sendAndReceive(errorHolder);
   }

   /**
    * Send an action message. 
    * 
    * <p>This will execute the command sent by registerWriteByte or registerWriteWord. 
    * Use in combination with BROADCAST_ID to synchronize execution.</p> 
    * 
    * @param id 0-254 
    * @param errorHolder Holder to provide the return status from the Dynamixel, not null
    * 
    * @throws DynamixelTimeoutException if no response has been received from the Dynamixel
    * @throws DynamixelDataCorruptedException if unexpected data has been received from the Dynamixel. Check for no conflicting IDs 
    * @throws IOException if the connection to the serial port died
    */
   public void action(int id, DynamixelErrorHolder errorHolder)
         throws DynamixelTimeoutException, DynamixelDataCorruptedException, IOException
   {
      instructionPacket[ID] = (byte) id;
      instructionPacket[INSTRUCTION] = INST_ACTION;
      instructionPacket[LENGTH] = 2;

      sendAndReceive(errorHolder);
   }

   /**
    * Factory reset of the Dynamixel. Will reset the ID 
    * 
    * <p>A factory reset will reset the ID back to 1. After calling this function, you probably need to setup the Dynamixel again.</p> 
    * 
    * @param id 0-254 
    * @param errorHolder Holder to provide the return status from the Dynamixel, not null
    * 
    * @throws DynamixelTimeoutException if no response has been received from the Dynamixel
    * @throws DynamixelDataCorruptedException if unexpected data has been received from the Dynamixel. Check for no conflicting IDs 
    * @throws IOException if the connection to the serial port died
    */
   public void reset(int id, DynamixelErrorHolder errorHolder)
         throws DynamixelTimeoutException, IOException, DynamixelDataCorruptedException
   {
      instructionPacket[ID] = (byte) id;
      instructionPacket[INSTRUCTION] = INST_RESET;
      instructionPacket[LENGTH] = 2;
      
      sendAndReceive(errorHolder);
   }

   /**
    * Read a single byte from a Dynamixel register. 
    * 
    * 
    * @param id 0-254 
    * @param address The address to read. An element from the Dynamixel Control Table enum in us.ihmc.dynamixel.actuators, not null
    * @param errorHolder Holder to provide the return status from the Dynamixel, not null
    * 
    * @throws DynamixelTimeoutException if no response has been received from the Dynamixel
    * @throws DynamixelDataCorruptedException if unexpected data has been received from the Dynamixel. Check for no conflicting IDs 
    * @throws IOException if the connection to the serial port died
    */
   public int readByte(int id, DynamixelControlTableElement address, DynamixelErrorHolder errorHolder)
         throws DynamixelTimeoutException, IOException, DynamixelDataCorruptedException
   {
      instructionPacket[ID] = (byte) id;
      instructionPacket[INSTRUCTION] = INST_READ;
      instructionPacket[PARAMETER] = (byte) address.getAddress();
      instructionPacket[PARAMETER + 1] = 1;
      instructionPacket[LENGTH] = 4;

      sendAndReceive(errorHolder);

      return statusPacket[PARAMETER] & 0xFF;
   }

   /**
    * Read two bytes from a Dynamixel register as an 16 bit integer
    * 
    * <p>Pass in the low byte address to get the whole value</p>
    * 
    * 
    * @param id 0-254 
    * @param address The address to read. An element from the Dynamixel Control Table enum in us.ihmc.dynamixel.actuators, not null
    * @param errorHolder Holder to provide the return status from the Dynamixel, not null
    * 
    * @throws DynamixelTimeoutException if no response has been received from the Dynamixel
    * @throws DynamixelDataCorruptedException if unexpected data has been received from the Dynamixel. Check for no conflicting IDs 
    * @throws IOException if the connection to the serial port died
    */
   public int readWord(int id, DynamixelControlTableElement address, DynamixelErrorHolder errorHolder)
         throws DynamixelTimeoutException, IOException, DynamixelDataCorruptedException
   {
      instructionPacket[ID] = (byte) id;
      instructionPacket[INSTRUCTION] = INST_READ;
      instructionPacket[PARAMETER] = (byte) address.getAddress();
      instructionPacket[PARAMETER + 1] = 2;
      instructionPacket[LENGTH] = 4;

      sendAndReceive(errorHolder);

      return makeWord(statusPacket[PARAMETER], statusPacket[PARAMETER + 1]);
   }

   
   /**
    * Write a single byte to a Dynamixel register
    * 
    * 
    * @param id 0-254 
    * @param address The address to write. An element from the Dynamixel Control Table enum in us.ihmc.dynamixel.actuators, not null
    * @param value The value to write, unsigned byte in the range 0-255.
    * @param errorHolder Holder to provide the return status from the Dynamixel, not null
    * 
    * @throws DynamixelTimeoutException if no response has been received from the Dynamixel
    * @throws DynamixelDataCorruptedException if unexpected data has been received from the Dynamixel. Check for no conflicting IDs 
    * @throws IOException if the connection to the serial port died
    */
   public void writeByte(int id, DynamixelControlTableElement address, int value, DynamixelErrorHolder errorHolder)
         throws DynamixelTimeoutException, IOException, DynamixelDataCorruptedException
   {
      instructionPacket[ID] = (byte) id;
      instructionPacket[INSTRUCTION] = INST_WRITE;
      instructionPacket[PARAMETER] = (byte) address.getAddress();
      instructionPacket[PARAMETER + 1] = (byte) value;
      instructionPacket[LENGTH] = 4;

      sendAndReceive(errorHolder);
   }

   /**
    * Write a single 16 bit integer to a Dynamixel register
    * 
    * <p>Pass in the low byte address to write the whole value</p>
    * 
    * @param id 0-254 
    * @param address The address to write. An element from the Dynamixel Control Table enum in us.ihmc.dynamixel.actuators, not null
    * @param value The value to write, unsigned integer in the range 0-65535.
    * @param errorHolder Holder to provide the return status from the Dynamixel, not null
    * 
    * @throws DynamixelTimeoutException if no response has been received from the Dynamixel
    * @throws DynamixelDataCorruptedException if unexpected data has been received from the Dynamixel. Check for no conflicting IDs 
    * @throws IOException if the connection to the serial port died
    */
   public void writeWord(int id, DynamixelControlTableElement address, int value, DynamixelErrorHolder errorHolder)
         throws DynamixelTimeoutException, IOException, DynamixelDataCorruptedException
   {
      instructionPacket[ID] = (byte) id;
      instructionPacket[INSTRUCTION] = INST_WRITE;
      instructionPacket[PARAMETER] = (byte) address.getAddress();
      instructionPacket[PARAMETER + 1] = lowByte(value);
      instructionPacket[PARAMETER + 2] = highByte(value);
      instructionPacket[LENGTH] = 5;

      sendAndReceive(errorHolder);
   }

   
   /**
    * Send a single byte to a Dynamixel, but do not write to the register. Call action() to write.
    * 
    * <p>Use this function to synchronize execution of multiple commands on multiple Dynamixels</p>
    * 
    * 
    * @param id 0-254 
    * @param address The address to write. An element from the Dynamixel Control Table enum in us.ihmc.dynamixel.actuators, not null
    * @param value The value to write, unsigned byte in the range 0-255.
    * @param errorHolder Holder to provide the return status from the Dynamixel, not null
    * 
    * @throws DynamixelTimeoutException if no response has been received from the Dynamixel
    * @throws DynamixelDataCorruptedException if unexpected data has been received from the Dynamixel. Check for no conflicting IDs 
    * @throws IOException if the connection to the serial port died
    */
   public void registerWriteByte(int id, DynamixelControlTableElement address, int value, DynamixelErrorHolder errorHolder)
         throws DynamixelTimeoutException, IOException, DynamixelDataCorruptedException
   {
      instructionPacket[ID] = (byte) id;
      instructionPacket[INSTRUCTION] = INST_REG_WRITE;
      instructionPacket[PARAMETER] = (byte) address.getAddress();
      instructionPacket[PARAMETER + 1] = (byte) value;
      instructionPacket[LENGTH] = 4;

      sendAndReceive(errorHolder);
   }

   /**
    * Write a single 16 bit integer to a Dynamixel register. Call action() to write.
    * 
    * <p>Use this function to synchronize execution of multiple commands on multiple Dynamixels</p>
 
    * <p>Pass in the low byte address to write the whole value</p>
    * 
    * @param id 0-254 
    * @param address The address to write. An element from the Dynamixel Control Table enum in us.ihmc.dynamixel.actuators, not null
    * @param value The value to write, unsigned integer in the range 0-65535.
    * @param errorHolder Holder to provide the return status from the Dynamixel, not null
    * 
    * @throws DynamixelTimeoutException if no response has been received from the Dynamixel
    * @throws DynamixelDataCorruptedException if unexpected data has been received from the Dynamixel. Check for no conflicting IDs 
    * @throws IOException if the connection to the serial port died
    */
   public void registerWriteWord(int id, DynamixelControlTableElement address, int value, DynamixelErrorHolder errorHolder)
         throws DynamixelTimeoutException, IOException, DynamixelDataCorruptedException
   {
      instructionPacket[ID] = (byte) id;
      instructionPacket[INSTRUCTION] = INST_REG_WRITE;
      instructionPacket[PARAMETER] = (byte) address.getAddress();
      instructionPacket[PARAMETER + 1] = lowByte(value);
      instructionPacket[PARAMETER + 2] = highByte(value);
      instructionPacket[LENGTH] = 5;

      sendAndReceive(errorHolder);
   }
   
   private int makeWord(int lowbyte, int highbyte)
   {
      return ((highbyte & 0xFF) << 8) + (lowbyte & 0xFF);
   }

   private byte highByte(int value)
   {
      return (byte) ((value & 0xFF00) >> 8);
   }

   private byte lowByte(int value)
   {
      return (byte) (value & 0xFF);
   }

}
