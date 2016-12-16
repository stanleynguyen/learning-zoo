Attribute VB_Name = "HelperFunctions"
Option Explicit
Dim ev As EventController

' IP Log File Constant
Const TEMP_CACHE As String = "temp.txt"

' Unique Tag Name for adding topics to slides
Const ZOO_TAG As String = "ZOO_TOPIC_TAG"
Const ZOO_SUBJECT As String = "ZOO_SUBJECT_TAG"

' Http Request Constants
Const SERVER As String = "https://learning-zoo.herokuapp.com"
Const SERVER_USER = Null
Const SERVER_PASSWORD As String = ""

Public Sub zooFormOpen()
    TopicSetter.Show
End Sub

Sub Auto_Open()
    Set ev = New EventController
End Sub

Function getUUID()
    Dim s As String
    Dim ssidParser As New RegExp
    
    s = ShellExecWithReturn("wmic csproduct get UUID")
    
    ' Clean Values
    ssidParser.Pattern = Chr(0)
    ssidParser.Global = True
    s = ssidParser.Replace(s, "")
    
    ssidParser.Pattern = ".*"
    ssidParser.Global = True
    
    Dim matches
    
    Set matches = ssidParser.Execute(s)
    
    s = matches(2)
    
    ssidParser.Pattern = "\s"
    s = ssidParser.Replace(s, "")
    
    getUUID = s
End Function

' Server Code

Function HttpPostRequestJson(requestType As String, endpoint As String, Optional json As String = "")

    Dim objXML As Object
    Set objXML = CreateObject("MSXML2.XMLHTTP.6.0")
    
    objXML.Open requestType, SERVER + endpoint, False, SERVER_USER, SERVER_PASSWORD
    objXML.setRequestHeader "Content-type", "application/json"
    objXML.Send json
    
    HttpPostRequestJson = objXML.responseText
    
End Function

' Topic Setter, Getter


Function getCurrentPresentationTag() As String
    getCurrentPresentationTag = Application.ActivePresentation.tags(ZOO_SUBJECT)
End Function

Function setCurrentPresentationTag(ByVal subj As String)
    ActivePresentation.tags.Add "ZOO_SUBJECT_TAG", subj
End Function

Function getSlideTag(slide As slide) As String
    getSlideTag = slide.tags(ZOO_TAG)
End Function

Function getCurrentSlideTag() As String
    On Error GoTo Handler
    getCurrentSlideTag = Application.ActiveWindow.View.slide.tags(ZOO_TAG)
    
Finally:
    On Error GoTo 0
    Exit Function
    
Handler:
    On Error GoTo 0
    getCurrentSlideTag = ""
    Resume Finally
    
End Function

Function setCurrentSlideTag(ByVal assign As String)
    On Error Resume Next
    setCurrentSlideTag = Application.ActiveWindow.View.slide.tags.Add(ZOO_TAG, assign)
    On Error GoTo 0
End Function

Function getAllSlideTags()
    Dim s As slide
    ReDim get_tags(ActivePresentation.Slides.Count) As String
    Dim i As Integer
    
    i = 0
    For Each s In ActivePresentation.Slides
        get_tags(i) = s.tags(ZOO_TAG)
        i = i + 1
    Next
    
    getAllSlideTags = get_tags
End Function

' IP Sweep Code

Sub getAllIP()
    Dim s As Variant
    For Each s In getAllNetworkName()
        Debug.Print s
    Next
End Sub

Function getAllNetworkName()
    Dim raw_ips As String
    raw_ips = ShellExecWithReturn("netsh wlan show networks")
    
    Dim ssidParser As New RegExp
    
    ssidParser.Pattern = "SSID [0-9]+ : ([^\s\.]*)"
    ssidParser.Global = True
    
    Dim matches
    Dim i As Integer
    
    If ssidParser.test(raw_ips) Then
        Set matches = ssidParser.Execute(raw_ips)
        
        ReDim output(matches.Count) As String
        
        For i = 0 To matches.Count - 1
            output(i) = matches(i).SubMatches(0)
        Next
        
        getAllNetworkName = output
    
    End If
    
End Function

Function ShellExecWithReturn(ByVal command As String)
    
    Dim wsh As Object
    Set wsh = VBA.CreateObject("WScript.Shell")
    
    wsh.Run "cmd /c " + command + " > " + TEMP_CACHE, 0, True
    
    Dim strOut As String
    Dim fileObj As Object
    Set fileObj = VBA.CreateObject("Scripting.FileSystemObject")
    
    strOut = fileObj.OpenTextFile(TEMP_CACHE).ReadAll()
    
    fileObj.DeleteFile TEMP_CACHE
    
    ShellExecWithReturn = strOut

End Function
