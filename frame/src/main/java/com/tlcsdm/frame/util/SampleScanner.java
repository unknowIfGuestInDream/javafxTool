package com.tlcsdm.frame.util;

import com.tlcsdm.core.util.InterfaceScanner;
import com.tlcsdm.frame.FXSamplerProject;
import com.tlcsdm.frame.Sample;
import com.tlcsdm.frame.model.EmptySample;
import com.tlcsdm.frame.model.Project;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * All the code related to classpath scanning, etc for samples.
 */
public class SampleScanner {

    private static final Map<String, FXSamplerProject> PACKAGE_TO_PROJECT_MAP = new HashMap<>();

    static {
        System.out.println("Initialising FXSampler sample scanner...");
        System.out.println("\tDiscovering projects...");
        // find all projects on the classpath that expose a FXSamplerProject
        // service. These guys are our friends....
        ServiceLoader<FXSamplerProject> loader = ServiceLoader.load(FXSamplerProject.class);
        for (FXSamplerProject project : loader) {
            final String projectName = project.getProjectName();
            final String basePackage = project.getSampleBasePackage();
            PACKAGE_TO_PROJECT_MAP.put(basePackage, project);
            System.out
                    .println("\t\tFound project '" + projectName + "', with sample base package '" + basePackage + "'");
        }

        if (PACKAGE_TO_PROJECT_MAP.isEmpty()) {
            System.out.println("\tError: Did not find any projects!");
        }
    }

    private final Map<String, Project> projectsMap = new HashMap<>();

    /**
     * Gets the list of sample classes to load
     *
     * @return The classes
     */
    public Map<String, Project> discoverSamples() {
        Class<?>[] results = new Class[]{};

        try {
            results = InterfaceScanner.loadFromPathScanning(Sample.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Class<?> sampleClass : results) {
            if (sampleClass.isInterface()) {
                continue;
            }
            if (Modifier.isAbstract(sampleClass.getModifiers())) {
                continue;
            }
            if (sampleClass == EmptySample.class) {
                continue;
            }

            Sample sample = null;
            try {
                sample = (Sample) sampleClass.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException
                    | InvocationTargetException e) {
                e.printStackTrace();
            }
            if (sample == null || !sample.isVisible()) {
                continue;
            }

            final String packageName = sampleClass.getPackage().getName();

            for (String key : PACKAGE_TO_PROJECT_MAP.keySet()) {
                if (packageName.contains(key)) {
                    final FXSamplerProject fxSamplerProject = PACKAGE_TO_PROJECT_MAP.get(key);
                    final String prettyProjectName = fxSamplerProject.getProjectName();
                    Project project;
                    if (!projectsMap.containsKey(prettyProjectName)) {
                        project = new Project(prettyProjectName, key);
                        project.setModuleName(fxSamplerProject.getModuleName());
                        project.setWelcomePage(fxSamplerProject.getWelcomePage());
                        projectsMap.put(prettyProjectName, project);
                    } else {
                        project = projectsMap.get(prettyProjectName);
                    }
                    project.addSample(packageName, sample);
                }
            }
        }
        return projectsMap;
    }

}
