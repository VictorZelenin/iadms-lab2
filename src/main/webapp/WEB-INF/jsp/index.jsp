<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>

<html lang="en">
<head>
    <link rel="stylesheet" type="text/css" href="webjars/bootstrap/3.3.7/css/bootstrap.min.css"/>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.1.4/sockjs.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="../../jquery.bootstrap-growl.min.js"></script>
    <script type="text/javascript">
        var stompClient = null;
        var answeredQuestionsNum = 0;
        var map = {};

        function setConnected(connected) {
            document.getElementById('connect').disabled = connected;
            document.getElementById('disconnect').disabled = !connected;
            document.getElementById('main').style.visibility = connected ? 'visible' : 'hidden';
            for (var obj in document.getElementsByTagName("button")) {
                obj.disabled = false;
            }
        }

        function connect() {
            var socket = new SockJS('/test');
            stompClient = Stomp.over(socket);
            stompClient.connect({}, function (frame) {
                setConnected(true);
                console.log("Connected: " + frame);
                stompClient.subscribe("/topic/test", function (message) {
                    var question = JSON.parse(message.body);
                    $.bootstrapGrowl("Question: " + question['description'] + "</br></br>New complexity:" + question['complexity'], {
                        width: 350,
                        delay: 2500
                    });
                });
                stompClient.subscribe("/topic/result", function (message) {
                    document.getElementById('main').innerHTML = "Your result is " + message.body;
                });
            })
        }

        function disconnect() {
            if (stompClient !== null) {
                stompClient.disconnect();
            }

            setConnected(false);
            console.log("Disconnected");
        }

        function sendMessage(id) {
            var studentName = document.getElementById('studentName').value;
            var answerValue = document.querySelector('input[name = "' + id + '"]:checked').value;
            document.getElementsByName('btn-' + id)[0].disabled = true;

            answeredQuestionsNum++;

            stompClient.send("/app/test", {}, JSON.stringify(
                {
                    'studentName': studentName,
                    'questionId': id,
                    'answerValue': answerValue // TODO: should be id of answer value
                })
            );

            map[id] = answerValue;

            if (answeredQuestionsNum === totalQuestionsNum()) {
                // send to different controller
                stompClient.send("/app/result", {}, JSON.stringify(
                    {
                        'studentName': studentName,
                        'questionAnswers': map
                    })
                );
            }
        }

        function totalQuestionsNum() {
            return document.getElementsByTagName("h5").length;
        }

        function showMessageOut(message) {
            var response = document.getElementById('response');
            var p = document.createElement('p');
            p.style.wordWrap = 'break-word';
            p.appendChild(document.createTextNode(message)); // TODO: make pop-up menu
            response.appendChild(p);
        }
    </script>
</head>
<body onload="setConnected(false)">

<nav class="navbar navbar-inverse">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">Spring Boot</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li class="active"><a href="#">Home</a></li>
                <li><a href="#about">About</a></li>
            </ul>
        </div>
    </div>
</nav>

<div class="container-fluid">

    <div class="row">
        <div class="col-md-4 col-md-offset-5">
            <div>
                <input type="text" id="studentName" placeholder="Student name"/>
            </div>
            <br/>
            <div>
                <button id="connect" class="btn" onclick="connect();">Start Testing</button>
                <button id="disconnect" class="btn" disabled="disabled" onclick="disconnect();">
                    Exit
                </button>
            </div>
        </div>
    </div>
    <div>
        <br/>
        <div id="main">
            <ul>
                <c:forEach items="${questions}" var="question">
                    <h5>${question.description}</h5>
                    <c:forEach items="${question.answers}" var="answer">
                        <input type="radio" name="${question.id}" value="${answer.answerValue}">  ${answer.description}
                        <br>
                    </c:forEach>
                    <br>
                    <button onclick="sendMessage(${question.id});" class="btn" name="btn-${question.id}"
                            id="answer_btn">Send Answer
                    </button>
                    <br>
                </c:forEach>
                <div id="response"></div>
            </ul>
        </div>
    </div>
</div>


<script type="text/javascript" src="webjars/bootstrap/3.3.7/js/bootstrap.min.js"></script>

</body>

</html>