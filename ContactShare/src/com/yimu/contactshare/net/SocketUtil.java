package com.yimu.contactshare.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import android.content.Context;

/**
 * socket��������
 * 
 * @author linwei
 * 
 */
public class SocketUtil {

	Socket socket;
	String IP;
	BufferedReader in;
	PrintWriter out;

	public boolean isConnected = false;

	public SocketUtil() {

		this.IP = "192.168.9.114";

	}

	/**
	 * ������������
	 * 
	 * @throws Exception
	 */
	public void connectServer() throws Exception {

		if (socket != null) {
			return;
		}
		try {
			socket = new Socket(IP, 9000);
			socket.setSoTimeout(10000); // ������ʱ��������������
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
			isConnected = true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("connect fail...check connectServer() method");
			isConnected = false;
			throw e;
			// ����server�Ƿ��Ѿ�����
		}
	}

	/**
	 * ע���û�
	 * 
	 * @param username
	 * @param pwd
	 * @param workcode
	 * @return isSuccess
	 */
	public boolean register(String username, String pwd, String workcode) {
		boolean isSuccess = false;
		try {
			out.println("create;" + username + ";" + pwd + ";" + workcode);
			out.flush();
			String getstr = in.readLine();
			System.out.println("registget=" + getstr);
			if (getstr.equals("USERCREATED")) {
				isSuccess = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return isSuccess;
	}

	/**
	 * ����ǩ����Ϣ
	 * 
	 * @return isSuccess
	 */
	public boolean sendCheck(String username, String password, String workcode) {

		boolean isSuccess = false;

		try {
			out.println("check;" + username + ";" + password + ";" + workcode);
			out.flush();
			String getstr = in.readLine();
			System.out.println("checkget=" + getstr);
			if (getstr.equals("CHECKSUCCESS")) {
				isSuccess = true;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isSuccess;
	}

	/**
	 * ��������
	 * 
	 * @param username
	 * @param password
	 * @param newPass
	 * @return
	 */
	public boolean changeMM(String username, String password, String workcode,
			String newPass) {
		boolean isSuccess = false;
		try {
			out.println("changemm;" + username + ";" + password + ";"
					+ workcode + ";" + newPass);
			out.flush();
			String getstr = in.readLine();
			System.out.println("changemmget=" + getstr);
			if (getstr.equals("CHANGEMMSUCCESS")) {
				isSuccess = true;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isSuccess;
	}

	/**
	 * ��¼
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean login(String username, String password, String workcode) {
		boolean isSuccess = false;
		try {
			out.println("login;" + username + ";" + password + ";" + workcode);
			out.flush();
			String getstr = in.readLine();
			System.out.println("loginget=" + getstr);
			if (getstr.equals("LOGINSUCCESS")) {
				isSuccess = true;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isSuccess;
	}

	/**
	 * �رշ���������
	 */
	public void close() {
		try {
			if (socket != null) {
				socket.close();
			}
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
