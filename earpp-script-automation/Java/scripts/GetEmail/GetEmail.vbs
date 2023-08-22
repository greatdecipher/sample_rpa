'''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
' NAME:         GetEmail.vbs
' AUTHOR:       Alex Robes
' CREATED:      2022/02/09
' PURPOSE:      Get email details/attachments using set parameters
' RESTRICTIONS: Outlook client must be installed
' LOG FILES:    None
' INPUT :  		Outlook folder address|Email Subject|MarkRead True/False|GetAll True/False|Mail Parameter|Target SaveAs path|File filter|Overwrite True/False
'				MarkUnread - True will mark found mail item as Read
'               Overwrite - False will rename downloaded files to Filename_1.ext, Filename_2.ext, etc.
'				GetAll - False will only process the first unread email and ignore the rest	  
' OUTPUT : 		None if successful.
'				ErrMessage if an exception occured
' USAGE: 		All arguments besides Folder Address and Email Subject are optional
'               GetEmail.vbs "\\MyMailbox.Name@safeway.com\Inbox\SubFolder|Subject|MailParam|True|C:\Automations\Folder\|.xlsx|False|False"
'				Download attachment: cscript.exe /nologo GetEmail.vbs "\\Alexander.Robes@safeway.com\Inbox\WICMapping|WIC_Mapping|False|False|MailSubject,MailBody>C:\Automations\WICMapping\|C:\Automations\WICMapping\|.dat|True"
'				Get info		   : cscript.exe /nologo GetEmail.vbs "\\Alexander.Robes@safeway.com\Inbox\MerakiARPRequest|Meraki ARP Request|False|True|MailSubject"
'				Get multiple info  : cscript.exe /nologo C:\Users\arob111\Desktop\Tasks\Utilities\GetEmail\GetEmail.vbs "\\Alexander.Robes@safeway.com\Inbox\MerakiARPRequest|Meraki ARP Request|MailSubject,MailCc|False|True"	
' HISTORY:
' NAME            DATE         DESCRIPTION
' --------------- ----------   ---------------------------------------------------
' Alex Robes      2022/02/09   Initial creation
' Alex Robes      2022/02/09   Added WIC
' Alex Robes      2022/02/10   Added Support for multiple email info
'''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
Option Explicit
Public Const IS_DEBUG = False
Public Const OUTVAL_DELIM = "~"
Public Const OUTSUBVAL_DELIM = "^"
Public Const DETAIL_DELIM = ","
Public Const OUTFILE_DELIM = ">"
Public Const OUTFILE_EXT = ".txt"

Public oTargetFolder
Public ErrMessage

Main
If Err Then WScript.StdOut.Write ErrMessage
WScript.Quit

Sub Main
	Dim oOutlook, oNamespace, oInbox, oMailItem, oAttachment 
	Dim sFolderAddress, sSavePath, sFileExt, sOverwrite, sSubject, sGetAll, sSaveFileName, sDetailParam, sMarkRead, sReturn, sSubReturn, sDetailFile, sDetail, sMailBody
	Dim bHasAttachment
	Dim iFileCtr
	Dim arrParams, arrDetails

'On Error Resume Next
'	Set oOutlook = GetObject(, "Outlook.Application")
'	If oOutlook = Empty Then 
	 	Set oOutlook = CreateObject("Outlook.Application")
'	End if
'On Error Goto 0
On Error Resume Next	
	Set oNamespace = oOutlook.GetNamespace("MAPI")
	WSCript.Sleep 2000
	'oNamespace.Logon
    oNamespace.Logon("Outlook")
	oNamespace.SendAndReceive(True)
	Set oInbox = oNamespace.GetDefaultFolder(6) '6 = olFolderInbox

On Error Resume Next	
	If Not IS_DEBUG Then arrParams = Split(Trim(WScript.Arguments.Item(0)), "|")	
	sFolderAddress = arrParams(0)
	sSubject = arrParams(1)
	Select Case sSubject
		Case "Meraki ARP Request"
						'Call Meraki Java here
		Case "has been assigned to group Service Delivery - Retail Inv & Order Mgmt"
						'Call TMAToggle Java here
		Case "WIC_Mapping"
						bHasAttachment = True
						'Call WIC Java here
		Case "AS_Automation - Run_DB_Revocation"
						bHasAttachment = True
						'Call RunDB Java here
		Case "?"
						WScript.StdOut.Write "Not yet implemented"
						Call Main()
						Exit sub
		Case Else						     
	End Select
	sMarkRead = arrParams(2)
	sGetAll = arrParams(3)
	sDetailParam = arrParams(4)
	If bHasAttachment Then
		sSavePath = arrParams(5)
		sFileExt = arrParams(6)
		sOverwrite = arrParams(7)
	End If
