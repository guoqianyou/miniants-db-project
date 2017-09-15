package db.dbcore.cconnection.dialect;

import core.util.Assert;
import db.DbErrorCode;
import org.apache.log.output.db.ColumnType;

import java.sql.Types;
import java.util.Map;

/**
 * 方言 接口
 * Created by guoqi on 2017/3/9.
 */
public interface Dialect {
    String aliasAggrFun(String fieldName, String aliasName);
    /**
     * 返回限定的属性名称。tableAliasName将不会进行quote。
     *
     * @param aliasName      aliasName
     * @param fieldName      fieldName
     * @param tableAliasName tableAliasName
     * @return String
     */
    String aliasFullName(String tableAliasName, String fieldName, String aliasName);

    /**
     * 将数据中限定的表名 去一个 aliasName。
     *
     * @param tableName 区分大小写，需要进行quote
     * @param aliasName 不区分大小写，不进行quote
     * @return String
     */
    String aliasTableName(String tableName, String aliasName);


    char closeStringQuote();
    /**
     * concat(false,"a","bbb") return "abb";<br>
     * concat(true,"a","'bbb'") return "a'b'"<br>
     * 需要quote的字符串需要传参前进行处理。
     *
     * @param strs 需要一次链接起来的字符串
     *             @return String
     */
    String concat(String... strs);


    boolean needLowCaseTableName();

    boolean needSetParamForCount();

    /**
     * 约束类型默认为 唯一性约束
     *
     * @param beanName    beanName
     * @param columnNames columnNames
     * @param name        name
     * @return String
     */
    default String obtainConstraintSql(String beanName, String name, String... columnNames) {
        Assert.notNullAndEmpty(columnNames, DbErrorCode.RSQL_INIT_ERROR, "约束字段列表不能为空");

        StringBuilder sb = new StringBuilder("ALTER TABLE ")
                .append(quoteKey(beanName))
                .append(" ADD CONSTRAINT ")
                .append(name)
                .append(" UNIQUE ( ");
        for (String c : columnNames) {
            Assert.notNullAndEmpty(c,DbErrorCode.RSQL_INIT_ERROR,"约束字段不能为空");
            sb.append(quoteKey(c)).append(",");
        }
        sb.deleteCharAt(sb.length() - 1).append(")");
        return sb.toString();
    }

    /**
     * 将指定的keys进行连续quote。如oracle中，【"roles"."staff"."name"】，采用 quoteKey("roles","staff","name")
     *
     * @param keys keys
     * @return String
     */
    default String quoteKey(String... keys) {
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            sb.append(openQuote()).append(key).append(closeQuote()).append(".");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }


    char openQuote();

    char closeQuote();

    String obtainCountSql(String sqlString);

    String obtainCreateSerialNumberFunctionSQL();

    StringBuilder obtainDecodeSQL(Map<String, String> map, final String decodeKey, final String displayName);

    /**
     * indexName的生成方法为：
     * beanName中的大写字母+"_"+columnNames的各首字符连接+各个列连接字符串的hash值
     * 最后全部大写
     *
     * @param beanName    beanName
     * @param columnNames columnNames
     * @return String
     */
    default String obtainIndexName(String beanName, String... columnNames) {
        StringBuilder idxname = new StringBuilder(beanName.replaceAll("[^A-Z]", "")).append("_");
        StringBuilder cols = new StringBuilder(beanName);
        for (String c : columnNames) {
            idxname.append(c.charAt(0));
            cols.append(c);
        }
        return idxname.append(String.valueOf((beanName + cols.toString()).hashCode()).replaceAll("\\-", "_")).toString().toUpperCase();
    }

    default String obtainIndexSql(String beanName, String name, String... columnNames) {

        StringBuilder sb = new StringBuilder("CREATE INDEX ")
                .append(name)
                .append(" ON ")
                .append(quoteKey(beanName))
                .append(" ( ");
        for (String c : columnNames) {
            sb.append(quoteKey(c)).append(",");
        }
        sb.deleteCharAt(sb.length() - 1).append(")");
        return sb.toString();

    }

    String obtainPagingSQL(String sqlString, long start, long end, long rowCount);

    String obtainQuerySerialNumberFunctionSQL();

    /*
     * 获取 当前方言中标示index的名字
     */
    String obtainSQLIndexNameField();

    String obtainSQLSelectIndexs(String beanName);

    /**
     * @return String
     */
    String obtainSQLSelectTableNames();

    /**
     * @param beanName beanName
     * @return String
     */
    String obtainSQLSelectTablesColumnNames(String beanName);

    /**
     * @param integer {@link Types}
     * @return String
     */
    String obtainSQLTypeString(int integer);

    String obtainSQLTypeString(ColumnType columnType);

    String obtainSelectRegex();

    char openStringQuote();

    String prepareSqlForCount(String sqlString);

    String quoteAsString(Object value);

    /**
     * 采用方言指定的符号对字段进行quot，其中表名是不做处理的，
     *
     * @param fieldName fieldName
     * @param tableName 表名，是不需要进行处理的表名可直接使用。如oracle下，即：tableName."fieldName"
     * @return String
     */
    String quoteFullName(String tableName, String fieldName);

    String renameColumnSql();

    String renameTableSql();


}
