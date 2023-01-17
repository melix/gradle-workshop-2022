/*
 * Copyright 2003-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.demo;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.FileSystemOperations;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public abstract class HelloWorldGenerator extends DefaultTask {

    @Input
    public abstract Property<String> getPackageName();

    @Input
    public abstract Property<String> getMessage();

    @OutputDirectory
    public abstract DirectoryProperty getOutputDirectory();

    @Inject
    protected abstract FileSystemOperations getFileOperations();

    @TaskAction
    public void generate() throws IOException {
        File outputDir = getOutputDirectory().getAsFile().get();
        getFileOperations().delete(spec -> spec.delete(outputDir));
        File packageDir = new File(outputDir, getPackageName().get().replace('.', '/'));
        getLogger().lifecycle("Generating sources in {}", packageDir);
        Files.createDirectories(packageDir.toPath());
        String message = getMessage().get();
        File helloWorldFile = new File(packageDir, "Constants.java");
        Files.write(helloWorldFile.toPath(), List.of(
                "package " + getPackageName().get() + ";",
           "",
           "public class Constants {",
           "    public static final String GREETING = \"" + message + "\";",
           "}"
        ));
    }
}
