import socket
import screen_brightness_control as b
import time
ip=''
port=1201
s=socket.socket(socket.AF_INET,socket.SOCK_DGRAM)
s.bind((ip,port))

while(True):
    data,addr=s.recvfrom(1024)
    if(data.decode('utf-8')[8:]=='y:MinValue'):
        b.fade_brightness(100,blocking=True)
    else:      
        data=float(data.decode('utf-8')[8:])
        print(data)
        b.fade_brightness(data*5,blocking=True)
        time.sleep(2)

