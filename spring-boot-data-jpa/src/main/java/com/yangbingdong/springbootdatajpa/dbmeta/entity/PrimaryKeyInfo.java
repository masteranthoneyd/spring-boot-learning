package com.yangbingdong.springbootdatajpa.dbmeta.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ybd
 * @date 19-1-11
 * @contact yangbingdong1994@gmail.com
 */
@Data
@Accessors(chain = true)
public class PrimaryKeyInfo {
	private boolean unionPrimaryKey;
	List<FieldInfo> primaryKeys = new ArrayList<>();

	public void addPrimaryKey(FieldInfo fieldInfo) {
		primaryKeys.add(fieldInfo);
	}

	public PrimaryKeyInfo judgeUnion() {
		return this.setUnionPrimaryKey(primaryKeys.size() > 1);
	}
}
