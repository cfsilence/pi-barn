<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="Todd Sharp">
    <link rel="shortcut icon" href="${assetPath(src: 'favicon.ico')}" type="image/x-icon">
    <title>pi-barn</title>
    <asset:javascript src="jquery-1.12.1.js"/>
    <asset:javascript src="angular.min.js"/>
    <asset:javascript src="moment.js"/>
    <asset:javascript src="bootstrap.js"/>

    <g:render template="/page/common/global"/>

    <!-- Bootstrap core CSS -->
    <asset:stylesheet href="bootstrap.css"/>
    <asset:stylesheet href="font-awesome/css/font-awesome.css"/>
    <!-- Custom styles for this template -->
    <asset:stylesheet href="main.css"/>

    <link href="https://fonts.googleapis.com/css?family=Roboto+Mono" rel="stylesheet">
    <g:layoutHead/>
</head>

<body>
    <nav class="navbar navbar-inverse navbar-fixed-top">
        <div class="container-fluid">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <g:link action="index" controller="page" class="navbar-brand"><span class="cfsilence-barn"></span> pi-barn</g:link>
            </div>
            <div id="navbar" class="navbar-collapse collapse">
                <ul class="nav navbar-nav navbar-right">
                    <sec:ifLoggedIn>
                        <li>
                            <a href="#" data-toggle="modal" data-target="#helpModal"><i class="fa fa-question-circle"></i> Help</a>
                        </li>
                        <li>
                            <g:link controller="logout" action="index"><i class="fa fa-sign-out"></i> Logout</g:link>
                        </li>
                    </sec:ifLoggedIn>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container-fluid">
        <g:layoutBody/>
    </div>
</body>
</html>
