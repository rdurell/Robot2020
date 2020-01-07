/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.drivetrainpower;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;

/**
 * Add your docs here.
 */
public class DriverJoysticks implements IDrivetrainPowerSource{
    private Joystick left;
    private Joystick right;

    public DriverJoysticks() {
        this.right = Robot.oi.driveRightJoystick;
        this.left = Robot.oi.driverLeftJoystick;
    }

    @Override
    public DrivetrainPower Get() {
        double leftJsValue = left.getY();
        double rightJsValue = right.getY();

        //should have some sort of abstraction in case we want to pull number for somewhere else
        double tuningValue = SmartDashboard.getNumber("DRIVETRAIN TUNING", 0.2);

        //scaling the stick to power ratio -- accelate the "power" the closer you get to the high or low position on the sitck 
        //Yhis represents x = ax^3+(1-a)x where leftJsValue = x; tuningValue = a;
        double leftPower = (tuningValue * (leftJsValue * leftJsValue * leftJsValue) + (1-tuningValue) * leftJsValue);
        double rightPower = (tuningValue * (rightJsValue * rightJsValue * rightJsValue) + (1-tuningValue) * rightJsValue);
        return new DrivetrainPower(leftPower, rightPower, 0);
    }
}
