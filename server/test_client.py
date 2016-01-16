import pyaudio
import sys
import socket
import audioop
from threading import Thread

frames1 = []
frames2 = []

udp = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
udp_for_deep_learning = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

def result_of_deep_learning():
    global udp_for_deep_learning
    udp_for_deep_learning.bind((str(sys.argv[1]), 56799))
    while True:
        data, addr = udp_for_deep_learning.recvfrom(256) # buffer size is 1024 bytes
        print "received message:", data

def udp_stream_out():
    global udp
    udp.sendto("start", (str(sys.argv[2]), int(sys.argv[3])))   

    while True:
        if len(frames1) > 0:
            udp.sendto(frames1.pop(0), (str(sys.argv[2]), int(sys.argv[3]))

    udp.close()

def udp_stream_in(CHUNK):
    global udp
    while True:
        soundData, addr = udp.recvfrom(CHUNK * CHANNELS * 2)
        frames2.append(soundData)

    udp.close()

def record(stream, CHUNK):    
    while True:
        frames1.append(stream_in.read(CHUNK))

def play(stream, CHUNK):
    BUFFER = 8
    while True:
            if len(frames2) == BUFFER:
                while True:
                    stream_out.write(frames2.pop(0), CHUNK)
            
if __name__ == "__main__":
    CHUNK = 1024
    FORMAT = pyaudio.paInt16
    CHANNELS = 2
    RATE = 16000

    p = pyaudio.PyAudio()

    stream_in = p.open(format = FORMAT,
                    channels = CHANNELS,
                    rate = RATE,
                    input = True,
                    frames_per_buffer = CHUNK,
                    )

    stream_out = p.open(format=FORMAT,
                    channels = CHANNELS,
                    rate = RATE,
                    output = True,
                    frames_per_buffer = CHUNK,
                    )

    deep_Thread = Thread(target = result_of_deep_learning)

    Tr1 = Thread(target = record, args = (stream_in, CHUNK,))
    Ts1 = Thread(target = udp_stream_out)

    Tr2 = Thread(target = play, args = (stream_out, CHUNK,))
    Ts2 = Thread(target = udp_stream_in, args = (CHUNK,))

    Tr1.setDaemon(True)
    Ts1.setDaemon(True)

    Tr2.setDaemon(True)
    Ts2.setDaemon(True)

    deep_Thread.setDaemon(True)
    deep_Thread.start()

    Tr1.start()
    Ts1.start()

    Tr2.start()
    Ts2.start()

    Tr1.join()
    Ts1.join()

    Tr2.join()
    Ts2.join()
