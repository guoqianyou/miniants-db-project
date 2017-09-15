package db.dbcore.aspect;

import db.RsqlConstants;
import db.dbcore.transaction.RsqlTransaction;
import db.dbcore.transaction.TransactionManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import static db.dbcore.cconnection.RDSManager.DEFAULT_SPACE;

/**
 * spring 切面 用于数据库事物
 * Created by guoqi on 2017/3/13.
 */
@Aspect
public class RsqlTransactionalAspect {
    public RsqlTransactionalAspect() {
        super();
        RsqlConstants.logger.info("数据库事物切面类+"+this.getClass()+"初始化完成！");
    }
    @Around(value = "@annotation(db.dbcore.transaction.RsqlTransaction)&&@annotation(rsqlTranAnno)")
    public Object doAround(final ProceedingJoinPoint pjp, RsqlTransaction rsqlTranAnno) throws Throwable {
        try {
            TransactionManager.start(DEFAULT_SPACE);
            Object retVal = pjp.proceed();
            TransactionManager.commit(DEFAULT_SPACE);
            return retVal;
        }catch (Throwable t){
            TransactionManager.rollback(t,DEFAULT_SPACE);
            throw t;
        }
    }
}
