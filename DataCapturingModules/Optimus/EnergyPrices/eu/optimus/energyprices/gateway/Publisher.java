package eu.optimus.energyprices.gateway;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.log4j.Logger;


public class Publisher {

    private URL serverURL;
    private static  org.apache.log4j.Logger log = Logger.getLogger(Publisher.class);
    private String logFileName;
    
    public Publisher(URL serverURL) {
        this.serverURL = serverURL;
    }

    public Publisher(URL serverURL, String logFileName) {
      this.serverURL = serverURL;
      this.logFileName = logFileName;
    }
   
    public int publish(Event[] events) throws IOException {
      HttpURLConnection con = (HttpURLConnection) serverURL.openConnection();
      con.setRequestMethod("POST");
      con.setRequestProperty("Content-Type", "application/ztreamy-event");
      con.setDoOutput(true);
      OutputStream out = con.getOutputStream();
      OutputStream log = null;
      if (logFileName != null) {
          log = new FileOutputStream(logFileName, true);
      }
      for (Event event: events) {
          byte[] data = event.serialize();
          out.write(data);
          if (logFileName != null) {
              log.write(data);
          }
      }
      out.close();
      if (logFileName != null) {
          log.close();
      }
      return con.getResponseCode();
  }
    
    public int publish(Event event) throws IOException {
        return publish(new Event[] {event});
    }

//    public static void main(String[] args) throws IOException, InterruptedException {
//        if (args.length != 2) {
//            System.err.println("Error: one command-line parameter expected");
//            System.err.println("java Publisher <stream publication URI> <number of events>");
//            System.exit(1);
//        }
//        Publisher publisher = new Publisher(new URL(args[0]));
//        int numEvents = Integer.parseInt(args[1]);
//        String sourceId = Event.createUUID();
//        for (int i = 0; i < numEvents; i++) {
//            int result = publisher.publish(new TestEvent(sourceId));
//            if (result == 200) {
//                System.out.println("An event just just been sent to the server");
//            } else {
//                System.out.println("The server responded with error " + result);
//            }
//            Thread.sleep(5000);
//        }
//    }
}
