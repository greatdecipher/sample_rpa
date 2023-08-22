ldap_config = {
    "email": "erey118@safeway.com",
    "username": "EREY118",
    "password": "Qkgu_8HCna",
    "sal_plan_password": "aw6_MPKA7K",
    "otp_secret": "62hnq7fcnbhs5bfr",
}

abs_config = {
        "ldap_email": {
            "selector": "#i0116",
            "error_results": {
                    "error_code": "Manage Grade-001",
                    "error_message": "Email input box not found."
                }
        },
        "next_button": {
            "selector": "#idSIButton9",
            "error_results": {
                    "error_code": "Manage Grade-002",
                    "error_message": "Next button not found."
                }
        },
        "error_login": {
            "selector": "#usernameError",
            "error_results": {
                    "error_code": "Manage Grade-003",
                    "error_message": "Username or Password is incorrect."
                }
        },
        "ldap_username": {
            "selector": "#userNameInput",
            "error_results": {
                    "error_code": "Manage Grade-004",
                    "error_message": "Username input box not found."
                }
            },
        "ldap_password": {
            "selector": "#passwordInput",
            "error_results": {
                    "error_code": "Manage Grade-005",
                    "error_message": "Password input box not found."
                }
            },
        "ldap_login_button": {
                "selector": "span#submitButton",
                "error_results": {
                    "error_code": "Manage Grade-006",
                    "error_message": "Sign in button not found."
                }
            },
        "ldap_error_text": {
                "selector": "#errorText",
                "error_results": {
                    "error_code": "Manage Grade-007",
                    "error_message": "Incorrect user ID or password. "
                                     "Type the correct user ID and password, and try again."
                }
            },
        "otp_input_box": {
                "selector": "input[name='otc']",
                "error_results": {
                    "error_code": "Manage Grade-008",
                    "error_message": "OTP Input Box not found."
                }
            },
        "otp_error_text": {
                "selector": "#idSpan_SAOTCC_Error_OTC",
                "error_results": {
                    "error_code": "Manage Grade-009",
                    "error_message": "OTP Code is invalid."
                }
            },
        "verify_otp_btn": {
                "selector": "#idSubmit_SAOTCC_Continue",
                "error_results": {
                    "error_code": "Manage Grade-010",
                    "error_message": "Verify OTP Button not found"
                },
            }, # #ssoBtn
        "sso_btn": {
                "selector": "#ssoBtn",
                "error_results": {
                    "error_code": "Manage Grade-011",
                    "error_message": "Single Sign-on Button not found"
                },
            },
}

loader_config = {
        "url": {
            "PROD": {
                "value": "https://eofd.fa.us6.oraclecloud.com/fscmUI/",
                "error_results": {
                        "error_code": "Loader-001",
                        "error_message": "Unable load url."
                        }
                },
            "DEV": {
                "value": "https://eofd-dev8.fa.us6.oraclecloud.com/",
                "error_results": {
                        "error_code": "Loader-001",
                        "error_message": "Unable load url."
                        }
                }
        },
        "group_node_tab": {
            "selector": "#groupNode_workforce_management",
            "error_results": {
                    "error_code": "Loader-002",
                    "error_message": "My Client Group tab not found"
                        }
                },
        "data_exchange_card": {
            "selector": "#itemNode_workforce_management_data_exchange",
            "error_results": {
                    "error_code": "Loader-003",
                    "error_message": "Data Exchange card not found"
                        }
        },
        "template_link": {
            "selector": "#_FOpt1\:_FOr1\:0\:_FONSr2\:0\:_FOTsr1\:0\:ll01Upl\:UPsp1\:ll01Pce\:ll01Itr\:1\:ll02Pce\:ll01Lv\:1\:ll01Pse\:ll01Cl",
            "error_results": {
                    "error_code": "Loader-004",
                    "error_message": "Template link not found"
                        }
                },
        "clear_button": {
            "selector": "#_FOpt1\:_FOr1\:0\:_FONSr2\:0\:MAnt2\:1\:AP1\:ls1\:_LSCL",
            "error_results": {
                    "error_code": "Loader-005",
                    "error_message": "Clear Button not found"
                        }
                },
        "keyword_input_box": {
            "value": "CSG_Manage Grades with ATB",
            "selector": "#_FOpt1\:_FOr1\:0\:_FONSr2\:0\:MAnt2\:1\:AP1\:ls1\:_LSSF\:\:content",
            "error_results": {
                    "error_code": "Loader-006",
                    "error_message": "Keyword input box not found"
                        }
                },
        "search_button": {
            "selector": "#_FOpt1\:_FOr1\:0\:_FONSr2\:0\:MAnt2\:1\:AP1\:ls1\:_LSSB",
            "error_results": {
                    "error_code": "Loader-007",
                    "error_message": "Search Button not found"
                        }
                },
        "div_click": {
            "selector": "#_FOpt1\:_FOr1\:0\:_FONSr2\:0\:MAnt2\:1\:AP1\:ls1\:pgl10",
            "error_results": {
                    "error_code": "Loader-008",
                    "error_message": "Search Button not found"
                        }
                },
        "report_link": {
                "selector": "#_FOpt1\:_FOr1\:0\:_FONSr2\:0\:MAnt2\:1\:AP1\:ls1\:lv1\:25\:commandLink3",
                "error_results": {
                    "error_code": "Loader-009",
                    "error_message": "Report link not found"
                        }
                },
        "preview_button": {
                "selector": "#_FOpt1\:_FOr1\:0\:_FONSr2\:0\:MAnt2\:2\:AP1\:gb1",
                "error_results": {
                    "error_code": "Loader-010",
                    "error_message": "Preview Button not found"
                        }
        },
        "home_button": {
                "selector": "#pt1\:_UIShome\:\:icon > g:nth-child(5) > path",
                "error_results": {
                    "error_code": "Loader-011",
                    "error_message": "Home Button not found"
                        }
        },

}

