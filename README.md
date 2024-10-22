# TimestreamWriteHelloWorld

for story [PGDFHIR-1893](https://issues.mobilehealth.va.gov/browse/PGDFHIR-1893)

## What does this project do ?

It writes 2 records in database `App.DATABASE_NAME`, in table `App.TABLE_NAME` using region `App.REGION`.

One for cpu, and another for memory utilization.

## How to compile

`mvn clean package`

## How to run

Look at the possible problems first, then run.

## Possible problems

* `SdkClientException`
  - `Unable to load AWS credentials ...`
  - You need to set up  `AWS_ACCESS_KEY` and `AWS_SECRET_KEY` with access values. 
  - check *Access Management* in your AWS console to find them.
  - If you don't find them, then you need to create a IAM user, and give him access.
* `ResourceNotFoundException`
  - `The table host_metrics does not exist...`
  - You need to have the table `App.TABLE_NAME` in the database `App.DATABASE_NAME`

## How to run.

`$ java -jar` _<jarFile\>_

where

_<jarFile\>_=`target/TimestreamWriteHelloWorld-1.0-SNAPSHOT-jar-with-dependencies.jar`

## Results

should look like:

hostname | az | region | measure_name | time | measure_value::double
---------|----|--------|--------------|------|----------------------
host_xyz | zone_xyz | region_xyz | cpu_utilization    | 2024-09-27 16:16:05.373000000 | 13.5
host_xyz | zone_xyz | region_xyz | memory_utilization | 2024-09-27 16:16:05.373000000 | 40.0

## Nota

Plugin addition to `pom.xml` (*for executable, fat jar*)

`maven-assembly-plugin`
