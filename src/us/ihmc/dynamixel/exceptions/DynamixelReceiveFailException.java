package us.ihmc.dynamixel.exceptions;

import java.io.IOException;

public class DynamixelReceiveFailException extends IOException
{
   private static final long serialVersionUID = 8896855131967266343L;

   public DynamixelReceiveFailException(IOException e)
   {
      super(e);
   }

}
