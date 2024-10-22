package com.aboutobjects;

import java.util.ArrayList;
import java.util.List;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.services.timestreamwrite.AmazonTimestreamWrite;
import com.amazonaws.services.timestreamwrite.AmazonTimestreamWriteClientBuilder;
import com.amazonaws.services.timestreamwrite.model.*;
import com.amazonaws.services.timestreamwrite.model.Record;

public class App  {

    private static final String DATABASE_NAME   = "devops"          ;
    private static final String TABLE_NAME      = "host_metrics"    ;
    private static final String REGION          = "us-east-2"       ;

    public static void main (String [] args) {

        System.out.println ("Writing records") ;

        //------ Creating dimensions. (time independent data).
        List<Dimension> dimensions  = new ArrayList<>();
        final Dimension dimRegion   = new Dimension ().withName ("region")      .withValue ("region_xyz")   ;
        final Dimension dimAz       = new Dimension ().withName ("az")          .withValue ("zone_xyz"  )   ;
        final Dimension dimHost     = new Dimension ().withName ("hostname")    .withValue ("host_xyz"  )   ;
        dimensions.add (dimRegion)  ;
        dimensions.add (dimAz)      ;
        dimensions.add (dimHost)    ;

        //------ Creating two records, adding Measurements. (time Dependent data).
        final long time = System.currentTimeMillis () ;
        Record cpuUtilization = new Record()
            .withDimensions (dimensions)
            .withMeasureName ("cpu_utilization")    .withMeasureValue ("13.5")  .withMeasureValueType(MeasureValueType.DOUBLE)
            .withTime   (String.valueOf(time)) ;
        Record memoryUtilization = new Record()
            .withDimensions (dimensions)
            .withMeasureName ("memory_utilization") .withMeasureValue("40")     .withMeasureValueType(MeasureValueType.DOUBLE)
            .withTime (String.valueOf(time)) ;

        //------ Creating write record request.
        List<Record> records = new ArrayList<>();
        records.add (cpuUtilization)    ;
        records.add (memoryUtilization) ;
        WriteRecordsRequest writeRecordsRequest = new WriteRecordsRequest()
            .withDatabaseName   (DATABASE_NAME)
            .withTableName      (TABLE_NAME)
            .withRecords        (records);

        //------ Creating Write Client.
        final ClientConfiguration clientConfiguration = new ClientConfiguration ()
            .withMaxConnections (5000)
            .withRequestTimeout (20 * 1000)
            .withMaxErrorRetry  (10)            ;

        AmazonTimestreamWrite amazonTimestreamWrite = AmazonTimestreamWriteClientBuilder
                .standard                   ()
                .withRegion                 (REGION)
                .withClientConfiguration    (clientConfiguration)
                .build                      ()  ;

        //------ Writing.
        try {
            WriteRecordsResult writeRecordsResult = amazonTimestreamWrite.writeRecords(writeRecordsRequest);
            System.out.println("WriteRecords Status: " + writeRecordsResult.getSdkHttpMetadata().getHttpStatusCode());
        } catch (RejectedRecordsException e) {
            System.out.println("RejectedRecords: " + e);
            e.getRejectedRecords().forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}
