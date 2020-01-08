/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import frc.robot.Robot;
import frc.robot.commands.drivetrainparameters.DrivetrainParameters;
import frc.robot.commands.drivetrainparameters.IDrivetrainParametersSource;
import frc.robot.subsystems.Drivetrain;

public class TankDrive extends BaseCommand {
  private Drivetrain drivetrain;
  private IDrivetrainParametersSource powerSource;
  private IDrivetrainParametersSource defualtPowerSource;
 
  public TankDrive(IDrivetrainParametersSource defaultPowerSource) {
    this.defualtPowerSource = defaultPowerSource;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    requires(Robot.drivetrain);
 
    this.drivetrain = Robot.drivetrain;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    IDrivetrainParametersSource source = powerSource == null ? defualtPowerSource : powerSource;
    DrivetrainParameters power = source.Get();
    drivetrain.drive(power.Left, power.Right, power.Angle);
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

  public void setPowerSource(IDrivetrainParametersSource powerSource){
    this.powerSource = powerSource;
  }

  public IDrivetrainParametersSource getPowerSource(){
    return this.powerSource;
  }

  public void resetPowerSource(){
    this.powerSource = null;
  }
}
