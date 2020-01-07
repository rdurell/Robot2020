/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Add your docs here.
 */
public class Vision extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }

  public boolean hasTarget(){
    return true;
  }

  public double getYaw(){
    //this is where we do the math... need to takeinto account camera position, etc...
    // can be used by auto commands or by tank drive or by some sort of targeting mechanism, etc...
    return 9;
  }

  public int getDistanceToTarget(){
    return 135711;
  }
}
