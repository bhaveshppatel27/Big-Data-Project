latlondata = LOAD '/project/dataset/DropoffCombined' USING PigStorage(',') as (latitude:float,longitude:float,total:int);

latlondata = ORDER latlondata BY total DESC;
Top1000Dropoff = LIMIT latlondata 1000;
store Top1000Dropoff into '/project/dataset/Top1000DropoffCombined' using PigStorage(',','-schema');
