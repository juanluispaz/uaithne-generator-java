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

import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import org.uaithne.generator.commons.EntityInfo;
import org.uaithne.generator.commons.ExecutorModuleInfo;
import org.uaithne.generator.commons.OperationInfo;
import org.uaithne.generator.processors.database.QueryGenerator;
import org.uaithne.generator.processors.database.sql.SqlGenerator;

public class MyBatisSqlCallOrQueryGenerator extends SqlGenerator {

    private boolean useCallForSelect;
    private boolean useCallForInsert = true;
    private boolean useCallForUpdate = true;
    private boolean useCallForDelete = true;
    private boolean useCallForGetLastInsertedId;
    private QueryGenerator callGenerator;
    private QueryGenerator queryGenerator;
    private QueryGenerator procedureGenerator;
    private ExecutorModuleInfo module;
    private String packageName;
    private String name;
    private Writer writer;

    public MyBatisSqlCallOrQueryGenerator(QueryGenerator callGenerator, QueryGenerator queryGenerator, QueryGenerator procedureGenerator) {
        this.callGenerator = callGenerator;
        this.queryGenerator = queryGenerator;
        this.procedureGenerator = procedureGenerator;
    }

    public boolean isUseCallForSelect() {
        return useCallForSelect;
    }

    public void setUseCallForSelect(boolean useCallForSelect) {
        this.useCallForSelect = useCallForSelect;
    }

    public boolean isUseCallForInsert() {
        return useCallForInsert;
    }

    public void setUseCallForInsert(boolean useCallForInsert) {
        this.useCallForInsert = useCallForInsert;
    }

    public boolean isUseCallForUpdate() {
        return useCallForUpdate;
    }

    public void setUseCallForUpdate(boolean useCallForUpdate) {
        this.useCallForUpdate = useCallForUpdate;
    }

    public boolean isUseCallForDelete() {
        return useCallForDelete;
    }

    public void setUseCallForDelete(boolean useCallForDelete) {
        this.useCallForDelete = useCallForDelete;
    }

    public boolean isUseCallForGetLastInsertedId() {
        return useCallForGetLastInsertedId;
    }

    public void setUseCallForGetLastInsertedId(boolean useCallForGetLastInsertedId) {
        this.useCallForGetLastInsertedId = useCallForGetLastInsertedId;
    }

    public QueryGenerator getCallGenerator() {
        return callGenerator;
    }

    public void setCallGenerator(QueryGenerator callGenerator) {
        this.callGenerator = callGenerator;
    }

    public QueryGenerator getQueryGenerator() {
        return queryGenerator;
    }

    public void setQueryGenerator(QueryGenerator queryGenerator) {
        this.queryGenerator = queryGenerator;
    }

    public QueryGenerator getProcedureGenerator() {
        return procedureGenerator;
    }

    public void setProcedureGenerator(QueryGenerator procedureGenerator) {
        this.procedureGenerator = procedureGenerator;
    }

    @Override
    public void setProcessingEnv(ProcessingEnvironment processingEnv) {
        super.setProcessingEnv(processingEnv);
        if (callGenerator != null) {
            callGenerator.setProcessingEnv(processingEnv);
        }
        if (queryGenerator != null) {
            queryGenerator.setProcessingEnv(processingEnv);
        }
        if (procedureGenerator != null) {
            procedureGenerator.setProcessingEnv(processingEnv);
        }
    }

    @Override
    public void setUseAutoIncrementId(boolean useAutoIncrementId) {
        super.setUseAutoIncrementId(useAutoIncrementId);
        if (callGenerator != null) {
            callGenerator.setUseAutoIncrementId(useAutoIncrementId);
        }
        if (queryGenerator != null) {
            queryGenerator.setUseAutoIncrementId(useAutoIncrementId);
        }
        if (procedureGenerator != null) {
            procedureGenerator.setUseAutoIncrementId(useAutoIncrementId);
        }
    }

    @Override
    public void setSequenceRegex(String sequenceRegex) {
        super.setSequenceRegex(sequenceRegex);
        if (callGenerator != null) {
            callGenerator.setSequenceRegex(sequenceRegex);
        }
        if (queryGenerator != null) {
            queryGenerator.setSequenceRegex(sequenceRegex);
        }
        if (procedureGenerator != null) {
            procedureGenerator.setSequenceRegex(sequenceRegex);
        }
    }

    @Override
    public void setSequenceReplacement(String sequenceReplacement) {
        super.setSequenceReplacement(sequenceReplacement);
        if (callGenerator != null) {
            callGenerator.setSequenceReplacement(sequenceReplacement);
        }
        if (queryGenerator != null) {
            queryGenerator.setSequenceReplacement(sequenceReplacement);
        }
        if (procedureGenerator != null) {
            procedureGenerator.setSequenceReplacement(sequenceReplacement);
        }
    }

    @Override
    public void begin(ExecutorModuleInfo module, String packageName, String name) {
        super.begin(module, packageName, name);
        if (callGenerator != null) {
            callGenerator.begin(module, packageName, name);
        }
        if (queryGenerator != null) {
            queryGenerator.begin(module, packageName, name);
        }
        if (procedureGenerator != null) {
            procedureGenerator.begin(module, packageName, name);
        }
        this.module = module;
        this.packageName = packageName;
        this.name = name;
    }

