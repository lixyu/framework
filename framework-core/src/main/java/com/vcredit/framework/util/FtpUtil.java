package com.vcredit.framework.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FtpUtil {
	/**
	 * 获取ftp连接
	 * 
	 * @param ipAddr
	 *            IP地址
	 * @param port
	 *            端口
	 * @param userName
	 *            用户名
	 * @param pwd
	 *            密码
	 * @param path
	 *            资源路径
	 * @return
	 * @throws Exception
	 */
	public static FTPClient connectFtp(String ipAddr, Integer port, String userName, String pwd) throws Exception {
		FTPClient ftp = new FTPClient();
		int reply;
		ftp.connect(ipAddr, port);
		ftp.login(userName, pwd);
		ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
		ftp.setConnectTimeout(200000);
		reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftp.disconnect();
			return ftp;
		}
		return ftp;
	}

	/**
	 * @param FTPClient
	 *            ftp
	 * 
	 * @param filename
	 *            上传到FTP服务器上的文件名
	 * @param input
	 *            输入流
	 * @return 成功返回true，否则返回false
	 */
	public static boolean uploadFile(FTPClient ftp, String filename, InputStream input) throws IOException {
		boolean success = false;
		try {
			ftp.enterLocalPassiveMode();
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftp.storeFile(filename, input);
			input.close();
			success = true;
		} catch (IOException e) {
			throw e;
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return success;
	}

	/**
	 * @param FTPClient
	 *            ftp
	 * 
	 * @param filename
	 *            上传到FTP服务器上的文件名外部关闭FTP连接
	 * @param input
	 *            输入流
	 * @return 成功返回true，否则返回false
	 * @throws Exception
	 */
	public static boolean uploadFileOpen(FTPClient ftpClient, String filename, InputStream input, String path)
			throws Exception {
		boolean success = false;
		try {
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();
//			ftpClient.makeDirectory(path);
			ftpClient.changeWorkingDirectory(path);
			ftpClient.storeFile(filename, input);
			input.close();
			success = true;
		} catch (IOException e) {
			throw e;
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return success;
	}

	/**
	 * @param FTPClient
	 *            ftp
	 * 
	 * @param fileName
	 *            要下载的文件名
	 * @param localPath
	 *            下载后保存到本地的路径
	 * @return
	 */
	public static boolean downFile(FTPClient ftp, String fileName, String localPath) throws IOException {
		boolean success = false;
		try {
			File dir = new File(localPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File localFile = new File(localPath + "/" + fileName);
			localFile.lastModified();
			OutputStream is = new FileOutputStream(localFile);
			ftp.enterLocalPassiveMode();
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftp.retrieveFile(fileName, is);
			is.close();
			success = true;
		} catch (IOException e) {
			throw e;
		}
		return success;
	}

	/**
	 * ftp 删除文件
	 * 
	 * @param ftp
	 * @param fileName
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static boolean deleteFile(FTPClient ftp, String fileName) throws IOException {
		boolean success = false;
		ftp.enterLocalPassiveMode();
		ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
		FTPFile[] files = ftp.listFiles(fileName);
		if (files.length == 1) // 文件是否存在
		{
			boolean status = ftp.deleteFile(fileName);
			success = status ? true : false;
		}
		return success;
	}

	/**
	 * 关闭ftp连接
	 */
	public static void closeFtp(FTPClient ftp) {
		if (ftp != null && ftp.isConnected()) {
			try {
				ftp.logout();
				ftp.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
