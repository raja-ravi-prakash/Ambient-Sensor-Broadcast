import socket
import screen_brightness_control as b
import time
ip=''
port=1201
s=socket.socket(socket.AF_INET,socket.SOCK_DGRAM)
s.bind((ip,port))

while(True):
    data,addr=s.recvfrom(1024)
    data=data.decode('utf-8').split(':')
    data[2]=data[2].replace(',','')
    data[2]=int(data[2])*100/2047
    print(data[2])
    if(data[2]<15):
        b.set_brightness(data[2])
        continue
    b.fade_brightness(data[2],blocking=True)

'''
while(True):
    data,addr=s.recvfrom(1024)
    if(data.decode('utf-8')[8:]=='y:MinValue'):
        b.fade_brightness(100,blocking=True)
    else:      
        data=float(data.decode('utf-8')[8:])
        print(data)
        b.fade_brightness(data*5,blocking=True)
        time.sleep(2)
'''
