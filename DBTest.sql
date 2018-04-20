-- step=Connect
-- username=utp
-- password=utp
-- url=jdbc:mysql://localhost:3306/utp
-- driver_class=com.mysql.jdbc.Driver
-- database=utp
-- dialect=org.appvance.hibernate.dialect.MySQLDialect
-- connect
-- step=Step1
SELECT * FROM `utp`.`SummaryElement` LIMIT 1000;
SELECT * FROM `utp`.`Definition` LIMIT 1000;
-- step=Step2
SELECT * FROM `utp`.`summarystatsontime` LIMIT 1000;
-- step=Step3
SELECT * FROM `utp`.`nodedefinition` LIMIT 1000;
-- step=Step4
SELECT * FROM `utp`.`nodeperscenario` LIMIT 1000;
-- step=Step5
SELECT * FROM `utp`.`summaryelement` LIMIT 1000;
-- step=Step6
SELECT * FROM `utp`.`vduration` LIMIT 1000;
-- step=Step7
SELECT * FROM `utp`.`utpiterationperexecution` LIMIT 1000;
-- step=Step8
SELECT * FROM `utp`.`vactiveusers` LIMIT 1000;
-- step=Step9
SELECT * FROM `utp`.`vactiveusers` LIMIT 1000;
-- step=Step10
SELECT * FROM `utp`.`vdurationpercentil` LIMIT 1000;
-- step=Step11
SELECT * FROM `utp`.`vsuccessfailure` LIMIT 1000;
-- step=Step12
SELECT * FROM `utp`.`vsizepercentil` LIMIT 1000;
-- step=Step13
SELECT * FROM `utp`.`vactiveusers` LIMIT 1000;
-- step=Step14
SELECT * FROM `utp`.`typelong` LIMIT 1000;
-- step=Step15
SELECT * FROM `utp`.`utpscenarioexecution` LIMIT 1000;
-- step=Step16
SELECT * FROM `utp`.`utpiterationperexecution` LIMIT 1000;
-- step=Step17
SELECT * FROM `utp`.`defaultpercentil` LIMIT 1000;
-- step=Step18
SELECT * FROM `utp`.`detailedduration` LIMIT 1000;
-- step=Step19
SELECT * FROM `utp`.`detailedelementattribute` LIMIT 1000;
-- step=Step20
SELECT * FROM `utp`.`summarychildelement` LIMIT 1000;
-- step=Step21
SELECT * FROM `utp`.`summarychildelement` LIMIT 1000;
-- step=BeforeDisc
-- step=Disconnect
-- disconnect
                