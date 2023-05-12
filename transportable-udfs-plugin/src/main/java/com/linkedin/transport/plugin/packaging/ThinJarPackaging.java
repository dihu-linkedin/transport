/**
 * Copyright 2019 LinkedIn Corporation. All rights reserved.
 * Licensed under the BSD-2 Clause license.
 * See LICENSE in the project root for license information.
 */
package com.linkedin.transport.plugin.packaging;

import com.github.jengelman.gradle.plugins.shadow.ShadowBasePlugin;
import com.google.common.collect.ImmutableList;
import com.linkedin.transport.plugin.Platform;
import java.util.List;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.component.AdhocComponentWithVariants;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.bundling.Jar;


/**
 * A {@link Packaging} class which generates a Jar containing the autogenerated code for the platform
 */
public class ThinJarPackaging implements Packaging {

  @Override
  public List<TaskProvider<? extends Task>> configurePackagingTasks(Project project, Platform platform,
      SourceSet platformSourceSet, SourceSet mainSourceSet) {
    /*
      task <platformName>ThinJar(type: Jar, dependsOn: <platformName>Classes) {
        classifier '<platformName>-thin'
        from sourceSets.<platform>.output
        from sourceSets.<platform>.resources
      }
    */

    TaskProvider<? extends Task> thinJarTask =
        project.getTasks().register(platformSourceSet.getTaskName(null, "thinJar"), Jar.class, task -> {
          task.dependsOn(project.getTasks().named(platformSourceSet.getClassesTaskName()));
          task.setDescription("Assembles a thin jar archive containing the " + platform.getName()
              + " classes to be included in the distribution");
          task.setClassifier(platform.getName() + "-thin");
          task.from(platformSourceSet.getOutput());
          task.from(platformSourceSet.getResources());
        });

    String configuration = ShadowBasePlugin.getCONFIGURATION_NAME();
    project.getArtifacts().add(configuration, thinJarTask);
    AdhocComponentWithVariants java = project.getComponents().withType(AdhocComponentWithVariants.class).getByName("java");
    java.addVariantsFromConfiguration(project.getConfigurations().getByName(configuration), v -> v.mapToOptional());

    return ImmutableList.of(thinJarTask);
  }
}
