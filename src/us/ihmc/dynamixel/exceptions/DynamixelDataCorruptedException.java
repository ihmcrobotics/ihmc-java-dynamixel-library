package us.ihmc.dynamixel.exceptions;

/**
 * This exception is thrown when data received from the Dynamixel somehow has been corrupted.
 *
 */
public class DynamixelDataCorruptedException extends Exception
{
   private static final long serialVersionUID = 8896855131967266343L;

   public DynamixelDataCorruptedException(String msg)
   {
      super(msg);
   }

}
