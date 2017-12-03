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
    <script type="text/javascript">
        var stompClient = null;

        function setConnected(connected) {
            document.getElementById('connect').disabled = connected;
            document.getElementById('disconnect').disabled = !connected;
            document.getElementById('main').style.visibility = connected ? 'visible' : 'hidden';
        }

        function connect() {
            var socket = new SockJS('/test');
            stompClient = Stomp.over(socket);
            stompClient.connect({}, function (frame) {
                setConnected(true);
                console.log("Connected: " + frame);
                stompClient.subscribe("/topic/test", function (message) {
                    showMessageOut(message.body);
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

            stompClient.send("/app/test", {}, JSON.stringify( // TODO: count a number of send messages, and then send final msg.
                {
                    'studentName': studentName,
                    'questionId': id,
                    'answerValue': answerValue // TODO: should be id of answer value
                })
            );
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
                    <button onclick="sendMessage(${question.id});" id="answer_btn">Send Answer</button>
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