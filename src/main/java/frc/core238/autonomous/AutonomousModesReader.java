/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.core238.autonomous;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.core238.Logger;
import frc.robot.commands.BaseCommand;
import frc.robot.commands.IAutonomousCommand;

/**
 * Add your docs here.
 */
public class AutonomousModesReader {
    private final IAutonomousModeDataSource dataSource;

    public AutonomousModesReader(final IAutonomousModeDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public HashMap<String, CommandGroup> getAutonmousModes() {
        final String classPath = "frc.robot.commands.";
        final HashMap<String, CommandGroup> autoModes = new HashMap<>();

        final List<AutonomousModeDescriptor> modeDescriptors = getAutonomousModeDescriptors();

        modeDescriptors.forEach(modeDescriptor -> {
            final String name = modeDescriptor.getName();
            final CommandGroup commands = new CommandGroup();

            modeDescriptor.getCommands().forEach(commandDescriptor -> {

                final String commandName = commandDescriptor.getName();
                final String className = classPath + commandName;

                // create a command object
                IAutonomousCommand autoCommand = null;
                try {
                    autoCommand = (IAutonomousCommand) Class.forName(className).getConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException | NoSuchMethodException | SecurityException
                        | ClassNotFoundException e) {
                    frc.core238.Logger
                            .Debug("AutonomousModesReader.getAutonmousModes unable to instantiate " + className);
                }

                // call set parameters
                autoCommand.setParameters(commandDescriptor.getParameters());
                autoCommand.setIsAutonomousMode(true);

                // add to list
                commands.addSequential((BaseCommand) autoCommand);
            });

            // add to dictionary
            autoModes.put(name, commands);
        });

        return autoModes;
    }

    private List<AutonomousModeDescriptor> getAutonomousModeDescriptors() {

        List<AutonomousModeDescriptor> modeDescriptors = new ArrayList<AutonomousModeDescriptor>();
        final String json = dataSource.getJson();

        if (json == null) {
            return modeDescriptors;
        }

        final ObjectMapper mapper = new ObjectMapper();

        try {
            modeDescriptors = mapper.readValue(json, new TypeReference<List<AutonomousModeDescriptor>>() {});
        } catch (JsonMappingException e) {
            Logger.Error(e.getStackTrace().toString());
        } catch (JsonProcessingException e) {
            Logger.Error(e.getStackTrace().toString());
        }
        
        return modeDescriptors;
    }
    
}
