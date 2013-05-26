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
package org.uaithne.generator.templates;

import java.io.IOException;
import java.util.ArrayList;
import org.uaithne.generator.commons.OperationInfo;

public class GwtAccesorTemplate extends ClassTemplate {

    private ArrayList<OperationInfo> operations;

    public ArrayList<OperationInfo> getOperations() {
        return operations;
    }

    public void setOperations(ArrayList<OperationInfo> operations) {
        this.operations = operations;
    }

    public GwtAccesorTemplate(ArrayList<OperationInfo> operations, String packageName, String className, String sharedPackageDot, String sharedGwtPackageDot) {
        setPackageName(packageName);
        addImport("com.google.gwt.user.client.rpc.AsyncCallback", packageName);
        for (OperationInfo operation : operations) {
            operation.appendReturnImports(packageName, getImport());
        }
        addImport(sharedGwtPackageDot + "client.AsyncExecutorGroup", packageName);
        addImport(sharedGwtPackageDot + "shared.rpc.AwaitGwtOperation", packageName);
        addImport(sharedGwtPackageDot + "shared.rpc.AwaitGwtResult", packageName);
        addImport(sharedGwtPackageDot + "shared.rpc.CombinedGwtOperation", packageName);
        addImport(sharedGwtPackageDot + "shared.rpc.CombinedGwtResult", packageName);
        addImport(sharedPackageDot + "Operation", packageName);
        setClassName(className);
        addImplement("AsyncExecutorGroup");
        this.operations = operations;
    }

    @Override
    protected void writeContent(Appendable appender) throws IOException {
        appender.append("    private AsyncExecutorGroup chainedExecutorGroup;\n"
                + "\n"
                + "    @Override\n"
                + "    public ").append(OPERATION_BASE_DEFINITION).append("\n"
                + "        void execute(final OPERATION operation, final AsyncCallback<RESULT> asyncCallback) {\n"
                + "        chainedExecutorGroup.execute(operation, asyncCallback);\n"
                + "    }\n"
                + "\n"
                + "    public <RESULT extends CombinedGwtResult> void execute(CombinedGwtOperation<RESULT> operation,  AsyncCallback<RESULT> asyncCallback) {\n"
                + "        chainedExecutorGroup.execute(operation, asyncCallback);\n"
                + "    }\n"
                + "\n"
                + "    public void execute(AwaitGwtOperation operation,  AsyncCallback<AwaitGwtResult> asyncCallback) {\n"
                + "        chainedExecutorGroup.execute(operation, asyncCallback);\n"
                + "    }\n"
                + "\n");

        for (OperationInfo operation : operations) {
            appender.append("    public void execute(").append(operation.getDataType().getQualifiedName()).append(" operation, AsyncCallback<").append(operation.getReturnDataType().getSimpleName()).append("> asyncCallback) {\n"
                    + "        chainedExecutorGroup.execute(operation, asyncCallback);\n"
                    + "    }\n"
                    + "\n");
        }

        appender.append("    public AsyncExecutorGroup getChainedExecutorGroup() {\n"
                + "        return chainedExecutorGroup;\n"
                + "    }\n"
                + "\n"
                + "    public ").append(getClassName()).append("(AsyncExecutorGroup chainedExecutorGroup) {\n"
                + "        if (chainedExecutorGroup == null) {\n"
                + "            throw new IllegalArgumentException(\"chainedExecutorGroup for the ").append(getClassName()).append(" cannot be null\");\n"
                + "        }\n"
                + "        this.chainedExecutorGroup = chainedExecutorGroup;\n"
                + "    }");
    }
}
