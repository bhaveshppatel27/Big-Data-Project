data = LOAD '/project/dataset/faretripjoincombined' USING PigStorage(',') as (id:chararray,pickupdate:datetime,surcharge:float,tip:float,toll:float,totalamount:float,dropoffdate:datetime,
passenger:int,distance:float,pickuplon:float,pickuplat:float,dropofflon:float,dropofflat:float);

newdata = foreach data Generate pickuplon, pickuplat, dropofflon,dropofflat,distance,passenger,totalamount,surcharge,tip,toll;

customgrouped = GROUP newdata BY (pickuplon, pickuplat, dropofflon,dropofflat) PARALLEL 1;

customaggregate = FOREACH customgrouped 
{
  noofpassengerspayingtip = FILTER newdata BY (tip > 0);

GENERATE FLATTEN(group) AS (pickuplon, pickuplat, dropofflon,dropofflat),
COUNT(newdata) AS totalrides,
org.apache.pig.builtin.ROUND_TO((AVG(newdata.distance)),2) AS averagedistance,
SUM(newdata.passenger) AS totalpassenger,
org.apache.pig.builtin.ROUND_TO((AVG(newdata.totalamount)),2) AS averageamount,
org.apache.pig.builtin.ROUND_TO((AVG(newdata.surcharge)),2) AS averagesurcharge,
COUNT(noofpassengerspayingtip) AS noofpassengerspayingtip,
org.apache.pig.builtin.ROUND_TO((AVG(newdata.toll)),2) AS averagetoll;
};

top20basedontotalrides = ORDER customaggregate BY totalrides DESC;
top20basedontotalrides = LIMIT top20basedontotalrides 20;

top20longestdistance = ORDER customaggregate BY averagedistance DESC;
top20longestdistance = LIMIT top20longestdistance 20;

top20basedontotalpassengers = ORDER customaggregate BY totalpassenger DESC;
top20basedontotalpassengers = LIMIT top20basedontotalpassengers 20;

top20basedonamount = ORDER customaggregate BY averageamount DESC;
top20basedonamount = LIMIT top20basedonamount 20;

top20highestsurcharge = ORDER customaggregate BY averagesurcharge DESC;
top20highestsurcharge = LIMIT top20highestsurcharge 20;

top20tiparea = ORDER customaggregate BY noofpassengerspayingtip DESC;
top20tiparea = LIMIT top20tiparea 20;

top20highesttollroute = ORDER customaggregate BY averagetoll DESC;
top20highesttollroute = LIMIT top20highesttollroute 20;

store top20basedontotalrides into '/project/dataset/RegionAnalysis/top20basedontotalrides' using PigStorage(',','-schema');
store top20longestdistance into '/project/dataset/RegionAnalysis/top20longestdistance' using PigStorage(',','-schema');
store top20basedontotalpassengers into '/project/dataset/RegionAnalysis/top20basedontotalpassengers' using PigStorage(',','-schema');
store top20basedonamount into '/project/dataset/RegionAnalysis/top20basedonamount' using PigStorage(',','-schema');
store top20highestsurcharge into '/project/dataset/RegionAnalysis/top20highestsurcharge' using PigStorage(',','-schema');
store top20tiparea into '/project/dataset/RegionAnalysis/top20tiparea' using PigStorage(',','-schema');
store top20highesttollroute into '/project/dataset/RegionAnalysis/top20highesttollroute' using PigStorage(',','-schema');

