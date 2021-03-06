//loading the 'login' angularJS modu
var app = angular.module('signup', []);
//defining the login controller
app.controller('signup', function($scope, $http) {
	//alert("s");

	$scope.submit = function($event){
		angular.forEach($scope.registration.$error.required, function(field) {
			field.$setDirty();
		});
		angular.forEach($scope.registration.$error.pattern, function(field) {
			field.$setDirty();
		});
		if($scope.registration.$error.required || $scope.registration.$error.$pattern) {
			$event.preventDefault();
			alert("You must enter valid details");
		}

		else{
			$http({
				method : "POST",
				url : '/registerUser',
				data : {

					"fullName" : $scope.fullName,
					"email" : $scope.email,
					"password" : $scope.password,

					"linkedInUrl" : $scope.linkedInUrl,

					"contactNumber" : $scope.contactNumber


				}
			}).success(function(data) {
//				alert(data.status);
				//checking the response data for statusCode
				if (data.status == 500) {

					//		alert("in 500");
					$scope.error = "something is wrong";
				}
				else{
//					alert("200");
					//Making a get call to the '/redirectToHomepage' API
					window.location.assign("/loginUser");
				}
			}).error(function(error) {
				$scope.unexpected_error = "something is wrong!";

			});

		}
	}
});
