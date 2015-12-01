#!/usr/bin/env python

import os
import logging

from tornado.ioloop import IOLoop
from tornado.options import define, options
from tornado.web import Application, RequestHandler
from tornado.websocket import WebSocketHandler


rel = lambda *x: os.path.abspath(os.path.join(os.path.dirname(__file__), *x))


class MainHandler(RequestHandler):
    def get(self):
        self.render('index.html')


class EchoWebSocket(WebSocketHandler):
    clients = []
    
    def open(self):
        logging.info('WebSocket opened from %s', self.request.remote_ip)
        EchoWebSocket.clients.append(self)

    def on_message(self, message):
        logging.info('got message from %s: %s', self.request.remote_ip, message)
        for client in EchoWebSocket.clients:
            if client is self:
                continue
            client.write_message(message)

    def on_close(self):
        logging.info('WebSocket closed')
        EchoWebSocket.clients.remove(self)


def main():
    define('listen', metavar='IP', default='192.168.0.16', help='listen on IP address (default 127.0.0.1)')
    define('port', metavar='PORT', default=8888, type=int, help='listen on PORT (default 8888)')
    define('debug', metavar='True|False', default=False, type=bool, 
        help='enable Tornado debug mode: templates will not be cached '
        'and the app will watch for changes to its source files '
        'and reload itself when anything changes')
    
    options.parse_command_line()
    
    settings = dict(
        template_path=rel('templates'),
        static_path=rel('static'),
        debug=options.debug
    )
    
    application = Application([
        (r'/', MainHandler),
        (r'/ws', EchoWebSocket),
    ], **settings)
    
    application.listen(address=options.listen, port=options.port)
    IOLoop.instance().start()


if __name__ == '__main__':
    main()
