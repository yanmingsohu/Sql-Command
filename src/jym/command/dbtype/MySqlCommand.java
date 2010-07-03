// CatfoOD 2009-9-19 ионГ09:07:44

package jym.command.dbtype;

public class MySqlCommand implements IListCommand {

	@Override
	public String listDBName() {
		return "show databases";
	}

	@Override
	public String listTableName() {
		return "show tables";
	}

}
