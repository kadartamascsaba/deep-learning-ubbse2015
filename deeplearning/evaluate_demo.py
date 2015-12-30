import neuralnetwork as nn

n = nn.Net()
n.load('matrix.txt')

print n.evaluate('test/FMEV_Sr10.wav')