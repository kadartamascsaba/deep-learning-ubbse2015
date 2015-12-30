# This program is a demo for using deep learning algorithm for OCR digit recognition
# Based on the tutorial here: 	http://deeplearning.net/tutorial/mlp.html
# A stripped down version: 		https://gist.github.com/honnibal/6a9e5ef2921c0214eeeb
#
# This version of the demo was tested on Linux

import os
import sys
import time
from os import path
from os.path import isfile, join

import gzip
import cPickle


import neuralnetwork as nn
import numpy
import theano
import spectrogram as sg

def load_data(train_dir, test_dir):
    
    train_input = [f for f in os.listdir(train_dir) if isfile(join(train_dir, f))]
    test_input  = [f for f in os.listdir(test_dir) if isfile(join(test_dir, f))]
    
    classes   = []
    train_set = []
    test_set  = []

    # Training data
    for element in train_input:
        
        elem_id = element.split(".")[0].split("_")[0]

        try:
            index = classes.index(elem_id)
        except:
            index = len(classes)
            classes.append(elem_id)

        stgrm = sg.generate_spectrogram(join(train_dir, element))

        train_set.extend(_make_array(stgrm, index))

    # Test data
    for element in test_input:
        
        elem_id = element.split(".")[0].split("_")[0]

        if elem_id in classes:
            index = classes.index(elem_id)
            stgrm = sg.generate_spectrogram(join(test_dir, element))
            test_set.extend(_make_array(stgrm, index))

    train_min_nr = min([len(x[0]) for x in train_set])
    test_min_nr  = min([len(x[0]) for x in test_set])

    train_data = [x[:train_min_nr] for x in train_set]
    test_data  = [x[:test_min_nr] for x in test_set]

    return numpy.random.permutation(train_data), test_data, classes


def _make_array(x, y):
    return zip(
        numpy.asarray(x, dtype=theano.config.floatX),
        numpy.asarray([y]*len(x), dtype='int32'))



def main(train_dir='train', test_dir='test'):

    print '... loading data'        
    train_set, test_set, classes = load_data(train_dir, test_dir)
    
    print '... building the model'
    n = nn.Net(len(classes))
    
    n.load()
    # n.save('meoo.txt')
    error = numpy.mean([n.evaluate_model(x[0], x[1]) for x in test_set])
    print('epoch %i, validation error %f %%' % (0, error * 100))
    exit(1)

    # n.add_hidden_layer(601, 300)
    # n.add_hidden_layer(300, 150)
    # n.add_hidden_layer(150,  75)
    # n.add_hidden_layer( 75,  25)

    print '... compiling the model'
    n.compile_model()
    
    current_error = 1
    error         = 1
    # We train the network 100 times
    # Each time we evaluate the results and write out the error percentage
    for epoch in range(1, 5):

        if error < current_error:
            f = open('error.txt','a')
            f.write('{}'.format(error))
            f.write('\n')
            f.close()
            current_error = error
            n.save()

        # print '... training'            
        for x in train_set:
            n.train_model(x[0], x[1])

        print '... calculating error'            
        # compute zero-one loss on validation set
        error = numpy.mean([n.evaluate_model(x[0], x[1]) for x in test_set])
        print('epoch %i, validation error %f %%' % (epoch, error * 100))


if __name__ == '__main__':
    main()
