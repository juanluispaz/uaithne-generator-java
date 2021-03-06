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
package org.uaithne.generator.processors.database.providers.oracle;

import java.util.ArrayList;
import org.uaithne.annotations.sql.CustomSqlQuery;
import org.uaithne.generator.commons.EntityInfo;
import org.uaithne.generator.commons.FieldInfo;
import org.uaithne.generator.commons.OperationInfo;
import org.uaithne.generator.processors.database.myBatis.MyBatisSqlQueryGenerator;

public class MyBatisOracle10SqlQueryGenerator extends MyBatisSqlQueryGenerator {
    
    private boolean selectOneRowRequireEnvolve;
    
    @Override
    public String currentSqlDate() {
        return "current_timestamp";
    }
    
    @Override
    public String falseValue() {
        return "0";
    }
    
    @Override
    public String trueValue() {
        return "1";
    }
    
    @Override
    public String[] getIdSequenceNextValue(EntityInfo entity, FieldInfo field) {
        return new String[] {getIdSequenceName(entity, field) + ".nextval"};
    }
    
    @Override
    public String[] getIdSequenceCurrentValue(EntityInfo entity, FieldInfo field) {
        return new String[] {"select " + getIdSequenceName(entity, field) + ".currval from dual"};
    }

    @Override
    public String[] envolveInSelectPage(String[] query) {
        String[] r = new String[query.length + 9];
        r[0] = "<if test='offset != null or maxRowNumber != null'> select * from (</if>";
        r[1] = "";
        System.arraycopy(query, 0, r, 2, query.length);
        r[r.length - 7] = "";
        r[r.length - 6] = "<if test='offset != null or maxRowNumber != null'> ) t";
        r[r.length - 5] = "    <where>";
        r[r.length - 4] = "        <if test='offset != null'>rownumber__ &gt; #{offset,jdbcType=NUMERIC}</if>";
        r[r.length - 3] = "        <if test='maxRowNumber != null'>and rownumber__ &lt;= #{maxRowNumber,jdbcType=NUMERIC}</if>";
        r[r.length - 2] = "    </where>";
        r[r.length - 1] = "</if>";
        return r;
    }

    @Override
    public void appendSelectPageBeforeSelect(StringBuilder result, ArrayList<FieldInfo> orderBys, CustomSqlQuery customQuery) {
    }

    @Override
    public boolean appendSelectPageAfterWhere(StringBuilder result, boolean requireAnd, ArrayList<FieldInfo> orderBys, CustomSqlQuery customQuery) {
        return false;
    }
    
    @Override
    public void appendSelectPageAfterOrderBy(StringBuilder result, ArrayList<FieldInfo> orderBys, CustomSqlQuery customQuery) {
    }

    @Override
    public String[] completeQuery(String[] query, OperationInfo operation, boolean count, boolean selectPage, boolean ignoreCustomQueryWhenCount) {
        selectOneRowRequireEnvolve = false;
        return super.completeQuery(query, operation, count, selectPage, ignoreCustomQueryWhenCount);
    }
    
    @Override
    public String[] envolveInSelectOneRow(String[] query) {
        if (!selectOneRowRequireEnvolve) {
            return query;
        }
        
        selectOneRowRequireEnvolve = false;
        
        String[] r = new String[query.length + 6];
        r[0] = "select * from (";
        r[1] = "";
        System.arraycopy(query, 0, r, 2, query.length);
        r[r.length - 4] = "";
        r[r.length - 3] = ") t";
        r[r.length - 2] = "where";
        r[r.length - 1] = "    rownumber__ = 1";
        return r;
    }

    @Override
    public void appendSelectOneRowBeforeSelect(StringBuilder result, ArrayList<FieldInfo> orderBys, CustomSqlQuery customQuery) {
    }

    @Override
    public boolean appendSelectOneRowAfterWhere(StringBuilder result, boolean requireAnd, ArrayList<FieldInfo> orderBys, CustomSqlQuery customQuery) {
        if (selectOneRowRequireEnvolve) {
            return false;
        }
        
        if (requireAnd) {
            result.append("\n    and ");
        } else {
            result.append("    ");
        }
        result.append("rownum = 1");
        return true;
    }

