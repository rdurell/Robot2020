/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Tests;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Assert;
import org.junit.Test;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.core238.autonomous.*;


/**
 * Add your docs here.
 */
public class JsonTests {
    public static void main(String[] args) {

    }

    @Test
    public void AutonomousModeReaderTest() throws JsonMappingException, JsonProcessingException {
        /*
         * [ { "name": "Some auto mode", "commands": [ { "name":
         * "DriveStraightNavBoard", "parameters": [ "1", "2" ] }, { "name":
         * "DriveStraightVision", "parameters": [ "3", "4" ] } ] } ]
         */

        String json = "[{\"name\":\"Some auto mode\",\"commands\":[{\"name\":\"DriveStraightNavBoard\",\"parameters\":[\"1\",\"2\"]},{\"name\":\"DriveStraightVision\",\"parameters\":[\"3\",\"4\"]}]}]";
        final ObjectMapper mapper = new ObjectMapper();

        List<AutonomousModeDescriptor> modeDescriptors = new ArrayList<AutonomousModeDescriptor>();
        modeDescriptors = mapper.readValue(json, new TypeReference<List<AutonomousModeDescriptor>>() {});

        System.out.println(modeDescriptors.size());

        IAutonomousModeDataSource dataSource = new JsonStringAutonomousModeDataSource(json);
        AutonomousModesReader reader = new AutonomousModesReader(dataSource);
        var modes = reader.getAutonmousModes();
        Assert.assertEquals("Incorrect number of auto modes", 1, modes.size());

        CommandGroup cg = modes.get("Some auto mode");
        System.out.println(cg.getName());
        Assert.assertNotNull(cg);

        // in a real API, you would be able to get a collection of the commands added to
        // the command group
        // Assert.assertEquals("Incorrect number of commands", 2,
        // cg.getCommands().size());
    }

    @Test
    public void AutonmousModeCommandFileGenerationTest() throws IOException {
        List<AutonomousCommandDescriptor> descriptors = new ArrayList<AutonomousCommandDescriptor>();

        Reflections reflections = new Reflections("frc.robot.commands", new TypeAnnotationsScanner(), new SubTypesScanner(false));
        var autonomousCommandClasses = reflections.getTypesAnnotatedWith(AutonomousModeAnnotation.class, true);
        
        for(var command : autonomousCommandClasses){
            var modifiers = command.getModifiers();
            var isAbstract = Modifier.isAbstract(modifiers);
            if (isAbstract){
                continue;
            }

            AutonomousModeAnnotation annotation = (AutonomousModeAnnotation)command.getAnnotation(AutonomousModeAnnotation.class);

            var descriptor = new AutonomousCommandDescriptor();
            descriptor.CommandName = command.getSimpleName();
            descriptor.ParameterNames = annotation.parameterNames();
            descriptors.add(descriptor);
        }

        ObjectMapper mapper = new ObjectMapper();

        String json = mapper.writeValueAsString(descriptors);
        System.out.println(json);

        //optionally output this to disk so it can be imported to auto mode builder app
        mapper.writeValue(Paths.get("commands.json").toFile(), descriptors);
 
    }
}