csg_config = {
    # DEV* https://eofd-dev8.fa.us6.oraclecloud.com/
        "url": {
            "PROD": {
                "value": "https://eofd.fa.us6.oraclecloud.com/fscmUI/",
                "error_results": {
                        "error_code": "CSG-001",
                        "error_message": "Unable load url."
                        }
                },
            "DEV": {
                "value": "https://eofd-dev8.fa.us6.oraclecloud.com/",
                "error_results": {
                        "error_code": "CSG-001",
                        "error_message": "Unable load url."
                        }
                }
        },
        "csg_with_atb_url": {
            "PROD":{
                "value": "https://eofd.fa.us6.oraclecloud.com/analytics/saw.dll?bipublisherEntry&Action=open&itemType=.xdo&bipPath=%2FCustom%2FHuman%20Capital%20Management%2FWorkforce%20Management%2FPublisher%20Objects%2FReport%2FCSG_Manage%20Grades%20Report.xdo&path=%2Fshared%2FCustom%2FHuman%20Capital%20Management%2FWorkforce%20Management%2FPublisher%20Objects%2FReport%2FCSG_Manage%20Grades%20Report.xdo",
                "error_results": {
                        "error_code": "CSG-002",
                        "error_message": "Unable load url."
                            }
                    },
            "DEV": {
                "value": "https://eofd-dev8.fa.us6.oraclecloud.com/analytics/saw.dll?bipublisherEntry&Action=open&itemType=.xdo&bipPath=%2FCustom%2FHuman%20Capital%20Management%2FWorkforce%20Management%2FPublisher%20Objects%2FReport%2FCSG_Manage%20Grades%20Report.xdo&path=%2Fshared%2FCustom%2FHuman%20Capital%20Management%2FWorkforce%20Management%2FPublisher%20Objects%2FReport%2FCSG_Manage%20Grades%20Report.xdo",
                "error_results": {
                        "error_code": "CSG-002",
                        "error_message": "Unable load url."
                }
            }
        },
        "csg_existing_url": {
            "PROD": {
                "value": "https://eofd.fa.us6.oraclecloud.com/analytics/saw.dll?bipublisherEntry&Action=open&itemType=.xdo&bipPath=%2FCustom%2FHuman%20Capital%20Management%2FWorkforce%20Management%2FPublisher%20Objects%2FReport%2FCSG_Manage%20Existing%20ATB%20Report.xdo&path=%2Fshared%2FCustom%2FHuman%20Capital%20Management%2FWorkforce%20Management%2FPublisher%20Objects%2FReport%2FCSG_Manage%20Existing%20ATB%20Report.xdo",
                "error_results": {
                    "error_code": "CSG-003",
                    "error_message": "Unable load url."
                        }
                    },
            "DEV": {
                "value": "https://eofd-dev8.fa.us6.oraclecloud.com/analytics/saw.dll?bipublisherEntry&Action=open&itemType=.xdo&bipPath=%2FCustom%2FHuman%20Capital%20Management%2FWorkforce%20Management%2FPublisher%20Objects%2FReport%2FCSG_Manage%20Existing%20ATB%20Report.xdo&path=%2Fshared%2FCustom%2FHuman%20Capital%20Management%2FWorkforce%20Management%2FPublisher%20Objects%2FReport%2FCSG_Manage%20Existing%20ATB%20Report.xdo",
                "error_results": {
                        "error_code": "CSG-002",
                        "error_message": "Unable load url."
                }
            }
        },
        "iframe": {
                "selector": "iframe.ContentIFrame",
                "error_results": {
                    "error_code": "CSG-004",
                    "error_message": "iFrame Object not found"
                },
        },
        "group_code_dropdown": {
                "selector": "#xdo\:xdo\:_paramspgrdCode_div_input",
                "error_results": {
                    "error_code": "CSG-005",
                    "error_message": "Group Code Dropdown not found"
                },
        },
        "search_option": {
                "selector": "#xdo\:xdo\:_paramspgrdCode_div_s > span > span.dialogFloatLeft",
                "error_results": {
                    "error_code": "CSG-006",
                    "error_message": "Search Dialog text not found"
                },
        },
        'search_item_box': {
                "value": "AC0",
                "selector": "#xdo\:xdo\:_paramspgrdCode_div_input_searchDialog_input",
                "error_results": {
                    "error_code": "CSG-007",
                    "error_message": "Search Dialog input box not found"
                },
            },
        'search_button': {
                "value": "AC0",
                "selector": "#xdo\:xdo\:_paramspgrdCode_div_input_searchDialog_button",
                "error_results": {
                    "error_code": "CSG-008",
                    "error_message": "Search Dialog Button not found"
                },
            },
        'search_result_item': {
                "value": "AC0",
                "selector": "#xdo\:xdo\:_paramspgrdCode_div_input_searchDialog_searchResults > div",
                "error_results": {
                    "error_code": "CSG-009",
                    "error_message": "Result Item not found"
                },
            },
        'search_result_item_020': {
                "value": "AC0",
                "selector": "#xdo\:xdo\:_paramspgrdCode_div_input_searchDialog_searchResults > div:has-text('AC01-020')",
                "error_results": {
                    "error_code": "CSG-010",
                    "error_message": "Result Item not found"
                },
            },
        'move_item_button': {
                "value": "AC0",
                "selector": "#xdo\:xdo\:_paramspgrdCode_div_input_searchDialog_moveImg",
                "error_results": {
                    "error_code": "CSG-011",
                    "error_message": "Move Item Buttom not found"
                },
            },
        'okay_button': {
                "value": "AC0",
                "selector": "#xdo\:xdo\:_paramspgrdCode_div_input_searchDialog_dialogTable > tbody > tr:nth-child(3) > td.dialog_footer > table > tbody > tr > td.dialog_footer-content > button:nth-child(1)",
                "error_results": {
                    "error_code": "CSG-012",
                    "error_message": "Okay Button not found"
                },
            },
        'review_and_apply_button': {
                "value": "AC0",
                "selector": "button#reportViewApply",
                "error_results": {
                    "error_code": "CSG-013",
                    "error_message": "Review and Apply Button not found"
                },
            },
        'export_icon_button': {
                "value": "AC0",
                "selector": "#xdo\:viewFormatLink",
                "error_results": {
                    "error_code": "CSG-014",
                    "error_message": "iFrame Object not found"
                },
            },
        'excel_export_button': {
                "value": "AC0",
                "selector": "#_xdoFMenu1 > div > div > ul > li:nth-child(4) > div > a",
                "error_results": {
                    "error_code": "CSG-015",
                    "error_message": "Excel Export button not found"
                },
        },
        "home_button": {
                "selector": "#pt1\:_UIShome\:\:icon > g:nth-child(5) > path",
                "error_results": {
                    "error_code": "CSG-016",
                    "error_message": "Home Button not found"
                        }
        }, #xdo\:xdo\:_paramspgrdCode_div_input_searchDialog_idNoDataFound
        "data_not_found": {
                "selector": "#xdo\:xdo\:_paramspgrdCode_div_input_searchDialog_idNoDataFound",
                "error_results": {
                    "error_code": "CSG-017",
                    "error_message": "Home Button not found"
                        }
        },
}

