package com.yangbingdong.springbootdatajpa.dbmeta;

import com.yangbingdong.springbootdatajpa.dbmeta.entity.ClassInfo;
import com.yangbingdong.springbootdatajpa.dbmeta.entity.FieldInfo;
import com.yangbingdong.springbootdatajpa.dbmeta.entity.IndexInfo;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.yangbingdong.springboot.common.utils.function.Trier.tryBiFunction;
import static com.yangbingdong.springbootdatajpa.util.StringPlusUtils.underlineToCamelCase;
import static com.yangbingdong.springbootdatajpa.util.StringPlusUtils.upperCaseFirst;
import static java.util.stream.Collectors.toMap;

/**
 * @author ybd
 * @date 19-1-9
 * @contact yangbingdong1994@gmail.com
 */
public class TableInfoParser {
	static final String DATA_URL = "jdbc:mysql://localhost:3306/jpa_test?useSSL=false";

	// 数据库的用户名与密码，需要根据自己的设置
	static final String USER = "root";
	static final String PASS = "root";

	public static void main(String[] args) {
		TableInfoParser tableInfoParser = TableInfoParser.of(DATA_URL, USER, PASS, "test_table");
		ClassInfo parse = tableInfoParser.parse();
		System.out.println(parse);
	}

	private ConnectionProvider connectionProvider;

	private final String tableName;
	private final String catalog;

	public static TableInfoParser of(String url, String user, String password, String tableName) {
		return new TableInfoParser(url, user, password, tableName);
	}

	private TableInfoParser(String url, String user, String password, String tableName) {
		connectionProvider = new NativeConnectionProvider(url, user, password);
		this.tableName = tableName;
		catalog = parseCatalog(url);
	}

	private String parseCatalog(String s) {
		return s.substring(s.lastIndexOf("/") + 1, s.indexOf("?"));
	}

