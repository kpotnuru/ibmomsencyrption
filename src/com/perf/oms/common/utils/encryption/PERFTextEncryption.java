/*
 * 
 * Copyright (c) 2007-2010, The JASYPT team (http://www.jasypt.org)
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Copyright [2017] [Kiran Potnuru] [Perficient Inc.]
 * 
 */

package com.perf.oms.common.utils.encryption;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.io.IOUtils;
import org.jasypt.util.text.StrongTextEncryptor;

import com.perf.oms.common.base.PERFConstants;
import com.yantra.ycp.japi.util.YCPEncrypter;
import com.yantra.yfc.log.YFCLogCategory;

public class PERFTextEncryption implements YCPEncrypter {

	private static final String ENCODING = "UTF-8";
	private final YFCLogCategory perfLogger = YFCLogCategory
			.instance("com.yantra.yfc.log.YFCLogCategory");

	StrongTextEncryptor textEncryptor = new StrongTextEncryptor();

	private String saltFile;

	public PERFTextEncryption() {
		this.saltFile = PERFConstants.SALT_KEY_FILE;
	}

	public PERFTextEncryption(String saltKeyFile) {
		this.saltFile = saltKeyFile;
	}

	public String encrypt(String plainText) throws IOException {

		return encrypt(plainText, getSaltStringFromFile());

	}

	public String encrypt(String plainText, String salt) {

		textEncryptor.setPassword(salt);
		return textEncryptor.encrypt(plainText);

	}

	public String decrypt(String encryptedText)
			throws IllegalBlockSizeException, BadPaddingException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IOException,
			InvalidAlgorithmParameterException, InvalidKeySpecException {

		return decrypt(encryptedText, getSaltStringFromFile());
	}

	public String decrypt(String encryptedText, String salt) {

		textEncryptor.setPassword(salt);
		return textEncryptor.decrypt(encryptedText);

	}

	private String getSaltStringFromFile() throws IOException {

		InputStream is = null;
		try {

			is = this.getClass().getResourceAsStream(saltFile);

			return IOUtils.toString(is, ENCODING);

		} catch (IOException | NullPointerException e) {
			throw e;
		} finally {
			IOUtils.closeQuietly(is);
		}

	}

	protected void debugLog(String message) {
		perfLogger.debug(message);
	}
}