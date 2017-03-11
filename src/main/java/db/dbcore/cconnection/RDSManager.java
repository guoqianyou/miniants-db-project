package db.dbcore.cconnection;

import core.util.PackageUtil;
import db.DbErrorCode;
import db.RsqlConstants;
import db.dbcore.model.Modelable;
import db.exception.RsqlConnectionException;

import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 数据库管理中心 负责 不同space 的数据库的管理
 * Created by guoqi on 2017/3/8.
 */
public class RDSManager {

    private static  RDSManager rdsManager;
    private static  Map<String,RDSSpaceConfig> spaceConfigs = new HashMap<>();

    private RDSManager(){}

    static void createSpace(RDSSpaceConfig rdsSpaceConfig) {
        (rdsManager != null ? rdsManager : new RDSManager()).create(rdsSpaceConfig);

    }

    /**
     * 获取数据库链接池
     * @param spaceName 数据库 空间名字
     * @return 返回连接池
     */
    public static Connection getLocalConnection(final String spaceName) {
        RDSSpaceConfig localSpaceConfig = getLocalSpaceConfig(spaceName);
        //TODO 事物
        try {
            return localSpaceConfig.getDataSource().getConnection();
        } catch (SQLException e) {
            throw new RsqlConnectionException(DbErrorCode.RSQL_CONNECTION_ERROR, "数据库连接错误", e);
        }
    }

    /**
     * 释放 链接
     * @param spaceName 要释放的 数据库 空间名字
     * @param con 释放的链接
     */
    public static void freeLocalConnection(String spaceName, Connection con) {
//        if (!isTransaction(spaceName)) {
            try {
                con.close();
            } catch (SQLException e) {
                throw new RsqlConnectionException(DbErrorCode.RSQL_CONNECTION_ERROR, "释放数据库连接错误", e);
            }
//        }
    }


    /**
     * 获取数据库连接池配置
     * @param spaceName 数据库 空间名字
     * @return 返回配置
     */
    public static RDSSpaceConfig getLocalSpaceConfig(String spaceName){
        return spaceConfigs.get(spaceName);
    }


    /**
     *  扫描配置中包的model  并且 放入  spaceConfigs
     * @param rdsSpaceConfig 数据库设置参数
     */
    private void create(RDSSpaceConfig rdsSpaceConfig) {
        Map<String,Class<? extends Modelable>> ormBeans = new HashMap<>();
        RsqlConstants.logger.info("数据库空间【" + rdsSpaceConfig.getSpaceName() + "】初始化中,cn.remex下属所有的包都默认添加到ORMBeans中...");
//        Map<String, Class<? extends Modelable>> ormBeans = new HashMap<>();

        List<String> ormBeanPackages = rdsSpaceConfig.getOrmBeanPackages();

        ormBeanPackages.forEach(packagePath -> {
            Set<Class<?>> orbs = PackageUtil.getClasses(packagePath);
            orbs.forEach(aClass -> {
                String cn = aClass.getSimpleName();
                String fn = aClass.getName();
                // TODO 试试 instanof
                if (Modelable.class.isAssignableFrom(aClass))
                    if (ormBeans.get(cn) != null) {
                        RsqlConstants.logger.error("ORMBeans 的包中有重复类简名的类，因数据库建表表明无法重复，这种情况是不允许的！,此类将被忽略，其名为" + fn);
                    } else if (fn.indexOf('$') > 0) {
                        if (!fn.contains("cn.remex"))RsqlConstants.logger.warn("ORMBeans 的包中有inner类，不建议在数据模型类中使用这种类型并持久化！您依然可以使用该类编程，但数据库建模中将忽略此类，其名为" + fn);
                    } else if (Modifier.isAbstract(aClass.getModifiers())) {
                        if (!fn.contains("cn.remex"))RsqlConstants.logger.info("在数据库建模package中发现一个抽象类" + fn + "将被忽略!");
                    } else if (aClass.isInterface()) {
                        if (!fn.contains("cn.remex"))RsqlConstants.logger.info("在数据库建模package中发现一个接口" + fn + "将被忽略!");
                    } else {
                        ormBeans.put(cn, (Class<? extends Modelable>) aClass);
                        //构建bean缓存
                        //spaceConfig.getDbBeanPool().put(c, new HashMap<>(15));
                    }
            });
        });
        rdsSpaceConfig.setOrmBeans(ormBeans);


//        //建表或刷新表
//        try {
//            if (rebuildDB) {
//                // RsqlDefiner.initDBSystemTable(spaceConfig);
//                RsqlDefiner.refreshORMBaseTables(spaceConfig);
//                RsqlDefiner.refreshORMCollectionTables(spaceConfig);
//                // RsqlDefiner.refreshORMMapTables(ormBeans);
//                // 建立约束或者刷新约束
//                RsqlDefiner.refreshORMConstraints(spaceConfig);
//            }
//
//        } catch (NestedException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new RsqlException(ServiceCode.RSQL_INIT_ERROR, "初始化数据库错误！", e);
//        }
        spaceConfigs.put(rdsSpaceConfig.getSpaceName(),rdsSpaceConfig);
    }
}