	private void printDbBaseInfo(DatabaseMetaData metaData) {
		try {
			System.out.println("/////////////////////// Saber Code Generator ////////////////////////////\n" +
					"////// -> MySQL Version: " + metaData.getDatabaseProductVersion() + "\n" +
					"////// -> Driver Version: " + metaData.getDriverVersion() + "\n" +
					"////// -> Generating...............\n" +
					"/////////////////////// Saber Code Generator ////////////////////////////\n");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public ClassInfo parse() {
		try (Connection connection = connectionProvider.provideConnection()) {
			DatabaseMetaData metaData = connection.getMetaData();
			printDbBaseInfo(metaData);
			ClassInfo classInfo = parseClassInfo(metaData);
			List<FieldInfo> fieldInfos = parseFieldInfos(metaData);
			classInfo.setFieldList(fieldInfos)
					 .setCreateDdl(parseDdl(connection));
			handlerIndex(classInfo, metaData);
			handlerPrimaryKey(classInfo, metaData);
			return classInfo;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private ClassInfo parseClassInfo(DatabaseMetaData metaData) {
		try {
			// table type. Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
			ResultSet rs = metaData.getTables(catalog, null, tableName, new String[]{"TABLE"});
			ClassInfo classInfo = new ClassInfo();
			boolean tableExist = false;
			while (rs.next()) {
				if (tableExist) {
					throw new IllegalArgumentException("More than one table!");
				}
				tableExist = true;
				String tableName = rs.getString("TABLE_NAME");
				classInfo.setTableName(tableName)
						 .setClassName(upperCaseFirst(underlineToCamelCase(tableName)))
						 .setClassComment(rs.getString("REMARKS"));
			}
			if (!tableExist) {
				throw new IllegalArgumentException("Table `" + tableName + "` not exist!");
			}
			return classInfo;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private String parseDdl(Connection connection) {
		try {
			Statement statement = connection.createStatement();
			String ddlQuery = MessageFormat.format("SHOW CREATE TABLE {0}", tableName);
			ResultSet resultSet = statement.executeQuery(ddlQuery);
			if (resultSet.next()) {
				return resultSet.getString("CREATE TABLE");
			} else {
				throw new IllegalArgumentException("Not ddl found");
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private List<FieldInfo> parseFieldInfos(DatabaseMetaData metaData) {
		try {
			ResultSet rs = metaData.getColumns(catalog, null, tableName, "%");
			List<FieldInfo> fieldList = new ArrayList<>(16);
			while (rs.next()) {
				FieldInfo fieldInfo = new FieldInfo();
				String columnName = rs.getString("COLUMN_NAME");
				fieldInfo.setColumnName(columnName)
						 .setFieldComment(rs.getString("REMARKS"))
						 .setFieldClass(parseDataType(rs.getString("TYPE_NAME")))
						 .setFieldName(underlineToCamelCase(columnName));
				int dataType = rs.getInt("DATA_TYPE");
				int sqlDataType = rs.getInt("SQL_DATA_TYPE");
				fieldList.add(fieldInfo);
			}
			return fieldList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private void handlerIndex(ClassInfo classInfo, DatabaseMetaData metaData) {
		try{
			Map<String, FieldInfo> fieldMap = resolveToFieldInfoMap(classInfo);
			ResultSet rs = metaData.getIndexInfo(catalog, null, tableName, false, true);
			Map<String, IndexInfo> indexInfoMap = new HashMap<>(16);
			while (rs.next()) {
				String indexName = rs.getString("INDEX_NAME");
				if ("PRIMARY".equalsIgnoreCase(indexName)) {
					continue;
				}
				/*IndexInfo indexInfo = indexInfoMap.computeIfAbsent(indexName, key -> new IndexInfo());
				indexInfo.setIndexName(indexName)
						 .setUnique(!rs.getBoolean("NON_UNIQUE"))
						 .addFieldInfo(fieldMap.get(rs.getString("COLUMN_NAME")));
				indexInfoMap.put(indexName, indexInfo);*/

				indexInfoMap.compute(indexName,
						tryBiFunction((s, indexInfo) -> Optional.ofNullable(indexInfo)
																 .orElse(new IndexInfo())
																 .setIndexName(indexName)
																 .setUnique(!rs.getBoolean("NON_UNIQUE"))
																 .addFieldInfo(fieldMap.get(rs.getString("COLUMN_NAME")))));
			}
			if (indexInfoMap.size() > 0) {
				List<IndexInfo> indexList = new ArrayList<>(indexInfoMap.values());
				indexList.forEach(index -> index.setUnionIndex(index.getFieldList().size() > 1));
				classInfo.setIndexList(indexList);
			}
		} catch (SQLException e){
			throw new RuntimeException(e);
		}
	}

	private void handlerPrimaryKey(ClassInfo classInfo, DatabaseMetaData metaData) {
		try{
			Map<String, FieldInfo> fieldMap = resolveToFieldInfoMap(classInfo);
			ResultSet rs = metaData.getPrimaryKeys(catalog, null, tableName);
			while (rs.next()){
				fieldMap.computeIfPresent(rs.getString("COLUMN_NAME"), (s, fieldInfo) -> fieldInfo.setPrimary(true));
			}
		}catch (SQLException e){
			throw new RuntimeException(e);
		}
	}

	private Map<String, FieldInfo> resolveToFieldInfoMap(ClassInfo classInfo) {
		return classInfo.getFieldList()
						.stream()
						.collect(toMap(FieldInfo::getColumnName, Function.identity()));
	}

	private String parseDataType(String nativeType) {
		nativeType = nativeType.toLowerCase();
		if (nativeType.contains("char") || nativeType.contains("text")) {
			return String.class.getSimpleName();
		} else if (nativeType.contains("bigint")) {
			return Long.class.getSimpleName();
		} else if (nativeType.contains("tinyint(1)")) {
			return Boolean.class.getSimpleName();
		} else if (nativeType.contains("int")) {
			return Integer.class.getSimpleName();
		}  else if (nativeType.contains("bit")) {
			return Boolean.class.getSimpleName();
		} else if (nativeType.contains("decimal")) {
			return BigDecimal.class.getSimpleName();
		}  else if (nativeType.contains("float")) {
			return Float.class.getSimpleName();
		} else if (nativeType.contains("double")) {
			return Double.class.getSimpleName();
		} else if (nativeType.contains("json") || nativeType.contains("enum")) {
			return String.class.getSimpleName();
		} else if (nativeType.contains("date") || nativeType.contains("time") || nativeType.contains("year")) {
			switch (nativeType) {
				case "date":
					return LocalDate.class.getSimpleName();
				case "time":
					return LocalTime.class.getSimpleName();
				case "year":
					return Year.class.getSimpleName();
				default:
					return LocalDateTime.class.getSimpleName();
			}
		}
		return String.class.getSimpleName();
	}
}
