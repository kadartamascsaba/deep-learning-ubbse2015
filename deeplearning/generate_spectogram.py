import scipy
import numpy
import scipy.io.wavfile as WAV

# Converts the input signal to mono (if it is stereo)
def stereo_to_mono(data):
    
    objType = type(data).__name__.strip()
       
    if objType <> "ndarray":
        raise Exception('data argument is no instance of numpy.array')

    size = len(data)
    if (size < 1):
        raise Exception('data array is empty') 

    if data.ndim == 1:
        return data
    else:
        if data.ndim == 2:
            return ( (data[:,1] / 2) + (data[:,0] / 2) )
        else:
            return -1

# Performs the Short Time Fourier Transform of the given data
def stft(data, fs, framesize = 0.075, hopsize = 0.0625):

    # data = a numpy array containing the signal to be processed
    # fs = a scalar which is the sampling frequency of the data

    objType = type(data).__name__.strip()
       
    if objType <> "ndarray":
        raise Exception('data argument is no instance of numpy.array')

    size = len(data)
    if (size < 1):
        raise Exception('data array is empty')  

    frameSamp = int(framesize * fs)
    hopSamp = int(hopsize * fs)
    window = scipy.hanning(frameSamp)

    threshold = numpy.mean(numpy.absolute(data))*0.30
    
    X = numpy.array([numpy.absolute(scipy.fft(window * data[i : (i + frameSamp)])) for i in xrange(0, len(data) - frameSamp, hopSamp) if numpy.mean(numpy.absolute(data[i : (i + frameSamp)])) > threshold])

    # Deleting the second half of each row
    # Fourier Transform gives Hermite-symmetric result for real-valued input
    X = numpy.array([X[i][: numpy.ceil((X.shape[1] + 1.0) / 2)] for i in xrange(0, X.shape[0])])

    #Normalizing
    X = numpy.array([X[i] / numpy.linalg.norm(X[i]) for i in xrange(0, X.shape[0])])
    
    return X

# Gets a wav file as input and generates its spectrogram
def generate_spectrogram(filename):

    fs, audio = WAV.read(filename)
    audioMono = stereo_to_mono(audio)

    spectrogram = stft(audioMono, fs)

    return spectrogram