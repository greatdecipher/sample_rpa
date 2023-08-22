# Loader Automation and Copy of Cloud Build
Downloader for Myaci Loader templates and Automation for Excel Navigation

# Automation Setup
1. Create project folder.
2. Clone Repository
3. Setup virtual environment (venv)
4. Requirements are updating, to install all of it:
  ```pip install -r requirements.txt```
5. to switch into parent folder 
  ```cd ..```
6. Run demo_loader.py

# Module Information
* __main_app.py__  - to run the application.
* __playwright_bot.py__ - for myaci login/MFA and downloader automation.
* __loader_automation.py__ - for Manage grades excel navigation using PYWINAUTO Library.
* __pgrade_loader_auto.py__ - for Progression grades excel navigation using PYWINAUTO Library.
* __detect_transfer_bot.py__ - for detecting loader changes and copy cloud build to loader.
* __config.py__ - all configs.
* __getMfacode.py__ - for manual authentication.

