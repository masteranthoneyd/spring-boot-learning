package com.yangbingdong.springbootdatajpa.dbmeta.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ybd
 * @date 19-1-10
 * @contact yangbingdong1994@gmail.com
 */
@Data
@Accessors(chain = true)
public class FieldInfo {
	private String columnName;
	private String fieldName;
	private String fieldClass;
	private String fieldComment;
	private boolean primary = false;
}
