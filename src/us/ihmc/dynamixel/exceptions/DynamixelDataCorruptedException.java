package us.ihmc.dynamixel.exceptions;

public class DynamixelDataCorruptedException extends Exception
{
   private static final long serialVersionUID = 8896855131967266343L;

   public DynamixelDataCorruptedException(String msg)
   {
      super(msg);
   }

}
