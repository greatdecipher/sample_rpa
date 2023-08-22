Option Explicit
Public emailFound
Public ErrMessage

Main

Sub Main
	Dim objOutlook, objShell, objNamespace, objFolder, objItems, objItem
	Dim filepath
	Dim searchSubject, emailHTML
	Dim strSender, strSentTime, strCC

On Error Resume Next	
	' Create Outlook objects
	Set objOutlook = CreateObject("Outlook.Application")
	Set objShell = WScript.CreateObject("Wscript.Shell")
	
	If objOutlook = Empty Then 

		filepath = Chr(34) & "C:\Program Files (x86)\Microsoft Office\root\Office16\OUTLOOK.EXE" & Chr(34)
		openOutlook = objShell.Run(filepath, 1, False)
		WScript.Sleep 10000
		
	End if
On Error Goto 0 
On Error Resume Next
	Set objOutlook = GetObject(, "Outlook.Application")
	
	For i = 1 To 5 ' Repeat 5 times max if Outlook cannot be found
		If objOutlook = Empty Then
			Set objOutlook = GetObject(, "Outlook.Application")
		Else
			Exit For
		End If
	Next
	Set objNamespace = objOutlook.GetNamespace("MAPI")
	
	'Test input data to be deleted
	'searchSubject = "[Action Needed] Active OOSLA Incidents 05-24-2023 Czarina Iva Esguerra"
	searchSubject = WScript.Arguments(0) ' Read the subject from the command-line argument
	
	emailFound = False
	' Get the Inbox folder
	Set objFolder = objNamespace.GetDefaultFolder(6)  ' 6 represents the Inbox folder

	' Get the "Out_of_SLA_Sent" subfolder of the Inbox
	Set objFolder = objFolder.Folders("Out_of_SLA_Sent")

	' Get all items (emails) in the folder
	Set objItems = objFolder.Items

	' Apply a filter to search for emails with a specific subject
	Set objItems = objItems.Restrict("[Subject] = '" & searchSubject & "'")
	
	' Loop through each item in the filtered items collection
	For Each objItem in objItems
		
		strSender = objItem.SenderName & " <" & objItem.SenderEmailAddress & ">"
		
		strSentTime = FormatDateTime(objItem.SentOn, vbLongDate) & " " & FormatDateTime(objItem.SentOn, vbLongTime)
		
		If Not IsNull(objItem.CC) And objItem.CC <> "" Then
			strCC = "<br><strong>Cc:</strong>" & objItem.CC & "&lt;<a href=""mailto:" & objItem.CC & """>" & objItem.CC & "</a>&gt;"
		Else
			strCC = ""
		End If
		emailHTML =   " <p>-----Original Message-----</p><br><br>" _
					& " <strong>From:</strong> " & strSender &"&lt;<a href=""mailto:" & strSender & """>" & strSender & "</a>&gt;&nbsp;" _
                    & " <br><strong>Sent:</strong> " & strSentTime & "<br>" & vbCrLf _
					& " <strong>To:</strong> " & objItem.To & "&lt;<a href=""mailto:" & objItem.To & """>" & objItem.To & "</a>&gt;" _
                    & strCC _
					& " <br><strong>Subject:</strong> " & objItem.Subject & "</p>" _
                    & " <p style='margin-right:0in;margin-left:0in;font-size:15px;font-family:""Calibri"",sans-serif;margin:0in;'>&nbsp;</p>" _
                    & vbCrLf & objItem.HTMLBody & vbCrLf & vbCrLf
		 
		emailFound = True
    
    		' Exit the loop after forwarding the email
   	 	Exit For
	Next

	' Clean up objects
	Set objItems = Nothing
	Set objFolder = Nothing
	Set objNamespace = Nothing
	Set objOutlook = Nothing
	Set searchSubject = Nothing
	
	If Err Then ErrMessage = Err.Number & " " & Err.Description	
	
	WScript.Echo emailHTML
End Sub

If emailFound Then
    WScript.Quit 0 ' Success
Else
    WScript.Quit 1 ' Email not found
End If