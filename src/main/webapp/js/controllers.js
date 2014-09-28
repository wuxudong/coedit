'use strict';

/* Controllers */

angular.module('myApp.controllers', [])
	.controller('PaperController', [ '$scope', 'ngstomp', '$http', '$timeout', function($scope, ngstomp, $http,
	$timeout) {
    $scope.name = "";
    $scope.messages = new Array();
    $scope.participants = new Array();

    $scope.changes = new Array();

    $scope.sendChangesLoop = function() {
        if($scope.changes.length > 0) {
            // TODO merge ops to reduce network
            var c = $scope.changes.shift();
            c.revision = $scope.nextRevision;

            $http.post(location.pathname + '/op', c).success(function(opResult) {
                if(opResult.resync) {
                    $scope.resync();
                } else {
                    $scope.nextRevision = opResult.nextRevision;
                }

                $scope.sendChangesLoop();
            }).error(function() {$scope.changes = new Array(); $scope.sendChangesLoop(); $scope.resync();});
        } else {
            $timeout($scope.sendChangesLoop, 1000);
        }
    }

    $scope.sendChangesLoop();


    if(! $scope.name) {
        // show name dialog
         $('#nameModal').modal('show');
    }

    $scope.resync = function() {
        $http.get(location.pathname + '/resync').success(function(data) {
            $scope.editor.replaceRange( data.content, {'line': 0, 'ch': 0}, {'line':
                Number.MAX_VALUE, 'ch': Number.MAX_VALUE }, '*remoteupdate');

            $scope.nextRevision = data.nextRevision;
        });
    }

    $scope.enterName = function() {
        if($scope.name) {
            $('#nameModal').modal('hide');

            $scope.room = location.pathname.substring(location.pathname.indexOf("/") + 1)
            // submit username to server
            $http.post(location.pathname + '/join', {name: $scope.name}).success(function(data) {
                $scope.uid = data.uid;

                $scope.client = ngstomp('/chatroom');

                var headers = {
                    login : $scope.uid,
                    passcode : $scope.uid,
                    room : $scope.room,
                    name : $scope.name
                };

                $scope.client.connect(headers,
                    function(frames) {
                        // successful connection
//                        $scope.name = frames[0].headers['user-name'];
                        var queueSuffix = frames[0].headers['queue-suffix'];

                        $scope.client.subscribe("/app/messageHistory", function(message) {
                            $scope.messages = JSON.parse(message.body);
                        });
                        $scope.client.subscribe("/app/participants", function(message) {
                            $scope.participants = JSON.parse(message.body);
                        });

                        $scope.client.subscribe("/topic/message-updates", function(message) {
                            $scope.messages.push(JSON.parse(message.body));
                        });
                        $scope.client.subscribe("/topic/participant-updates", function(message) {
                            $scope.participants = JSON.parse(message.body);
                        });
                        $scope.client.subscribe("/queue/errors"+queueSuffix, function(message) {
                            $scope.error = JSON.parse(message.body);
                        });

                        $scope.client.subscribe("/topic/" + $scope.room + "/paper-updates", function(message) {
                            var opResult = JSON.parse(message.body);
                            var change = opResult.change;
                            var resync = opResult.resync;
                            var nextRevision = opResult.nextRevision;

                            if(resync) {
                                $scope.resync();
                            } else {
                                if(change.uid != $scope.uid) {
                                    $scope.editor.replaceRange( change.text, {'line': change.from.line, 'ch': change.from.ch }, {'line':
                                        change.to.line, 'ch': change.to.ch }, '*remoteupdate');
                                    if(!$scope.$$phase) {
                                        $scope.$apply();
                                    }
                                }

                                $scope.nextRevision = nextRevision;
                            }
                        });

                    },
                    function(frames){
                        console.log(frames);
                    }
                );
            });
        }
    }

    $scope.editorOptions = {
        lineNumbers: true,
        matchBrackets: true,

        onLoad : function(_editor) {
             // Editor part
             var _doc = _editor.getDoc();

             $scope.editor = _editor;

             _editor.focus();

             $http.get(location.pathname + '/resync').success(function(data) {
                $scope.editor.replaceRange( data.content, {'line': 0, 'ch': 0}, {'line':
                                Number.MAX_VALUE, 'ch': Number.MAX_VALUE }, '*remoteupdate');


                $scope.nextRevision = data.nextRevision;
                // Events
                _editor.on("change", function(doc, changeObj){
                     if(changeObj.origin != '*remoteupdate') {
                         changeObj.uid = $scope.uid;
                         $scope.changes.push(changeObj);
                     }

                     $scope.op = changeObj;
                });

                _editor.on("cursorActivity", function(instance){
                    $scope.instance = instance;
                });

             });

        }
    };

    $scope.languageOptions = [
        {name:"plain text", mode:""},
        {name:"java", mode:"text/x-java"},
        {name:"c", mode:"text/x-csrc"},
        {name:"c++", mode:"text/x-c++src"},
        {name:"c#", mode:"text/x-csharp"},
        {name:"python", mode:"text/x-python"}
    ];

    $scope.language="";

    $scope.$watch('language', function(newValue, oldValue) {
        $scope.editorOptions.mode = newValue;
        console.log("language change to " + newValue);
    });

} ]);