package cn.oasistech.tracer;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
 
public class TagTracer {
    private String name;
    private Date startTime;
    private long traceCount;
    private List<TagStatistician> statisticians;
    
    private static final Logger logger = Logger.getLogger(TagTracer.class);
    
    public TagTracer(String name) {
        this.name = name;
        this.statisticians = new ArrayList<TagStatistician>();
    }
    
    public TagTracer trace(Object ...es) {
        if (startTime == null) {
            startTime = new Date();
        }
        
        traceCount++;
        
        for (TagStatistician stat : statisticians) {
            stat.record(es);
        }

        return this;
    }
    
    public TagTracer addStatistician(TagStatistician stat) {
        statisticians.add(stat);
        return this;
    }
    
    public List<TagStatistician> getStatisticians() {
        return statisticians;
    }
    
    public String getName() {
        return name;
    }
    
    public Date getStartTime() {
        return startTime;
    }
    
    public long getTraceCount() {
        return traceCount;
    }
    
    public class ReportThread implements Runnable {
        private int period;
        
        public ReportThread(int period) {
            this.period = period;
        }
        
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(period * 1000);
                    for (TagStatistician stat : getStatisticians()) {
                        logger.info("\r\n\r\n" + stat.getResult());
                        if (stat.isClearWhenReported()) {
                            stat.clearResult();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void report(int period) {
        Thread thread = new Thread(new ReportThread(period));
        thread.start();
    }
}
