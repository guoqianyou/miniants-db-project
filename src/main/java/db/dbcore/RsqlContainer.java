package db.dbcore;

import core.util.Assert;
import core.util.Judgment;
import core.util.StringHelper;
import db.Container;
import db.DbCvo;
import db.RsqlConstants.DataStatus;
import db.RsqlConstants.SqlOper;
import db.dbcore.cconnection.RDSManager;
import db.dbcore.model.Modelable;

import static db.DbErrorCode.RSQL_BEANCLASS_ERROR;
import static db.DbErrorCode.RSQL_DATA_ERROR;
import static db.RsqlConstants.DataStatus.DS_saving;

/**
 * Created by guoqi on 2017/3/10.
 */
public class RsqlContainer implements Container {

    private String spaceName = "";

    public RsqlContainer() {
    }

    @Override
    public <T> Object store(T o) {
        return null;
    }


//    private <T extends Modelable> Object store(final T obj, DbCvo dbCvo) {
//        Assert.notNull(obj, RSQL_DATA_ERROR, "不能保存空对象");
//        Assert.isTrue(Modelable.class.isAssignableFrom(obj.getClass()), RSQL_BEANCLASS_ERROR, "数据库操作的对象不是继承于modelable");
//
//        String beanName = StringHelper.getClassSimpleName(obj.getClass());
//        Class<? extends Modelable> ormBeanClassByName = RDSManager.getLocalSpaceConfig(spaceName).getOrmBeanClassByName(beanName);
//        // 必须考虑的cgil加强,后面用这个原始beanClass替换了
//        // 没有在配置文件中配置的bean不保存
//
//        // 进入对象保存，
//        // 为了避免无限递归必须检查数据状态，如需保存还需设置saving
//        DataStatus dataStatus = obj._getDataStatus();
//        if (DS_saving.equals(dataStatus))
//            return null;
//        else
//            obj._setDataStatus(DS_saving);
//        //TODO why? 直接set 更妥？~
////            updateDS(obj, DS_saving);
////        String obj_id = getPK(obj, false);
//        if (obj._isAopModelBean() && !Judgment.nullOrBlank(obj._getModifyFileds())) {
//            for (String fieldName : obj._getModifyFileds().split("[,;]")) {
//                dbCvo._getRootColumn().withColumn(fieldName);
//            }
//        }
//        SqlOper oper = obtainOper(obj_id, dataStatus, dbCvo.getOper(), cvo); // del/add/edit
//
//        DbCvo<T, ParentType> dbCvo = new DbCvo<>(spaceName, (Class<T>) obj.getClass(), oper, /*dt, 2016-8-21 LHY 删除dataType*/cvo._getRootColumn());// 需要生成本地所需的DBCVO
//        dbCvo.setId(obj_id);
//        dbCvo.setFilter(cvo.getFilter());
//        dbCvo.setBean(obj);//新建时，需要通过bean中的 generateId 获取id
//        if (!SqlOper.del.equals(oper)) {
//            dbCvo.putParameters(maplizeObject(obj, oper, cvo)); // 更新、插入时才需要序列化基本数据列，id，datastatus列必须处理。
//        }
//        // 外键对象数据
////        Map<OneToOne,Modelable> map = null;
////        //if (dt.contains(DT_object)) LHY 2016-8-21
////        map = this.storeFKBean(beanClass, obj, dbCvo);// 保存外键对象并将更新的外键对象的Id存入DbCvo,如Person.department更新后将partment.id更新至本obj的DbCvo为partment=id
//
//        // 执行base及object's id 保存.
////        dbCvo._initForRsqlDao();// LHY 2015-1-17 挪到此处更加恰当，Dao切面捕获异常时需要用到sqlbean，必须提前初始化。
////        dbRvo = RsqlExecutor.getDefaultSqlExecutor().executeUpdate(dbCvo);
////
////        // 更新one2one 关系 ，关系只能在保存完当前对象后 更新
////        updateOne2One(obj,map);
//
////        // 对象基本数据保存成功后必须更新其id
////        if (dbRvo.getStatus() && null != dbRvo.getId()) {
////            obj_id = dbRvo.getId();
////            this.updatePK(obj, obj_id);
////            this.updateDS(obj, !SqlOper.del.equals(oper) ? DS_managed : DS_removed);
////        } else {
////            this.updateDS(obj, dataStatus);
////        }
//
////        /** =================检查并保存collection类型数据============ */
//////        if (dt.contains(DT_collection))
////        this.storeFKList(beanClass, obj, dbCvo, obj_id, beanName, oper);
//
//        // 标记对象已经完成保存。
//        // updateDS(obj, DS_managed); LHY 挪动至461
//        return dbRvo;
//    }


//    private SqlOper obtainOper(final String p_id, final Object dataStatus, final SqlOper sqlOper, final DbCvo cvo) {
//
//    }
}
