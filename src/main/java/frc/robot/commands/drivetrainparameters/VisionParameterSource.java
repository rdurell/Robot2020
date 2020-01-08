/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.drivetrainparameters;

import frc.robot.Robot;
import frc.robot.subsystems.Vision;

/**
 * Add your docs here.
 */
public class VisionParameterSource implements IDrivetrainParametersSource {
    private Vision vision;

    public VisionParameterSource() {
        vision = Robot.vision;
    }

    @Override
    public DrivetrainParameters Get() {
        int distance = vision.getDistanceToTarget();
        double angle = vision.getYaw();

        // do the math to calculate left/right power for turning and moving towards target
        double power = distance*.1;

        return new DrivetrainParameters(power, power, angle);
    }
}
