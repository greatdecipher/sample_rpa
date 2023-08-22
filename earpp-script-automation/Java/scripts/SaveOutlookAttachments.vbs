'''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
' NAME:         SaveOutlookAttachments.vbs
' AUTHOR:       Alex Robes
' CREATED:      2021/04/05
' PURPOSE:      Save email attachments using set parameters
' RESTRICTIONS: Outlook client must be installed
' LOG FILES:    None
' INPUT :  		Outlook folder address|Email Subject|Target SaveAs path|File filter|Overwrite True/False|DownloadAll True/False
'				Overwrite - False will rename downloaded files to Filename_1.ext, Filename_2.ext, etc.
'				DownloadAll - False will only download for the first unread email and ignore the rest	  
' OUTPUT : 		None if successful.
'				ErrMessage if an exception occured
' USAGE: 		SaveOutlookAttachments.vbs "\\MyMailbox.Name@safeway.com\Inbox\SubFolder|Subject|C:\Automations\Folder\|.xlsx|False|False"
'				SaveOutlookAttachments.vbs "\Inbox\RunDBRevocation|Run_DB_Revocation|C:\Automations\RunDBRevocation\|.xlsx|False|True"
'
' HISTORY:
' NAME            DATE         DESCRIPTION
' --------------- ----------   ---------------------------------------------------
' Alex Robes      2021/04/05   Initial creation
' Alex Robes      2021/04/09   Added DownloadAll feature
'
'''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
Option Explicit
Public oTargetFolder
Public ErrMessage

Main

Sub Main
	Dim oOutlook, oNamespace, oInbox, oMailItem, oAttachment 
	Dim sFolderAddress, sSavePath, sFileExt, sOverwrite, sSubject, sIsDownloadAll, sSaveFileName
	Dim iFileCtr
	Dim arrParams0, arrParams1, arrParams2, arrParams3, arrParams4, arrParams5, arrParams6, arrParams7
	
	Dim filepath
	Dim oShell
	Dim openOutlook
	
On Error Resume Next
	Set oOutlook = GetObject(, "Outlook.Application")
	Set oShell = WScript.CreateObject("Wscript.Shell")
		
	If oOutlook = Empty Then 

		filepath = Chr(34) & "C:\Program Files (x86)\Microsoft Office\root\Office16\OUTLOOK.EXE" & Chr(34)
		openOutlook = oShell.Run(filepath, 1, False)
		WScript.Sleep 10000
		
	End if
On Error Goto 0 
On Error Resume Next	
	Set oOutlook = GetObject(, "Outlook.Application")
	
	For i = 1 To 5 ' Repeat 5 times max if Outlook cannot be found
		If oOutlook = Empty Then
			Set oOutlook = GetObject(, "Outlook.Application")
		Else
			Exit For
		End If
	Next
	
	Set oNamespace = oOutlook.GetNamespace("MAPI")
	WScript.Sleep 2000
    oNamespace.Logon("Outlook")
	oNamespace.SendAndReceive(True)
	Set oInbox = oNamespace.GetDefaultFolder(6) '6 = olFolderInbox
	arrParams0 = Split(WScript.Arguments.Item(0), "|") '\\Inbox\\Weekly_Closure_Chg, [Scheduled
	arrParams1 = WScript.Arguments.Item(1) 'Report]
	arrParams2 = WScript.Arguments.Item(2) 'Production
	arrParams3 = WScript.Arguments.Item(3) 'Changes
	arrParams4 = WScript.Arguments.Item(4) 'with
	arrParams5 = WScript.Arguments.Item(5) 'Closure
	arrParams6 = WScript.Arguments.Item(6) 'Policy
	arrParams7 = Split(WScript.Arguments.Item(7), "|") 'Violation, C:\\Automations\\Weekly_Closure_Chg\\ , .xlsx, True, True
	
	sFolderAddress = arrParams0(0)
	sSubject = arrParams0(1)+" "+arrParams1 + " " + arrParams2 + " "+ arrParams3 + " " +arrParams4 + " " +arrParams5+ " " +arrParams6+ " " +arrParams7(0)
	sSavePath = arrParams7(1)
	sFileExt = arrParams7(2)
	sOverwrite = arrParams7(3)
	sIsDownloadAll = arrParams7(4)
	
	GetSubfolders oInbox, sFolderAddress
	iFileCtr = 1
	
	For Each oMailItem in oTargetFolder.Items
	    If oMailItem.Unread Then
	    	If InStr(oMailItem.Subject, sSubject) Then
		        For Each oAttachment in oMailItem.Attachments	        	
		        	If InStr(oAttachment.FileName, sFileExt) Then
		        		If sOverwrite = "False" Then
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
				oMailItem.Unread = False
		    End If
			Exit For
        	' If sIsDownloadAll <> "True" Then Exit For
	    End If	    
	Next
	
	Set oOutlook = Nothing
	Set oNamespace = Nothing
	Set oInbox = Nothing
If Err Then ErrMessage = Err.Number & " " & Err.Description		
End Sub

Sub GetSubfolders(oParentFolder, sPathAddress)
Dim oFolder, oSubfolder
	Set oTargetFolder = oParentFolder  ' by default, it is Inbox
	For Each oFolder in oParentFolder.Folders
		Set oSubfolder = oParentFolder.Folders(oFolder.Name)
		If InStr(oFolder.Folderpath, sPathAddress) Then ' if it is the correct folder under Inbox 
			Set oTargetFolder = oFolder
			Exit For
		End If
		GetSubfolders oSubfolder, sPathAddress
	Next
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