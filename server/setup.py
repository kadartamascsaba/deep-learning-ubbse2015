from distutils.core import setup
from os import getcwd, walk, chdir
from os.path import join
from zipfile import ZipFile

# ALWAYS import numpy here, otherwise py2exe will fail!
import sys
import numpy
import py2exe
import logging

def buildLogger(name = 'example'):
    logger = logging.getLogger(name)
    logger.setLevel(logging.INFO)
    
    consoleLogger = logging.StreamHandler(stream=sys.stdout)
    consoleLogger.setFormatter(logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s'))
    logger.addHandler(consoleLogger)
    return logger

if __name__ == '__main__':

	logger = buildLogger('Server-dist')
	
	startScript = '%s/server.py' %(getcwd())
	distDir = '%s/dist' %(getcwd())
	distZipFile = '%s/deep-learning-server.zip' %(getcwd())
	
	options = {
		'py2exe': {
			'dist_dir': distDir,
			'includes': ['SocketServer']
		}
	}
	
	logger.info('Start script: %s' %startScript)
	logger.info('Distributable directory: %s' %distDir)
	
	
	# this will create the distributable in the dist folder
	logger.info('Creating distributable')
	setup(name = 'server',
		  console = [startScript],
		  options = options
	)
	
	
	# create a ZIP of dist folder
	logger.info('Creating ZIP of distributable: %s' %distZipFile)
	ziphandler = ZipFile(distZipFile, 'w')
	chdir(distDir)
	for root, dirs, files in walk('.'):
		for f in files:
			ziphandler.write(join(root, f))
	ziphandler.close()
	chdir('..')
