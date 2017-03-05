latlondata = LOAD '/project/dataset/PickupCombined' USING PigStorage(',') as (latitude:float,longitude:float,total:int);

latlondata = ORDER latlondata BY total DESC;
Top1000Pickup = LIMIT latlondata 1000;
store Top1000Pickup into '/project/dataset/Top1000PickupCombined' using PigStorage(',','-schema');
