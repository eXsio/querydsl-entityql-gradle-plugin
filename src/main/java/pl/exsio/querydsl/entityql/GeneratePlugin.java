/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package pl.exsio.querydsl.entityql;

import org.gradle.api.Project;
import org.gradle.api.Plugin;
import org.gradle.api.internal.file.UnionFileCollection;
import org.gradle.api.logging.LogLevel;
import org.gradle.api.plugins.BasePlugin;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class GeneratePlugin implements Plugin<Project> {

    private static final String EXTENSION_NAME = "entityql";
    private static final String TASK_NAME = "generateModels";

    public void apply(Project project) {
        GeneratePluginExtension generatePluginExtension = project.getExtensions().create(EXTENSION_NAME,
                GeneratePluginExtension.class, project);

        GenerateTask generateTask = project.getTasks().create(TASK_NAME, GenerateTask.class);
        generateTask.setGroup(BasePlugin.BUILD_GROUP);
        generateTask.setDescription("Generates EntityQL Static Query Models.");
        generateTask.setExtension(generatePluginExtension);
        generateTask.setBaseDir(project.getBuildDir());
        project.afterEvaluate(evaluatedProject -> {
            SourceSetContainer sourceSets = (SourceSetContainer) project.getProperties().get("sourceSets");
            Set<File> paths;
            if (sourceSets != null) {
                UnionFileCollection mainClasspath = (UnionFileCollection) sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME).getRuntimeClasspath();
                paths = mainClasspath.getFiles();
            } else {
                paths = new HashSet<>();
            }
            generateTask.setSourcePaths(paths);
        });
    }
}
