import os
from os import listdir
from os.path import isfile, join
import time
import argparse
import configparser
import logging
from logging.handlers import RotatingFileHandler
import openpyxl
from threading import Thread

import sys
import pathlib
sys.path.append(str(pathlib.Path(__file__).parents[1].resolve()))
from manage_grades import manage_grades
import pathlib
# from progression_grade_ladder import progression_grade_ladder

class fileWatcher:
     
    useCase = "" 
    cfgUseCase = ""    
    inFolderToWatch = [] 
    processFolder = []   
    archiveFolder = []
    outFolder = []    
    psbuildtypes = []
    processnames = []    
    sleeptime = 0
    environment = "" 
    fw_folderpath = str(pathlib.Path(__file__).parent.resolve())
    psbuildtypecount = 3    
       
    #function to return files in In-bound folder
    def fileInInboundFolder(self, folder):
        #onlyfiles = [f for f in listdir(folder) if isfile(join(folder, f))]
        files = os.listdir(folder)
        onlyfiles = [f for f in files if f.endswith('.xlsx')]        
        return(onlyfiles)
    

    #function comparing two lists
    def listComparison(self, OriginalList: list, NewList: list):
        differencesList = [x for x in NewList if x not in OriginalList] #Note if files get deleted, this will not highlight them
        return(differencesList)


    #function check if file is not locked
    def checkFileNotLocked(self, myFile: str, psbtype: str):
        fileNotLocked = False
        try:
            fp = open(myFile,'a')
            fp.close()
            logging.info(f"{self.useCase}-{psbtype}:Found File {myFile}")
            fileNotLocked = True            
        except IOError as e:           
            logging.error(f"{self.useCase}:{e}")
        finally:
            return(fileNotLocked)        

  
    #function check excel file properties
    def checExcelFileProperties(self, myExcelFile: str, psbtype: str):
        try:
            wb = load_workbook(myExcelFile)
            wb.properties
            #logging.info(f"{myExcelFile} Metadata/Properties: {wb.properties}")
            logging.info(f"{self.useCase}-{psbtype}:{myExcelFile} Metadata/Properties extracted")
            return(wb.properties)
        except IOError as e:           
            logging.error(f"{self.useCase}-{psbtype}:{e}")

 
    #function to execute main script for transformation
    def runTransformationMain(self, psbtype: str, fileID: str):   
        logging.info(f"{self.useCase}-{psbtype}:Start processing fileID: {fileID}")               
        if self.useCase == "Manage Grades":
            #Execute here main py script for Manage Grades Use Case           
            #manage_grade(myFile, psbtype.replace(" ","")) 
            # mgfile_path = str(pathlib.Path(__file__).parents[1].resolve()) + "/manage_grades.py"
            # command = f"python {mgfile_path} --file_id {fileID} --psbuild_type {psbtype.replace(' ','')}"
            # print(command)       
            # os.system(f"start cmd /c {command}")
            manage_grades(fileID, psbtype.replace(" ",""))                                 
        elif self.useCase == "Progression Grade Ladder":
            progression_grade_ladder(fileID, psbtype.replace(" ",""))
       # Temporary: just to show aysnchronous run    
       # for i in range(20):
       #     print(f"{self.useCase}-{psbtype}:{i}:Processing Transformation Task for: {myFile} -{email}")
       #     time.sleep(1)

 
    #funtion to process new In File(s)
    def processNewInBoundfiles(self, procname: str, psbtype: str, newFiles: list, sourceFolder: str, targetFolder: str):    
        logging.info(f'{self.useCase}-{psbtype}:Processing new INBOUND file(s) {newFiles}')                   
        for filename in newFiles:
            status = False
            for i in range(10):
                time.sleep(self.sleeptime)
                status = self.checkFileNotLocked(f'{sourceFolder}{filename}',psbtype)
                if status == True:
                   break
                                      
            #if self.checkFileNotLocked(f'{sourceFolder}{filename}',psbtype) == True: 
            if status == True:            
                #newFilePath = self.renameAndMoveFile(procname, sourceFolder, targetDir, filename)                
                sourceFilePath = f"{sourceFolder}{filename}"
                tmpFileName = filename.split(".")
                tmpFileName2= tmpFileName[0].split("_")
                tmpDateTimeStr = time.strftime("%Y%m%d%H%M%S")                                                                            
                targetFilePath = f"{targetFolder}{tmpFileName[0]}_{procname}_{tmpDateTimeStr}.{tmpFileName[1]}"                        
                if os.path.isfile(sourceFilePath):
                    os.rename(sourceFilePath, targetFilePath)
                    logging.info(f"{self.useCase}-{psbtype}:In-bound file: {sourceFilePath}")
                    logging.info(f"{self.useCase}-{psbtype}:Moved to file: {targetFilePath}") 
                    
                # Execute Main Transformation script 
                fileID =  f"{tmpFileName2[1]}_{tmpFileName2[2]}_{procname}_{tmpDateTimeStr}"
                #print(fileID)                
                #self.runTransformationMain(psbtype, targetFilePath, fileID)
                self.runTransformationMain(psbtype, fileID)
  
  
    # Function that checks for folder and create when not found that is called from Main
    def folderChecker(self, myDir: str, psbtype: str):
        #Check In-bound Folders Exist                       
        if os.path.isdir(myDir):
            logging.info(f"{self.useCase}-{psbtype}:Found Folder {myDir}")
        else:
            logging.info(f"{self.useCase}-{psbtype}:Not Found Folder {myDir}")   
            try:           
                os.makedirs(myDir)
                logging.info(f"{self.useCase}-{psbtype}:Created Folder {myDir}")
            except IOError as e:                
                logging.error(f"{self.useCase}-{psbtype}:{e}")            


    def manageGrades(self):
        self.inFolderToWatch.append(config.get(f"{self.environment}.{self.cfgUseCase}",'infolderWithExistingATB'))                      
        self.inFolderToWatch.append(config.get(f"{self.environment}.{self.cfgUseCase}",'infolderWithATB'))            
        self.inFolderToWatch.append(config.get(f"{self.environment}.{self.cfgUseCase}",'infolderWithoutATB'))
            
        self.processFolder.append(config.get(f"{self.environment}.{self.cfgUseCase}",'processfolderWithExistingATB'))                      
        self.processFolder.append(config.get(f"{self.environment}.{self.cfgUseCase}",'processfolderWithATB'))            
        self.processFolder.append(config.get(f"{self.environment}.{self.cfgUseCase}",'processfolderWithoutATB'))
            
        self.archiveFolder.append(config.get(f"{self.environment}.{self.cfgUseCase}",'archivefolderWithExistingATB'))                      
        self.archiveFolder.append(config.get(f"{self.environment}.{self.cfgUseCase}",'archivefolderWithATB'))            
        self.archiveFolder.append(config.get(f"{self.environment}.{self.cfgUseCase}",'archivefolderWithoutATB'))
            
        self.outFolder.append(config.get(f"{self.environment}.{self.cfgUseCase}",'outfolderWithExistingATB'))                      
        self.outFolder.append(config.get(f"{self.environment}.{self.cfgUseCase}",'outfolderWithATB'))            
        self.outFolder.append(config.get(f"{self.environment}.{self.cfgUseCase}",'outfolderWithoutATB'))
                
        logging.info(f"{self.useCase}:PS Build types: {self.psbuildtypes}")
        logging.info(f"{self.useCase}:Process names: {self.processnames}")
        logging.info(f"{self.useCase}:In-bound Folders: {self.inFolderToWatch}")             
        logging.info(f"{self.useCase}:Process Folders: {self.processFolder}")
        logging.info(f"{self.useCase}:Archive Folders: {self.archiveFolder}")  
        logging.info(f"{self.useCase}:Archive Folders: {self.outFolder}")
        
        #Run folder Checker 
        #In-bound folder
        logging.info(f"{self.useCase}:Initiating folder checker for in-bound files.")
        for index, folder in enumerate(self.inFolderToWatch, start=0):
            self.folderChecker(folder, self.psbuildtypes[index])
             
        #Process folder 
        logging.info(f"{self.useCase}:Initiating folder checker for files to be processed.")              
        for index, folder in enumerate(self.processFolder, start=0):
            self.folderChecker(folder, self.psbuildtypes[index])
            
        #Out-bound folder 
        logging.info(f"{self.useCase}:Initiating folder checker for out-bound files.")              
        for index, folder in enumerate(self.outFolder, start=0):
            self.folderChecker(folder, self.psbuildtypes[index])
 
 
    def progressionGradeLadder(self):                                     
        self.inFolderToWatch.append(config.get(f"{self.environment}.{self.cfgUseCase}",'infolderWithATB'))            
        self.inFolderToWatch.append(config.get(f"{self.environment}.{self.cfgUseCase}",'infolderWithoutATB'))
                                          
        self.processFolder.append(config.get(f"{self.environment}.{self.cfgUseCase}",'processfolderWithATB'))            
        self.processFolder.append(config.get(f"{self.environment}.{self.cfgUseCase}",'processfolderWithoutATB'))
                                          
        self.archiveFolder.append(config.get(f"{self.environment}.{self.cfgUseCase}",'archivefolderWithATB'))            
        self.archiveFolder.append(config.get(f"{self.environment}.{self.cfgUseCase}",'archivefolderWithoutATB'))
                                          
        self.outFolder.append(config.get(f"{self.environment}.{self.cfgUseCase}",'outfolderWithATB'))            
        self.outFolder.append(config.get(f"{self.environment}.{self.cfgUseCase}",'outfolderWithoutATB'))
                
        logging.info(f"{self.useCase}:PS Build types: {self.psbuildtypes}")
        logging.info(f"{self.useCase}:Process names: {self.processnames}")
        logging.info(f"{self.useCase}:In-bound Folders: {self.inFolderToWatch}")             
        logging.info(f"{self.useCase}:Process Folders: {self.processFolder}")
        logging.info(f"{self.useCase}:Archive Folders: {self.archiveFolder}")  
        logging.info(f"{self.useCase}:Archive Folders: {self.outFolder}")
        
        #Run folder Checker 
        #In-bound folder
        logging.info(f"{self.useCase}:Initiating folder checker for in-bound files.")
        for index, folder in enumerate(self.inFolderToWatch, start=0):
            self.folderChecker(folder, self.psbuildtypes[index])
             
        #Process folder 
        logging.info(f"{self.useCase}:Initiating folder checker for files to be processed.")              
        for index, folder in enumerate(self.processFolder, start=0):
            self.folderChecker(folder, self.psbuildtypes[index])
            
        #Out-bound folder 
        logging.info(f"{self.useCase}:Initiating folder checker for out-bound files.")              
        for index, folder in enumerate(self.outFolder, start=0):
            self.folderChecker(folder, self.psbuildtypes[index])
        
        
    # Main file watcher function that is called from Main
    def fileWatcher(self):
        #self.object.runmainprocess()    
        sleepctr = 0
        
        newInFileList = [[]] * self.psbuildtypecount       
        infileDiff = [[]] * self.psbuildtypecount  
        previousInFileList = [[]] * self.psbuildtypecount              
        while True:
            # Handle logging of sleep everytime sleep counter is set to 1. Maximum counter is 25 then goes back to 1. 
            sleepctr += 1    
            if sleepctr == 1: 
                logging.info(f"{self.useCase}:Sleeping...")
            elif sleepctr == 60:
                sleepctr = 0            
               
            time.sleep(self.sleeptime)
            
            # In-bound folders watch         
            for index, item in enumerate(self.inFolderToWatch, start=0):
                newInFileList[index] = self.fileInInboundFolder(item)
                infileDiff[index] = self.listComparison(previousInFileList[index], newInFileList[index])
                previousInFileList[index] = newInFileList[index]
                if len(infileDiff[index]) > 0: 
                    #print(infileDiff[index])
                    #self.processNewInBoundfiles(infileDiff[index],item)
                    try:
                        t = Thread(target=self.processNewInBoundfiles, args=(self.processnames[index], self.psbuildtypes[index], infileDiff[index], item, self.processFolder[index]))                        
                        t.start()                                                 
                    except:
                        logging.error(f"{self.useCase}-{self.psbuildtypes[index]}: Unable to start thread.")
                    
                                                                                                                                                                    