On Error Goto 0
	GetSubfolders oInbox, sFolderAddress
	iFileCtr = 1
	
	For Each oMailItem in oTargetFolder.Items
	    If oMailItem.Unread Then
	    	If InStr(oMailItem.Subject, sSubject) Then
	    		If sMarkRead = "True" Then oMailItem.Unread = False
	    		If bHasAttachment Then	    	
			        For Each oAttachment in oMailItem.Attachments	        	
			        	If InStr(UCase(oAttachment.FileName), UCase(sFileExt)) Then
			        		If sOverwrite <> "True" Then
			        			sSaveFileName = oAttachment.FileName
			        			Do While FileExists(sSavePath & sSaveFileName) 
			        				sSaveFileName = Replace(oAttachment.FileName , sFileExt, "_" & iFileCtr & sFileExt)
			        				iFileCtr = iFileCtr + 1		        						        				
			        			Loop
			            		oAttachment.SaveAsFile sSavePath & sSaveFileName
			            	Else
			            		oAttachment.SaveAsFile sSavePath & oAttachment.FileName
			            	End If		            			            	
			            End If		            	
			        Next
			    End If
		    	If Len(sDetailParam) > 0 Then 'asking for return, so pass the info to the console
		    		arrDetails = Split(Trim(sDetailParam), DETAIL_DELIM)
		    		If Len(sReturn) > 1 Then sReturn = sReturn & OUTVAL_DELIM
		    		 
		    		For Each sDetail In arrDetails		    			
		    			If InStr(sDetail, OUTFILE_DELIM) Then 
							sDetailFile = Mid(sDetail, (InStr (sDetail, OUTFILE_DELIM)) + 1, Len(sDetail) - InStr(sDetail, OUTFILE_DELIM))
							sDetail = Left(sDetail, (InStr (sDetail, OUTFILE_DELIM)) - 1)
							sDetailFile = sDetailFile & sDetail & OUTFILE_EXT
						End If
						Select Case sDetail  
							Case "MailSubject"
								sSubReturn = oMailItem.Subject
							Case "MailTo"
								sSubReturn = oMailItem.To
				    		Case "MailFrom"
								sSubReturn = oMailItem.From
							Case "MailCc"
								sSubReturn = oMailItem.CC
				    		Case "MailBody"
				    			If Len(sDetailFile) Then 'set to pass value to file instead of console
				    				WriteFile oMailItem.Body, sDetailFile
				    			Else
				    				sSubReturn = oMailItem.Body
				    			End if				    			
				    		Case Else
								WScript.StdOut.Write "Unknown mail parameter"
						End Select
						If Len(sSubReturn) > 1 Then sReturn = sReturn & sSubReturn & OUTSUBVAL_DELIM
						sSubReturn = ""
					Next
				End If
				If Right(sReturn,1) = OUTSUBVAL_DELIM Then sReturn = Left(sReturn, Len(sReturn) - 1)
				If sGetAll <> "True" Then Exit For
		    End If
	    End If	    
	Next
	
	If Len(sReturn) > 0 Then WScript.StdOut.Write sReturn
	
	Set oOutlook = Nothing
	Set oNamespace = Nothing
	Set oInbox = Nothing
If Err Then ErrMessage = Err.Number & " " & Err.Description		
End Sub

Sub GetSubfolders(oParentFolder, sPathAddress)
Dim colFolders, oFolder, oSubfolder
	Set colFolders = oParentFolder.Folders
	For Each oFolder in colFolders
		Set oSubfolder = oParentFolder.Folders(oFolder.Name)
		If InStr(oFolder.Folderpath, sPathAddress) Then 
			Set oTargetFolder = oFolder
			Exit For
		End If
		GetSubfolders oSubfolder, sPathAddress
	Next
Set colFolders = Nothing	
End Sub

Function FileExists(sFile)
On Error Resume Next
Dim oFso
	Set oFso = CreateObject("Scripting.FileSystemObject")
		If oFso.FileExists (sFile) Then
			FileExists = True
		End If
	Set oFso = Nothing
On Error GoTo 0
End Function

Sub WriteLog(ByVal sMessage, sLogFile, sMode)
Dim oFso, oFile
    Set oFso = CreateObject("Scripting.FileSystemObject")
    If oFso.FileExists (sLogFile) Then
        Set oFile = oFso.OpenTextFile(sLogFile, 8) '8 = FOR_APPENDING
    Else
        Set oFile = oFso.CreateTextFile(sLogFile, True) 'True = Overwrite
    End If
    
  	Select Case sMode
		Case 0
			Exit Sub
		Case 1
			sMode = "DEBUG  : "
		Case 2
			sMode = "ERROR  : "
		Case 3
			sMode = "WARNING: "
		Case Else
			sMode = "INFO   : "
	End Select
  
    oFile.WriteLine Now & " " & sMode & " " &  sMessage
	oFile.Close
	Set oFile = Nothing
	Set oFso = Nothing
End Sub

Sub WriteFile(ByVal sMessage, sFile)
Dim oFso, oFile
    Set oFso = CreateObject("Scripting.FileSystemObject")
    Set oFile = oFso.CreateTextFile(sFile, True) 'True = Overwrite
    	oFile.WriteLine sMessage
		oFile.Close
	Set oFile = Nothing
	Set oFso = Nothing
End Sub

Function GetNull(sInStr)
	
End Function