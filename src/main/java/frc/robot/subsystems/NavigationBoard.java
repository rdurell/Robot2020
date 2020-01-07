/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.core238.Logger;


import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;

public class NavigationBoard extends Subsystem {

  public final static double NAVIGATION_TURNING_DEADZONE = 1.5;

	AHRS ahrs;
	double currentYaw;
	double currentRoll;
	double targetYaw;
	double ultrasonicDistance;
	DigitalInput lineSensorInput;

	//Ultrasonic myUltrasonic;

	final static double kCollisionThreshold_DeltaG = 0.75f;
	double last_world_linear_accel_x = 0;
	double last_world_linear_accel_y = 0;

	int count = 0;
	double start = 0;
	double current = 0;
	double elapsed = 0;

	public void init()
	{
		//lineSensorInput = new DigitalInput(CrusaderCommon.LINE_SENSOR);


		ahrs = new AHRS(SPI.Port.kMXP);
		currentYaw = ahrs.getYaw();

		currentRoll = ahrs.getRoll();
		//		myUltrasonic = new Ultrasonic(CrusaderCommon.SONIC_OUTPUT_PORT,CrusaderCommon.SONIC_INPUT_PORT);
		//		myUltrasonic.setEnabled(true);
		//		myUltrasonic.setAutomaticMode(true);

		count = 0;
		start = 0;
		current = 0;
		elapsed = 0;
	}

	public boolean getLineSensor()
	{
		return lineSensorInput.get();
	}

	/**
	 * Checks if the NavX Board is actually connected
	 * @return
	 */
	public boolean isConnected(){
		return ahrs.isConnected();
	}

	public void reset(){
		ahrs.reset();
	}

	public void zeroYaw() {
		ahrs.zeroYaw();
	}

	public double getRoll() {
		return ahrs.getRoll();
	}
	public double getYaw() {
		//TODO: add comments on what this is actually doing 
		currentYaw = ((ahrs.getYaw() % 360) + 360) % 360;
		return currentYaw;
	}

		
	// useful for turns, set yaw and query for isAtTargetYaw
	public void setTargetYaw(double targetValue) {
		targetYaw = Math.abs(targetValue);
	}

	//TODO: create and return an object to reflect these values
	public void navxValues()
	{
		Timer.delay(0.020);
	
		/*SmartDashboard.putBoolean(  "IMU_Connected",        ahrs.isConnected());
      SmartDashboard.putBoolean(  "IMU_IsCalibrating",    ahrs.isCalibrating());

		  SmartDashboard.putNumber("Gyro_X", ahrs.getRawGyroX());
		  SmartDashboard.putNumber("Gyro_Y", ahrs.getRawGyroY());
		  SmartDashboard.putNumber("Gyro_Z", ahrs.getRawGyroZ());

		  SmartDashboard.putNumber("Accel_X", ahrs.getRawAccelX());
		  SmartDashboard.putNumber("Accel_Y", ahrs.getRawAccelY());
		  SmartDashboard.putNumber("Accel_Z", ahrs.getRawAccelZ());*/

		//RM SmartDashboard.putNumber("IMU_Yaw", ahrs.getYaw());
		//RM SmartDashboard.putNumber("IMU_Pitch", ahrs.getPitch());
		//RM SmartDashboard.putNumber("IMU_Roll", ahrs.getRoll());

		//RM SmartDashboard.putNumber("Refresh Rate", ahrs.getActualUpdateRate());
		// haveWeCollided();
		//SmartDashboard.putBoolean("Are We Moving?", ahrs.isMoving());
	}

	//Tells us if we are at our target yaw
	public boolean isAtTargetYaw()
	{
		currentYaw = ahrs.getYaw();
		currentYaw = Math.abs(currentYaw);

		Logger.Debug("Navigation(): isAtTargetYaw(): Current Yaw is : "+ currentYaw);
		Logger.Debug("Navigation(): isAtTargetYaw(): Target is : "+ targetYaw);

		if((currentYaw >= (targetYaw - NAVIGATION_TURNING_DEADZONE))) 
		{
			return true;
		}
		else
		{

			return false;
		}
	}


	/**
	 * This function tells us if we are moving by using the NavX board .isMoving() function
	 * @return
	 */
	public boolean haveWeCollided()
	{

		boolean collisionDetected = false;


		double curr_world_linear_accel_x = ahrs.getWorldLinearAccelX();
		last_world_linear_accel_x = curr_world_linear_accel_x;
		double currentJerkX = curr_world_linear_accel_x - last_world_linear_accel_x;


		double curr_world_linear_accel_y = ahrs.getWorldLinearAccelY();
		last_world_linear_accel_y = curr_world_linear_accel_y;
		double currentJerkY = curr_world_linear_accel_y - last_world_linear_accel_y;


		if ( ( Math.abs(currentJerkX) > kCollisionThreshold_DeltaG ) ||
				( Math.abs(currentJerkY) > kCollisionThreshold_DeltaG) ) {

			collisionDetected = true;

			Logger.Debug("Navigation(): haveWeCollided(): CollisionDetected =" + collisionDetected);
		}
		// SmartDashboard.putNumber("cOLLISIONvaLUE x: ", currentJerkX);
		// SmartDashboard.putNumber("cOLLISIONvaLUE y: ", currentJerkY);

		// Logger.Log("Navigation(): haveWeCollided(): Collision X :" + currentJerkX);
		// Logger.Log("Navigation(): haveWeCollided(): Collision Y :" + currentJerkY);

		// SmartDashboard.putBoolean(  "CollisionDetected", collisionDetected);

		return collisionDetected;

	}


  @Override
  protected void initDefaultCommand() {
    // TODO Auto-generated method stub

  }


}
