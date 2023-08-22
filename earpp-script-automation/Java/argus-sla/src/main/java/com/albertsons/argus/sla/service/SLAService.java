package com.albertsons.argus.sla.service;

import java.util.List;
import java.util.Map;

import com.albertsons.argus.sla.dto.SLAReport;
import com.albertsons.argus.sla.exception.SLAException;

public interface SLAService {
    public boolean checkIfReportExists();
    public Map<String, List<SLAReport>> readExcel() throws SLAException;
    public Integer prepareAndSendReport(Map<String, List<SLAReport>> reportMap) throws SLAException;
    public void sendMailNotification(String notif, int num);
}
