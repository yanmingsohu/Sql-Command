// CatfoOD 2009-9-18 下午06:31:13

package jym.helper;

import java.util.ArrayList;
import java.util.Iterator;

import jym.command.IPrinter;

public class TableFormatOut {
	private ArrayList<String>[] col;
	
	private int[] tlength;
	private int colcont;
	
	public final int MAX_LINE_CONT = 79;
	public final int PAGE_LENGTH = 10;
	
	public TableFormatOut(int columnCount) {
		col = new ArrayList[columnCount];
		tlength = new int[columnCount];
		for (int i=0; i<col.length; ++i) {
			col[i] = new ArrayList<String>();
			tlength[i] = 0;
		}
		colcont = 0;
	}
	
	public void put(String text) {
		text = text.trim();
		if (colcont>=col.length) colcont = 0;
		int tlen = text.getBytes().length + 2;
		if (tlen>=MAX_LINE_CONT) {
			tlen = MAX_LINE_CONT-1;
		}
		if (tlen>tlength[colcont]) {
			tlength[colcont] = tlen;
		}
		col[colcont].add(text);
		colcont++;
	}
	
	public void put(Object o) {
		if (o==null) {
			put("null");
		} else {
			put(o.toString());
		}
	}
	
	public void print(IPrinter d) {
		print(d, PAGE_LENGTH);
	}
	
	public void print(IPrinter d, int pagelen) {
		int totalpage = getTotalPage(pagelen);
		int page = 0;
		Row[] rows = marchColume();
		
		setLine();
		while (page<totalpage) {
			for (int r=0; r<rows.length; ++r) {
				
				int start = page*pagelen+2;
				int end = start+pagelen;
				int rowcont = col[0].size();
				if (end>=rowcont) {
					end = rowcont;
				}
				if (start==end) break;
				
				// 表头 0-1 为表头
				printRows(d, rows[r], 0, 2);
				
				// 表内容
				printRows(d, rows[r], start, end);
				d.pl();
			}
			page++;
		}
		clearPrinted();
	}
	
	private void setLine() {
		for (int i=0; i<col.length; ++i) {
			String element = FormatOut.getSymbol(tlength[i]-1, '-');
			col[i].set(1, element);
		}
	}
	
	private void clearPrinted() {
		for (int i=0; i<col.length; ++i) {
			String e0 = col[i].get(0);
			String e1 = col[i].get(1);
			col[i].clear();
			col[i].add(e0);
			col[i].add(e1);
		}
	}
	
	private void printRows(IPrinter d, Row r, int start, int end) {
		for (int i=start; i<end; ++i) {
			Iterator<Integer> itr = r.getIterator();
			
			while (itr.hasNext()) {
				int cindex = itr.next();
				String tx = FormatOut.getMaxLen(
						col[cindex].get(i), MAX_LINE_CONT);
				
				String bl = FormatOut.getBlank( 
						tlength[cindex] - tx.getBytes().length );
				
				d.p(tx);
				d.p(bl);
			}
			d.pl();
		}
	}
	
	private int getTotalPage(int pagelen) {
		int totalpage;
		int size = col[0].size()-2;
		if (size>pagelen) {
			int off = 0;
			if (size%pagelen>0) {
				off = 1;
			}
			totalpage = size / pagelen + off;
		} else {
			totalpage = 1;
		}
		return totalpage;
	}
	
	private Row[] marchColume() {
		ArrayList<Row> list = new ArrayList<Row>();
		Row r = new Row();
		list.add(r);
		for (int i=0; i<col.length; ++i) {
			if (!r.put(i)) {
				r = new Row();
				r.put(i);
				list.add(r);
			}
		}
		return list.toArray(new Row[list.size()]);
	}
	
	private class Row {
		private ArrayList<Integer> num = new ArrayList<Integer>();
		private int totallen = 0;
		public boolean put(int col) {
			int len = tlength[col];
			if ( totallen+len <= MAX_LINE_CONT ) {
				totallen += len;
				return num.add(col);
			} else {
				return false;
			}
		}
		public Iterator<Integer> getIterator() {
			return num.iterator();
		}
	}
}
