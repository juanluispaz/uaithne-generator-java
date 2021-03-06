/*
 * Copyright 2012 and beyond, Juan Luis Paz
 *
 * This file is part of Uaithne.
 *
 * Uaithne is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Uaithne is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Uaithne. If not, see <http://www.gnu.org/licenses/>.
 */
package org.uaithne.generator.processors.database.myBatis;

import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import org.uaithne.annotations.myBatis.SharedMyBatisLibrary;
import org.uaithne.generator.commons.DataTypeInfo;
import org.uaithne.generator.commons.GenerationInfo;
import org.uaithne.generator.commons.NamesGenerator;
import org.uaithne.generator.commons.TemplateProcessor;
import org.uaithne.generator.templates.shared.myBatis.ApplicationParameterDriverTemplate;
import org.uaithne.generator.templates.shared.myBatis.ManagedSqlSessionExecutorGroupTemplate;
import org.uaithne.generator.templates.shared.myBatis.ManagedSqlSessionProviderTemplate;
import org.uaithne.generator.templates.shared.myBatis.RetainIdPluginTemplate;
import org.uaithne.generator.templates.shared.myBatis.SqlSessionManagementInterceptor_WithLamdas;
import org.uaithne.generator.templates.shared.myBatis.SqlSessionProviderTemplate;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedAnnotationTypes("org.uaithne.annotations.myBatis.SharedMyBatisLibrary")
public class SharedMyBatisLibraryProcessor extends TemplateProcessor {

    @Override
    public boolean doProcess(Set<? extends TypeElement> set, RoundEnvironment re) {
        for (Element element : re.getElementsAnnotatedWith(SharedMyBatisLibrary.class)) {
            if (element.getKind() == ElementKind.CLASS) {
                TypeElement classElement = (TypeElement) element;
                String packageName = NamesGenerator.createPackageNameFromFullName(classElement.getQualifiedName());

                boolean includeRetainIdPlugin = true;
                boolean includeApplicationParameterDriver = true;
                SharedMyBatisLibrary sl = element.getAnnotation(SharedMyBatisLibrary.class);
                if (sl != null) {
                    if (packageName == null || packageName.isEmpty()) {
                        DataTypeInfo.updateSharedMyBatisPackage("");
                    } else {
                        DataTypeInfo.updateSharedMyBatisPackage(packageName);
                    }
                    includeRetainIdPlugin = sl.includeRetainIdPlugin();
                    includeApplicationParameterDriver = sl.includeApplicationParameterDriver();
                    if (!sl.generate()) {
                        continue;
                    }
                }

                GenerationInfo generationInfo = getGenerationInfo();
                processClassTemplate(new SqlSessionProviderTemplate(packageName), element);
                if (generationInfo.getContextParameterType() == null) {
                    processClassTemplate(new ManagedSqlSessionProviderTemplate(packageName), element);
                    if (generationInfo.isLambdasEnabled()) {
                        processClassTemplate(new SqlSessionManagementInterceptor_WithLamdas(packageName), element);
                    } else {
                        processClassTemplate(new ManagedSqlSessionExecutorGroupTemplate(packageName), element);
                    }
                }
                if (includeRetainIdPlugin) {
                    processClassTemplate(new RetainIdPluginTemplate(packageName), element);
                }
                if (includeApplicationParameterDriver) {
                    processClassTemplate(new ApplicationParameterDriverTemplate(packageName), element);
                }
            }
        }
        return true; // no further processing of this annotation type
    }

}
