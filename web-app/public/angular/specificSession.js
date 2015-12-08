//loading the 'login' angularJS module
var app = angular.module('sessions', []);
//defining the login controller
app.controller('sessions', function($scope, $http) {
	
	
	$scope.interested = function(sessionId){
		
		$http({
			method : "GET",
			url : '/addAttendees',
			params : {
				"sessionId" : sessionId
				}
		}).success(function(response) {
		
			if (response.status == 200) {
				//alert(JSON.stringify(response));
				
				document.getElementById("btn").className = "btn btn-success";
				$scope.buttonSuccess  = document.getElementById("btn").className;

				
				
					$http({
						method : "GET",
						url : '/getSession',
						params : {
							"sessionId" : sessionId
							}
					}).success(function(response) {
					
						if (response.status == 200) {
						
							$scope.attendees = response.data.attendees;	
							$scope.currentlyAttending = response.data.currentlyAttending;
						
							}
						else
							{
								alert("error");
								
							}
					}).error(function(response){alert("error")});
			}
			
				
		else{
				
					document.getElementById("btn").className = "btn btn-default";
					$scope.buttonSuccess  = document.getElementById("btn").className;	
					
				}

			});

		}

	
		$scope.getSession = function(sessionId)
		{
			
			
			$http({
				method : "GET",
				url : '/getSession',
				params : {
					"sessionId" : sessionId
					}
			}).success(function(response) {
			
				if (response.status == 200) {
					
					$scope.sessionName = response.data.sessionName;	
					$scope.speakerName = response.data.speakerName;
					$scope.sessionDesc = response.data.sessionDesc;
					$scope.sessionTimings = response.data.sessionTimings;
					$scope.sessionLocation = response.data.sessionLocation;
					$scope.attendees = response.data.attendees;
					$scope.currentlyAttending = response.data.currentlyAttending;
					
				}
				else
					{
					
						$scope.sessions = "";
					}
			}).error(function(response){alert("error")});
		
		}
		
	
});
	