timedata = LOAD '/project/dataset/TimeDataCombined' USING PigStorage(',') as (id:chararray,month:int,date:int,weekday:int,weekinyear:int,hour:int,surcharge:float,totalamount:float,passenger:int);

userdata = FILTER timedata BY (id=='00B7691D86D96AEBD21DD9E138F90840');
customgrouped = GROUP userdata BY (id,month,date) PARALLEL 1;

customaggregate = FOREACH customgrouped GENERATE FLATTEN(group) AS (id,month,date),
COUNT(userdata.id) AS totalrides,
org.apache.pig.builtin.ROUND_TO((SUM(userdata.totalamount)),2) AS totalamount,
SUM(userdata.passenger) AS totalpassenger;


store customaggregate into '/project/dataset/IndividualAnalysisCombinedPig' using PigStorage(',','-schema');
