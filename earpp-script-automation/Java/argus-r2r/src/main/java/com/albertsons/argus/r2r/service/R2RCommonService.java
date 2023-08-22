package com.albertsons.argus.r2r.service;

import com.albertsons.argus.r2r.exception.ArgusR2RException;

public interface R2RCommonService {

    public String getMfaCode(String pythonScript, String secretKey);
    
    public boolean checkIfFileIsRunning(String folder, String filename) throws ArgusR2RException;

}
