VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} TopicSetter 
   Caption         =   "Learning Zoo"
   ClientHeight    =   1032
   ClientLeft      =   108
   ClientTop       =   456
   ClientWidth     =   4584
   OleObjectBlob   =   "TopicSetter.frx":0000
   ShowModal       =   0   'False
   StartUpPosition =   1  'CenterOwner
End
Attribute VB_Name = "TopicSetter"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Private Sub ComboBox1_Change()
    setCurrentSlideTag (ComboBox1.text)
End Sub

Private Sub ComboBox1_DropButtonClick()
    Dim tag As Variant
    Dim i As Integer
    Dim hasDup As Boolean
    Dim member As Variant
    
    While ComboBox1.ListCount > 0
        ComboBox1.RemoveItem (0)
    Wend
    
    For Each tag In getAllSlideTags()
        hasDup = False
        If Not IsNull(ComboBox1.List) Then
            For Each member In ComboBox1.List
                If member = tag Then hasDup = True
                If hasDup Then Exit For
            Next
        End If
        
        If Not hasDup Then
            ComboBox1.AddItem (tag)
        End If
    Next
End Sub

Private Sub ComboBox2_Change()
    Select Case ComboBox2.text
        Case "50.001 Introduction to Information Systems"
            setCurrentPresentationTag ("1")
        Case "50.002 Computational Structures"
            setCurrentPresentationTag ("2")
        Case "50.004 Introduction to Algorithms"
            setCurrentPresentationTag ("3")
    End Select
End Sub

Private Sub UserForm_Initialize()
    Dim inp As String
    
    inp = getCurrentSlideTag()
    
    If inp <> Empty Then
        ComboBox1.text = inp
    End If
    
    inp = getCurrentPresentationTag
    
    Select Case inp
        Case "1"
            inp = "50.001 Introduction to Information Systems"
        Case "2"
            inp = "50.002 Computational Structures"
        Case "3"
            inp = "50.004 Introduction to Algorithms"
    End Select
    
    If inp <> Empty Then
        ComboBox2.text = inp
    End If
    
    ComboBox2.Clear
    ComboBox2.AddItem ("50.001 Introduction to Information Systems")
    ComboBox2.AddItem ("50.002 Computational Structures")
    ComboBox2.AddItem ("50.004 Introduction to Algorithms")
End Sub

