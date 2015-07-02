package cn.oasistech.tracer;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

public class Tracer<E> {
    private String name;
    private Date startTime;
    private long traceCount;
    private List<Statistician<E>> statisticians;
    
    private static final Logger logger = Logger.getLogger(Tracer.class);
    
    public Tracer(String name) {
        this.name = name;
        this.statisticians = new ArrayList<Statistician<E>>();
    }
    
    public Tracer<E> trace(E e) {
        if (startTime == null) {
            startTime = new Date();
        }
        
        traceCount++;
        
        for (Statistician<E> stat : statisticians) {
            stat.record(e);
        }

        return this;
    }
    
    public Tracer<E> addStatistician(Statistician<E> stat) {
        statisticians.add(stat);
        return this;
    }
    
    public List<Statistician<E>> getStatisticians() {
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
	            	for (Statistician<E> stat : getStatisticians()) {
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
