/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Robot;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Vision;

public class TankDrive extends BaseCommand {
  private Drivetrain drivetrain;
  private Joystick rightJoystick;
  private Joystick leftJoystick;
  private Vision vision;

public TankDrive() {
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    requires(Robot.drivetrain);
    //requires(Robot.vision);

    this.drivetrain = Robot.drivetrain;
    this.rightJoystick = Robot.oi.driveRightJoystick;
    this.leftJoystick = Robot.oi.driverLeftJoystick;

    this.vision = Robot.vision;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if (rightJoystick.getRawButton(1234)){
      double desiredAngle = vision.hasTarget() ? vision.getYaw() : 0;
      drivetrain.drive(leftJoystick.getY(), rightJoystick.getY(), desiredAngle);
    } else {
      drivetrain.drive(leftJoystick.getY(), rightJoystick.getY());
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
