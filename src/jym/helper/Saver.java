// CatfoOD 2009-9-18 上午08:34:49 
package jym.helper;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class Saver {
	private PrintWriter out = null;
	private DateFormat form;
	
	public Saver(String file) {
		try {
			form = DateFormat.getDateTimeInstance();
			out = new PrintWriter( new BufferedWriter(new FileWriter(file, true)) );
			System.out.println("[操作记录在文件:" + file + "]");
		} catch (IOException e) {
			out = new PrintWriter( new NullWriter() );
			System.out.println("[记录文件创建失败，不能记录操作记录 :" + e.getMessage()+"]");
		}
	}
	
	public void put(String text) {
		out.print( getData() );
		out.print('\t');
		out.println(text);
	}
	
	private String getData() {
		Calendar cal = Calendar.getInstance();
		return "[" + form.format( new Date() ) + "]";
	}
	
	public void flush() {
		out.flush();
	}
	
	public void close() {
		out.close();
	}
	
	private class NullWriter extends Writer {
		public void close() throws IOException {
		}
		public void flush() throws IOException {
		}
		public void write(char[] cbuf, int off, int len) throws IOException {
		}
	}
}
