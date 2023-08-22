' Description: This script copies the log file to the destination location.

' Usage:
' Execute script with 4 arguments
' WScript.Arguments.Item(0) --> Source Folder
' WScript.Arguments.Item(1) --> Filename (Ex. argus-rundb-)
' WScript.Arguments.Item(2) --> File Extension (Ex. .log)
' WScript.Arguments.Item(3) --> Destination Folder (Add a backslash at the end)

' Notes:
' Avoid using common filenames (Ex. argus)
' Instead, use specific filenames (Ex. argus-rundb-)

Option Explicit

Main

Sub Main
Dim fso
Dim fileToMove
Dim sourceFolder
Dim fileName
Dim fileExtension
Dim destFolder
Dim objFolder
Dim colFiles
Dim objFile

Set fso = CreateObject("Scripting.FileSystemObject")

sourceFolder = WScript.Arguments.Item(0)
fileName = WScript.Arguments.Item(1)
fileExtension = WScript.Arguments.Item(2)
destFolder = WScript.Arguments.Item(3)

If ( fso.FolderExists(sourceFolder) ) Then
	
	Set objFolder = fso.GetFolder(sourceFolder)

	Set colFiles = objFolder.Files
	For Each objFile in colFiles
	   If instr(objFile.Name, fileName) <> 0 AND instr(objFile.Name, fileExtension) <> 0 Then
		   If ( fso.FolderExists(destFolder) ) Then
				fso.CopyFile objFile.Name, destFolder
			End If
	   End If
	Next
	
End If
End Sub