sal_plan_config = {
        "url": {
            "value": "https://albehcm-dev.opc.albertsons.com/psp/ALBEHDEV/?cmd=login&languageCd=ENG&",
            "error_results": {
                    "error_code": "SPA-001",
                    "error_message": "Unable load url."
                        }
        },
        "username": {
            "selector": "#userid",
            "error_results": {
                    "error_code": "SPA-002",
                    "error_message": "Username input box not found."
                        }
                },
        "password": {
            "selector": "#pwd",
            "error_results": {
                    "error_code": "SPA-003",
                    "error_message": "Password input box not found"
                        }
                },
        "submit_button": {
            "selector": "input[type=submit]",
            "error_results": {
                    "error_code": "SPA-004",
                    "error_message": "Submit button not found"
                        }
                },
        "navbar_button": {
            "selector": "#PT_NAVBAR",
            "error_results": {
                    "error_code": "SPA-005",
                    "error_message": "Navbar Button not found"
                        }
                },
        "navbar_iframe": {
            "selector": "#psNavBarIFrame",
            "error_results": {
                    "error_code": "SPA-006",
                    "error_message": "Navbar iFrame Object not found"
                        }
        },
        "classic_home_tab": {
            "selector": "#PTNUI_DOCK_REC_GROUPLET_LBL\$4",
            "error_results": {
                    "error_code": "SPA-007",
                    "error_message": "Classic Home Tab button not found"
                        }
        },
        "main_menu_dropdown": {
            "selector": "#pthnavbca_PORTAL_ROOT_OBJECT",
            "error_results": {
                    "error_code": "SPA-008",
                    "error_message": "Main Menu drowpdown not found"
                        }
                },
        "reporting_tools": {
            "selector": "#fldra_PT_REPORTING_TOOLS",
            "error_results": {
                    "error_code": "SPA-009",
                    "error_message": "Reporting Tools Dropwdown not found"
                        }
                },
        "query": {
            "value": "CSG_Manage Grades without ATB",
            "selector": "#fldra_PT_QUERY",
            "error_results": {
                    "error_code": "SPA-010",
                    "error_message": "Query dropdown not found"
                        }
                },
        "query_manager": {
            "selector": "#crefli_PT_QUERY_MANAGER_GBL > a",
            "error_results": {
                    "error_code": "SPA-011",
                    "error_message": "Query Manage item not found"
                        }
                },
        "iframe_query": {
            "selector": "#ptifrmtgtframe",
            "error_results": {
                    "error_code": "SPA-012",
                    "error_message": "iFrame result Object not found"
                        }
                },
        "toolbar_div": {
            "selector": "#win0divPSTOOLBAR",
            "error_results": {
                    "error_code": "SPA-013",
                    "error_message": "Toolbar div not found"
                        }
                },
        "advance_search_btn": {
                "selector": "#QRYSELECT_WRK_QRYADVSEARCH",
                "error_results": {
                    "error_code": "SPA-014",
                    "error_message": "Advanced Search button not found"
                        }
                },
        "basic_search_btn": {
                "selector": "#QRYADVSRCH_WRK_QRYSRCHBASIC",
                "error_results": {
                    "error_code": "SPA-015",
                    "error_message": "Basic Search Button not found"
                        }
        },
        "condition_option": {
                "selector": "#QRYADVSRCH_WRK_QRYADVSRCHTYPE1",
                "error_results": {
                    "error_code": "SPA-015",
                    "error_message": "Condition Option not found"
                        }
        },
        "condition_value": {
                "selector": "#QRYADVSRCH_WRK_QRYADVSRCHTYPE1 > option:nth-child(8)",
                "error_results": {
                    "error_code": "SPA-016",
                    "error_message": "Condition Item 'contains' not found"
                        }
        },
        "name_value_box": {
                "selector": "#QRYADVSRCH_WRK_QRYSEARCHTEXT1",
                "value": "CONTRACTS_LA_SAL_PLAN_ACCUM",
                "error_results": {
                    "error_code": "SPA-017",
                    "error_message": "Name Value input box not found"
                        }
        },
        "search_query": {
                "selector": "#QRYSELECT_WRK_QRYSEARCHBTN",
                "error_results": {
                    "error_code": "SPA-018",
                    "error_message": "Search Query Button not found"
                        }
        },
        "result_table": {
                "selector": "#win0divQRYSELECT_WRK\$0",
                "error_results": {
                    "error_code": "SPA-019",
                    "error_message": "Result table not found"
                        }
        },
        "edit_button": {
                "selector": "#QRYSELECT_WRK_QRYEDITFIELD\$0",
                "error_results": {
                    "error_code": "SPA-020",
                    "error_message": "Edit Button not found"
                        }
        },
        "criteria_tab": {
                "selector": "#ICTAB_5",
                "error_results": {
                    "error_code": "SPA-021",
                    "error_message": "Criteria Tab not found"
                        }
        },
        "expression_columns": {
                "selector": "#QRYCRIT1_WRK_QRYDELETECRITERIA\$1",
                "sub_selector": "",
                "error_results": {
                    "error_code": "SPA-022",
                    "error_message": "Expression Columns not found"
                        }
        },
        "run_button": {
                "selector": "#ICTAB_10",
                "error_results": {
                    "error_code": "SPA-023",
                    "error_message": "Run Button not found"
                        }
        },
        "loader": {
                "selector": "#WAIT_win0",
                "error_results": {
                    "error_code": "SPA-024",
                    "error_message": "Loader not found"
                        }
        },
        "download_button": {
                "selector": "#win0divQRY_VIEWER_WRK_HTMLAREA > div > table:nth-child(4) > tbody > tr > td:nth-child(1) > span:nth-child(5) > a",
                "error_results": {
                    "error_code": "SPA-025",
                    "error_message": "Download Button not found"
                        }
        },
}