/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.drivetrainpower;

import frc.robot.Robot;
import frc.robot.subsystems.Vision;

/**
 * Add your docs here.
 */
public class VisionPowerSource implements IDrivetrainPowerSource {
    private Vision vision;

    public VisionPowerSource() {
        vision = Robot.vision;
    }

    @Override
    public DrivetrainPower Get() {
        int distance = vision.getDistanceToTarget();
        double angle = vision.getYaw();

        // do the math to calculate left/right power for turning and moving towards target
        double power = distance*.1;

        return new DrivetrainPower(power, power, angle);
    }
}
