# We assume that
# 1. We use Firefox.
# 2. The password has already stored in Firefox.

# 멀티 계정 관리
login_list = [
        "아이디",
        "아이디",
        "아이디",
        ]

# ex. Total villages = 4 * 10 = 40
vill_iter = 4 # You should edit this


def open_browser():
    sleep(5)
    openApp("firefox") # We use Firefox, because the script can work stably. More stable than Chrome.
    sleep(10)
    sleep(5)
    wait("1469194780023.png",30)
    click("1469194780023.png")
    sleep(5)
    type("www.")
    sleep(1)
    type("tribal")
    sleep(1)
    type("wars2.")
    sleep(1)
    type("com")
    sleep(1)
    type(Key.ENTER)
    wait("1469195055526.png",30)

def close_browser():
    type(Key.F4, KeyModifier.ALT)
    sleep(5)

def pop_up_handling():
    # Claim pop up
    if exists("1469199662214.png",10):
        click("1469199662214.png")
        sleep(5)

def login(account):
    click("1469195035292.png")
    type(account) # id
    sleep(1)
    type(Key.TAB) # We assume that the password has already stored in Firefox.
    sleep(1)
    type(Key.ENTER)
    sleep(5)
    wait("1469196068206.png",30)
    click("1469196068206.png")

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
    if account == "memory.killer":
        if loop % vill_iter == 1:
            type("cat ~/etc/tw/scripts/build1.js | xclip -i -selection clipboard"+Key.ENTER)
        elif loop % vill_iter == 2:
            type("cat ~/etc/tw/scripts/build2.js | xclip -i -selection clipboard"+Key.ENTER)
        elif loop % vill_iter == 3:
            type("cat ~/etc/tw/scripts/build3.js | xclip -i -selection clipboard"+Key.ENTER)            
        else:
            type("cat ~/etc/tw/scripts/build4.js | xclip -i -selection clipboard"+Key.ENTER)
    else:
        type("cat ~/etc/tw/scripts/build.js | xclip -i -selection clipboard"+Key.ENTER)

    sleep(5)
    click("1469194883617.png")
    sleep(5)
    cb = Env.getClipboard()
    paste(cb)
    type(Key.ENTER)
    sleep(90)

def farming(account, loop):
    click("1463996398215.png")
    sleep(5)
    
    # 마을이 너무 많은 계정일 경우 마을 10개 단위로 컨트롤
    if account == "memory.killer":
        if loop % vill_iter == 1:
            type("cat ~/etc/tw/scripts/farm1.js | xclip -i -selection clipboard"+Key.ENTER)
        elif loop % vill_iter == 2:
            type("cat ~/etc/tw/scripts/farm2.js | xclip -i -selection clipboard"+Key.ENTER)
        elif loop % vill_iter == 3:
            type("cat ~/etc/tw/scripts/farm3.js | xclip -i -selection clipboard"+Key.ENTER)            
        else:
            type("cat ~/etc/tw/scripts/farm4.js | xclip -i -selection clipboard"+Key.ENTER)
    else:
        type("cat ~/etc/tw/scripts/farm.js | xclip -i -selection clipboard"+Key.ENTER)

    sleep(5)
    click("1469194883617.png")
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
        pop_up_handling()    
        open_console()
        building(account, loop)
        close_browser()

        # Farming Turn
        open_browser()
        login(account)
        wait_for_loading()
        pop_up_handling()
        open_console()
        farming(account, loop)
        close_browser()

        # end of "for account in login_list:"
    #################################################################
    # wait for next iteration...
    loop += 1
    
    sleep(90*60) # 90분 마다 스크립트 실행
    # end of "while True:"
