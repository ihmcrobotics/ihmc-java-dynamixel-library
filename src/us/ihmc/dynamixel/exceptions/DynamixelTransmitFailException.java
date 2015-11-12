package us.ihmc.dynamixel.exceptions;

import java.io.IOException;

public class DynamixelTransmitFailException extends IOException
{
   private static final long serialVersionUID = -2290883804439043089L;

   public DynamixelTransmitFailException(IOException e)
   {
      super(e);
   }
}
