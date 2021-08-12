# /py src/module/movement/playerfollower

from mine import *
import math
from src.module.functions import loadconfig
import keyboard

def Main():
    mc = Minecraft()
    modulenm = "PlayerFollower"
    #configs = loadconfig()['modules'][modulenm]
    #TrackerFreq = configs['playertrackerfreq']
    whichEntity = 0
    while True:
        if keyboard.is_pressed('g'):
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
                newyaw = math.degrees(math.atan2(diffz, diffx))-90
            except:
                mc.postToChat("epic fail")

            distance = math.sqrt((diffx ** 2) + (diffy ** 2) + (diffz ** 2))

            if distance == 0.0:
                whichEntity += 1
            elif distance > 2:
                mc.entity.setRotation(player, newyaw)
                mc.entity.setPitch(player, newpitch)


if __name__ == '__main__':
    Main()