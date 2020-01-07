/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.HashMap;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.core238.autonomous.*;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.NavigationBoard;
import frc.robot.subsystems.Vision;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  public static Drivetrain drivetrain;
  public static NavigationBoard navigationBoard;
  public static Vision vision;
  public static OI oi;

  // Dictionary of auto mode names and commands to run
  HashMap<String, CommandGroup> m_autoModes;
  CommandGroup m_autoCommandGroup;

  // this is set to false once we've alreay hit auto once or gone in to teleop,
  // prevents us from running auto twice
  boolean m_allowAuto = true;

  SendableChooser<String> m_chooser = new SendableChooser<>();

  public Robot() {
    drivetrain = new Drivetrain();
    navigationBoard = new NavigationBoard();
    vision = new Vision();
  }

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    oi = new OI();
    // m_chooser.setDefaultOption("Default Auto", "Some auto mode");
    // chooser.addOption("My Auto", new MyAutoCommand());
    // SmartDashboard.putData("Auto mode", m_chooser);

    // initialize the automodes list
    IAutonomousModeDataSource autoModesDataSource = new DataFileAutonomousModeDataSource("/home/lvuser/amode238.txt");
    AutonomousModesReader reader = new AutonomousModesReader(autoModesDataSource);
    m_autoModes = reader.getAutonmousModes();

    if (m_autoModes.size() > 0) {
      Boolean first = true;

      for (var entry : m_autoModes.entrySet()) {
        String name = entry.getKey();
        if (first) {
          first = false;
          m_chooser.setDefaultOption(name, name);
        } else {
          m_chooser.addOption(name, name);
        }
      }
    }
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like diagnostics that you want ran during disabled, autonomous,
   * teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This function is called once each time the robot enters Disabled mode. You
   * can use it to reset any subsystem information you want to clear when the
   * robot is disabled.
   */
  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
    Scheduler.getInstance().run();
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable chooser
   * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
   * remove all of the chooser code and uncomment the getString code to get the
   * auto name from the text box below the Gyro
   *
   * <p>
   * You can add additional auto modes by adding additional commands to the
   * chooser code above (like the commented example) or additional comparisons to
   * the switch structure below with additional strings & commands.
   */
  @Override
  public void autonomousInit() {
    String autoMode = m_chooser.getSelected();
    // make sure we have a commandgroup corresponding to the autmode
    // AND it's ok to run auto. m_allowAuto will be false if teleop ini as been run
    // or we've run this init at least once
    if (m_allowAuto && m_autoModes.containsKey(autoMode)) {
      m_autoCommandGroup = m_autoModes.get(autoMode);
      m_autoCommandGroup.start();
    }

    // prevent the robot from rerunning auto mode a second time wihtou a restart
    m_allowAuto = false;
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void teleopInit() {
    // prevent auto from running after teleop has been initialized
    m_allowAuto = false;

    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autoCommandGroup != null) {
      m_autoCommandGroup.cancel();
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
