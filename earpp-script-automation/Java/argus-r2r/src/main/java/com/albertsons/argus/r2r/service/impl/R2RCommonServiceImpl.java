package com.albertsons.argus.r2r.service.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import com.albertsons.argus.r2r.exception.ArgusR2RException;
import com.albertsons.argus.r2r.service.R2RCommonService;

@Service
public class R2RCommonServiceImpl implements R2RCommonService {
	public static final Logger LOG = LogManager.getLogger(R2RCommonServiceImpl.class);

	@Autowired
	Environment environment;

	@Override
	public String getMfaCode(String pythonScript, String secretKey){
		LOG.log(Level.DEBUG, () -> "start getMfaCode method. . .");
		
		try {
			String []cmd = {"python", pythonScript, secretKey};
			
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec(cmd);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String s = "", mfaCode = "";
			
			while((s = in.readLine()) != null){
				mfaCode = s.trim();

				if (mfaCode.length() == 6){ // make sure it's 6 digits
					return mfaCode;
				}
			}

		} catch (Exception e){
			LOG.log(Level.DEBUG, () -> "error retrieving mfa code. . .");
			LOG.error(e);
		}

		return null;
	}

	@Override
	public boolean checkIfFileIsRunning(String folder, String filename) throws ArgusR2RException {
		LOG.log(Level.DEBUG, () -> "start checkIfFileIsRunning method. . .");
		
		try {
			File folderPath = new File(folder);
		
			if (folderPath.isDirectory()) {
				for (File file: folderPath.listFiles()) {
					if (file.getName().contains(filename) || file.getName().equalsIgnoreCase(filename)){
						return true;
					}
				}
			}

			return false;

		} catch (Exception e){
			LOG.log(Level.INFO, () -> "error checking if file is currently running. . .");
			throw new ArgusR2RException(e.getMessage());
		}
	}
	
}
