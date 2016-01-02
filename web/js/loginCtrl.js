angular.module('popupApp')
  .controller('loginCtrl', function($scope, $auth) {

    $scope.authenticate = function(provider) {
      $auth.authenticate(provider);
    };
	
	$scope.logout = function(){
		$auth.logout();
	};

  });