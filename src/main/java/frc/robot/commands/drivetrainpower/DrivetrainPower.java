/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.drivetrainpower;

/**
 * Add your docs here.
 */
public class DrivetrainPower {
    
    public final double Left;
    public final double Right;
    public final double Angle;

    public DrivetrainPower(double left, double right, double angle) {
        Left = left;
        Right = right;
        Angle = angle;
    }
}
