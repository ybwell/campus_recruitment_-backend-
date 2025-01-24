package com.caohao.config;

import com.caohao.common.enums.EnumCode;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// 标注该类型处理器处理的Java类型为实现了EnumCode接口的枚举类型
@MappedTypes(EnumCode.class)
// 标注该类型处理器处理的JDBC类型为INTEGER
@MappedJdbcTypes(JdbcType.INTEGER)
public class EnumValueTypeHandler<E extends Enum<E> & EnumCode> extends BaseTypeHandler<EnumCode> {

    private final Class<E> type;

    public EnumValueTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, EnumCode parameter, JdbcType jdbcType) throws SQLException {
        // 将枚举的code值设置到PreparedStatement中
        ps.setLong(i, parameter.getCode());
    }

    @Override
    public EnumCode getNullableResult(ResultSet rs, String columnName) throws SQLException {
        // 从ResultSet中获取数据库字段的值
        long code = rs.getLong(columnName);
        return getEnumByCode(code);
    }

    @Override
    public EnumCode getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        // 从ResultSet中获取数据库字段的值
        long code = rs.getLong(columnIndex);
        return getEnumByCode(code);
    }

    @Override
    public EnumCode getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        // 从CallableStatement中获取数据库字段的值
        long code = cs.getLong(columnIndex);
        return getEnumByCode(code);
    }

    private EnumCode getEnumByCode(long code) {
        // 遍历枚举类型的所有值，找到对应的枚举实例
        E[] enumConstants = type.getEnumConstants();
        for (E enumConstant : enumConstants) {
            if (enumConstant.getCode() == code) {
                return enumConstant;
            }
        }
        return null;
    }
}