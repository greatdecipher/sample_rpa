import os
from os import listdir
from os.path import isfile, join
from os.path import basename
import time
import argparse
import configparser
import logging
from logging.handlers import RotatingFileHandler
import zipfile
import glob
import pathlib

class fileCleaner:
    fc_folderpath = str(pathlib.Path(__file__).parent.resolve())

    useCase = ""
    psBuildType = ""
    processName = ""    
    processFolder = []   
    processedArchiveFolder = []
    erroredArchiveFolder = []
    psbuildtypes = []
    processnames = []
    sleeptime = 0
    folderdivider = "/"     
    cfgUseCase = ""
    environment = ""
    configpath = fc_folderpath + "/filecleanerCfg.ini"
    config = configparser.ConfigParser() 
    logfilepath = fc_folderpath + "/filecleaner.log"
    processedFilesforDelete = []
    fileID = ""
    psbuildtypecount = 3
    
    
    # Function that checks for folder and create when not found that is called from Main
    def folderChecker(self, myDir: str):
        #Check In-bound Folders Exist                       
        if os.path.isdir(myDir):
            logging.info(f"{self.useCase}:Found Folder {myDir}")
        else:
            logging.info(f"{self.useCase}:Not Found Folder {myDir}")   
            try:           
                os.makedirs(myDir)
                logging.info(f"{self.useCase}:Created Folder {myDir}")
            except IOError as e:                
                logging.error(f"{self.useCase}:{e}")            


    #def archiveProcessedFiles(self, sourceDir: str, targetDir: str, psbtype: str, uploader: str):
    def archiveProcessedFiles(self, sourceDir: str, targetDir: str, fileID: str):     
        try:                         
            #origFileNameList = origfilename.split(".")
            YYYY_mm = time.strftime("%Y_%m") #time.strftime("%m")            
            zipDir = f"{targetDir}{YYYY_mm}{self.folderdivider}"    
            self.folderChecker(zipDir)
            tmpDateTimeStr = time.strftime("%Y%m%d-%H%M%S")
            ZipFilePath = f"{zipDir}{fileID}.zip"                        
            with zipfile.ZipFile(ZipFilePath, 'w') as zf:                
                for file in glob.glob(f'{sourceDir}PSBuild_{fileID}.xlsx'):                     
                    zf.write(file, basename(file))
                    if "MGEATB" in fileID:
                       self.processedFilesforDelete.append(file)                     
                for file in glob.glob(f'{sourceDir}CBuild_{fileID}.xlsx'):
                    zf.write(file, basename(file))
                    self.processedFilesforDelete.append(file)                    
                for file in glob.glob(f'{sourceDir}Loader_{fileID}.xlsx'):
                    zf.write(file, basename(file))
                    self.processedFilesforDelete.append(file)                     
                for file in glob.glob(f'{sourceDir}CSGRepL_{fileID}.xlsx'):            
                    zf.write(file, basename(file))
                    self.processedFilesforDelete.append(file) 
                for file in glob.glob(f'{sourceDir}CSGRepV_{fileID}.xlsx'):            
                    zf.write(file, basename(file))
                    self.processedFilesforDelete.append(file) 
                for file in glob.glob(f'{sourceDir}PSSalAcc_{fileID}.xlsx'):            
                    zf.write(file, basename(file))
                    self.processedFilesforDelete.append(file)                    
                for file in glob.glob(f'{sourceDir}PSBuild_Error_{fileID}.xlsx'):            
                    zf.write(file, basename(file))
                    self.processedFilesforDelete.append(file)                    
            zf.close()
            logging.info(f"{self.useCase}-{self.psBuildType}:Processed files zip {ZipFilePath}")           
        except IOError as e:            
            logging.error(f"{self.useCase}-{self.psBuildType}:{e}")                 

    
    #def archiveErroredFiles(self, sourceDir: str, targetDir: str, psbtype: str, uploader: str):
    def archiveErroredFiles(self, sourceDir: str, targetDir: str, fileID: str):
        try:       
            YYYY_mm = time.strftime("%Y_%m")
            zipDir = f"{targetDir}{YYYY_mm}{self.folderdivider}"
            self.folderChecker(zipDir)                        
            sourceFilePath = f'{sourceDir}PSBuild_Error_{fileID}.xlsx'
            targetFilePath = f'{zipDir}PSBuild_Error_{fileID}.xlsx'
            if os.path.isfile(sourceFilePath):
                    os.rename(sourceFilePath, targetFilePath)             
            logging.info(f"{self.useCase}-{self.psBuildType}:Error files moved to {targetFilePath}")        
        except IOError as e:            
            logging.error(f"{self.useCase}-{self.psBuildType}:{e}")   
           
    
    def manageGradesCfg(self):
        try:
            self.processFolder.append(self.config.get(f"{self.environment}.{self.cfgUseCase}",'processfolderWithExistingATB'))                      
            self.processFolder.append(self.config.get(f"{self.environment}.{self.cfgUseCase}",'processfolderWithATB'))            
            self.processFolder.append(self.config.get(f"{self.environment}.{self.cfgUseCase}",'processfolderWithoutATB'))
         
            if self.processFolder[0].find("/"):
                self.folderdivider = "/"
            else:
                self.folderdivider = "\\" 
         
            self.processedArchiveFolder.append(self.config.get(f"{self.environment}.{self.cfgUseCase}",'archivefolderWithExistingATB') + f"Processed{self.folderdivider}")                      
            self.processedArchiveFolder.append(self.config.get(f"{self.environment}.{self.cfgUseCase}",'archivefolderWithATB') + f"Processed{self.folderdivider}")            
            self.processedArchiveFolder.append(self.config.get(f"{self.environment}.{self.cfgUseCase}",'archivefolderWithoutATB') + f"Processed{self.folderdivider}")
            
            self.erroredArchiveFolder.append(self.config.get(f"{self.environment}.{self.cfgUseCase}",'archivefolderWithExistingATB') + f"Errored{self.folderdivider}")                      
            self.erroredArchiveFolder.append(self.config.get(f"{self.environment}.{self.cfgUseCase}",'archivefolderWithATB') + f"Errored{self.folderdivider}")            
            self.erroredArchiveFolder.append(self.config.get(f"{self.environment}.{self.cfgUseCase}",'archivefolderWithoutATB') + f"Errored{self.folderdivider}")
        
            logging.info(f"{self.useCase}:PS Build Type param: {self.psBuildType}")
            logging.info(f"{self.useCase}:Process names: {self.processnames}")
            #logging.info(f"{self.useCase}:Uplodder param: {self.upLoader}")
            logging.info(f"{self.useCase}:Loaded config from {self.configpath}")
            logging.info(f"{self.useCase}:PS Build Types from config: {self.psbuildtypes}")                                       
            logging.info(f"{self.useCase}:Process Folders from config: {self.processFolder}")
            logging.info(f"{self.useCase}:Archive Folders(Processed) from config: {self.processedArchiveFolder}")
            logging.info(f"{self.useCase}:Archive Folders(Errored) from config: {self.erroredArchiveFolder}") 
        
            #Run folder Checker                        
            #Processed Archive folder 
            logging.info(f"{self.useCase}:Initiating folder checker for processed files to be archived.")              
            for index, folder in enumerate(self.processedArchiveFolder, start=0):
                self.folderChecker(folder)
                                    
            #Errored Archive folder 
            logging.info(f"{self.useCase}:Initiating folder check for Error file to be archived.")              
            for index, folder in enumerate(self.erroredArchiveFolder, start=0):
                self.folderChecker(folder)     
                
        except IOError as e:
            #logging.error(f"{self.useCase}:Error Encnoutered during Execution")
            #logging.error(e.errno)
            logging.error(f"{self.useCase}:{e}")
        
    def progressionGradeLadderCfg(self):        
        try:                                  
            self.processFolder.append(self.config.get(f"{self.environment}.{self.cfgUseCase}",'processfolderWithATB'))            
            self.processFolder.append(self.config.get(f"{self.environment}.{self.cfgUseCase}",'processfolderWithoutATB'))
         
            if self.processFolder[0].find("/"):
                self.folderdivider = "/"
            else:
                self.folderdivider = "\\" 
                                          
            self.processedArchiveFolder.append(self.config.get(f"{self.environment}.{self.cfgUseCase}",'archivefolderWithATB') + f"Processed{self.folderdivider}")            
            self.processedArchiveFolder.append(self.config.get(f"{self.environment}.{self.cfgUseCase}",'archivefolderWithoutATB') + f"Processed{self.folderdivider}")
                                              
            self.erroredArchiveFolder.append(self.config.get(f"{self.environment}.{self.cfgUseCase}",'archivefolderWithATB') + f"Errored{self.folderdivider}")            
            self.erroredArchiveFolder.append(self.config.get(f"{self.environment}.{self.cfgUseCase}",'archivefolderWithoutATB') + f"Errored{self.folderdivider}")
        
            logging.info(f"{self.useCase}:PS Build Type param: {self.psBuildType}")
            logging.info(f"{self.useCase}:Process names: {self.processnames}")
            #logging.info(f"{self.useCase}:Uplodder param: {self.upLoader}")
            logging.info(f"{self.useCase}:Loaded config from {self.configpath}")
            logging.info(f"{self.useCase}:PS Build Types from config: {self.psbuildtypes}")                                       
            logging.info(f"{self.useCase}:Process Folders from config: {self.processFolder}")
            logging.info(f"{self.useCase}:Archive Folders(Processed) from config: {self.processedArchiveFolder}")
            logging.info(f"{self.useCase}:Archive Folders(Errored) from config: {self.erroredArchiveFolder}") 
        
            #Run folder Checker                        
            #Processed Archive folder 
            logging.info(f"{self.useCase}:Initiating folder checker for processed files to be archived.")              
            for index, folder in enumerate(self.processedArchiveFolder, start=0):
                self.folderChecker(folder)
                                    
            #Errored Archive folder 
            logging.info(f"{self.useCase}:Initiating folder check for Error file to be archived.")              
            for index, folder in enumerate(self.erroredArchiveFolder, start=0):
                self.folderChecker(folder)     
                
        except IOError as e:
            #logging.error(f"{self.useCase}:Error Encnoutered during Execution")
            #logging.error(e.errno)
            logging.error(f"{self.useCase}:{e}")        
 
 
    def setConfig(self):   
        try:                                                
            if os.path.isfile(self.configpath):
                # Set additional configs
                #config = configparser.ConfigParser()
                self.config.read(self.configpath)
            
                # Main section config
                version = self.config.get('MainSection', 'VersionFC')            
                retry = self.config.get('MainSection', 'retry')
                self.sleeptime = int(self.config.get('MainSection', 'sleeptime'))
                self.environment = self.config.get('MainSection', 'environment')   
                
                # logger Section config
                self.logfilepath = self.config.get(f"{self.environment}.LoggerSection",'filepathFC')
            
                #loglevel = config.get(f"{environment}.LoggerSection",'level')            
                #logformat = config.get(f"{environment}.LoggerSection",'format')            
                logfilesize = int(self.config.get(f"{self.environment}.LoggerSection",'filesize'))
                logbackupcnt = int(self.config.get(f"{self.environment}.LoggerSection",'backupcount')) 
                
                # Logging configs
                logging.basicConfig(handlers=[RotatingFileHandler(self.logfilepath,maxBytes=logfilesize,backupCount=logbackupcnt)],level=logging.DEBUG,format='%(asctime)s %(levelname)s PID_%(process)d %(message)s')                            
                  
                logging.info(f"{self.useCase}:FileCleaner Started." )
                logging.info(f"{self.useCase}:Running Version from config: {version}" )
                logging.info(f"{self.useCase}:Sleep time: {self.sleeptime}sec from config")            
                logging.info(f"{self.useCase}:log file max size before roll: {logfilesize} from config")
                logging.info(f"{self.useCase}:log file max backup counter: {logbackupcnt} from config")
                logging.info(f"{self.useCase}:Config UseCase Section: {self.cfgUseCase}")
                                
                # UseCase section config    
                self.psbuildtypes = self.config.get(f"{self.environment}.{self.cfgUseCase}",'psbuildtypes').split(",")
                self.processnames = self.config.get(f"{self.environment}.{self.cfgUseCase}",'processnames').split(",")                                 
                                                               
                if self.cfgUseCase == "ManageGrades":
                   self.psbuildtypecount = 3
                   self.manageGradesCfg()
                elif self.cfgUseCase ==  "ProgressionGradeLadder":
                   self.psbuildtypecount = 2
                   self.progressionGradeLadderCfg()                 
                                                                    
        except IOError as e:
            #logging.error(f"{self.useCase}:Error Encnoutered during Execution")
            #logging.error(e.errno)
            logging.error(f"{self.useCase}:{e}")
        
        
    #def filecleaner(self, usecase, psbuildtype, uploader):
    #def filecleaner(self, usecase: str, psbuildtype: str, origfilename: str, fileid: str):
    def filecleaner(self, usecase: str, fileid: str):
        try:        
            self.useCase = usecase
            self.cfgUseCase = self.useCase.replace(" ","")
            tmpfileid = fileid.split("_")
            self.processName = tmpfileid[2]            
            #self.psBuildType = psbuildtype
                   
                                     
            # Set configs
            self.setConfig()  
            #psbindex = self.psbuildtypes.index(psbuildtype)            
            psbindex = self.processnames.index(self.processName)  
            #print(psbindex)            
            self.psBuildType = self.psbuildtypes[psbindex]
            
            # Run Archiving functions                        
            logging.info(f"{self.useCase}-{self.psBuildType}:Initiate file cleaner for fileID. {fileid}")            
            #self.archiveProcessedFiles(self.processFolder[psbindex], self.processedArchiveFolder[psbindex], self.psBuildType, origfilename, fileid)
            #self.archiveErroredFiles(self.processFolder[psbindex], self.erroredArchiveFolder[psbindex], self.psBuildType, origfilename, fileid)
            
            self.archiveProcessedFiles(self.processFolder[psbindex], self.processedArchiveFolder[psbindex], fileid)
            self.archiveErroredFiles(self.processFolder[psbindex], self.erroredArchiveFolder[psbindex], fileid)
            
            # Now Remove archived files from processing folder
            for f in self.processedFilesforDelete:
                if os.path.isfile(f): 
                    os.remove(f)
        except IOError as e:
            #logging.error(f"{self.useCase}:Error Encnoutered during Execution")
            #logging.error(e.errno)
            logging.error(f"{self.useCase}:{e}")
        finally:
            logging.info(f"{self.useCase}:FileCleaner Ended." )        
           
#if __name__ == '__main__':
    
    