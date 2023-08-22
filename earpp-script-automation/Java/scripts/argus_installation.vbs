StartArgusSetup

Sub StartArgusSetup
              'Call CopyAutomationToDriveC
              Call SetEnvironmentVariables
              Call ImportXML_to_TaskScheduler
              'Call RunJARFiles
              
              wscript.echo "Setup Complete"
End Sub

Sub CopyAutomationToDriveC
              'Copy entire folder to Drive C
              dim filesys
              set filesys=CreateObject("Scripting.FileSystemObject")
'myTargetFolder="\\phnaarr31.safeway.com\d1msrd\ARBTN00\Downloads\Backup\Automations"
myTargetFolder="\\phnaarr31.safeway.com\d1msrd\BRBTN00\Downloads\Backups\Automations"
              If filesys.FolderExists(myTargetFolder) Then
                             filesys.CopyFolder myTargetFolder, "C:\"
              End If
End Sub

Sub SetEnvironmentVariables
              'Set Environment Variables
              Set objShell = CreateObject("WScript.Shell")
              Set objEnv = objShell.Environment("User")

              'setup for TMA
              PathToAdd = "C:\Automations\Argus\TMA"
              objEnv("home_deploy") = PathToAdd
              
              'setup for MIM_Monitoring
              PathToAdd = "C:\Automations\Argus\WICMapping"
              objEnv("WIC_home_deploy") = PathToAdd

              ''setup for MIM_Monitoring
              'PathToAdd = "C:\Automations\Argus\MIM_Monitoring"
              'objEnv("home_deploy_MIM_Monitoring") = PathToAdd
              
              ''setup for PLAYWRIGHT_BROWSERS_PATH
              'PathToAdd = "C:\Playwright\cache"
              'objEnv("PLAYWRIGHT_BROWSERS_PATH") = PathToAdd
              
              ''setup for PLAYWRIGHT_SKIP_BROWSER_DOWNLOAD
              'PathToAdd = "0"
              'objEnv("PLAYWRIGHT_SKIP_BROWSER_DOWNLOAD") = PathToAdd
End Sub

Sub ImportXML_to_TaskScheduler          
              Dim sXMLFolder, sXMLFile
              'sXMLFolder = "\\phnaarr31.safeway.com\d1msrd\ARBTN00\Downloads\Deploy\task scheduler\prod"
              'sXMLFolder = "\\phnaarr31.safeway.com\d1msrd\BRBTN00\Downloads\Backups\Task Scheduler"
               sXMLFolder = "C:\Automations\Task Scheduler"
              Set oFSO = CreateObject("Scripting.FileSystemObject")

              For Each oFile In oFSO.GetFolder(sXMLFolder).Files
                             If UCase(oFSO.GetExtensionName(oFile.Name)) = "XML" Then
                                           Dim oSh                                           
                                           sXMLFile = oFile
                                           c_APP_NAME = Replace(Replace(Replace(oFile.Name, "_", " "), ".xml", ""), ".XML", "")
                                           Set oSh = CreateObject("WScript.Shell")
                                           oSh.Run "schtasks /Create /XML """ & sXMLFile & """ /TN """ & c_APP_NAME & """"
                                           oSh.Run "%windir%\system32\taskschd.msc /s"
                                           Set oSh = Nothing
                                           'Exit For
                             End if
              Next      
End Sub

Sub RunJARFiles
              Call RunJARperCMD("java -jar -Dspring.profiles.active=esedAll C:\Automations\Argus\TMA\argus-automation-tma-0.0.1-SNAPSHOT-exec-latest.jar", "C:\Automations\Argus\TMA")
              Call RunJARperCMD("java -jar -Dspring.profiles.active=esedRsil C:\Automations\Argus\TMA\argus-automation-tma-0.0.1-SNAPSHOT-exec-latest.jar", "C:\Automations\Argus\TMA")
              Call RunJARperCMD("java -jar -Dspring.profiles.active=esedRsil2 C:\Automations\Argus\TMA\argus-automation-tma-0.0.1-SNAPSHOT-exec-latest.jar", "C:\Automations\Argus\TMA")
              Call RunJARperCMD("java -jar -Dspring.profiles.active=so C:\Automations\Argus\TMA\argus-automation-tma-0.0.1-SNAPSHOT-exec-latest.jar", "C:\Automations\Argus\TMA")
              Call RunJARperCMD("java -jar C:\Automations\Argus\Run_DB\argus-rundb-0.0.1-SNAPSHOT-exec.jar", "C:\Automations\Argus\Run_DB")
End Sub

Function RunJARperCMD(sJARcommand, folderToRunIn)
              Dim wshShell
              On Error Resume Next
                 Set wshShell = WScript.CreateObject("WSCript.shell")
                 If Err.Number <> 0 Then
                               Wscript.Quit
                 End If
				 ' wshShell.Run "cmd.exe /C cd C:\Users\BRBTN00"
                 ' wshShell.Run "cmd.exe /C " & sJARcommand
				 
				 wshShell.Run "%comspec% /k C: & cd " & folderToRunIn & " & " & sJARcommand
                 'msgbox "DONE"
              On Error Goto 0
End Function
