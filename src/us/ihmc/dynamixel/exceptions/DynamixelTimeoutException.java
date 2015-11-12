package us.ihmc.dynamixel.exceptions;

public class DynamixelTimeoutException extends Exception
{
   private static final long serialVersionUID = 8896855131967266343L;

   public DynamixelTimeoutException(int bytesToRead, int bytesRead)
   {
      super("Expected " + bytesToRead + " bytes, got " + bytesRead);
   }

}
