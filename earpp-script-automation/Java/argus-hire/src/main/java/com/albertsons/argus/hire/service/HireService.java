package com.albertsons.argus.hire.service;

import java.util.List;

import com.albertsons.argus.hire.exception.HireException;
import com.albertsons.argus.hire.model.Hire;
import com.albertsons.argus.mail.exception.ArgusMailException;

public interface HireService {
    public final String SCENERIO_1 = "Straight Through Processing";
    public final String SCENERIO_2 = "Synchronization From Position";
    public final String SCENERIO_3 = "new hire date is before the pending";

    public String hireRehireDate(String scenario, String requestNumber, String empId, String assignNumber, String newHireDt) throws HireException;
    public void syncPositionRowExistsScenario(String scenario, String requestNumber, String empId,
            String assignNumber, String newHireDt) throws HireException;
    public void empNewHireDateBeforePendingWorkScenario(String scenario, String requestNumber, String empId,
            String assignNumber, String newHireDt) throws HireException;
    public List<Hire> getHireContentList(String fileName) throws HireException; 

    public void sendReHire(String subject, String message, String pathString) throws ArgusMailException;

}