    @Override
    public void end() {
        super.end();
        if (callGenerator != null) {
            callGenerator.end();
        }
        if (queryGenerator != null) {
            queryGenerator.end();
        }
        if (procedureGenerator != null) {
            procedureGenerator.end();
        }
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(MyBatisSqlCallOrQueryGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.module = null;
        this.packageName = null;
        this.name = null;
        this.writer = null;
    }

    private void initSqlFile() {
        if (writer != null) {
            return;
        }
        try {
            FileObject fo = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, packageName, name + ".sql", module.getElement());
            writer = fo.openWriter();
        } catch (IOException ex) {
            Logger.getLogger(MyBatisSqlCallOrQueryGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void write(String[] lines) {
        try {
            initSqlFile();
            if (writer != null && lines != null) {
                for (String line : lines) {
                    writer.write(line);
                    writer.write("\n");
                }
                writer.write("\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(MyBatisSqlCallOrQueryGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String[] getSelectManyQuery(OperationInfo operation) {
        if (useCallForSelect) {
            String[] procedure = procedureGenerator.getSelectManyQuery(operation);
            write(procedure);
            return callGenerator.getSelectManyQuery(operation);
        } else {
            return queryGenerator.getSelectManyQuery(operation);
        }
    }

    @Override
    public String[] getSelectOneQuery(OperationInfo operation) {
        if (useCallForSelect) {
            String[] procedure = procedureGenerator.getSelectOneQuery(operation);
            write(procedure);
            return callGenerator.getSelectOneQuery(operation);
        } else {
            return queryGenerator.getSelectOneQuery(operation);
        }
    }

    @Override
    public String[] getSelectPageCountQuery(OperationInfo operation) {
        if (useCallForSelect) {
            String[] procedure = procedureGenerator.getSelectPageCountQuery(operation);
            write(procedure);
            return callGenerator.getSelectPageCountQuery(operation);
        } else {
            return queryGenerator.getSelectPageCountQuery(operation);
        }
    }

    @Override
    public String[] getSelectPageQuery(OperationInfo operation) {
        if (useCallForSelect) {
            String[] procedure = procedureGenerator.getSelectPageQuery(operation);
            write(procedure);
            return callGenerator.getSelectPageQuery(operation);
        } else {
            return queryGenerator.getSelectPageQuery(operation);
        }
    }

    @Override
    public String[] getEntityDeleteByIdQuery(EntityInfo entity, OperationInfo operation) {
        if (useCallForDelete) {
            String[] procedure = procedureGenerator.getEntityDeleteByIdQuery(entity, operation);
            write(procedure);
            return callGenerator.getEntityDeleteByIdQuery(entity, operation);
        } else {
            return queryGenerator.getEntityDeleteByIdQuery(entity, operation);
        }
    }

    @Override
    public String[] getEntityInsertQuery(EntityInfo entity, OperationInfo operation) {
        if (useCallForInsert) {
            String[] procedure = procedureGenerator.getEntityInsertQuery(entity, operation);
            write(procedure);
            return callGenerator.getEntityInsertQuery(entity, operation);
        } else {
            return queryGenerator.getEntityInsertQuery(entity, operation);
        }
    }

    @Override
    public String[] getEntityLastInsertedIdQuery(EntityInfo entity, OperationInfo operation) {
        if (useCallForGetLastInsertedId) {
            String[] procedure = procedureGenerator.getEntityLastInsertedIdQuery(entity, operation);
            write(procedure);
            return callGenerator.getEntityLastInsertedIdQuery(entity, operation);
        } else {
            return queryGenerator.getEntityLastInsertedIdQuery(entity, operation);
        }
    }

    @Override
    public String[] getEntityMergeQuery(EntityInfo entity, OperationInfo operation) {
        if (useCallForUpdate) {
            String[] procedure = procedureGenerator.getEntityMergeQuery(entity, operation);
            write(procedure);
            return callGenerator.getEntityMergeQuery(entity, operation);
        } else {
            return queryGenerator.getEntityMergeQuery(entity, operation);
        }
    }

    @Override
    public String[] getEntitySelectByIdQuery(EntityInfo entity, OperationInfo operation) {
        if (useCallForSelect) {
            String[] procedure = procedureGenerator.getEntitySelectByIdQuery(entity, operation);
            write(procedure);
            return callGenerator.getEntitySelectByIdQuery(entity, operation);
        } else {
            return queryGenerator.getEntitySelectByIdQuery(entity, operation);
        }
    }

    @Override
    public String[] getEntityUpdateQuery(EntityInfo entity, OperationInfo operation) {
        if (useCallForUpdate) {
            String[] procedure = procedureGenerator.getEntityUpdateQuery(entity, operation);
            write(procedure);
            return callGenerator.getEntityUpdateQuery(entity, operation);
        } else {
            return queryGenerator.getEntityUpdateQuery(entity, operation);
        }
    }

    @Override
    public String[] getCustomDeleteQuery(OperationInfo operation) {
        if (useCallForDelete) {
            String[] procedure = procedureGenerator.getCustomDeleteQuery(operation);
            write(procedure);
            return callGenerator.getCustomDeleteQuery(operation);
        } else {
            return queryGenerator.getCustomDeleteQuery(operation);
        }
    }

    @Override
    public String[] getCustomInsertQuery(OperationInfo operation) {
        if (useCallForInsert) {
            String[] procedure = procedureGenerator.getCustomInsertQuery(operation);
            write(procedure);
            return callGenerator.getCustomInsertQuery(operation);
        } else {
            return queryGenerator.getCustomInsertQuery(operation);
        }
    }

    @Override
    public String[] getCustomUpdateQuery(OperationInfo operation) {
        if (useCallForUpdate) {
            String[] procedure = procedureGenerator.getCustomUpdateQuery(operation);
            write(procedure);
            return callGenerator.getCustomUpdateQuery(operation);
        } else {
            return queryGenerator.getCustomUpdateQuery(operation);
        }
    }
}