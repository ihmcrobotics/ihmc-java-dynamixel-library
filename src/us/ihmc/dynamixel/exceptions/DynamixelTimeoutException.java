package us.ihmc.dynamixel.exceptions;

/**
 * This exception is thrown when no data has been received from the Dynamixel within the timeout period
 *
 */
public class DynamixelTimeoutException extends Exception
{
   private static final long serialVersionUID = 8896855131967266343L;

   public DynamixelTimeoutException()
   {
      super();
   }

}
