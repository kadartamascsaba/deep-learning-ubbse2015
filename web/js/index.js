angular.module('myApp', ['ngFacebook'])
  .config(['$facebookProvider', function($facebookProvider) {
    $facebookProvider.setAppId('1000732483317996').setPermissions(['email','user_friends']);
  }])
  .run(['$rootScope', '$window', function($rootScope, $window) {
	(function(d, s, id) {
		var js, fjs = d.getElementsByTagName(s)[0];
		if (d.getElementById(id)) return;
		js = d.createElement(s); js.id = id;
		js.src = "//connect.facebook.net/hu_HU/sdk.js#xfbml=1&version=v2.5&appId=1000732483317996";
		fjs.parentNode.insertBefore(js, fjs);
	}(document, 'script', 'facebook-jssdk'));
    $rootScope.$on('fb.load', function() {
      $window.dispatchEvent(new Event('fb.load'));
    });
  }])
  .controller('myCtrl', ['$scope', '$facebook', function($scope, $facebook) {
	  $scope.$on('fb.auth.authResponseChange', function() {
		  $scope.status = $facebook.isConnected();
		  if($scope.status) {
			FB.api('/me', function(response) {
			   alert('Good to see you, ' + response.name + '.');
			   $scope.user = response;
			});
		  }
      });

    $scope.loginToggle = function() {
      if($scope.status) {
        $facebook.logout();
      } else {
        $facebook.login();
      }
    };

    $scope.getFriends = function() {
      if(!$scope.status) return;
      $facebook.cachedApi('/me/friends').then(function(friends) {
        $scope.friends = friends.data;
      });
    }
  }])
;