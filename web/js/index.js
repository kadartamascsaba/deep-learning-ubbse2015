var app = angular.module('myApp', ['ngDialog']);

app.controller('myCtrl', function ($scope, ngDialog) {
    $scope.clickToOpen = function () {
        ngDialog.open({ template: 'popupTmpl.html' });
    };
});