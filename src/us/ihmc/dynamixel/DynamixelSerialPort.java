package us.ihmc.dynamixel;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

class DynamixelSerialPort
{
   private final static int OPEN_TIMEOUT = 2000;
   private final static long nano = 1000000000;

   private final CommPortIdentifier identifier;
   private final int baudRate;

   private SerialPort serial;
   private InputStream rxStream;
   private OutputStream txStream;

   private boolean connected = false;

   public DynamixelSerialPort(String port, int baudRate) throws NoSuchPortException
   {
      if ((System.getProperty("os.name").toLowerCase().indexOf("linux") != -1))
      {
         System.setProperty("gnu.io.rxtx.SerialPorts", port);
      }
      this.identifier = CommPortIdentifier.getPortIdentifier(port);
      this.baudRate = baudRate;
   }

   public boolean isConnected()
   {
      return connected;
   }

   public void open() throws IOException
   {
      if (isConnected())
      {
         System.err.println("Already connected to dynamixel");
         return;
      }

      try
      {
         CommPort commPort = identifier.open(getClass().getSimpleName(), OPEN_TIMEOUT); 
         if(commPort instanceof SerialPort)
         {
            serial = (SerialPort) commPort;
            serial.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
            serial.setSerialPortParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            serial.enableReceiveTimeout(1); // Transfer time is 12us/byte. Waiting 1ms is plenty
            serial.disableReceiveThreshold();
            rxStream = new BufferedInputStream(serial.getInputStream());
            txStream = serial.getOutputStream();
         }
         else
         {
            throw new IOException(identifier + " is not a serial port");
         }
      }
      catch (PortInUseException e)
      {
         throw new IOException("Cannot open already opened port: " + e.getMessage());
      }
      catch (UnsupportedCommOperationException e)
      {
         throw new IOException("Cannot set serial port parameters: " + e.getMessage());
      }

      connected = true;

   }

   public void close()
   {
      try
      {
         rxStream.close();
         txStream.close();
      }
      catch (IOException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      serial.close();
      connected = false;
   }

   public void tx(byte[] packet, int length) throws IOException
   {
      txStream.write(packet, 0, length);
      txStream.flush();
   }

   public int read() throws IOException
   {
      return rxStream.read();
   }

   public int rx(byte[] packet, int offset, int length) throws IOException
   {
      return rxStream.read(packet, offset, length);
   }

   public void flush() throws IOException
   {
      rxStream.skip(rxStream.available());
   }

}
