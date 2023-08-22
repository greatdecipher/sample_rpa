from os.path import isfile, join
from os.path import basename
import os
import pathlib
import smtplib
import time
import argparse
import configparser
import logging
import openpyxl
from openpyxl import load_workbook
from logging.handlers import RotatingFileHandler
from email.mime.multipart import MIMEMultipart
from email.mime.base import MIMEBase
from email.mime.text import MIMEText
from email import encoders
import shutil


class eMailer:
    
    em_folderpath = str(pathlib.Path(__file__).parent.resolve())
    useCase = ""
    psBuildType = ""
    processName = ""    
    processFolder = []   
    processedArchiveFolder = []
    erroredArchiveFolder = []
    PGLinFolder = []
    psbuildtypes = []
    processnames = []
    sleeptime = 0
    folderdivider = "/"     
    cfgUseCase = ""
    environment = ""
    configpath = em_folderpath + "/emailerCfg.ini"
    config = configparser.ConfigParser() 
    logfilepath = em_folderpath + "/emailer.log"
    emaillookuppath = em_folderpath + "/uploaderemail.xlsx"
    emailAddcolumn = 3
    emailCCdistro = ""   
    fileID = ""    
    smtopserver="mailrouter.safeway.com"
    smtpport=25
    emailsender="erey118@albertsons.com"
    psbuildtypecount = 3


    def fileCopy(self, source: str, destination: str):  
        try:     
            shutil.copy(source, destination)
            logging.info(f"{self.useCase}:Copied file from {source} to {destination}")
        except IOError as e:            
            logging.error(f"{self.useCase}-{self.psBuildType}:{e}")
            
            
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
   
   
    def getEmailAddFromExcelFile(self, myExcelFile: str, uploaderldap: str):
        # Open the spreadsheet
        try:
            email = ""  # Default Group email just in case uploader is not found in spreadsheet
            name = ""            
            workbook = openpyxl.load_workbook(myExcelFile)            
            # Get the first sheet
            sheet = workbook.worksheets[0]            
            # Iterate over the rows in the sheet
            for row in sheet:
                # Get the value of the first cell
                # in the row (the "ldap" cell)
                ldap = row[0].value                
                #ldap = row[1].value
                email= row[self.emailAddcolumn].value
                name= row[1].value # Full Name                
                if (ldap.upper() == uploaderldap.upper()):   #or (ldap.find(ldap))
                   logging.info(f"{self.useCase}-{self.psBuildType}:Found Uploader LDAP: {ldap} - Name: {name}, E-mail {email}")
                   break 
            if email == "":
               logging.error(f"{self.useCase}-{self.psBuildType}: Not Found Uploader LDAP: {ldap}")            
        except IOError as e:            
            logging.error(f"{self.useCase}-{self.psBuildType}:{e}")
        finally:
            return(email,name) 
   
     
    def eMailSender(self, archiveDir: str, fileID: str, status: str, remark: str):
        try:                         
            tmpfileid = fileID.split("_")
            senderLDAP = tmpfileid[0]
            logging.info(f"{self.useCase}-{self.psBuildType}: FileID {fileID}")
            logging.info(f"{self.useCase}-{self.psBuildType}: Status {status}")
            logging.info(f"{self.useCase}-{self.psBuildType}: Remark {remark}")
            tmpEmailName = self.getEmailAddFromExcelFile(self.emaillookuppath, senderLDAP)            
            recipientEmail = tmpEmailName[0]
            recipientName = tmpEmailName[1]
            
            if recipientEmail == "":
               logging.info(f"{self.useCase}-{self.psBuildType}:Not Found Uploader ID and email. Sending email to {self.emailCCdistro}")
               recipientEmail == self.emailCCdistro
               recipientName == "Team"
               
            YYYY_mm = time.strftime("%Y_%m") #time.strftime("%m")            
            zipDir = f"{archiveDir}{YYYY_mm}{self.folderdivider}"    
            self.folderChecker(zipDir)
            tmpDateTimeStr = time.strftime("%Y%m%d-%H%M%S")
            zipFilePath = f"{zipDir}{fileID}.zip"
            logging.info(f"{self.useCase}-{self.psBuildType}: Attachment file {zipFilePath}")
            
            attachment = zipFilePath

            msg = MIMEMultipart()            
            msg['Subject'] = f"Bot: Manage Grades - {status} - {remark}"
            msg['From'] = self.emailsender
            msg['To'] = recipientEmail 
            msg['Cc'] = self.emailCCdistro
            body = (
                   f"Hi {recipientName},\n\n"
                   f"Please see attachment for your review.\n"
                   f"You may also access this via:\n"
                   f"{zipFilePath},\n\n"
                   f"Thank you!"
                   )
            msg.attach(MIMEText(body, 'plain'))  # Attaching body to email

            part = MIMEBase('application', "octet-stream")
            part.set_payload(open(attachment, "rb").read())
            encoders.encode_base64(part)

            part.add_header('Content-Disposition', 'attachment', filename=os.path.basename(attachment))
            msg.attach(part)

            try:
                logging.info(f"{self.useCase}-{self.psBuildType}: Sending email to {recipientEmail} for FileID {fileID}")
                smtpObj = smtplib.SMTP(self.smtopserver, self.smtpport)
                smtpObj.sendmail(self.emailsender, recipientEmail, msg.as_string())                         
                logging.info(f"{self.useCase}-{self.psBuildType}: Successfully sent email to {recipientEmail} for FileID {fileID}")
            except BaseException as e:
                logging.error(f"{self.useCase}-{self.psBuildType}:{e}") 
                
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
            
            self.PGLinFolder.append(self.config.get(f"{self.environment}.{self.cfgUseCase}",'PGLinfolderWithATB'))            
            self.PGLinFolder.append(self.config.get(f"{self.environment}.{self.cfgUseCase}",'PGLinfolderWithoutATB'))
                    
            logging.info(f"{self.useCase}:Process names: {self.processnames}")            
            logging.info(f"{self.useCase}:Loaded config from {self.configpath}")
            logging.info(f"{self.useCase}:PS Build Types from config: {self.psbuildtypes}")                                       
            logging.info(f"{self.useCase}:Process Folders from config: {self.processFolder}")
            logging.info(f"{self.useCase}:Archive Folders(Processed) from config: {self.processedArchiveFolder}")
            logging.info(f"{self.useCase}:Archive Folders(Errored) from config: {self.erroredArchiveFolder}")                     
                
        except IOError as e:           
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
                    
            logging.info(f"{self.useCase}:Process names: {self.processnames}")            
            logging.info(f"{self.useCase}:Loaded config from {self.configpath}")
            logging.info(f"{self.useCase}:PS Build Types from config: {self.psbuildtypes}")                                       
            logging.info(f"{self.useCase}:Process Folders from config: {self.processFolder}")
            logging.info(f"{self.useCase}:Archive Folders(Processed) from config: {self.processedArchiveFolder}")
            logging.info(f"{self.useCase}:Archive Folders(Errored) from config: {self.erroredArchiveFolder}")                     
                
        except IOError as e:           
            logging.error(f"{self.useCase}:{e}")    
 
 
    def setConfig(self):   
        try:                                                
            if os.path.isfile(self.configpath):
                # Set additional configs
                #config = configparser.ConfigParser()
                self.config.read(self.configpath)
            
                # Main section config
                version = self.config.get('MainSection', 'VersionEM')            
                retry = self.config.get('MainSection', 'retry')
                self.sleeptime = int(self.config.get('MainSection', 'sleeptime'))
                self.environment = self.config.get('MainSection', 'environment')   
                
                # logger Section config
                self.logfilepath = self.config.get(f"{self.environment}.LoggerSection",'filepathEM')
            
                #loglevel = config.get(f"{environment}.LoggerSection",'level')            
                #logformat = config.get(f"{environment}.LoggerSection",'format')            
                logfilesize = int(self.config.get(f"{self.environment}.LoggerSection",'filesize'))
                logbackupcnt = int(self.config.get(f"{self.environment}.LoggerSection",'backupcount'))                
                
                # Uploader Email Address Section config
                self.emaillookuppath = self.em_folderpath + "/" + self.config.get(f"{self.environment}.UploaderEmalSection",'filepath')
                self.emailAddcolumn = int(self.config.get(f"{self.environment}.UploaderEmalSection",'emailAddresscolumn'))
                self.emailCCdistro = self.config.get(f"{self.environment}.UploaderEmalSection",'emailCCdistro')     
                self.smtopserver = self.config.get(f"{self.environment}.UploaderEmalSection",'smtopserver')  
                self.smtpport = int(self.config.get(f"{self.environment}.UploaderEmalSection",'smtpport')) 
                self.emailsender = self.config.get(f"{self.environment}.UploaderEmalSection",'emailsender')  
                
                
                # Logging configs
                logging.basicConfig(handlers=[RotatingFileHandler(self.logfilepath,maxBytes=logfilesize,backupCount=logbackupcnt)],level=logging.DEBUG,format='%(asctime)s %(levelname)s PID_%(process)d %(message)s')                            
                  
                logging.info(f"{self.useCase}:E-Mailer Started." )
                logging.info(f"{self.useCase}:Running Version from config: {version}" )
                logging.info(f"{self.useCase}:Sleep time: {self.sleeptime}sec from config")            
                logging.info(f"{self.useCase}:log file max size before roll: {logfilesize} from config")
                logging.info(f"{self.useCase}:log file max backup counter: {logbackupcnt} from config")
                logging.info(f"{self.useCase}:Sender email lookup file: {self.emaillookuppath} from config")
                logging.info(f"{self.useCase}:Sender email column from lookup file: {self.emailAddcolumn} from config")
                logging.info(f"{self.useCase}:Sender email cc Distro: {self.emailCCdistro} from config")
                logging.info(f"{self.useCase}:SMTP Server: {self.smtopserver} from config")
                logging.info(f"{self.useCase}:SMTP port: {self.smtpport} from config")
                logging.info(f"{self.useCase}:Email sender: {self.emailsender} from config")
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
            logging.error(f"{self.useCase}:{e}")


    def emailer(self, usecase: str, fileid: str, status: str, remark: str):
        try:        
            self.useCase = usecase
            self.cfgUseCase = self.useCase.replace(" ","")
            tmpfileid = fileid.split("_")
            self.processName = tmpfileid[2]            
                                                           
            # Set configs
            self.setConfig()                        
            psbindex = self.processnames.index(self.processName)
            print(psbindex)               
            self.psBuildType = self.psbuildtypes[psbindex]
            logging.info(f"{self.useCase}:PS Build Type param: {self.psBuildType}")
            
            # Run Emailing functions                        
            logging.info(f"{self.useCase}-{self.psBuildType}:Initiate E-mail sender for fileID. {fileid}")                                    
            self.eMailSender(self.processedArchiveFolder[psbindex], fileid, status, remark)
            if self.cfgUseCase == "ManageGrades":
                if status == "Success":
                    if self.processName.upper() == "MGWATB":
                        self.fileCopy(f"{self.processFolder[1]}PSBUILD_{fileid}.xlsx", f"{self.PGLinFolder[0]}PSBUILD_{tmpfileid[0]}_{tmpfileid[1]}.xlsx")
                        if os.path.isfile(f"{self.processFolder[1]}PSBUILD_{fileid}.xlsx"): 
                           os.remove(f"{self.processFolder[1]}PSBUILD_{fileid}.xlsx")
                    elif self.processName.upper() == "MGWOATB":
                        self.fileCopy(f"{self.processFolder[2]}PSBUILD_{fileid}.xlsx", f"{self.PGLinFolder[1]}PSBUILD_{tmpfileid[0]}_{tmpfileid[1]}.xlsx")                   
                        if os.path.isfile(f"{self.processFolder[2]}PSBUILD_{fileid}.xlsx"): 
                           os.remove(f"{self.processFolder[2]}PSBUILD_{fileid}.xlsx")
                                                     
                        
        except IOError as e:
            #logging.error(f"{self.useCase}:Error Encnoutered during Execution")
            #logging.error(e.errno)
            logging.error(f"{self.useCase}:{e}")
        finally:
            logging.info(f"{self.useCase}:E-Mailer Ended." )     
