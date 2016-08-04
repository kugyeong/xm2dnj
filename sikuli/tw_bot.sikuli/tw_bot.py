from time import gmtime, strftime
import os
# 멀티 계정 관리
login_list = [
        ("","")
        ]

village_id_list = []
download_path = "C:\\Users\\Kugyeong\\Downloads"
workspace_path = "C:\\python-workspace\\xm2dnj-master"
tw_workspace_path = workspace_path + "\\tw-workspace"
script_path = workspace_path + "\\scripts"

def open_browser():
    sleep(3)
    #openApp("C:\\Users\\Kugyeong\\AppData\\Local\\Chromium\\Application\\chrome.exe")
    click("chrome-tool-bar.PNG")
    wait("1463996022613.png", 100)
    click("1463996022613.png")
    type("www.tribalwars2.com"+Key.ENTER)
    wait("1463997177505.png", 30)

def close_browser():
    type(Key.F4, KeyModifier.ALT)
    sleep(5)
    
def pop_up_handling():
    if exists("1464578586745.png"):
        click("1464578586745.png")
        sleep(5)
    
    if exists("1464578628552.png"):
        click("1464578720862.png")
        type("0000"+Key.ENTER)
        sleep(5)

def pop_up_handling2():
    # Claim pop up    
    if exists("1464079346152.png", 5): 
        click("1464079346152.png")
        sleep(5)
            
def login(account):
    click("1463996706288.png")
    type(account[0]+Key.TAB+account[1]+Key.ENTER)
    
    pop_up_handling()
    sleep(5)
    if exists("1464839920474.png", 120):
        click("1464839920474.png")

def wait_for_loading():
    sleep(5)
    waitVanish("1464084146830.png", 300)

def open_console():
    type(Key.F12)
    sleep(2)
    click("element.PNG")
    sleep(2)
    click("console.PNG")

def load_library():
    click("cmd_prompt.PNG")
    type("type "+script_path+"\\library.js | clip"+Key.ENTER)
    type("del "+download_path+"\\village_list.txt"+Key.ENTER)
    type("del "+download_path+"\\global.txt"+Key.ENTER)
    sleep(2)
    click("chrome-icon.png")
    sleep(2)
    cb = Env.getClipboard()
    paste(cb)
    type(Key.ENTER)
    sleep(30)
    
def make_vil_list():
    vil_list_file = download_path+"\\village_list.txt"
    while True:
        if os.path.exists(vil_list_file):
            with open(vil_list_file, "r") as f:
                for line in f:
                    list = line.split(",")
                    for id in list:
                        village_id_list.append(id)
            break;
        else:
            type(Key.F5)
            sleep(20)
            load_library()
            type(Key.ENTER)
    click("cmd_prompt.PNG")
    type("move /Y"+download_path+"\\global.txt "+tw_workspace_path+Key.ENTER)
    sleep(2)
    click("chrome-icon.png")
    sleep(2)
def make_vil_info(vil_id):
    while True:
        type("write_vil_info_file("+vil_id+")"+Key.ENTER)
        sleep(10)
        if os.path.exists(download_path+"\\"+vil_id+".txt"):
            break;
        else:
            type(Key.F5)
            sleep(20)
            load_library()
            type(Key.ENTER)
            
    click("cmd_prompt.PNG")
    type("move /Y"+download_path+"\\"+vil_id+".txt "+tw_workspace_path+Key.ENTER)

def make_command(vil_id):
    tag = strftime("%Y%m%d%H%M") + "-" + vil_id;
    #type("mkdir "+tw_workspace_path+"\\bak-"+tag+Key.ENTER);
    #type("copy "+tw_workspace_path+"\\global.txt "+tw_workspace_path+"\\bak-"+tag+"\\global-pre.txt"+Key.ENTER);
    type("java -jar "+tw_workspace_path+"\\CommandMaker.jar "+tw_workspace_path+"\\"+vil_id+".txt "+tw_workspace_path+"\\global.txt "+tw_workspace_path+"\\recruit_num.txt"+Key.ENTER); 
    sleep(2)
    #type("copy "+tw_workspace_path+"\\"+vil_id+".txt "+tw_workspace_path+"\\bak-"+tag+"\\"+vil_id+".txt"+Key.ENTER);                 
    #type("copy "+tw_workspace_path+"\\"+vil_id+"-command.txt "+tw_workspace_path+"\\bak-"+tag+"\\"+vil_id+"-command.txt"+Key.ENTER);                 
    #type("copy "+tw_workspace_path+"\\global.txt "+tw_workspace_path+"\\bak-"+tag+"\\global-post.txt"+Key.ENTER);
 
def execute_command(vil_id):
    click("chrome-icon.png")
    sleep(2)
    my_file = tw_workspace_path+"\\"+vil_id
    my_file += "-command.txt"
    if os.path.exists(my_file):
        with open(my_file, "r") as f:
            for line in f:
                sleep(2)
                type(line);
                type(Key.ENTER);
    else:
        type(Key.F5)
        sleep(20)
        load_library()   
        type(Key.ENTER)
        
loop=1
load_library()
make_vil_list()
while loop:
    for vil_id in village_id_list:
        
        make_vil_info(vil_id)
        make_command(vil_id)
        execute_command(vil_id)
        sleep(10)
    #################################################################
    # wait for next iteration...
    loop += 1
    # end of "while True:"