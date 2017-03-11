import db.DbCvo;
import db.DbRvo;
import db.dbcore.RsqlExecutor;
import db.sql.NamedParam;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guoqi on 2017/3/9.
 */
public class TestMain {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        DbCvo dbCvo = new DbCvo();
        dbCvo._setSqlString("SELECT count(openCourseNo)\n" +
                "FROM EpCourseInfoBase\n" +
                "WHERE timeStamp = ? AND timeType = ? ");

        NamedParam namedParam = new NamedParam(1,"epTimeStamp", Types.CHAR,"qq");
        NamedParam namedParam2 = new NamedParam(2,"epTimeType", Types.CHAR,"qxxxxxq");
        List<NamedParam> namedParams = new ArrayList<>();
        namedParams.add(namedParam);
        namedParams.add(namedParam2);
        dbCvo._setNamedParams(namedParams);
        dbCvo._setSpaceName("test");
        RsqlExecutor executor = new RsqlExecutor();
        DbRvo execute = executor.executeQuery(dbCvo);
    }

}
