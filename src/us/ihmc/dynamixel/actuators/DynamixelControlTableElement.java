package us.ihmc.dynamixel.actuators;

import us.ihmc.dynamixel.Access;

/**
 * Control table values for dynamixels. 
 * 
 * <p>The common usage for this interface is to define an enum implementing this interface which lists all possible Control Table values</p>
 * 
 * 
 * <p>
 * An easy way to generate the enums is 
 * - Look up control table on Robotis website (for example http://support.robotis.com/en/product/dynamixel/mx_series/mx-106.htm)
 * - Copy to Excel
 * - Swap column A & B
 * - Remove spaces from column A
 * - Export to CSV with fixed column width
 * - Use block edit mode to make it compile. See MX106ControlTable for example
 * </p>
 */
public interface DynamixelControlTableElement
{

   /**
    * 
    * @return the address of the Control Table Registry field. 0-254
    */
   public int getAddress();

   /**
    * 
    * @return a description of the registry field
    */
   public String getDescription();

   /**
    * 
    * @return access rights of the registry field. 
    */
   public Access getAccess();

   /**
    * 
    * @return The initial, factory default value of this field
    */
   public int getInitial();

}
