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
package org.uaithne.annotations.myBatis;

import org.uaithne.generator.processors.database.QueryGenerator;
import org.uaithne.generator.processors.database.providers.derby.MyBatisDerbySql2008QueryGenerator;
import org.uaithne.generator.processors.database.providers.mySql.MyBatisMySqlQueryGenerator;
import org.uaithne.generator.processors.database.providers.oracle.MyBatisOracle10OldSqlQueryGenerator;
import org.uaithne.generator.processors.database.providers.oracle.MyBatisOracle10PackageGenerator;
import org.uaithne.generator.processors.database.providers.oracle.MyBatisOracle10ProcedureGenerator;
import org.uaithne.generator.processors.database.providers.oracle.MyBatisOracle10SqlQueryGenerator;
import org.uaithne.generator.processors.database.providers.oracle.MyBatisOracle12SqlQueryGenerator;
import org.uaithne.generator.processors.database.providers.postgreSql.MyBatisPostgreSql2008QueryGenerator;
import org.uaithne.generator.processors.database.providers.sql.MyBatisSql2008QueryGenerator;
import org.uaithne.generator.processors.database.providers.sqlServer.MyBatisSqlServer2005SqlQueryGenerator;
import org.uaithne.generator.processors.database.providers.sqlServer.MyBatisSqlServer2012SqlQueryGenerator;

public enum MyBatisBackend {
    ORACLE_10_OLD     (MyBatisOracle10OldSqlQueryGenerator.class,   false, false ),
    ORACLE_10         (MyBatisOracle10SqlQueryGenerator.class,      false, true  ),
    ORACLE_11         (MyBatisOracle10SqlQueryGenerator.class,      false, true  ),
    ORACLE_12         (MyBatisOracle12SqlQueryGenerator.class,      false, true  ),
    ORACLE_10_PROCEDURE(MyBatisOracle10ProcedureGenerator.class,    false, true  ),
    ORACLE_10_PACKAGE (MyBatisOracle10PackageGenerator.class,       false, true  ),
    SQL_SERVER_2005   (MyBatisSqlServer2005SqlQueryGenerator.class, true,  true  ),
    SQL_SERVER_2008   (MyBatisSqlServer2005SqlQueryGenerator.class, true,  true  ),
    SQL_SERVER_2008_R2(MyBatisSqlServer2005SqlQueryGenerator.class, true,  true  ),
    SQL_SERVER_2012   (MyBatisSqlServer2012SqlQueryGenerator.class, true,  true  ),
    SQL_SERVER_2014   (MyBatisSqlServer2012SqlQueryGenerator.class, true,  true  ),
    SQL_SERVER_2016   (MyBatisSqlServer2012SqlQueryGenerator.class, true,  true  ),
    SQL_SERVER_2017   (MyBatisSqlServer2012SqlQueryGenerator.class, true,  true  ),
    POSTGRE_SQL_8_4   (MyBatisPostgreSql2008QueryGenerator.class,   true,  true  ),
    POSTGRE_SQL_9_0   (MyBatisPostgreSql2008QueryGenerator.class,   true,  true  ),
    POSTGRE_SQL_9_1   (MyBatisPostgreSql2008QueryGenerator.class,   true,  true  ),
    POSTGRE_SQL_9_2   (MyBatisPostgreSql2008QueryGenerator.class,   true,  true  ),
    POSTGRE_SQL_9_3   (MyBatisPostgreSql2008QueryGenerator.class,   true,  true  ),
    POSTGRE_SQL_9_4   (MyBatisPostgreSql2008QueryGenerator.class,   true,  true  ),
    POSTGRE_SQL_9_5   (MyBatisPostgreSql2008QueryGenerator.class,   true,  true  ),
    POSTGRE_SQL_9_6   (MyBatisPostgreSql2008QueryGenerator.class,   true,  true  ),
    POSTGRE_SQL_10_0  (MyBatisPostgreSql2008QueryGenerator.class,   true,  true  ),
    DERBY_10_6        (MyBatisDerbySql2008QueryGenerator.class,     true,  true  ),
    DERBY_10_7        (MyBatisDerbySql2008QueryGenerator.class,     true,  true  ),
    DERBY_10_8        (MyBatisDerbySql2008QueryGenerator.class,     true,  true  ),
    DERBY_10_9        (MyBatisDerbySql2008QueryGenerator.class,     true,  true  ),
    DERBY_10_10       (MyBatisDerbySql2008QueryGenerator.class,     true,  true  ),
    DERBY_10_11       (MyBatisDerbySql2008QueryGenerator.class,     true,  true  ),
    DERBY_10_12       (MyBatisDerbySql2008QueryGenerator.class,     true,  true  ),
    DERBY_10_13       (MyBatisDerbySql2008QueryGenerator.class,     true,  true  ),
    DERBY_10_14       (MyBatisDerbySql2008QueryGenerator.class,     true,  true  ),
    SQL_2008          (MyBatisSql2008QueryGenerator.class,          true,  true  ),
    MY_SQL_5_5        (MyBatisMySqlQueryGenerator.class,            true,  true  ),
    MY_SQL_5_6        (MyBatisMySqlQueryGenerator.class,            true,  true  ),
    MY_SQL_5_7        (MyBatisMySqlQueryGenerator.class,            true,  true  );
    
    private final Class generator;
    private final boolean useAutoIncrementId;
    private final boolean useGeneratedKeys;

    private MyBatisBackend(Class generator, boolean useAutoIncrementId, boolean useGeneratedKeys) {
        this.generator = generator;
        this.useAutoIncrementId = useAutoIncrementId;
        this.useGeneratedKeys = useGeneratedKeys;
    }
    
    public QueryGenerator getGenerator() {
        try {
            return (QueryGenerator) generator.newInstance();
        } catch (InstantiationException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean useAutoIncrementId() {
        return useAutoIncrementId;
    }

    public boolean useGeneratedKeys() {
        return useGeneratedKeys;
    }
    
}
