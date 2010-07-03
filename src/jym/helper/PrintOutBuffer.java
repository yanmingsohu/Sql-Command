// CatfoOD 2009-9-18 обнГ09:59:33

package jym.helper;

import java.io.BufferedOutputStream;
import java.io.PrintWriter;

public class PrintOutBuffer {
	
	private PrintWriter out;
	
	public PrintOutBuffer() {
		out = new PrintWriter( new BufferedOutputStream( System.out ) );
	}
	
	public void p(Object o) {
		out.print(o);
		out.flush();
	}

}
