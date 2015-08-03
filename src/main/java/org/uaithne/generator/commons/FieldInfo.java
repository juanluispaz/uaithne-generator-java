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
package org.uaithne.generator.commons;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;
import org.uaithne.annotations.*;

public class FieldInfo {

    private String[] documentation;
    private String name;
    private DataTypeInfo dataType;
    private boolean markAsOvwrride;
    private boolean markAsTransient;
    private String defaultValue;
    private VariableElement element;
    private boolean optional;
    private String mappedName;
    private boolean orderBy;
    private boolean identifier;
    private boolean identifierAutogenerated;
    private boolean setValueMark;
    private boolean ignoreWhenNull;
    private boolean insertUserMark;
    private boolean insertDateMark;
    private boolean updateUserMark;
    private boolean updateDateMark;
    private boolean deleteUserMark;
    private boolean deleteDateMark;
    private boolean deletionMark;
    private boolean versionMark;
    private boolean manually;
    private boolean manuallyProgrammatically;
    private FieldInfo related;
    private boolean deprecated;
    private boolean excludedFromConstructor;
    private boolean excludedFromToString;
    private boolean excludedFromObject;
    private String valueWhenNull;
    private final HashMap<Class<?>, Object> annotations = new HashMap<Class<?>, Object>(0);
    private ArrayList<DataTypeInfo> validationAnnotations;
    private ArrayList<DataTypeInfo> validationGroups;
    private HashMap<DataTypeInfo, DataTypeInfo> validationSubstitutions;
    private ValidationRule validationRule;
    private boolean validationAlreadyConfigured;
    private boolean hasDefaultValueWhenInsert;

    public String[] getDocumentation() {
        if (documentation == null && related != null) {
            return related.getDocumentation();
        }
        return documentation;
    }

    public void setDocumentation(String[] documentation) {
        this.documentation = documentation;
    }

    public void appendImports(String currentPackage, HashSet<String> imports) {
        dataType.appendImports(currentPackage, imports);
    }

    public DataTypeInfo getDataType() {
        return dataType;
    }

