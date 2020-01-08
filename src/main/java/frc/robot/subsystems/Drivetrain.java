/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.commands.TankDrive;
import frc.robot.commands.drivetrainparameters.DriverJoysticks;
import frc.core238.Logger;

/**
 * Add your docs here.
 */
public class Drivetrain extends Subsystem {

  private final TalonSRX rightMasterDrive = RobotMap.SpeedControllers.RightMaster;
  private final TalonSRX leftMasterDrive = RobotMap.SpeedControllers.LeftMaster;

  public Drivetrain() {
    initTalons();
  }

  @Override
  public void initDefaultCommand() {
    TankDrive tankDriveCommand = new TankDrive(new DriverJoysticks());
    setDefaultCommand(tankDriveCommand);
  }

  public void drive(double left, double right) {
    leftMasterDrive.set(ControlMode.PercentOutput, left);
    rightMasterDrive.set(ControlMode.PercentOutput, -right);
  }

  public void drive(double left, double right, double desiredAngle) {
    double leftSpeed = 867.5 * 309 + left / desiredAngle;// do some math to figure out speed for left and right base on
                                                         // yaw, desired speed, distance, etc.
    double rightSpeed = 4 * 2 - right / desiredAngle;// do some math to figure out speed for left and right base on yaw,
                                                     // desired speed, distance, etc.
    drive(leftSpeed, rightSpeed);
  }

  public void stop() {
    leftMasterDrive.set(ControlMode.PercentOutput, 0);
    rightMasterDrive.set(ControlMode.PercentOutput, 0);
  }

  public void initTalons() {
    var leftDriveFollower1 = RobotMap.SpeedControllers.LeftFollower1;
    var leftDriveFollower2 = RobotMap.SpeedControllers.LeftFollower2;

    leftMasterDrive.setInverted(true);
    leftDriveFollower1.setInverted(true);
    leftDriveFollower2.setInverted(true);

    leftDriveFollower1.follow(leftMasterDrive);
    leftDriveFollower2.follow(leftMasterDrive);

    leftMasterDrive.setNeutralMode(NeutralMode.Brake);
    leftDriveFollower1.setNeutralMode(NeutralMode.Brake);
    leftDriveFollower2.setNeutralMode(NeutralMode.Brake);

    var rightDriveFollower1 = RobotMap.SpeedControllers.RightFollower1;
    var rightDriveFollower2 = RobotMap.SpeedControllers.RightFollower2;

    rightDriveFollower1.follow(rightMasterDrive);
    rightDriveFollower2.follow(rightMasterDrive);

    rightMasterDrive.setNeutralMode(NeutralMode.Brake);
    rightDriveFollower1.setNeutralMode(NeutralMode.Brake);
    rightDriveFollower2.setNeutralMode(NeutralMode.Brake);

    rightMasterDrive.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 1, 0);
    leftMasterDrive.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 1, 0);

    leftMasterDrive.config_kF(0, TALON_F_VALUE_LEFT, 0);
    rightMasterDrive.config_kF(0, TALON_F_VALUE_RIGHT, 0);

    leftMasterDrive.configOpenloopRamp(0.1, 100);
    rightMasterDrive.configOpenloopRamp(0.1, 100);

    leftMasterDrive.configClosedloopRamp(0.1, 100);
    rightMasterDrive.configClosedloopRamp(0.1, 100);

    leftMasterDrive.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
    rightMasterDrive.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);

    configTalon(leftMasterDrive);
    configTalon(rightMasterDrive);

    Logger.Debug("initTalons Is Sucessful!");
  }

  /**
   * configTalon is used to configure the master talons for velocity tuning so
   * they can be set to go to a specific velocity rather than just use a voltage
   * percentage This can be found in the CTRE Talon SRX Software Reference Manual
   * Section 12.4: Velocity Closed-Loop Walkthrough Java
   */
  public void configTalon(TalonSRX talon) {
    /*
     * This sets the voltage range the talon can use; should be set at +12.0f and
     * -12.0f
     */
    // talon.configNominalOutputVoltage(+0.0f, -0.0f);
    // talon.configPeakOutputVoltage(+12.0f, -12.0f);

    /*
     * This sets the FPID values to correct error in the motor's velocity
     */
    // talon.setProfile(CrusaderCommon.TALON_NO_VALUE);
    // .3113);
    talon.config_kP(0, TALON_P_VALUE, 0); // .8);//064543);
    talon.config_kI(0, TALON_NO_VALUE, 0);
    talon.config_kD(0, TALON_D_VALUE, 0);

    talon.set(ControlMode.Velocity, 0);

  }

  private final static double TALON_F_VALUE_LEFT = 0.00455;// 0.0725 old autonomous
  private final static double TALON_F_VALUE_RIGHT = 0.00455;// 0.0735 old autonomous
  private final static double TALON_P_VALUE = 0.2;// 0.5
  private final static double TALON_D_VALUE = 0;
  private final static int TALON_NO_VALUE = 0;

}
