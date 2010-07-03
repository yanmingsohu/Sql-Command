// CatfoOD 2009-9-20 обнГ05:50:14

package jym.command.dbtype;

public class SqlServerComm implements IListCommand {

	@Override
	public String listDBName() {
		return "select name as databasename,filename as localfile " +
				"from master.dbo.sysdatabases";
	}

	@Override
	public String listTableName() {
		return "select Table_name,Table_type " +
				"from information_schema.tables";
	}

}
