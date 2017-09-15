package db.dbcore.aspect;

import core.exception.ServiceCode;
import core.util.Judgment;
import db.DbCvo;
import db.DbRvo;
import db.RsqlConstants;
import db.exception.RsqlDataException;
import db.exception.RsqlExecuteException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import static db.DbErrorCode.RSQL_DATA_INVALID;
import static db.RsqlConstants.SqlOper.sql;

/**
 * Created by guoqi on 2017/3/15.
 */
@Aspect
public class RsqlMonitorAspect {

    @Around(value = "execution(public * db.dbcore.RsqlExecutor.execute*(..))")
    public Object doAroundRemexSqlExecute(final ProceedingJoinPoint pjp) throws Throwable {
        long time = System.currentTimeMillis();
        String sqlExecuteMethod = pjp.getSignature().getName();
        DbRvo retVal = null;
        DbCvo dbCvo = (DbCvo) pjp.getArgs()[0];
        String msg = "RsqlExecutor.{}() executed [ {}ms ], Message:\r\n\tSQL: {}\r\n\tParameters: {}\r\n\tDataBase Msg: {}";
//        String sql = dbCvo._getSqlString() + (dbCvo._getSqlString().trim().startsWith("SELECT") ? Select.obtainSQLOrder(dbCvo) : "");
        String param = dbCvo._getNamedParams().toString();
        String errorMsg = null;
        try {
            retVal = (DbRvo) pjp.proceed();
        } catch (RsqlExecuteException e) {
            errorMsg = Judgment.nullOrBlank(e.getMessage())?e.toString():e.getMessage();
            if (null != e.getCause() && e.getCause().getMessage().contains("Duplicate entry")) {
                String causeMsg = e.getCause().getMessage();
                throw new RsqlDataException(RSQL_DATA_INVALID, "表["+(Judgment.nullOrBlank(dbCvo.getBeanName())?"-":dbCvo.getBeanName())+"]主键数据重复：" + causeMsg.substring(causeMsg.indexOf("Duplicate entry") + 15, causeMsg.indexOf("for")));
            } else if (null != e.getCause() && e.getCause().getMessage().contains("Data truncation: Data too long")) {
                String causeMsg = e.getCause().getMessage();
                throw new RsqlDataException(RSQL_DATA_INVALID, "表["+(Judgment.nullOrBlank(dbCvo.getBeanName())?"-":dbCvo.getBeanName())+"]字段数据过长：" + causeMsg.substring(causeMsg.indexOf("Data too long for column") + 24, causeMsg.indexOf(" at")));
            } else if (null != e.getCause() && e.getCause().getMessage().contains("cannot be null")) {
                String causeMsg = e.getCause().getMessage();
                throw new RsqlDataException(RSQL_DATA_INVALID, "表["+(Judgment.nullOrBlank(dbCvo.getBeanName())?"-":dbCvo.getBeanName())+"]字段不能为空：" + causeMsg.substring(causeMsg.indexOf("Column '")+8, causeMsg.indexOf("' cannot be null")));
            } else {
                throw e;
            }
        } catch (Exception e) {
            errorMsg = Judgment.nullOrBlank(e.getMessage())?e.toString():e.getMessage();
            throw e;
        } finally {
            RsqlConstants.logger.info(msg, sqlExecuteMethod, System.currentTimeMillis() - time, sql, param, retVal!=null?retVal.getMsg():"Rsql操作异常:"+errorMsg);
        }

        return retVal;
    }

}