    @Override
    public void appendSelectOneRowAfterOrderBy(StringBuilder result, ArrayList<FieldInfo> orderBys, CustomSqlQuery customQuery) {
    }

    @Override
    public void appendOrderByForSelectOneRow(StringBuilder result, ArrayList<FieldInfo> orderBys, CustomSqlQuery customQuery) {
    }

    @Override
    public void appendOrderByAfterSelectForSelectOneRow(StringBuilder result, ArrayList<FieldInfo> orderBys, CustomSqlQuery customQuery) {
        if (customQuery != null) {
            if (hasQueryValue(customQuery.orderBy())) {
                result.append(",\n    row_number() over (order by ");
                appendToQuerySp(result, customQuery.beforeOrderByExpression());
                appendToQuerySp(result, customQuery.orderBy());
                appendToQuerySp(result, customQuery.afterOrderByExpression());
                result.append(") as rownumber__");
                selectOneRowRequireEnvolve = true;
                return;
            } else if (hasQueryValue(customQuery.beforeOrderByExpression()) || hasQueryValue(customQuery.afterOrderByExpression())) {
                result.append(",\n    row_number() over (order by ");
                appendToQuerySp(result, customQuery.beforeOrderByExpression());
                appendOrderByContent(result, orderBys, "", "", ", ", "", "");
                appendToQuerySp(result, customQuery.afterOrderByExpression());
                result.append(") as rownumber__");
                selectOneRowRequireEnvolve = true;
                return;
            }
        }
        
        if (!orderBys.isEmpty()) {
            result.append(",\n    row_number()");
            appendOrderByContent(result, orderBys, "", "", ", ", " over (order by ", ")");
            result.append(" as rownumber__");
            selectOneRowRequireEnvolve = true;
        }
    }

    @Override
    public boolean handleVersionFieldOnInsert() {
        return false;
    }

    @Override
    public boolean handleVersionFieldOnUpdate() {
        return false;
    }

    @Override
    public boolean handleVersionFieldOnDelete() {
        return false;
    }

    @Override
    public void appendInitialVersionValue(StringBuilder result, EntityInfo entity, FieldInfo field) {
    }

    @Override
    public void appendNextVersionValue(StringBuilder result, EntityInfo entity, FieldInfo field) {
    }

    @Override
    public void appendOrderByForSelectPage(StringBuilder result, ArrayList<FieldInfo> orderBys, CustomSqlQuery customQuery) {
    }

    @Override
    public void appendOrderByAfterSelectForSelectPage(StringBuilder result, ArrayList<FieldInfo> orderBys, CustomSqlQuery customQuery) {
        if (customQuery != null) {
            if (hasQueryValue(customQuery.orderBy())) {
                result.append(",\n    row_number() over (order by ");
                appendToQuerySp(result, customQuery.beforeOrderByExpression());
                appendToQuerySp(result, customQuery.orderBy());
                appendToQuerySp(result, customQuery.afterOrderByExpression());
                result.append(") as rownumber__");
                return;
            } else if (hasQueryValue(customQuery.beforeOrderByExpression()) || hasQueryValue(customQuery.afterOrderByExpression())) {
                result.append(",\n    row_number() over (order by ");
                appendToQuerySp(result, customQuery.beforeOrderByExpression());
                appendOrderByContent(result, orderBys, "", "", ", ", "", "");
                appendToQuerySp(result, customQuery.afterOrderByExpression());
                result.append(") as rownumber__");
                return;
            }
        }
        
        result.append(",\n    row_number()");
        appendOrderByContent(result, orderBys, "", "", ", ", " over (order by ", ")");
        result.append(" as rownumber__");
    }
    
    public void appendToQuerySp(StringBuilder query, String[] array) {
        if (!hasQueryValue(array)) {
            return;
        }
        for (int i = 0; i < array.length; i++) {
            String s = array[i];
            query.append(s).append(" ");
        }
    }
    
}
