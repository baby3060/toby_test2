package com.tobsec.model.handler;

import com.tobsec.model.Level;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;

import org.apache.ibatis.type.BaseTypeHandler;
/**
 * @author Clinton Begin
 */
public class LevelOnlyHandler<E extends Enum<E>> extends BaseTypeHandler<E> {

  private final Class<E> type;

  public LevelOnlyHandler(Class<E> type) {
    if (type == null) {
      throw new IllegalArgumentException("Type argument cannot be null");
    }
    this.type = type;    
  }

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
    ps.setInt(i, parameter.ordinal());
  }

  @Override
  public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
    int ordinal = rs.getInt(columnName);
    if (rs.wasNull()) {
      return null;
    }
    return toOrdinalEnum(ordinal);
  }

  @Override
  public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    int ordinal = rs.getInt(columnIndex);
    if (rs.wasNull()) {
      return null;
    }
    return toOrdinalEnum(ordinal);
  }

  @Override
  public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    int ordinal = cs.getInt(columnIndex);
    if (cs.wasNull()) {
      return null;
    }
    return toOrdinalEnum(ordinal);
  }

  private E toOrdinalEnum(int ordinal) {
    try {
      return (E)Level.valueOf(ordinal);
    } catch (Exception ex) {
      throw new IllegalArgumentException("Cannot convert " + ordinal + " to " + type.getSimpleName() + " by ordinal value.", ex);
    }
  }
}