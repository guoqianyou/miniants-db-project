package db.sql;

import db.DbErrorCode;
import db.exception.RsqlException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Types;

/**
 * sql 中 参数
 * Created by guoqi on 2017/3/10.
 */
public class NamedParam {
    private int index;

    private String name;
    private int type;
    private Object value;

    public NamedParam(int index, String name, int type, Object value) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.value = value;
    }


    @Override
    public String toString() {
        return "[" +
                "param=" +
                this.index +
                ", type=" +
                this.type +
                ',' +
                this.name +
                '=' +
                this.value +
                ']';
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getValue() {
        if(null == this.value ||this.value.toString().length()==0){
            return null;
        }else if(this.type == Types.CHAR){
            return this.value.toString();
        }else if(this.type == Types.BOOLEAN){
            return String.valueOf(this.value);
        }else if (this.type == Types.INTEGER){
            try {
                return Integer.parseInt(this.value.toString());
            } catch (NumberFormatException e) {
                throw new RsqlException(DbErrorCode.RSQL_DATA_ERROR, "字段【"+this.name+"】数据类型应为整型[int]："+this.value);
                //				return null;//never arrived here
            }
        }else if(this.type == Types.FLOAT){
            try {
                return Float.parseFloat(this.value.toString());
            } catch (NumberFormatException e) {
                throw new RsqlException(DbErrorCode.RSQL_DATA_ERROR, "字段【"+this.name+"】数据类型应为数字类型[float]："+this.value);
                //				return null;//never arrived here
            }
        }else if(this.type == Types.DOUBLE){
            try {
                return Double.parseDouble(this.value.toString());
            } catch (NumberFormatException e) {
                throw new RsqlException(DbErrorCode.RSQL_DATA_ERROR, "字段【"+this.name+"】数据类型应为数字类型[double]："+this.value);
                //				return null;//never arrived here
            }
        }else if(this.type == Types.JAVA_OBJECT){ //处理字节流
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(this.value);
                String ret = baos.toString();
                oos.close();
                baos.close();
                return ret;
            } catch (IOException e) {
                throw new RsqlException(DbErrorCode.RSQL_DATA_ERROR, "字段【"+this.name+"】数据类型为对象类型[object]，但转化为字节流失败！"+this.value);
                //				return null;//never arrived here
            }
        } else {
            return this.value;
        }
    }

    public void setValue(Object namedParamValue) {
        Object value = namedParamValue;
        if (null != namedParamValue) {
            //兼容数组参数，当数组不为空时，通过第一个数组对象获取真实的数据类型
            if (namedParamValue.getClass().isArray() && ((Object[]) namedParamValue).length > 0) {
                value = ((Object[]) namedParamValue)[0];
            }
            // TODO 完善方法
//            this.setType(RsqlUtils.obtainSQLType(value.getClass().type);
        }
        this.value = value;
    }


}
