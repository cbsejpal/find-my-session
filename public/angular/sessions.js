//loading the 'login' angularJS module
var app = angular.module('sessions', []);
//defining the login controller
app.controller('sessions', function($scope, $http) {
	
	
	$http.get("/getAllSessions").success(function(response) {
		
		if (response.status == 200) {
			$scope.sessions = response.data;			
		}
		else
			{
			alert(response.status);
				$scope.sessions = "";
			}
	});
	
	
	$scope.getSession = function(session){
		alert(session.sessionId);
		
		$http({
			method : "GET",
			url : '/getSession',
			params : {
				"sessionId" : session.sessionId
				}
		});
	}
			
		
});		
	
	