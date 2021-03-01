package BSServer.server;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.text.*;
import java.util.*;

public class Console {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
	private static DataOutputStream logFile;

	public Console() {
		try {
			logFile = new DataOutputStream(new FileOutputStream("logfile.log"));
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void print(String info) {
		try {
			System.out.println(/*"[" + sdf.format(Calendar.getInstance().getTime()) + "] " +*/ info);
			logFile.writeChars(/*"[" + sdf.format(Calendar.getInstance().getTime()) + "] " + */info + "\n");
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}