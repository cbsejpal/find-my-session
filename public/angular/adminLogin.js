
var app = angular.module('adminLogin', []);

app.controller('adminLogin', function($scope, $http) {
	
	
	$scope.submit = function() {
		
		$scope.invalid = true;
		$http({
			
			method : "POST",
			url : '/adminLogin',
			data : {
				"email" : $scope.email,
				"password" : $scope.password
			}
		}).success(function(data) {
			
			if (data.status == 500) {
			
				//$scope.invalid_login = true;
				//alert("invalid");
				$scope.invalid = true;

			}
	});
	
}
});