//loading the 'login' angularJS module
var app = angular.module('login', []);
//defining the login controller
app.controller('login', function($scope, $http) {
	//Initializing the 'invalid_login' and 'unexpected_error' 
	//to be hidden in the UI by setting them true,
	//Note: They become visible when we set them to false
	$scope.invalid_login = true;
	$scope.unexpected_error = true;
	$scope.submit = function() {
	
		$http({
			
			method : "POST",
			url : '/loginUser',
			data : {
				"email" : $scope.email,
				"password" : $scope.password
			}
		}).success(function(data) {
			//checking the response data for statusCode
		
			if (data.status == 401 || data.status == 300) {
			
				$scope.invalid_login = false;
				$scope.unexpected_error = true;
			}
			else{
				
				//Making a get call to the '/redirectToHomepage' API
				window.location.assign("/userProfile");
			}
			
			
		}).error(function(error) {
			$scope.unexpected_error = false;
			$scope.invalid_login = true;
		});
	};
});
