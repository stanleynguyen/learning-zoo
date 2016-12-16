VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "EventController"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = False
Option Explicit
Public WithEvents App As Application
Attribute App.VB_VarHelpID = -1

Private pName As String
Private sessionName As String

Private sessionId As String
Private startTime As String

Private Sub App_SlideSelectionChanged(ByVal SldRange As SlideRange)
    Dim inp As String
    
    inp = getCurrentSlideTag()
    
    If inp <> Empty Then
        TopicSetter.ComboBox1.text = inp
    End If
End Sub

Private Sub App_SlideShowEnd(ByVal Pres As Presentation)
    HttpPostRequestJson "PATCH", "/sessions/" + sessionId, _
    "{ ""end_date"" : """ & Format(Now(), "YYYY-MM-DD hh:mm:ss +0800") & """ }"
    
    HttpPostRequestJson "PATCH", "/classrooms/1", _
    "{ ""current_session_id"" : ""null"" }"
End Sub

Private Sub Class_Initialize()
    Set Me.App = Application
End Sub

Private Sub App_SlideShowBegin(ByVal window As SlideShowWindow)
    pName = window.Presentation.Name
    
    Dim idParser As New RegExp
    
    idParser.Pattern = "{""id"":(.*),""name"":"
    idParser.Global = True
    
    startTime = Format(Now(), "YYYY-MM-DD hh:mm:ss +0800")
    
    Dim json As String
    json = HttpPostRequestJson("POST", "/sessions", _
    "{ ""name"" : """ & Application.ActivePresentation.Name & _
    """, ""machine_key"" : """ & getUUID() & _
    """, ""start_date"" : """ & startTime & _
    """, ""end_date"" : """ & Format(#12/31/9999 11:59:00 PM#, "YYYY-MM-DD hh:mm:ss +0800") & _
    """, ""subject_id"" : """ & getCurrentPresentationTag() & """ }")
    
    Dim matches
    
    Set matches = idParser.Execute(json)
    sessionId = matches(0).SubMatches(0)
    
    json = HttpPostRequestJson("PATCH", "/classrooms/1", _
    "{ ""current_session_id"" : """ & sessionId & """ }")
End Sub

Private Sub App_SlideShowNextSlide(ByVal window As SlideShowWindow)
    HttpPostRequestJson "POST", "/topics", _
    "{ ""name"" : """ & getSlideTag(window.View.slide) & _
    """, ""machine_key"" : """ & getUUID() & _
    """, ""time"" : """ & Format(Now(), "YYYY-MM-DD hh:mm:ss +0800") & """ }"
    
    HttpPostRequestJson "PATCH", "/classrooms/1", _
    "{ ""current_session_id"" : """ & sessionId & """ }"
End Sub
