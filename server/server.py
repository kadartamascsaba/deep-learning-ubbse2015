import SocketServer
import sys

clients = []
client_frame = []

class ClientHandler(SocketServer.BaseRequestHandler):

	def flush(self):
		global client_frame
		for c in client_frame:
			c = []

	def message_parser(self, msg):
		global clients
		global client_frame

		if msg.startswith("start"):
			print self.client_address[0] + " connected."
			clients.append(self.client_address)
			client_frame.append([])
			return True
		elif msg.startswith("stop"):
			print self.client_address[0] + " disconnected."
			index = next(i for i, (t1, t2) in enumerate(clients) if (t1 == self.client_address[0]))
			del clients[index]
			return True
				
		return False

	def handle(self):
		global clients
		global client_frame
		
		data = self.request[0]
		socket = self.request[1]

		if not self.message_parser(data):
			if len(clients) >= 2:
				index = next(i for i, (t1, t2) in enumerate(clients) if (t1 == self.client_address[0] and t2 == self.client_address[1]))
				client_frame[index].append(data)
				for x in clients:
					if x[0] != self.client_address[0] or x[1] != self.client_address[1]:
						address = (x[0], 56789)
						socket.sendto(client_frame[index].pop(0), address)
			else:
				self.flush()

if __name__ == "__main__":
	HOST, PORT = str(sys.argv[1]), int(sys.argv[2])
	server = SocketServer.UDPServer((HOST, PORT), ClientHandler)
	server.serve_forever()