if __name__ == '__main__':
    try:        
        fw = fileWatcher()
        
        # set arguments/parameters parser
        parser = argparse.ArgumentParser(description='Utility for searching through payment master report file')
        parser.add_argument('--usecase', help='UseCase', default="Manage Grades")
        parser.add_argument('--environment', help='Execution environment DEV or PROD', default="")
        parser.add_argument('--configfile', help='UseCase config file path', default="filewatcherCfg.ini")  
        parser.add_argument('--logfile', help='UseCase log file path', default="filewatcher.log")        
        args = parser.parse_args()
        
        # set main arugments/parameters values
        fw.useCase = args.usecase
        fw.cfgUseCase = fw.useCase.replace(" ","")        
        configpath = args.configfile  
        
        if configpath == "filewatcherCfg.ini":
           configpath = fw.fw_folderpath + "/filewatcherCfg.ini"        
        
        if os.path.isfile(configpath):
            # Set additional configs
            config = configparser.ConfigParser()
            config.read(configpath)
            
            # Main section config
            version = config.get('MainSection', 'VersionFW')            
            retry = config.get('MainSection', 'retry')
            fw.sleeptime = int(config.get('MainSection', 'sleeptime'))
            if args.environment != "":
                fw.environment = args.environment
            else:
                fw.environment = config.get('MainSection', 'environment')
            
            # logger Section config
            if args.logfile != "":                
                logfilepath = args.logfile 
            else:
                logfilepath = config.get(f"{fw.environment}.LoggerSection",'filepathFW')

            if logfilepath == "filewatcher.log":
               logfilepath = fw.fw_folderpath + "/filewatcher.log"             
                       
            logfilesize = int(config.get(f"{fw.environment}.LoggerSection",'filesize'))
            logbackupcnt = int(config.get(f"{fw.environment}.LoggerSection",'backupcount'))            
            #logging.basicConfig(handlers=[RotatingFileHandler(logfilepath,maxBytes=logfilesize,backupCount=logbackupcnt)],level=logging.DEBUG,format='%(asctime)s %(levelname)s PID_%(process)d %(message)s')  
            logging.basicConfig(handlers=[RotatingFileHandler(logfilepath,maxBytes=logfilesize,backupCount=logbackupcnt)],level=logging.DEBUG,format='%(asctime)s %(levelname)s %(message)s')                        
            
            # Uploader Email Address Section config
            fw.emailAddLookupFile = config.get(f"{fw.environment}.UploaderEmalAddresseSection",'filepath')
            fw.emailAddcolumn = int(config.get(f"{fw.environment}.UploaderEmalAddresseSection",'emailAddresscolumn'))            
             
            logging.info(f"{fw.useCase}:FileWatcher Started." )
            logging.info(f"{fw.useCase}:Running Version: {version}" )
            
            logging.info(f"{fw.useCase}:Loaded config from {configpath}")
            logging.info(f"{fw.useCase}:log file max size before roll: {logfilesize}")
            logging.info(f"{fw.useCase}:log file max backup counter: {logbackupcnt}")
            logging.info(f"{fw.useCase}:Sleep time: {fw.sleeptime}sec")
            logging.info(f"{fw.useCase}:Config UseCase Section: {fw.cfgUseCase}")
            
            # UseCase section config
            fw.psbuildtypes = config.get(f"{fw.environment}.{fw.cfgUseCase}",'psbuildtypes').split(",")
            fw.processnames = config.get(f"{fw.environment}.{fw.cfgUseCase}",'processnames').split(",")            
            
            if fw.cfgUseCase == "ManageGrades":
               fw.psbuildtypecount = 3 
               fw.manageGrades()
            elif fw.cfgUseCase ==  "ProgressionGradeLadder":
               fw.psbuildtypecount = 2
               fw.progressionGradeLadder()
                                          
            #Run file watcher Main 
            logging.info(f"{fw.useCase}:Initiate file watcher process for inbound PS Build files.")
            fw.fileWatcher()
            
    except IOError as e:        
        logging.error(f"{fw.useCase}:{e}")
    finally:
        logging.info(f"{fw.useCase}:FileWatcher Ended." )
    