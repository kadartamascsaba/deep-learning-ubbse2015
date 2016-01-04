import unittest
import numpy
import neuralnetwork
import spectrogram
 
class TestDeepLearning(unittest.TestCase):

    # spectrogram.stereo_to_mono is expected to raise exception if the input is no instance of numpy.array
    def testStereoToMonoConverterRaisesExceptionInCaseOfWrongInput(self):
        self.failUnlessRaises(Exception, spectrogram.stereo_to_mono, None)

    # spectrogram.stereo_to_mono is expected to return -1 if the number of dimensions of the input array is > 2
    def testStereoToMonoConverterReturnsSpecialValueInCaseOfWrongDimensions(self):
        self.assertEqual(spectrogram.stereo_to_mono(numpy.array([[[]]])), -1)

    # spectrogram.stft is expected to raise exception if the input is array
    def testStftRaisesExceptionInCaseOfEmptyInput(self):
        self.failUnlessRaises(Exception, spectrogram.stft, numpy.array([]), 16000)

    # spectrogram.generate_spectrogram is expected to return correct dimension (1200 / 2 + 1) if the input is valid
    def testGenerateSpectrogramReturnsCorrectShapeInCaseOfValidInput(self):
        test = spectrogram.generate_spectrogram('FMEV_Sr10.wav')
        self.assertEqual(test.shape[1], 601)

    # neuralnetwork.Net class is expected to be instatiate correctly
    def testNeuralNetworkCanBeInstatiateCorrectly(self):
        n = neuralnetwork.Net()
        self.assertIsInstance(n, neuralnetwork.Net)

    # neuralnetwork.evaluate is expected to recognize the person identified as FMEV
    def testEvaulateInCaseOfAPersonItWasTrainedFor(self):
        n = neuralnetwork.Net()
        n.load('matrix.txt')
        test = n.evaluate('FMEV_Sr10.wav')

        self.assertEqual(test, 'FMEV')


if __name__ == '__main__':
    unittest.main()