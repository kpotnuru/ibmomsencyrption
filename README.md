### Encryption in IBM Sterling OMS

Audience – IBM Sterling Order Management implementers, IT Security staff and any other project stakeholders accountable for enterprise security breach.

Data Security is the first and foremost requirement of all retail implementations. Once Sterling OMS is installed all sensitive data like DB password is stored as clear text in application property files. Since encryption of property data in Sterling OMS is not provided as defaulted feature, the decisions about what type of data must be encrypted, and using what algorithm are dictated by the company security policies and threat model. You can encrypt the data after you installed the Sterling OMS.

In this article, we will discuss how encryption and decryption of passwords and other sensitive data in Sterling OMS files works. I will show you the list of steps to perform encryption of clear text value in property files. The example implementation source code Git link is provided at the end of this article. I hope this article will bring you one-step closer to achieve a robust secured retail system in your enterprise.

In IBM OMS, all property values are stored in application properties files, for example

- &lt;INSTALL\_DIR&gt;/properties/properties,
- &lt;INSTALL\_DIR&gt;/properties/properties

Here &lt;INSTALL\_DIR&gt; is your Sterling OMS installation home directory.

These property values can be encrypted and, sensitive data properties values like DB passwords in particular **must** be encrypted.

Exception to the rule: yfs.propertyencrypter.class property in the yfs.properties file mentioned cannot be encrypted.

Some other files that has sensitive data:

- Database passwords – &lt;INSTALL\_DIR&gt;/properties/jdbc.properties, &lt;INSTALL\_DIR&gt;/properties/sandbox.cfg
- Custom data – Carrier, Payment gateway connectivity credentials can be found in &lt;INSTALL\_DIR&gt;/properties/customer\_overrides.properties

Any property in the above-mentioned files can be encrypted by commenting the property from the application property file and using (overriding) it in the &lt;INSTALL\_DIR&gt;/properties/customer\_overrides.properties file.

Database passwords are the most common clear text sensitive data exposed in property files. In the article, I have chosen DB passwords as example to explain the steps for encryption/decryption process. As mentioned earlier, you can apply these steps to encrypt any property value.

**Encryption Steps:**

1. Implement the YCPEncrypter interface. For details about this interface, see the product Javadocs.  Here, in the the shared source code, the class &quot;com.perf.oms.common.utils.encryption.PERFTextEncryption&quot; implements YCPEncrypter interface.

2. Generate a random secret key (aka salt).

3. Copy the salt in a key file and copy the file to the location when Sterling OMS runtime can access it.

Note: You need to store the key file in a secure location and protect its access with the file system security. Your encryption and decryption class should have access to this key file. You should copy the key file to secured system location where sterling oms runtime class &quot;PERFTextEncryption.class&quot; can access this file.

In the example source code, the location path of the key file is added as constant so that PERFTextEncryption class can access it. You have to change constant path value to suit your system location.

4. Use the salt to create encrypted string for your DB password. Use PERFGenerateEncryptedPasswordString.class.

5. Append the property value you want to encrypt with encrypted: For example,

oraclePool.password=encrypted:QdVD5FZomxZcc/VPHkTPhDAWSz9jL25

Note: This should all be on one line with no trailing spaces. If using copy/paste, take care to not create linefeeds or add any spaces.

5. Configure the encryption/decryption class in customer\_overrides.properties file

For example:
propertyencrypter.class= com.perf.oms.common.utils.encryption.PERFTextEncryption

6. Ensure that the yfs.propertyencrypter.class property class is accessible through the CLASSPATH environment variable. Sterling OMS runtime will use this class when decryption is needed. As an example, decryption is needed when making db connection during server startup. You may package the classes  as a jar and install it as 3rd party jar.

Note: If you are getting &quot;java.security.InvalidKeyException: Illegal key size&quot; exception then you may need to download Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files for your java version. Extract the jar files from the zip and save them in ${java.home}/jre/lib/security/.

The source code contains implementation utilities java files for generating salt, encrypt password with salt to return an encrypted string. Below table explains what each file does.

1 - jasypt-1.9.2.jar - This jar will provide basic encryption capabilities with minimum effort. This jar is not included in the src repository. You may have to download it from Download section at [http://www.jasypt.org/download.html](http://www.jasypt.org/download.html) 

2. PERFTextEncryption.java -Implements the YCPEncrypter interface. Once production env, this class accesses the secret key file (KeyFile.txt) in the system folder location for salting password and decrypting. 

3. PERFGenerateSaltKey.java- Generates random salt key 

4. KeyFile.txt-Stores secret key 

5. PERFGenerateEncryptedPasswordString.java- To unit test encryption of the plain Text with secret key 

- If the Jasypt library does not provide security algorithm suitable for your company threat model, do implement higher level of security algorithm (like AES) by using java crypto package (reference provided below).
- You can write wrapping scripts utilities using encryption class for win and linux command line to quickly generate and verify encrypting and decrypting strings.
- To provide quality implementation, it is responsibility of Project Lead to ensure all sensitive information exposed in the files is encrypted before project custom code and configurations files are handed over to client for deployment &amp; testing.
- Company IT Security team must due-diligently perform regular PCA scanning on all these files in the system to ensure the passwords are encrypted and all raw sensitive data is not exposed as plain text before and after deployment.

**Git Source Code:**

 https://github.com/kpotnuru/ibmomsencyrption

**References:**

[http://www.jasypt.org/index.html](http://www.jasypt.org/index.html)

[https://stackoverflow.com/questions/1132567/encrypt-password-in-configuration-files](https://stackoverflow.com/questions/1132567/encrypt-password-in-configuration-files)

[https://docs.oracle.com/javase/7/docs/api/javax/crypto/KeyGenerator.html](https://docs.oracle.com/javase/7/docs/api/javax/crypto/KeyGenerator.html)
