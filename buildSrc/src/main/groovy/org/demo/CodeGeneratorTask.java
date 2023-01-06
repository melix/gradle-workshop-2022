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
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public abstract class CodeGeneratorTask extends DefaultTask {
    @Input
    abstract Property<String> getMessage();

    @OutputDirectory
    abstract DirectoryProperty getOutputDirectory();

    @TaskAction
    void generateCode() throws IOException {
        File outputDir = getOutputDirectory().getAsFile().get();
        Path javaFile = outputDir.toPath().resolve("org/demo/Constants.java");
        Files.createDirectories(javaFile.getParent());
        Files.write(javaFile, List.of(
                "package org.demo;",
                "public class Constants {",
                "    public static final String MESSAGE = \"" + getMessage().get() + "\";",
                "}"
        ));
    }
}
