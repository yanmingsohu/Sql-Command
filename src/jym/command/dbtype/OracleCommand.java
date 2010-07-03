// CatfoOD 2009-9-20 ионГ10:54:44

package jym.command.dbtype;

public class OracleCommand implements IListCommand{

	@Override
	public String listDBName() {
		return "select TABLESPACE_NAME " +
				"from all_tables group by TABLESPACE_NAME";
	}

	@Override
	public String listTableName() {
		return "select * from tab";
	}

}
