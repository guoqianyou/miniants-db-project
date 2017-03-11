package db.dbcore.cconnection;

import core.util.StringHelper;
import db.RsqlConstants.RDSType;
import db.dbcore.model.Modelable;
import org.springframework.beans.factory.InitializingBean;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 关系型数据库 配置
 * Created by guoqi on 2017/3/8.
 */
public class RDSSpaceConfig implements InitializingBean {

    private String spaceName; // 数据库空间命名
    private RDSType rdsType; //数据库类型  mysql  oracle
    private Map<String, Class<? extends Modelable>> ormBeans = new HashMap<>();
    private List<String> ormBeanPackages; // orm 的包路径 用于加载到spring 中
    private DataSource dataSource; // 数据池
    private boolean cannotRebuild; //无法重构？~ TODO

    private int defaultScale = 2; //默认double float的精度 TODO





    @Override
    public void afterPropertiesSet() throws Exception {
        RDSManager.createSpace(this);
    }

    public  Class<? extends Modelable> getOrmBeanClassByName(final String beanName){
        return beanName !=null ? this.ormBeans.get(StringHelper.handleCGLIBClassName(beanName)):null;
    }



    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    public RDSType getRdsType() {
        return rdsType;
    }

    public void setRdsType(RDSType rdsType) {
        this.rdsType = rdsType;
    }

    public List<String> getOrmBeanPackages() {
        return ormBeanPackages;
    }

    public void setOrmBeanPackages(List<String> ormBeanPackages) {
        this.ormBeanPackages = ormBeanPackages;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean isCannotRebuild() {
        return cannotRebuild;
    }

    public void setCannotRebuild(boolean cannotRebuild) {
        this.cannotRebuild = cannotRebuild;
    }

    public int getDefaultScale() {
        return defaultScale;
    }

    public void setDefaultScale(int defaultScale) {
        this.defaultScale = defaultScale;
    }

    public Map<String, Class<? extends Modelable>> getOrmBeans() {
        return ormBeans;
    }

    public void setOrmBeans(Map<String, Class<? extends Modelable>> ormBeans) {
        this.ormBeans = ormBeans;
    }
}
