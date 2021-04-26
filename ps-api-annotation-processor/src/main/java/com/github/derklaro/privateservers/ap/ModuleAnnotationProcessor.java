/*
 * This file is part of ps-system, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 - 2021 Pasqual Koschmieder and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.derklaro.privateservers.ap;

import com.github.derklaro.privateservers.api.module.annotation.Module;
import com.github.derklaro.privateservers.api.module.annotation.ModuleDescription;
import com.google.gson.Gson;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Set;

@SupportedAnnotationTypes("com.github.derklaro.privateservers.api.module.annotation.Module")
public class ModuleAnnotationProcessor extends AbstractProcessor {

  private ProcessingEnvironment processingEnvironment;
  private String foundModuleMainClass;

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    this.processingEnvironment = processingEnv;
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    if (roundEnv.processingOver() || this.foundModuleMainClass != null) {
      return false;
    }

    for (Element element : roundEnv.getElementsAnnotatedWith(Module.class)) {
      if (element.getKind() != ElementKind.CLASS) {
        continue;
      }

      Name qualifiedName = ((TypeElement) element).getQualifiedName();
      ModuleDescription description = ModuleDescription.from(element.getAnnotation(Module.class), qualifiedName.toString());
      // add the module description file to the output directory
      try {
        FileObject object = this.processingEnvironment.getFiler()
          .createResource(StandardLocation.CLASS_OUTPUT, "", "ps_module.json");
        try (Writer writer = new OutputStreamWriter(object.openOutputStream(), StandardCharsets.UTF_8)) {
          new Gson().toJson(description, writer);
        }
        // all done, save break
        this.foundModuleMainClass = qualifiedName.toString();
        break;
      } catch (IOException exception) {
        this.processingEnvironment.getMessager().printMessage(Diagnostic.Kind.ERROR, "Unable to generate plugin file", element);
      }
    }

    return false;
  }
}
