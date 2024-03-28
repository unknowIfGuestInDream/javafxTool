/*
 * Copyright (c) 2024 unknowIfGuestInDream.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of unknowIfGuestInDream, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL UNKNOWIFGUESTINDREAM BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.tlcsdm.core.util;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.EncryptionMethod;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

/**
 * Zip4J test.
 */
@DisabledIfSystemProperty(named = "workEnv", matches = "ci")
class Zip4jTest {

	private String parentPath = "C:\\work\\test\\zip\\";
	private String testZip = "issue_1273.zip";
	private String result = "result.zip";

	/**
	 * Creating a zip file with single file in it / Adding single file to an
	 * existing zip.
	 */
	@Test
	void add() throws ZipException {
		new ZipFile(parentPath + testZip).addFile(parentPath + "add.txt");
	}

	/**
	 * Creating a zip file with multiple files / Adding multiple files to an
	 * existing zip.
	 */
	@Test
	void create() throws ZipException {
		new ZipFile(parentPath + "filename.zip").addFiles(
				Arrays.asList(new File(parentPath + "first_file.txt"), new File(parentPath + "second_file.txt")));
	}

	/**
	 * Creating a zip file by adding a folder to it / Adding a folder to an existing
	 * zip
	 */
	@Test
	void addFolder() throws ZipException {
//		ExcludeFileFilter excludeFileFilter = filesToExclude::contains;
//		ZipParameters zipParameters = new ZipParameters();
//		zipParameters.setExcludeFileFilter(excludeFileFilter);
//		new ZipFile("filename.zip").addFolder(new File("/users/some_user/folder_to_add"), zipParameters)
		new ZipFile(parentPath + "filename.zip").addFolder(new File(parentPath + "folder"));
	}

	/**
	 * Creating a password protected zip file / Adding files to an existing zip with
	 * password protection
	 * <p>
	 * AES encryption
	 */
	@Test
	void password() throws ZipException {
		ZipParameters zipParameters = new ZipParameters();
		zipParameters.setEncryptFiles(true);
		zipParameters.setEncryptionMethod(EncryptionMethod.AES);
		// Below line is optional. AES 256 is used by default. You can override it to
		// use AES 128. AES 192 is supported only for extracting.
		zipParameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);
		List<File> filesToAdd = Arrays.asList(new File(parentPath + "first_file.txt"),
				new File(parentPath + "second_file.txt"));
		ZipFile zipFile = new ZipFile(parentPath + "password.zip", "o4+j4fh/UbVZ8yUaZSXZ/6PvCFbgWalZ".toCharArray());
		zipFile.addFiles(filesToAdd, zipParameters);
	}

	/**
	 * Creating a password protected zip file / Adding files to an existing zip with
	 * password protection
	 * <p>
	 * AES encryption
	 */
	@Test
	void encrypt() throws IOException {
		new ZipFile(parentPath + testZip).extractAll(parentPath + "/tmp");

		ZipParameters zipParameters = new ZipParameters();
		zipParameters.setEncryptFiles(true);
		zipParameters.setEncryptionMethod(EncryptionMethod.AES);
		// Below line is optional. AES 256 is used by default. You can override it to
		// use AES 128. AES 192 is supported only for extracting.
		zipParameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);
		ZipFile zipFile = new ZipFile(parentPath + result, "o4+j4fh/UbVZ8yUaZSXZ/6PvCFbgWalZ".toCharArray());
		zipFile.addFolder(new File(parentPath + "/tmp"), zipParameters);
	}

	@Test
	void dencrypt() throws IOException {
		new ZipFile(parentPath + result, "o4+j4fh/UbVZ8yUaZSXZ/6PvCFbgWalZ".toCharArray())
				.extractAll(parentPath + "/result");
	}

	/**
	 * Creating a split zip file.
	 * <p>
	 * Zip 文件格式指定最小 65536 字节 （64KB） 作为拆分文件的最小长度。Zip4j 将抛出一个 如果指定了小于此值的任何值，则为
	 * exception。
	 */
	@Test
	void split() throws ZipException {
		List<File> filesToAdd = Arrays.asList(new File(parentPath + "first_file"), new File(parentPath + "second"));
		ZipFile zipFile = new ZipFile(parentPath + "filename.zip");
		// using 10MB in this example
		zipFile.createSplitZipFile(filesToAdd, new ZipParameters(), true, 10485760);
	}

	/**
	 * 创建具有密码保护的拆分 zip.
	 */
	@Test
	void splitPass() throws ZipException {
		ZipParameters zipParameters = new ZipParameters();
		zipParameters.setEncryptFiles(true);
		zipParameters.setEncryptionMethod(EncryptionMethod.AES);
		List<File> filesToAdd = Arrays.asList(new File(parentPath + "first_file"), new File(parentPath + "second"));
		ZipFile zipFile = new ZipFile(parentPath + "filename.zip");
		// using 10MB in this example
		zipFile.createSplitZipFile(filesToAdd, new ZipParameters(), true, 10485760);
	}

	/**
	 * 从 zip 中提取所有文件
	 */
	@Test
	void extract() throws ZipException {
		new ZipFile(parentPath + "filename.zip").extractAll(parentPath + "/extract");
	}

	/**
	 * 从受密码保护的 zip 中提取所有文件
	 */
	@Test
	void extractPass() throws ZipException {
		new ZipFile(parentPath + "password.zip", "o4+j4fh/UbVZ8yUaZSXZ/6PvCFbgWalZ".toCharArray())
				.extractAll(parentPath + "/extractPass");
	}

}
