/*
UI Automatic tests with Nightwatch.js
*/

// Testing gitHub login with token
module.exports = {
  'gitHubLogin' : function (browser) {
    browser
      .url('http://triqla.ddns.net:110')
      .waitForElementVisible('body', 1000)
      .waitForElementVisible('a[name=login]', 1000)
      .click('a[name=login]')
      .pause(1000)
      .assert.containsText('#github-login', 'Sign in with GitHub')
      .click('button[id=github-login]')
      .pause(1000)
      .assert.containsText('#logout', 'Logout')
      .end();
  }
};

// Muting microphone
module.exports = {
  'mutingMicrophone' : function (browser) {
    browser
      .url('http://triqla.ddns.net:110/conferece/<uuid>')
      .waitForElementVisible('body', 1000)
      .waitForElementVisible('button[name=mute]', 1000)
      .click('button[name=mute]')
      .pause(1000)
      .assert.attributeContains('#localAudio', 'autoplay', 'muted');
      .end();
  }
};
