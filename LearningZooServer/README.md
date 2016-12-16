# README

##Subject

###Get All Subjects
`curl -X GET http://localhost:3000/subjects`

response:
`[{"id":1,"name":"abcd"},{"id":2,"name":"abcde"},{"id":3,"name":"fghij"}]`

###Subject Creation:
`curl -H "Content-Type: application/json" -X POST -d '{"name": "abcde"}' http://localhost:3000/subjects`

response:
`{"id":3,"name":"fghij"}`

###Update Subject
Subjects can be partially updated, just put what you want to update inside the json. For full list of fields, read the `schema.rb`.
`curl -H "Content-Type: application/json" -X PATCH -d '{"name": "comstrekt"}' http://localhost:3000/subjects/1`

response:
`{"id":1,"name":"comstrekt"}`

##Classroom
This object is not meant to be created from our devices, contact me to create one.

###Get All Classrooms
`curl -X GET http://localhost:3000/classrooms`

response:

###Get a Classroom
Fetch data of a classroom, each Pi should have a unique classroom id inside.

`curl -X GET http://localhost:3000/classrooms/:id`

response:
`[{"id":1,"name":"CC10","current_session_id":null}]`

###Update Classroom
This is important to update at the beginning of each session.

`curl -H "Content-Type: application/json" -X PATCH -d '{"current_session_id": "1"}' http://localhost:3000/classrooms/1`

response:
`{"id":1,"current_session_id":1,"name":"CC10"}`

##Session

###Get all sessions
`curl -X GET http://localhost:3000/sessions`

response:
`[{"id":1,"name":"session1","time":null,"lecturer_name":"","current_slide":0,"classroom":null,"slides_sequence":null,"subject_id":1,"machine_key":"1234","start_date":"2016-11-27T18:19:06.000Z","end_date":"2016-11-27T22:19:06.000Z"}]`

###Session Creation:
`curl -H "Content-Type: application/json" -X POST -d '{"name": "session1", "machine_key": "1234", "start_date": "2016-11-28 02:19:06 +0800", "end_date": "2016-11-28 06:19:06 +0800", "subject_id": "1"}' http://localhost:3000/sessions`

response:
`{"id":1,"name":"session1","time":null,"lecturer_name":"","current_slide":0,"classroom":null,"slides_sequence":null,"subject_id":1,"machine_key":"1234","start_date":"2016-11-27T18:19:06.000Z","end_date":"2016-11-27T22:19:06.000Z"}`

###Get a session
`curl -X GET http://localhost:3000/sessions/:id`

response:
``{"id":1,"name":"session1","time":null,"lecturer_name":"","current_slide":0,"classroom":null,"slides_sequence":null,"subject_id":1,"machine_key":"1234","start_date":"2016-11-27T18:19:06.000Z","end_date":"2016-11-27T22:19:06.000Z"}``

##Topic

###Get all topics:
`curl -X GET http://localhost:3000/topics`

response:
`[{"id":1,"name":"topic1","start_slide":null,"end_slide":null,"std_unclear":0,"machine_key":"1234","time":"2016-11-27T20:19:06.000Z"}]`

###Get a topic
`curl -X GET http://localhost:3000/topics/:id`

response:
`{"id":1,"name":"topic1","start_slide":null,"end_slide":null,"std_unclear":0,"machine_key":"1234","time":"2016-11-27T20:19:06.000Z"}`

###Topics Creation
`curl -H "Content-Type: application/json" -X POST -d '{"name": "topic1", "machine_key": "1234", "time": "2016-11-28 04:19:06 +0800"}' http://localhost:3000/topics`

response:
`{"id":1,"name":"topic1","start_slide":null,"end_slide":null,"std_unclear":0,"machine_key":"1234","time":"2016-11-27T20:19:06.000Z"}`

###Update Topic
Topics can be partially updated, just put what you want to update inside the json. For full list of fields, read the `schema.rb`.
`curl -H "Content-Type: application/json" -X PATCH -d '{"name": "not_topic"}' http://localhost:3000/topics/1`

response:
`{"id":1,"name":"not_topic","machine_key":"1234","time":"2016-11-27T20:19:06.000Z","start_slide":null,"end_slide":null,"std_unclear":0}`

###Topics Query (Find all topics related to a session):
`curl -X GET http://localhost:3000/sessions/1/get_topics`

response:
`[{"id":1,"name":"topic1","start_slide":null,"end_slide":null,"std_unclear":0,"machine_key":"1234","time":"2016-11-27T20:19:06.000Z"}]`

###Topics increase counter:
Pi just need to send a GET request to this URL, but it needs to know the current_topic so poll beforehand.
`curl -X GET http://localhost:3000/topics/:id/incr_counter`

response:
`{"id":1,"name":"topic1","start_slide":null,"end_slide":null,"std_unclear":1,"machine_key":"1234","time":"2016-11-27T20:19:06.000Z"}`
