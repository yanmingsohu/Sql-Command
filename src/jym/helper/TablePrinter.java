// CatfoOD 2009-9-17 下午08:05:35

package jym.helper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class TablePrinter {
	
	private Displayable disp;
	
	public TablePrinter(Displayable d) {
		disp = d;
	}
	
	public void printTable(ResultSet r) throws SQLException {
		ResultSetMetaData rsmd = r.getMetaData();
		TableFormatOut tout = new TableFormatOut(rsmd.getColumnCount());
		
		int columncount = rsmd.getColumnCount();
		
		for (int i=1; i<=columncount; ++i) {
			tout.put( "[" + rsmd.getColumnName(i) + "]" );
		}

		for (int i=0; i<columncount; ++i) {
			tout.put( "-" );
		}

		disp.pl();
		int rows = 0;
		int matchrow = tout.PAGE_LENGTH * 9 / (columncount+1);
		boolean notpage = false;
		
		while (r.next()) {
			for (int i=1; i<=columncount; ++i) {
				try {
					tout.put(r.getObject(i));
				} catch (SQLException e) {
					tout.put(e.getMessage());
				}
			}
			rows++;
			if ( rows % matchrow == 0 ) {
				tout.print(disp);
				
				if (!notpage) {
					String p = disp.in( "<:回车继续,'s'全部打印,'b'中断", true);
					disp.pl();
					if ("b".equalsIgnoreCase(p)) return;
					notpage = "s".equalsIgnoreCase(p);
				}
			}
		}
		
		tout.print(disp);
	}
	
}
