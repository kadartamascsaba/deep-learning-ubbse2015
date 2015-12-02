angular.module('popupApp', ['satellizer'])
  .config(function($authProvider) {

    $authProvider.facebook({
      clientId: '1000732483317996'
    });

    $authProvider.google({
      clientId: '1017671207934-pk6kih5gkiiehtl8gdi26c1ha9r8utgp.apps.googleusercontent.com'
    });

    $authProvider.github({
      clientId: '7698815143a2878024e1'
    });

	
	$authProvider.httpInterceptor = function() { return true; },
	$authProvider.withCredentials = false;
	$authProvider.tokenRoot = null;
	$authProvider.cordova = false;
	$authProvider.baseUrl = '/';
	$authProvider.loginUrl = '/auth/login';
	$authProvider.signupUrl = '/auth/signup';
	$authProvider.unlinkUrl = '/auth/unlink/';
	$authProvider.tokenName = 'token';
	$authProvider.tokenPrefix = 'satellizer';
	$authProvider.authHeader = 'Authorization';
	$authProvider.authToken = 'Bearer';
	$authProvider.storageType = 'localStorage';

	// Facebook
	$authProvider.facebook({
	name: 'facebook',
	url: 'http://localhost:3000/auth/facebook',
	authorizationEndpoint: 'https://www.facebook.com/v2.5/dialog/oauth',
	redirectUri: window.location.origin + '/',
	requiredUrlParams: ['display', 'scope'],
	scope: ['email'],
	scopeDelimiter: ',',
	display: 'popup',
	type: '2.0',
	popupOptions: { width: 580, height: 400 }
	});

	// Google
	$authProvider.google({
	url: 'http://localhost:3000/auth/google',
	authorizationEndpoint: 'https://accounts.google.com/o/oauth2/auth',
	redirectUri: window.location.origin,
	requiredUrlParams: ['scope'],
	optionalUrlParams: ['display'],
	scope: ['profile', 'email'],
	scopePrefix: 'openid',
	scopeDelimiter: ' ',
	display: 'popup',
	type: '2.0',
	popupOptions: { width: 452, height: 633 }
	});

	// GitHub
	$authProvider.github({
	url: 'http://localhost:3000/auth/github',
	authorizationEndpoint: 'https://github.com/login/oauth/authorize',
	redirectUri: window.location.origin,
	optionalUrlParams: ['scope'],
	scope: ['user:email'],
	scopeDelimiter: ' ',
	type: '2.0',
	popupOptions: { width: 1020, height: 618 }
	});
	
  });