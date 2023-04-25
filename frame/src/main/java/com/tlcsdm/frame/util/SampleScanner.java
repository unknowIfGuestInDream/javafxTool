/*
 * Copyright (c) 2019, 2023 unknowIfGuestInDream
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of unknowIfGuestInDream, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL UNKNOWIFGUESTINDREAM BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.tlcsdm.frame.util;

import com.tlcsdm.core.util.InterfaceScanner;
import com.tlcsdm.frame.Sample;
import com.tlcsdm.frame.model.EmptySample;
import com.tlcsdm.frame.model.Project;
import com.tlcsdm.frame.service.FXSamplerProject;

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
            } catch (ExceptionInInitializerError e) {
                e.printStackTrace();
                continue;
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
