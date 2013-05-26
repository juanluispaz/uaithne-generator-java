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
package org.uaithne.generator.templates.shared.gwt.client;

import java.io.IOException;
import org.uaithne.generator.templates.ClassTemplate;

public class PostOperationAsyncExecutorGroupTemplate extends ClassTemplate {

    public PostOperationAsyncExecutorGroupTemplate(String sharedGwtPackageDot, String sharedPackageDot) {
        String packageName = sharedGwtPackageDot + "client";
        setPackageName(packageName);
        addImport("com.google.gwt.user.client.rpc.AsyncCallback", sharedPackageDot);
        addImport(sharedPackageDot + "Operation", packageName);
        setClassName("PostOperationAsyncExecutorGroup");
        addImplement("AsyncExecutorGroup");
    }
    
    @Override
    protected void writeContent(Appendable appender) throws IOException {
        appender.append("    private AsyncExecutorGroup chainedExecutorGroup;\n"
                + "\n"
                + "    /**\n"
                + "     * @return the chainedExecutorGroup\n"
                + "     */\n"
                + "    public AsyncExecutorGroup getChainedExecutorGroup() {\n"
                + "        return chainedExecutorGroup;\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public ").append(OPERATION_BASE_DEFINITION).append(" void execute(final OPERATION operation, final AsyncCallback<RESULT> asyncCallback) {\n"
                + "        AsyncCallback<RESULT> postOperationAsyncCallback = new AsyncCallback<RESULT>() {\n"
                + "\n"
                + "            @Override\n"
                + "            public void onSuccess(RESULT result) {\n"
                + "                result = operation.executePostOperation(result);\n"
                + "                asyncCallback.onSuccess(result);\n"
                + "            }\n"
                + "\n"
                + "            @Override\n"
                + "            public void onFailure(Throwable caught) {\n"
                + "                asyncCallback.onFailure(caught);\n"
                + "            }\n"
                + "        };\n"
                + "        chainedExecutorGroup.execute(operation, postOperationAsyncCallback);\n"
                + "    }\n"
                + "\n"
                + "    public PostOperationAsyncExecutorGroup(AsyncExecutorGroup chainedExecutorGroup) {\n"
                + "        if (chainedExecutorGroup == null) {\n"
                + "            throw new IllegalArgumentException(\"chainedExecutorGroup for the PostOperationAsyncExecutorGroup cannot be null\");\n"
                + "        }\n"
                + "        this.chainedExecutorGroup = chainedExecutorGroup;\n"
                + "    }");
    }
    
}
