package com.yangbingdong.springbootdatajpa.dbmeta;

import com.yangbingdong.springbootdatajpa.dbmeta.entity.ClassInfo;
import com.yangbingdong.springbootdatajpa.dbmeta.entity.FieldInfo;
import com.yangbingdong.springbootdatajpa.dbmeta.entity.IndexInfo;
import com.yangbingdong.springbootdatajpa.dbmeta.entity.PrimaryKeyInfo;

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
			System.out.println("/////////////////////// Berserker Generator ////////////////////////////\n" +
					"////// -> MySQL Version: " + metaData.getDatabaseProductVersion() + "\n" +
					"////// -> Driver Version: " + metaData.getDriverVersion() + "\n" +
					"////// -> Generating...............\n" +
					"/////////////////////// Berserker Generator ////////////////////////////\n");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public ClassInfo parse() {
		try (Connection connection = connectionProvider.provideConnection()) {
			printDdl(connection);
			DatabaseMetaData metaData = connection.getMetaData();
			printDbBaseInfo(metaData);
			ClassInfo classInfo = parseClassInfo(metaData);
			List<FieldInfo> fieldInfos = parseFieldInfos(metaData);
			classInfo.setFieldList(fieldInfos);
			handlerPrimaryKeyAndIndex(classInfo, metaData);
			printSuccess();
			return classInfo;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private void printSuccess() {
		System.out.println("\n/////////////////////// Generated Successful ////////////////////////////\n");
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

	private void printDdl(Connection connection) {
		try {
			Statement statement = connection.createStatement();
			String ddlQuery = MessageFormat.format("SHOW CREATE TABLE {0}", tableName);
			ResultSet resultSet = statement.executeQuery(ddlQuery);
			if (resultSet.next()) {
				String createTable = resultSet.getString("CREATE TABLE");
				System.out.println("/////////////////////// ↓ Create Table DDL ↓ ////////////////////////////\n\n" + createTable);
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
				fieldList.add(fieldInfo);
			}
			return fieldList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private void handlerPrimaryKeyAndIndex(ClassInfo classInfo, DatabaseMetaData metaData) {
		try {
			Map<String, FieldInfo> fieldMap = resolveToFieldInfoMap(classInfo);
			Map<String, IndexInfo> indexInfoMap = new HashMap<>(16);
			PrimaryKeyInfo primaryKeyInfo = new PrimaryKeyInfo();
			ResultSet rs = metaData.getIndexInfo(catalog, null, tableName, false, true);
			while (rs.next()) {
				String indexName = rs.getString("INDEX_NAME");
				if ("PRIMARY".equalsIgnoreCase(indexName)) {
					primaryKeyInfo.addPrimaryKey(fieldMap.computeIfPresent(rs.getString("COLUMN_NAME"), (s, fieldInfo) -> fieldInfo.setPrimary(true)));
					continue;
				}
				indexInfoMap.compute(indexName,
						tryBiFunction((s, indexInfo) -> Optional.ofNullable(indexInfo)
																.orElse(new IndexInfo())
																.setIndexName(indexName)
																.setUnique(!rs.getBoolean("NON_UNIQUE"))
																.addFieldInfo(fieldMap.get(rs.getString("COLUMN_NAME")))));
			}
			if (indexInfoMap.size() > 0) {
				classInfo.setIndexList(new ArrayList<>(indexInfoMap.values()))
						 .setHasIndex(true)
						 .getIndexList()
						 .forEach(IndexInfo::judgeUnion);
			}
			classInfo.setPrimaryKey(primaryKeyInfo.judgeUnion());
		} catch (SQLException e) {
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
