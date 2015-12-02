/*
Initializing Node JS server
*/

var config = require('./config/config.json'),
    server = require('./lib/server');

server.run(config);
