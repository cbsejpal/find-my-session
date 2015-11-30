
var app = angular.module('addSession', []);

app.controller('addSession', function($scope, $http, $location) {
	
	
	$scope.submit = function() {
		
		$http({
			
			method : "POST",
			url : '/addSession',
			data : {
				"sessionName" : $scope.sessionName,
				"sessionDesc" : $scope.sessionDesc,
				
				"speakerName" : $scope.speakerName,
				"speakerDetails" : $scope.speakerDetails,
				"sessionTimings" : $scope.sessionTimings,
				"sessionLocation" : $scope.sessionLocation

			}
		}).success(function(data) {
			//checking the response data for statusCode

			if (data.status == 401 || data.status == 300) {
			
				
			}
			else{
			
				window.location.assign("displayAdminSessions");
			}
			
			
		}).error(function(error) {
			
		});
	}
});
