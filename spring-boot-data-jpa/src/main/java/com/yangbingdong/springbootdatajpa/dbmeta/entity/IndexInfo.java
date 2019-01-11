package com.yangbingdong.springbootdatajpa.dbmeta.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ybd
 * @date 19-1-10
 * @contact yangbingdong1994@gmail.com
 */
@Data
@Accessors(chain = true)
public class IndexInfo {
	private String indexName;
	private boolean unique;
	private boolean unionIndex;
	private List<FieldInfo> fieldList = new ArrayList<>();

	public IndexInfo addFieldInfo(FieldInfo fieldInfo) {
		fieldList.add(fieldInfo);
		return this;
	}

	public IndexInfo judgeUnion() {
		return this.setUnionIndex(fieldList.size() > 1);
	}
}
