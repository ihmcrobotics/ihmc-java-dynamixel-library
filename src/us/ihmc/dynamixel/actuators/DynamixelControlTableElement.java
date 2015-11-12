package us.ihmc.dynamixel.actuators;

import us.ihmc.dynamixel.Access;

/**
 * Control table for dynamixels. 
 * 
 * 
 * An easy way to generate the enums is 
 * - Look up control table on Robotis website (for example http://support.robotis.com/en/product/dynamixel/mx_series/mx-106.htm)
 * - Copy to Excel
 * - Swap column A & B
 * - Remove spaces from column A
 * - Export to CSV with fixed column width
 * - Use block edit mode to make it compile. See MX106ControlTable for example
 * 
 * @author jesper
 *
 */
public interface DynamixelControlTableElement
{

   public int getAddress();

   public String getDescription();

   public Access getAccess();

   public int getInitial();

}
