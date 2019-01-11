package com.yangbingdong.springbootdatajpa.dbmeta.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author ybd
 * @date 19-1-10
 * @contact yangbingdong1994@gmail.com
 */
@Data
@Accessors(chain = true)
public class ClassInfo {
	private String tableName;
	private String className;
	private String classComment;
	private boolean hasIndex;
	private PrimaryKeyInfo primaryKey;
	private List<FieldInfo> fieldList;
	private List<IndexInfo> indexList;
}
