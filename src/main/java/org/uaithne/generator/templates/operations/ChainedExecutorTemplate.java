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
package org.uaithne.generator.templates.operations;

import java.io.IOException;
import org.uaithne.generator.commons.ExecutorModuleInfo;
import org.uaithne.generator.commons.OperationInfo;

public class ChainedExecutorTemplate extends ExecutorModuleTemplate {

    public ChainedExecutorTemplate(ExecutorModuleInfo executorModule, String packageName, String sharedPackageDot) {
        setPackageName(packageName);
        executorModule.appendDefinitionImports(packageName, getImport());
        addImport(sharedPackageDot + "Operation", packageName);
        setClassName(executorModule.getNameUpper() + "ChainedExecutor");        
        addImplement(executorModule.getExecutorInterfaceName());
        setExecutorModule(executorModule);
    }

    @Override
    protected void writeContent(Appendable appender) throws IOException {
        appender.append("    private ").append(getExecutorModule().getExecutorInterfaceName()).append(" chainedExecutor;\n"
                + "\n");
        writeGetExecutorSelector(appender);
        appender.append("\n"
                + "    public ").append(getExecutorModule().getExecutorInterfaceName()).append(" getChainedExecutor() {\n"
                + "        return chainedExecutor;\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public ").append(OPERATION_BASE_DEFINITION).append(" RESULT execute(OPERATION operation) {\n"
                + "        return operation.execute(this);\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public ").append(OPERATION_BASE_DEFINITION).append(" RESULT executeOther(OPERATION operation) {\n"
                + "        return chainedExecutor.execute(operation);\n"
                + "    }\n");

        for (OperationInfo operation : getExecutorModule().getOperations()) {
            appender.append("\n"
                    + "    @Override\n");
            writeOperationMethodHeader(appender, operation);
            appender.append(" {\n"
                    + "        return chainedExecutor.execute(operation);\n"
                    + "    }\n");
        }
        appender.append("\n"
                + "    public ").append(getClassName()).append("(").append(getExecutorModule().getExecutorInterfaceName()).append(" chainedExecutor) {\n"
                + "        if (chainedExecutor == null) {\n"
                + "            throw new IllegalArgumentException(\"chainedExecutor for the ").append(getClassName()).append(" cannot be null\");\n"
                + "        }\n"
                + "        this.chainedExecutor = chainedExecutor;\n"
                + "    }");
    }
}
