# 멀티 계정 관리
login_list = [
        (아이디,비밀번호),
        (아이디,비밀번호),
        (아이디,비밀번호),
        ]


def open_browser():
    sleep(5)
    openApp("chromium-browser")
    wait("1463996150597.png", 30)
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
    waitVanish("1464084146830.png", 120)

def open_console():
    type(Key.F12)
    sleep(5)

def building(account, loop):
    click("1463996398215.png")
    sleep(5)
    
    # 마을이 너무 많은 계정일 경우 마을 10개 단위로 컨트롤
    if account[0] == "memory.killer":
        if loop % 2 == 1:
            type("cat ~/etc/tw/scripts/build1.js | xclip -i -selection clipboard"+Key.ENTER)
        elif loop % 2 == 0:
            type("cat ~/etc/tw/scripts/build2.js | xclip -i -selection clipboard"+Key.ENTER)
    else:
        type("cat ~/etc/tw/scripts/build.js | xclip -i -selection clipboard"+Key.ENTER)

    sleep(5)
    click("1463996268415.png")
    sleep(5)
    cb = Env.getClipboard()
    paste(cb)
    type(Key.ENTER)
    sleep(90)

def farming(account, loop):
    click("1463996398215.png")
    sleep(5)
    
    # 마을이 너무 많은 계정일 경우 마을 10개 단위로 컨트롤
    if account[0] == "memory.killer":
        if loop % 2 == 1:
            type("cat ~/etc/tw/scripts/farm1.js | xclip -i -selection clipboard"+Key.ENTER)
        elif loop % 2 == 0:
            type("cat ~/etc/tw/scripts/farm2.js | xclip -i -selection clipboard"+Key.ENTER)
    else:
        type("cat ~/etc/tw/scripts/farm.js | xclip -i -selection clipboard"+Key.ENTER)

    sleep(5)
    click("1463996268415.png")
    sleep(5)
    cb = Env.getClipboard()
    paste(cb)
    type(Key.ENTER)
    sleep(90)


loop=1
while loop:
    for account in login_list:
        
        # Building Turn
        open_browser()
        login(account)
        wait_for_loading()
        pop_up_handling2()    
        open_console()
        building(account, loop)
        close_browser()
    
        # Farming Turn
        open_browser()
        login(account)
        wait_for_loading()
        pop_up_handling2()    
        open_console()
        farming(account, loop)
        close_browser()

        # end of "for account in login_list:"
    #################################################################
    # wait for next iteration...
    loop += 1
    sleep(90*60) # 90분 마다 스크립트 실행
    # end of "while True:"
