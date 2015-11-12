package us.ihmc.dynamixel.exceptions;

import java.io.IOException;

public class DynamixelReceiveTimeoutException extends IOException
{
   private static final long serialVersionUID = 8896855131967266343L;

   public DynamixelReceiveTimeoutException(int bytesToRead, int bytesRead)
   {
      super("Expected " + bytesToRead + " bytes, got " + bytesRead);
   }

}
