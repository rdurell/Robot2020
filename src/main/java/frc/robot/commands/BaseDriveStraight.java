/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.util.List;

import frc.core238.Logger;
import frc.core238.autonomous.AutonomousModeAnnotation;
import frc.robot.Robot;
import frc.robot.commands.BaseCommand;
import frc.robot.commands.IAutonomousCommand;
import frc.robot.subsystems.Drivetrain;

@AutonomousModeAnnotation(parameterNames = {"Speed","Distance"})
public abstract class BaseDriveStraight extends BaseCommand implements IAutonomousCommand {

  private double speed = 0;
  private int distance = 0;
  private int distanceTravelled = 0;

  private Drivetrain drivetrain;
  private boolean isAutonomousMode = false;

  public BaseDriveStraight() {
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    requires(Robot.drivetrain);

    drivetrain = Robot.drivetrain;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    Logger.Debug("DriveStraight.execute() : Speed: " + speed + ", Distance: " + distance);
    double offset = getOffset();

    drivetrain.drive(speed, speed, offset);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return distanceTravelled >= distance;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    drivetrain.stop();
  }

  @Override
  public void setParameters(List<String> parameters) {
    speed = Double.parseDouble(parameters.get(0));
    distance = Integer.parseInt(parameters.get(1));
  }

  protected double getOffset() {
    return 0;
  }

  @Override
  public boolean getIsAutonomousMode() {
    return isAutonomousMode;
  }

  @Override
  public void setIsAutonomousMode(boolean isAutonomousMode) {
    this.isAutonomousMode = isAutonomousMode;
  }

}
