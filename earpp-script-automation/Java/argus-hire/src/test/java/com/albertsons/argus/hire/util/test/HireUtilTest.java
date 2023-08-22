package com.albertsons.argus.hire.util.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.albertsons.argus.ArgusHireApplication;
import com.albertsons.argus.hire.model.HireError;
import com.albertsons.argus.hire.util.HireUtil;

@SpringBootTest(classes = ArgusHireApplication.class)
public class HireUtilTest {
    
    @Test
    void getHireErrorHTMLTest(){

        HireUtil hUtil = new HireUtil();

        List<HireError> lists = new ArrayList<>();
        HireError hireError2 = new HireError();

                            hireError2.setEmployeeNumber("20413069");
                            hireError2.setFileName("Incidents Sample Report_06302023_2.xlsx");
                            hireError2.setScenario("Employee's new hire date is before the pending worker's termination date");
                            hireError2.setErrorMessage("There is something wrong during navigate the Edit Terminal Page. Detail: Error { message='Timeout 120000ms exceeded. =========================== logs =========================== waiting for locator(\"input[id = 'pt1:atkfr1:0:rQuick:3:up1Upl:UPsp1:WnwPse:Wnw2Rgn:1:sis2:aridis::content']\") locator resolved to [          ]");

                            lists.add(hireError2);

                            hireError2 = new HireError();
                    hireError2.setEmployeeNumber("20413777");
                            hireError2.setFileName("Incidents Sample Report_06302023_2.xlsx");
                            hireError2.setScenario("Employee's new hire date is before the pending worker's termination date");
                            hireError2.setErrorMessage("runHireSchedule exception: There is something wrong during navigate the Edit Terminal Page. Detail: Error { message='Timeout 120000ms exceeded.=========================== logs =========================== waiting for locator(\"div[id = 'pt1:atkfr1:0:rQuick:3:up1Upl:UPsp1:WnwPse:PSEt1']\") to be visible ============================================================ name='TimeoutError stack='TimeoutError: Timeout 120000ms exceeded.");

                            lists.add(hireError2);

        String out = hUtil.getHireErrorHTML(lists);
        System.out.print(out);
        assertEquals(true, out!=null);
    }
}
