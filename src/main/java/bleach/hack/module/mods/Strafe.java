# /py src/module/movement/gocrazy

from socket import SocketIO
import input as input
from mine import *
from mcpi.minecraft import *
import random
from src.module.functions import loadconfig
import keyboard

def Main():
    mc = Minecraft()
    modulenm = "GoCrazy"
    configs = loadconfig()['modules'][modulenm]
    player = mc.getPlayerId()
    crazyamount = configs['crazyamount']
    if crazyamount < 1:
        crazyamount = int(crazyamount*10)
        under1 = True
    else:
        crazyamount = int(crazyamount)
        under1 = False

    def gostupid():
        if under1:
            return ((random.randint(1, crazyamount))/10)
        else:
            return random.randint(1, crazyamount)

    while True:
        if keyboard.is_pressed('r'):

            try:
                entitys = mc.getPlayerEntityIds()
                player = mc.getPlayerId()
            except Exception as e:
                print(e)
                print('fail')
                continue

            posplayer = mc.entity.getPos(player)
            distances = {}
            for entit in entitys:
                try:
                    posentity = mc.entity.getPos(entit)
                except:
                    continue
                diffx = posentity.x - posplayer.x
                diffz = posentity.z - posplayer.z
                diffy = posentity.y - posplayer.y
                distance = math.sqrt((diffx ** 2) + (diffy ** 2) + (diffz ** 2))
                if distance == 0.0:
                    continue
                distances[entit] = distance

            try:
                lowestdistuple = min(distances.items(), key=lambda x: x[1])
                lowestdistanceguy, dista = lowestdistuple
            except:
                continue
            
            if player == lowestdistanceguy:
                continue
            

            try:
                posentity = mc.entity.getPos(lowestdistanceguy)
            except:
                continue

            
            diffx = posentity.x - posplayer.x
            diffz = posentity.z - posplayer.z
            diffy = posentity.y - posplayer.y
            newpitch = 0
            newyaw = 0
            distance = math.sqrt((diffx ** 2) + (diffy ** 2) + (diffz ** 2))

            try:
                newpitch = math.degrees(-math.tan(diffy/distance))
                newyaw = math.degrees(math.atan2(diffz, diffx))
            except:
                mc.postToChat("epic fail")

            distance = math.sqrt((diffx ** 2) + (diffy ** 2) + (diffz ** 2))

            try:
                pos = mc.entity.getPos(player)
                yaw = mc.entity.getRotation(player)
            except:
                continue
            move = False

            if input.wasPressedSinceLast(input.KEY_W):
                pos.z -= gostupid() * sin(radians(yaw))
                pos.x -= gostupid() * cos(radians(yaw))
                pos.z += gostupid() * sin(radians(yaw))
                pos.x += gostupid() * cos(radians(yaw))
                move = True
            if input.wasPressedSinceLast(input.KEY_S):
                pos.z += gostupid() * sin(radians(yaw))
                pos.x += gostupid() * cos(radians(yaw))
                pos.z -= gostupid() * sin(radians(yaw))
                pos.x -= gostupid() * cos(radians(yaw))
                move = True
            if input.wasPressedSinceLast(input.KEY_D):
                pos.x += gostupid() * -sin(radians(yaw))
                pos.z += gostupid() * cos(radians(yaw))
                pos.x -= gostupid() * -sin(radians(yaw))
                pos.z -= gostupid() * cos(radians(yaw))
                move = True
            if input.wasPressedSinceLast(input.KEY_A):
                pos.x -= gostupid() * -sin(radians(yaw))
                pos.z -= gostupid() * cos(radians(yaw))
                pos.x += gostupid() * -sin(radians(yaw))
                pos.z += gostupid() * cos(radians(yaw))
                move = True
            if input.wasPressedSinceLast(input.SPACE):
                posorneg = random.randint(1,2)
                if posorneg == 1:
                    pos.y += gostupid()
                else:
                    pos.y -= gostupid()
                move = True
            if move:
                try:
                    mc.entity.setPos(player,pos)
                    mc.entity.setRotation(player, newyaw-90)
                    mc.entity.setPitch(player, newpitch)
                except:
                    mc.postToChat("Error with "+modulenm)


if __name__ == '__main__':
    Main()



# /py src/module/movement/elytrafollower

from mine import *
import math
from src.module.functions import loadconfig
import keyboard

def Main():
    mc = Minecraft()
    modulenm = "ElytraFollower"
    #configs = loadconfig()['modules'][modulenm]
    #TrackerFreq = configs['playertrackerfreq']
    whichEntity = 0
    while True:
        if keyboard.is_pressed('r'):
            try:
                entitys = mc.getPlayerEntityIds()
                player = mc.getPlayerId()
            except Exception as e:
                print(e)
                continue

            posplayer = mc.entity.getPos(player)
            distances = {}
            for entit in entitys:
                try:
                    posentity = mc.entity.getPos(entit)
                except:
                    continue
                diffx = posentity.x - posplayer.x
                diffz = posentity.z - posplayer.z
                diffy = posentity.y - posplayer.y
                distance = math.sqrt((diffx ** 2) + (diffy ** 2) + (diffz ** 2))
                if distance == 0.0:
                    continue
                distances[entit] = distance

            try:
                lowestdistuple = min(distances.items(), key=lambda x: x[1])
                lowestdistanceguy, dista = lowestdistuple
            except:
                continue
            
            if player == lowestdistanceguy:
                continue
            

            try:
                posentity = mc.entity.getPos(lowestdistanceguy)
            except:
                continue

            
            diffx = posentity.x - posplayer.x
            diffz = posentity.z - posplayer.z
            diffy = posentity.y - posplayer.y
            newpitch = 0
            newyaw = 0
            distance = math.sqrt((diffx ** 2) + (diffy ** 2) + (diffz ** 2))

            try:
                newpitch = math.degrees(-math.tan(diffy/distance))
                newyaw = math.degrees(math.atan2(diffz, diffx))
            except:
                mc.postToChat("epic fail")

            distance = math.sqrt((diffx ** 2) + (diffy ** 2) + (diffz ** 2))

            if distance == 0.0:
                whichEntity += 1
            elif distance > 0.2:
                mc.entity.setRotation(player, newyaw-90)
                mc.entity.setPitch(player, newpitch)


if __name__ == '__main__':
    Main()