    public void setDataType(DataTypeInfo dataType) {
        this.dataType = dataType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLowerCaseName() {
        return name.toLowerCase();
    }

    public String getCapitalizedName() {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public boolean isMarkAsTransient() {
        return markAsTransient;
    }

    public void setMarkAsTransient(boolean markAsTransient) {
        this.markAsTransient = markAsTransient;
    }

    public boolean isMarkAsOvwrride() {
        return markAsOvwrride;
    }

    public void setMarkAsOvwrride(boolean markAsOvwrride) {
        this.markAsOvwrride = markAsOvwrride;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public VariableElement getElement() {
        return element;
    }

    public void setElement(VariableElement element) {
        this.element = element;
    }

    public <A extends Annotation> A getAnnotation(Class<A> type) {
        A a = getOwnAnnotation(type);
        if (a == null && related != null) {
            return related.getAnnotation(type);
        }
        return a;
    }

    public <A extends Annotation> A getOwnAnnotation(Class<A> type) {
        A annotation = (A) annotations.get(type);
        if (annotation != null) {
            return annotation;
        }
        if (annotations.containsKey(type)) {
            return null;
        }
        if (element == null) {
            annotations.put(type, null);
            return null;
        }
        annotation = element.getAnnotation(type);
        annotations.put(type, annotation);
        return annotation;
    }

    public void addAnnotation(Annotation annotation) {
        annotations.put(annotation.annotationType(), annotation);
    }

    public <A extends Annotation> void removeAnnotation(Class<A> type) {
        annotations.put(type, null);
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public String getMappedName() {
        if (mappedName == null && related != null) {
            return related.getMappedName();
        }
        return mappedName;
    }

    public boolean hasOwnMappedName() {
        return mappedName != null;
    }

    public String getMappedNameOrName() {
        String result = getMappedName();
        if (result == null || result.isEmpty()) {
            return name;
        }
        return result;
    }

    public void setMappedName(String mappedName) {
        this.mappedName = mappedName;
    }

    public boolean isOrderBy() {
        return orderBy;
    }

    public void setOrderBy(boolean orderBy) {
        this.orderBy = orderBy;
    }

    public boolean isIdentifier() {
        return identifier;
    }

    public void setIdentifier(boolean identifier) {
        this.identifier = identifier;
    }

    public boolean isIdentifierAutogenerated() {
        return identifierAutogenerated;
    }

    public void setIdentifierAutogenerated(boolean identifierAutogenerated) {
        this.identifierAutogenerated = identifierAutogenerated;
    }

    public boolean isSetValueMark() {
        if (!setValueMark) {
            if (related != null) {
                return related.isSetValueMark();
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public void setSetValueMark(boolean setValueMark) {
        this.setValueMark = setValueMark;
    }

    public boolean isIgnoreWhenNull() {
        if (!ignoreWhenNull) {
            if (related != null) {
                return related.isIgnoreWhenNull();
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public void setIgnoreWhenNull(boolean ignoreWhenNull) {
        this.ignoreWhenNull = ignoreWhenNull;
    }

    public boolean isInsertUserMark() {
        if (!insertUserMark) {
            if (related != null) {
                return related.isInsertUserMark();
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public void setInsertUserMark(boolean insertUserMark) {
        this.insertUserMark = insertUserMark;
    }

    public boolean isInsertDateMark() {
        if (!insertDateMark) {
            if (related != null) {
                return related.isInsertDateMark();
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public void setInsertDateMark(boolean insertDateMark) {
        this.insertDateMark = insertDateMark;
    }

    public boolean isUpdateUserMark() {
        if (!updateUserMark) {
            if (related != null) {
                return related.isUpdateUserMark();
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public void setUpdateUserMark(boolean updateUserMark) {
        this.updateUserMark = updateUserMark;
    }

    public boolean isUpdateDateMark() {
        if (!updateDateMark) {
            if (related != null) {
                return related.isUpdateDateMark();
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public void setUpdateDateMark(boolean updateDateMark) {
        this.updateDateMark = updateDateMark;
    }

    public boolean isDeleteUserMark() {
        if (!deleteUserMark) {
            if (related != null) {
                return related.isDeleteUserMark();
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public void setDeleteUserMark(boolean deleteUserMark) {
        this.deleteUserMark = deleteUserMark;
    }

    public boolean isDeleteDateMark() {
        if (!deleteDateMark) {
            if (related != null) {
                return related.isDeleteDateMark();
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public void setDeleteDateMark(boolean deleteDateMark) {
        this.deleteDateMark = deleteDateMark;
    }

    public boolean isDeletionMark() {
        if (!deletionMark) {
            if (related != null) {
                return related.isDeletionMark();
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public void setDeletionMark(boolean deletionMark) {
        this.deletionMark = deletionMark;
    }

    public boolean isVersionMark() {
        if (!versionMark) {
            if (related != null) {
                return related.isVersionMark();
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public void setVersionMark(boolean versionMark) {
        this.versionMark = versionMark;
    }

    public String generateEqualsRule() {
        return dataType.generateEqualsRule(name);
    }

    public String generateHashCodeRule() {
        return dataType.generateHashCodeRule(name);
    }

    public boolean isManually() {
        if (!manually) {
            if (related != null) {
                return related.isManually();
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public void setManually(boolean manually) {
        this.manually = manually;
    }

    public boolean isManuallyProgrammatically() {
        if (!manuallyProgrammatically) {
            if (related != null) {
                return related.isManuallyProgrammatically();
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public void setManuallyProgrammatically(boolean manuallyProgrammatically) {
        this.manuallyProgrammatically = manuallyProgrammatically;
    }

    public FieldInfo getRelated() {
        return related;
    }

    public void setRelated(FieldInfo related) {
        this.related = related;
    }

    public boolean isDeprecated() {
        return deprecated;
    }

    public void setDeprecated(boolean deprecated) {
        this.deprecated = deprecated;
    }

    public boolean isExcludedFromConstructor() {
        if (isExcludedFromObject()) {
            return true;
        }
        return excludedFromConstructor;
    }

    public void setExcludedFromConstructor(boolean excludedFromConstructor) {
        this.excludedFromConstructor = excludedFromConstructor;
    }

    public boolean isExcludedFromToString() {
        if (!excludedFromToString) {
            if (related != null) {
                return related.isExcludedFromToString();
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public void setExcludedFromToString(boolean excludedFromToString) {
        this.excludedFromToString = excludedFromToString;
    }

    public boolean isExcludedFromObject() {
        if (!excludedFromObject) {
            if (related != null) {
                return related.isExcludedFromObject();
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public void setExcludedFromObject(boolean excludedFromObject) {
        this.excludedFromObject = excludedFromObject;
    }

    public String getValueWhenNull() {
        if (valueWhenNull == null) {
            if (related != null) {
                return related.getValueWhenNull();
            } else {
                return null;
            }
        } else {
            return valueWhenNull;
        }
    }

    public void setValueWhenNull(String valueWhenNull) {
        this.valueWhenNull = valueWhenNull;
    }

    public ArrayList<DataTypeInfo> getValidationAnnotations() {
        return validationAnnotations;
    }

    public void setValidationAnnotations(ArrayList<DataTypeInfo> validationAnnotations) {
        this.validationAnnotations = validationAnnotations;
    }

    public ArrayList<DataTypeInfo> getValidationGroups() {
        return validationGroups;
    }

    public void setValidationGroups(ArrayList<DataTypeInfo> validationGroups) {
        this.validationGroups = validationGroups;
    }

    public HashMap<DataTypeInfo, DataTypeInfo> getValidationSubstitutions() {
        return validationSubstitutions;
    }

    public void setValidationSubstitutions(HashMap<DataTypeInfo, DataTypeInfo> validationSubstitutions) {
        this.validationSubstitutions = validationSubstitutions;
    }

    public ValidationRule getValidationRule() {
        return validationRule;
    }

    public void setValidationRule(ValidationRule validationRule) {
        this.validationRule = validationRule;
    }

    public boolean isValidationAlreadyConfigured() {
        return validationAlreadyConfigured;
    }

    public void setValidationAlreadyConfigured(boolean validationAlreadyConfigured) {
        this.validationAlreadyConfigured = validationAlreadyConfigured;
    }

    public boolean hasDefaultValueWhenInsert() {
        if (!hasDefaultValueWhenInsert) {
            if (related != null) {
                return related.hasDefaultValueWhenInsert();
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public void setHasDefaultValueWhenInsert(boolean hasDefaultValueWhenInsert) {
        this.hasDefaultValueWhenInsert = hasDefaultValueWhenInsert;
    }
    
    public void ensureValidationsInfo(GenerationInfo generationInfo) {
        if (validationAlreadyConfigured) {
        } else if (identifier) {
            if (identifierAutogenerated || insertDateMark) {
                if (dataType.isPrimitive()) {
                    validationAnnotations = generationInfo.getValidationConfigurations().get(AnnotationConfigurationKeys.ID_PRIMITIVE);
                    validationSubstitutions = generationInfo.getValidationSubstitutions().get(AnnotationConfigurationKeys.ID_PRIMITIVE);
                } else if (dataType.isString()) {
                    validationAnnotations = generationInfo.getValidationConfigurations().get(AnnotationConfigurationKeys.ID_STRING);
                    validationSubstitutions = generationInfo.getValidationSubstitutions().get(AnnotationConfigurationKeys.ID_STRING);
                } else {
                    validationAnnotations = generationInfo.getValidationConfigurations().get(AnnotationConfigurationKeys.ID);
                    validationSubstitutions = generationInfo.getValidationSubstitutions().get(AnnotationConfigurationKeys.ID);
                }
                validationGroups = generationInfo.getValidationConfigurations().get(AnnotationConfigurationKeys.ID_WHEN_GROUP);
            } else {
                if (dataType.isPrimitive()) {
                    validationAnnotations = generationInfo.getValidationConfigurations().get(AnnotationConfigurationKeys.MANDATORY_PRIMITIVE);
                    validationSubstitutions = generationInfo.getValidationSubstitutions().get(AnnotationConfigurationKeys.MANDATORY_PRIMITIVE);
                } else if (dataType.isString()) {
                    validationAnnotations = generationInfo.getValidationConfigurations().get(AnnotationConfigurationKeys.MANDATORY_STRING);
                    validationSubstitutions = generationInfo.getValidationSubstitutions().get(AnnotationConfigurationKeys.MANDATORY_STRING);
                } else {
                    validationAnnotations = generationInfo.getValidationConfigurations().get(AnnotationConfigurationKeys.MANDATORY);
                    validationSubstitutions = generationInfo.getValidationSubstitutions().get(AnnotationConfigurationKeys.MANDATORY);
                }
                validationGroups = generationInfo.getValidationConfigurations().get(AnnotationConfigurationKeys.MANDATORY_WHEN_GROUP);
            }
        } else if (optional) {
            if (dataType.isString()) {
                validationAnnotations = generationInfo.getValidationConfigurations().get(AnnotationConfigurationKeys.OPTIONAL_STRING);
                validationSubstitutions = generationInfo.getValidationSubstitutions().get(AnnotationConfigurationKeys.OPTIONAL_STRING);
            } else {
                validationAnnotations = generationInfo.getValidationConfigurations().get(AnnotationConfigurationKeys.OPTIONAL);
                validationSubstitutions = generationInfo.getValidationSubstitutions().get(AnnotationConfigurationKeys.OPTIONAL);
            }
            validationGroups = generationInfo.getValidationConfigurations().get(AnnotationConfigurationKeys.OPTIONAL_WHEN_GROUP);
        } else if (hasDefaultValueWhenInsert) {
            if (dataType.isString()) {
                validationAnnotations = generationInfo.getValidationConfigurations().get(AnnotationConfigurationKeys.MANDATORY_WITH_DEFAULT_VALUE_WHEN_INSERT_STRING);
                validationSubstitutions = generationInfo.getValidationSubstitutions().get(AnnotationConfigurationKeys.MANDATORY_WITH_DEFAULT_VALUE_WHEN_INSERT_STRING);
            } else {
                validationAnnotations = generationInfo.getValidationConfigurations().get(AnnotationConfigurationKeys.MANDATORY_WITH_DEFAULT_VALUE_WHEN_INSERT);
                validationSubstitutions = generationInfo.getValidationSubstitutions().get(AnnotationConfigurationKeys.MANDATORY_WITH_DEFAULT_VALUE_WHEN_INSERT);
            }
            validationGroups = generationInfo.getValidationConfigurations().get(AnnotationConfigurationKeys.MANDATORY_WITH_DEFAULT_VALUE_WHEN_INSERT_WHEN_GROUP);
        } else {
            if (dataType.isPrimitive()) {
                validationAnnotations = generationInfo.getValidationConfigurations().get(AnnotationConfigurationKeys.MANDATORY_PRIMITIVE);
                validationSubstitutions = generationInfo.getValidationSubstitutions().get(AnnotationConfigurationKeys.MANDATORY_PRIMITIVE);
            } else if (dataType.isString()) {
                validationAnnotations = generationInfo.getValidationConfigurations().get(AnnotationConfigurationKeys.MANDATORY_STRING);
                validationSubstitutions = generationInfo.getValidationSubstitutions().get(AnnotationConfigurationKeys.MANDATORY_STRING);
            } else {
                validationAnnotations = generationInfo.getValidationConfigurations().get(AnnotationConfigurationKeys.MANDATORY);
                validationSubstitutions = generationInfo.getValidationSubstitutions().get(AnnotationConfigurationKeys.MANDATORY);
            }
            validationGroups = generationInfo.getValidationConfigurations().get(AnnotationConfigurationKeys.MANDATORY_WHEN_GROUP);
        }
        if (validationAnnotations == null) {
            validationAnnotations = new ArrayList<DataTypeInfo>(0);
        }
        if (validationGroups == null) {
            validationGroups = new ArrayList<DataTypeInfo>(0);
        }
        if (validationSubstitutions == null) {
            validationSubstitutions = new HashMap<DataTypeInfo, DataTypeInfo>(0);
        }
    }

    public FieldInfo(VariableElement element, GenerationInfo generationInfo, ProcessingEnvironment processingEnv) {
        this.element = element;
        name = element.getSimpleName().toString();
        if (element.getModifiers().contains(Modifier.TRANSIENT)) {
            markAsTransient = true;
        }
        dataType = NamesGenerator.createDataTypeFor(element.asType());
        defaultValue = Utils.getDefaultValue(dataType, element);
        orderBy = element.getAnnotation(OrderBy.class) != null;
        if (orderBy && !dataType.isString()) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "The data type of an OrderBy field must be String", element);
        }
        optional = element.getAnnotation(Optional.class) != null;
        if (optional && dataType.isPrimitive()) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Optional fields must allow null values, but a primitive data types do not allow it; you must use " + dataType.ensureBoxed().getSimpleName() + " instead of " + dataType.getSimpleName(), element);
        }
        Id id = element.getAnnotation(Id.class);
        if (id != null) {
            identifier = true;
            identifierAutogenerated = id.autogenerated();
        }
        if (identifier && optional) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Identifier fields cannot be marked as optionals", element);
        }
        hasDefaultValueWhenInsert = element.getAnnotation(HasDefaultValueWhenInsert.class) != null;
        if (hasDefaultValueWhenInsert && dataType.isPrimitive()) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Fields with default value when insert must allow null values, but a primitive data types do not allow it; you must use " + dataType.ensureBoxed().getSimpleName() + " instead of " + dataType.getSimpleName(), element);
        }
        SetValue setValue = element.getAnnotation(SetValue.class);
        if (setValue != null) {
            setValueMark = true;
            ignoreWhenNull = setValue.ignoreWhenNull();
            if (ignoreWhenNull && optional) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Only fields marked as optionals can ignore when null", element);
                ignoreWhenNull = false;
            }
        }
        setValueMark = element.getAnnotation(SetValue.class) != null;
        insertUserMark = element.getAnnotation(InsertUser.class) != null;
        insertDateMark = element.getAnnotation(InsertDate.class) != null;
        updateUserMark = element.getAnnotation(UpdateUser.class) != null;
        updateDateMark = element.getAnnotation(UpdateDate.class) != null;
        deleteUserMark = element.getAnnotation(DeleteUser.class) != null;
        deleteDateMark = element.getAnnotation(DeleteDate.class) != null;
        deletionMark = element.getAnnotation(DeletionMark.class) != null;
        versionMark = element.getAnnotation(Version.class) != null;
        MappedName mn = element.getAnnotation(MappedName.class);
        if (mn != null) {
            String[] value = mn.value();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < value.length - 1; i++) {
                sb.append(value[i]);
                sb.append(" ");
            }
            sb.append(value[value.length - 1]);
            mappedName = sb.toString();
        }
        
        Manually m = element.getAnnotation(Manually.class);
        if (m != null) {
            manually = true;
            manuallyProgrammatically = m.onlyProgrammatically();
        }
        
        ValueWhenNull whenNull = element.getAnnotation(ValueWhenNull.class);
        if (whenNull != null) {
            if (identifier) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Identifier fields cannot have value when null", element);
            } else if (!optional) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Mandatory fields cannot have value when null", element);
            } else if (insertDateMark || updateDateMark || deleteDateMark || deletionMark || versionMark) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Fields cannot have value when null and the same time generate automatically it value, like the fields annotated with: @InsertDate, @UpdateDate, @DeleteDate, @DeletionMark, @Version", element);
            } else if (ignoreWhenNull) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Fields ignored when null cannnot cannot have value when null", element);
            } else {
                valueWhenNull = whenNull.value();
                if (valueWhenNull != null && valueWhenNull.isEmpty()) {
                    valueWhenNull = null;
                }
            }
        }

        Doc doc = element.getAnnotation(Doc.class);
        if (doc != null) {
            documentation = doc.value();
        }

        deprecated = element.getAnnotation(Deprecated.class) != null;
        excludedFromConstructor = element.getAnnotation(ExcludeFromConstructor.class) != null;
        excludedFromToString = element.getAnnotation(ExcludeFromToString.class) != null;
        excludedFromObject = element.getAnnotation(ExcludeFromObject.class) != null;
        if (excludedFromObject) {
            if (defaultValue != null) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Fields excludes from the object cannot have default value", element);
                excludedFromObject = false;
            }
            if (orderBy) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Fields excludes from the object cannot use for determine the order by", element);
                excludedFromObject = false;
            }
            if (identifier) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Fields excludes from the object cannot marked as identifier", element);
                excludedFromObject = false;
            }
            if (valueWhenNull == null && !(insertDateMark || updateDateMark || deleteDateMark || deletionMark || versionMark)) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Fields excludes from the object must specify the value using the @ValueWhenNull or use with fields that generate automatically it value, like the fields annotated with: @InsertDate, @UpdateDate, @DeleteDate, @DeletionMark, @Version", element);
                excludedFromObject = false;
            }
        }
    }

    public FieldInfo(String name, DataTypeInfo dataType) {
        this.name = name;
        this.dataType = dataType;
    }

    public FieldInfo(String name, FieldInfo fieldInfo) {
        documentation = fieldInfo.documentation;
        this.name = name;
        dataType = fieldInfo.dataType;
        defaultValue = fieldInfo.defaultValue;
        element = fieldInfo.element;
        mappedName = fieldInfo.mappedName;
        orderBy = fieldInfo.orderBy;
        optional = fieldInfo.optional;
        identifier = fieldInfo.identifier;
        setValueMark = fieldInfo.setValueMark;
        ignoreWhenNull = fieldInfo.ignoreWhenNull;
        insertUserMark = fieldInfo.insertUserMark;
        insertDateMark = fieldInfo.insertDateMark;
        updateUserMark = fieldInfo.updateUserMark;
        updateDateMark = fieldInfo.updateDateMark;
        deleteUserMark = fieldInfo.deleteUserMark;
        deleteDateMark = fieldInfo.deleteDateMark;
        deletionMark = fieldInfo.deletionMark;
        versionMark = fieldInfo.versionMark;
        manually = fieldInfo.manually;
        manuallyProgrammatically = fieldInfo.manuallyProgrammatically;
        related = fieldInfo;
        deprecated = fieldInfo.deprecated;
        excludedFromConstructor = fieldInfo.excludedFromConstructor;
        excludedFromToString = fieldInfo.excludedFromToString;
        excludedFromObject = fieldInfo.excludedFromObject;
        valueWhenNull = fieldInfo.valueWhenNull;
        markAsOvwrride = fieldInfo.markAsOvwrride;
        markAsTransient = fieldInfo.markAsTransient;
        if (!name.equals(fieldInfo.name)) {
            if (getMappedName() == null) {
                mappedName = fieldInfo.name;
            }
        }
        hasDefaultValueWhenInsert = fieldInfo.hasDefaultValueWhenInsert;
    }

    public FieldInfo(FieldInfo fieldInfo) {
        documentation = fieldInfo.documentation;
        name = fieldInfo.name;
        dataType = fieldInfo.dataType;
        defaultValue = fieldInfo.defaultValue;
        element = fieldInfo.element;
        mappedName = fieldInfo.mappedName;
        orderBy = fieldInfo.orderBy;
        optional = fieldInfo.optional;
        identifier = fieldInfo.identifier;
        setValueMark = fieldInfo.setValueMark;
        ignoreWhenNull = fieldInfo.ignoreWhenNull;
        insertUserMark = fieldInfo.insertUserMark;
        insertDateMark = fieldInfo.insertDateMark;
        updateUserMark = fieldInfo.updateUserMark;
        updateDateMark = fieldInfo.updateDateMark;
        deleteUserMark = fieldInfo.deleteUserMark;
        deleteDateMark = fieldInfo.deleteDateMark;
        deletionMark = fieldInfo.deletionMark;
        versionMark = fieldInfo.versionMark;
        manually = fieldInfo.manually;
        manuallyProgrammatically = fieldInfo.manuallyProgrammatically;
        related = fieldInfo.related;
        deprecated = fieldInfo.deprecated;
        excludedFromConstructor = fieldInfo.excludedFromConstructor;
        excludedFromToString = fieldInfo.excludedFromToString;
        excludedFromObject = fieldInfo.excludedFromObject;
        valueWhenNull = fieldInfo.valueWhenNull;
        markAsOvwrride = fieldInfo.markAsOvwrride;
        markAsTransient = fieldInfo.markAsTransient;
        mappedName = fieldInfo.mappedName;
        hasDefaultValueWhenInsert = fieldInfo.hasDefaultValueWhenInsert;
    }

    public FieldInfo() {
    }
    
}
